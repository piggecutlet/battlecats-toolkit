package piggecutlet.eventdata;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import piggecutlet.assets.AssetsPatcher;
import piggecutlet.constant.Constant;
import piggecutlet.constant.ExtensionConstant;
import piggecutlet.constant.PathConstant;
import piggecutlet.util.FilesUtil;
import piggecutlet.util.GenerateUtil;
import piggecutlet.util.PrintUtil;

/** アセットを暗号化するクラス. */
public class EventDataEncryptor {

  /** リストファイルを暗号化するCipherインスタンス. */
  private Cipher listCipher;

  /** パックファイルを暗号化するCipherインスタンス. */
  private Cipher packCipher;

  /** コンストラクタ. */
  public EventDataEncryptor(String lang) {
    this.listCipher = GenerateUtil.generateListCipher(Cipher.ENCRYPT_MODE);
    this.packCipher = GenerateUtil.generateLocalePackCipher(Cipher.ENCRYPT_MODE, lang);
  }

  /** アセットを暗号化するメソッド. */
  public void main() {
    // 「workspace\encrypted」ディレクトリが存在しない場合は終了する
    if (!Files.isDirectory(PathConstant.ENCRYPTED_DIR)) {
      PrintUtil.notExist(PathConstant.ENCRYPTED_DIR.toString());
      System.exit(1);
    }

    // 暗号化するディレクトリを取得する
    List<Path> assetsDirList = new ArrayList<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(PathConstant.DECRYPTED_DIR)) {
      for (Path entry : stream) {
        if (Files.isDirectory(entry)) {
          assetsDirList.add(entry);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // 暗号化するディレクトリが存在しない場合は終了する
    if (assetsDirList.size() == 0) {
      System.err.println("Directory doesn't exist in " + PathConstant.DECRYPTED_DIR);
      System.exit(1);
    }

    // 暗号化されたパックファイルを作成する
    encryptPack(assetsDirList);

    // 暗号化されたリストファイルを作成する
    encryptList();

    System.out.println("Start: libnative-lib.so patch");
    AssetsPatcher patcher = new AssetsPatcher();
    patcher.main();

    // アセットをコピーする
    copyAssets();
  }

  /* 暗号化されたパックファイルを作成します. **/
  private void encryptPack(List<Path> assetsDirList) {
    // 「workspace\encrypted」ディレクトリを再作成する
    FilesUtil.recreateDir(PathConstant.ENCRYPTED_DIR);

    // 「workspace\temp」ディレクトリを再作成する
    FilesUtil.recreateDir(PathConstant.TEMP_DIR);

    for (Path assetsDir : assetsDirList) {
      // 「workspace\decrypted\DownloadLocal」から「workspace\encrypted\DownloadLocal.pack」を生成
      String packFileName = assetsDir.getFileName() + ExtensionConstant.PACK;
      Path packFilePath = PathConstant.ENCRYPTED_DIR.resolve(packFileName);

      String listFileName = assetsDir.getFileName() + ExtensionConstant.LIST;
      Path listFilePath = PathConstant.TEMP_DIR.resolve(listFileName);

      try (OutputStream os = Files.newOutputStream(packFilePath);
          BufferedOutputStream bos = new BufferedOutputStream(os);
          BufferedWriter bw = Files.newBufferedWriter(listFilePath, StandardCharsets.UTF_8)) {
        // 暗号化するファイルの一覧を取得する
        List<Path> filePathList = FilesUtil.getFilePathList(assetsDir);

        // 暗号化するファイルの数
        // bw.write(filePathList.size());
        // HtmlLocal.list対策
        if (filePathList.size() != 0) {
          bw.write(Integer.toString(filePathList.size()));
          bw.newLine();
        }

        int startByte = 0;

        for (Path filePath : filePathList) {
          byte[] data = Files.readAllBytes(filePath);

          if (!"ImageDataLocal".equals(assetsDir.getFileName().toString())) {
            try {
              data = packCipher.doFinal(data);
            } catch (IllegalBlockSizeException | BadPaddingException e) {
              e.printStackTrace();
            }
          }
          // パックファイルに書き込む
          bos.write(data);

          // リストファイルに書き込む
          // ファイル名
          bw.write(filePath.getFileName().toString());
          bw.write(Constant.LIST_FILE_SEPARATOR);
          // 開始バイト
          // bw.write(startByte);
          bw.write(Integer.toString(startByte));
          bw.write(Constant.LIST_FILE_SEPARATOR);
          // バイトサイズ
          // bw.write(data.length);
          bw.write(Integer.toString(data.length));
          bw.newLine();
          // 開始バイトを変更する
          startByte += data.length;
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      System.out.println("Encrypted " + assetsDir.getFileName());
    }
  }

  /* 暗号化されたリストファイルを作成する. **/
  private void encryptList() {
    List<Path> listPathList =
        FilesUtil.getFilePathList(PathConstant.TEMP_DIR, ExtensionConstant.LIST);

    for (Path path : listPathList) {
      byte[] decryptedData = null;
      try {
        // 作成したリストファイルを読み取る
        decryptedData = Files.readAllBytes(path);
      } catch (IOException e) {
        e.printStackTrace();
      }

      byte[] encryptedData = null;
      try {
        // リストファイルを暗号化する
        encryptedData = listCipher.doFinal(decryptedData);
      } catch (IllegalBlockSizeException | BadPaddingException e) {
        e.printStackTrace();
      }

      // 暗号化した結果を書き込むファイル
      Path encryptedFile = PathConstant.ENCRYPTED_DIR.resolve(path.getFileName());

      try {
        // 暗号化した結果をファイルに書き込む
        Files.write(encryptedFile, encryptedData);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /** リストファイルとパックファイルをコピーするメソッド. */
  private void copyAssets() {
    Path assetsDir = null;

    if (Files.exists(PathConstant.ASSETS_DIR_BY_APKEDITOR)) {
      assetsDir = PathConstant.ASSETS_DIR_BY_APKEDITOR;
    } else if (Files.exists(PathConstant.ASSETS_DIR_BY_APKTOOL)) {
      assetsDir = PathConstant.ASSETS_DIR_BY_APKTOOL;
    } else {
      // assetsなしで暗号化だけしたい場合
      return;
    }

    List<Path> listFilePathList =
        FilesUtil.getFilePathList(PathConstant.ENCRYPTED_DIR, ExtensionConstant.LIST);
    FilesUtil.copy(listFilePathList, assetsDir);

    List<Path> packFilePathList =
        FilesUtil.getFilePathList(PathConstant.ENCRYPTED_DIR, ExtensionConstant.PACK);
    FilesUtil.copy(packFilePathList, assetsDir);
  }

}
