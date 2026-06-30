package piggecutlet.assets;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import piggecutlet.constant.ExtensionConstant;
import piggecutlet.constant.PathConstant;
import piggecutlet.helper.FilesUtil;
import piggecutlet.helper.GenerateUtil;
import piggecutlet.helper.PrintUtil;

/** リストファイルとパックファイルを復号するクラス. */
public class AssetsDecryptor {

  /** リストファイルを復号するCipherインスタンス. */
  private Cipher listCipher;

  /** サーバーパックファイルを復号するCipherインスタンス. */
  private Cipher serverPackCipher;

  /** ローカルパックファイルを復号するCipherインスタンス. */
  private Cipher localPackCipher;

  /** Local パックも Server パック形式（AES/ECB）で処理する. */
  private final boolean oldMode;

  /** コンストラクタ. */
  public AssetsDecryptor(String lang, boolean oldMode) {
    this.oldMode = oldMode;
    this.listCipher = GenerateUtil.generateListCipher(Cipher.DECRYPT_MODE);
    this.serverPackCipher = GenerateUtil.generateServerPackCipher(Cipher.DECRYPT_MODE, lang);
    this.localPackCipher = GenerateUtil.generateLocalePackCipher(Cipher.DECRYPT_MODE, lang);
  }

  /** リストファイルとパックファイルを復号します. */
  public void main() {
    // 「encrypt」ディレクトリが存在しない場合は終了する
    if (!Files.isDirectory(PathConstant.ENCRYPT_DIR)) {
      PrintUtil.notExist(PathConstant.ENCRYPT_DIR.toString());
      System.exit(1);
    }

    // リストファイルの一覧を取得する
    List<Path> filePathList =
        FilesUtil.getFilePathList(PathConstant.ENCRYPT_DIR, ExtensionConstant.LIST);

    // リストファイルが存在しない場合は終了する
    if (filePathList.size() == 0) {
      System.err.println("「" + PathConstant.ENCRYPT_DIR + "」ディレクトリに「" + ExtensionConstant.LIST
          + "」ファイルが存在しません。");
      System.exit(1);
    }

    // リストファイルを復号する
    decryptList(filePathList);

    // パックファイルを復号する
    decryptPack(filePathList);
  }

  /* リストファイルを復号します. **/
  private void decryptList(List<Path> listFilePathList) {
    // 「tmp」ディレクトリを再作成する
    FilesUtil.recreateDir(PathConstant.TEMP_DIR);

    // リストファイルの数だけ繰り返す
    for (Path listFilePath : listFilePathList) {
      byte[] encryptedData = null;
      try {
        // 暗号化されたリストファイルを読み取る
        encryptedData = Files.readAllBytes(listFilePath);
      } catch (IOException e) {
        e.printStackTrace();
      }

      byte[] decryptedData = null;
      try {
        // 暗号化されたリストファイルを復号する
        decryptedData = listCipher.doFinal(encryptedData);
      } catch (IllegalBlockSizeException | BadPaddingException e) {
        e.printStackTrace();
      }

      // 復号した結果を書き込むファイル
      Path decryptedFile = PathConstant.TEMP_DIR.resolve(listFilePath.getFileName());

      try {
        // 復号した結果をファイルに書き込む
        Files.write(decryptedFile, decryptedData);
      } catch (IOException e) {
        e.printStackTrace();
      }

    }
  }

  /* パックファイルを復号します. **/
  private void decryptPack(List<Path> filePathList) {
    // 「decrypt」ディレクトリを再作成する
    FilesUtil.recreateDir(PathConstant.DECRYPT_DIR);

    // リストファイルの数だけ繰り返す
    for (Path filePath : filePathList) {
      String fileName = filePath.getFileName().toString();

      if (oldMode) {
        decryptPack(serverPackCipher, fileName);
        continue;
      }

      String lowerFileName = fileName.toLowerCase();

      // サーバーファイルの場合
      if (lowerFileName.contains("server")) {
        decryptPack(serverPackCipher, fileName);
      } else if (lowerFileName.contains("local")) {
        decryptPack(localPackCipher, fileName);
      } else {
        System.err.println("「" + fileName + "」はサーバーファイル、ローカルファイルのどちらでもありません。");
        continue;
      }

    }
  }

  /* パックファイルを復号するロジック部分. **/
  private void decryptPack(Cipher cipher, String listFileName) {
    // 復号したリストファイルが存在するか確認する
    Path listFilePath = PathConstant.TEMP_DIR.resolve(listFileName);
    if (Files.notExists(listFilePath)) {
      System.err.println("「" + listFilePath + "」ファイルが存在しません。");
      return;
    }

    // 拡張子を除いたファイル名
    String baseName = FilesUtil.getBaseName(listFileName);

    // 暗号化されたパックファイルが存在するか確認する
    String packBaseName =
        oldMode ? FilesUtil.resolveOldPackBaseName(baseName) : baseName;
    Path packFilePath =
        PathConstant.ENCRYPT_DIR.resolve(packBaseName + ExtensionConstant.PACK);
    if (Files.notExists(packFilePath)) {
      System.err.println("「" + packFilePath + "」ファイルが存在しません。");
      return;
    }

    Path outputDir = PathConstant.DECRYPT_DIR.resolve(baseName);
    try {
      // 出力先のディレクトリを作成する
      Files.createDirectories(outputDir);
    } catch (IOException e) {
      e.printStackTrace();
    }

    byte[] encryptedData = null;
    try {
      // 暗号化されたパックファイルを読み取る
      encryptedData = Files.readAllBytes(packFilePath);
    } catch (IOException e) {
      e.printStackTrace();
    }

    String listSeparator = ",";

    try (BufferedReader br = Files.newBufferedReader(listFilePath)) {
      // ファイル数
      int fileNum = Integer.parseInt(br.readLine());

      // ファイルの数だけ繰り返す
      for (int i = 0; i < fileNum; i++) {
        // 1行
        String line = br.readLine();

        if (line.split(listSeparator).length != 3) {
          System.err.println("「" + listFileName + "」の" + fileNum + "行目「" + line + "」は形式が正しくありません。");
          continue;
        }

        // ファイル名
        String outputFileName = line.split(listSeparator)[0];
        // バイト配列の開始位置
        int startByte = Integer.parseInt(line.split(listSeparator)[1]);
        // バイト配列の長さ
        int byteSize = Integer.parseInt(line.split(listSeparator)[2]);
        // バイト配列の終了位置
        int endByte = startByte + Integer.parseInt(line.split(listSeparator)[2]);

        byte[] outputData = new byte[byteSize];

        if (FilesUtil.isImageDataLocalBaseName(baseName)) {
          outputData = Arrays.copyOfRange(encryptedData, startByte, endByte);
        } else {
          try {
            outputData = cipher.doFinal(encryptedData, startByte, byteSize);
          } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
          }
        }

        Path outputFile = PathConstant.DECRYPT_DIR.resolve(baseName).resolve(outputFileName);

        Files.write(outputFile, outputData);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("Decrypted: " + baseName);

  }

}
