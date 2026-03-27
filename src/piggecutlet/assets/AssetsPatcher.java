package piggecutlet.assets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;
import piggecutlet.constant.ExtensionConstant;
import piggecutlet.constant.PathConstant;

/** 「libnative-lib.so」をパッチするクラス. */
public class AssetsPatcher {

  private MessageDigest md;

  private HexFormat hexFormat;

  /** コンストラクタ. */
  public AssetsPatcher() {
    try {
      md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    hexFormat = HexFormat.of();
  }

  /** 「libnative-lib.so」をパッチするメソッド. */
  public void main() {
    // ABIごとに「libnative-lib.so」のパスを取得する
    List<Path> libnativePathList = getLibnativePathList();

    // 暗号化だけの場合は終了する
    if (libnativePathList.size() == 0) {
      return;
    }

    patchByForeach(libnativePathList);

  }

  /** ABIごとに「libnative-lib.so」のパスを取得する. */
  private List<Path> getLibnativePathList() {
    List<Path> libnativePathList = new ArrayList<Path>();

    Path libDir = null;
    if (Files.exists(PathConstant.LIB_DIR_BY_APKTOOL)) {
      libDir = PathConstant.LIB_DIR_BY_APKTOOL;
    } else if (Files.exists(PathConstant.LIB_DIR_BY_APKEDITOR)) {
      libDir = PathConstant.LIB_DIR_BY_APKEDITOR;
    } else {
      // 暗号化だけの場合は早期リターン
      return libnativePathList;
    }

    String[] abiArray = {"armeabi-v7a", "arm64-v8a", "x86", "x86_64"};
    for (String abi : abiArray) {
      Path libnativePath = libDir.resolve(abi).resolve("libnative-lib.so");
      if (Files.exists(libnativePath)) {
        libnativePathList.add(libnativePath);
        System.out.println(abi + "\\libnative-lib.so exist.");
      } else {
        // System.out.println(abi + "\\libnative-lib.so doesn't exist.");
      }
    }

    return libnativePathList;
  }

  private void patchByForeach(List<Path> libnativePathList) {
    String[] localAssets = {"UnitLocal", "NumberLocal", "resLocal", "MapLocal", "ImageLocal",
        "HtmlLocal", "ImageDataLocal", "DataLocal", "DownloadLocal"};

    Path assetsDir = null;
    if (Files.exists(PathConstant.ASSETS_DIR_BY_APKTOOL)) {
      assetsDir = PathConstant.ASSETS_DIR_BY_APKTOOL;
    } else if (Files.exists(PathConstant.ASSETS_DIR_BY_APKEDITOR)) {
      assetsDir = PathConstant.ASSETS_DIR_BY_APKEDITOR;
    }

    for (Path libnativePath : libnativePathList) {
      byte[] libnativeByte = null;
      try {
        libnativeByte = Files.readAllBytes(libnativePath);
      } catch (IOException e) {
        e.printStackTrace();
      }

      for (String basename : localAssets) {
        Path oldListPath = assetsDir.resolve(basename + ExtensionConstant.LIST);
        Path newListPath = PathConstant.ENCRYPTED_DIR.resolve(basename + ExtensionConstant.LIST);
        patchByForeach(libnativeByte, oldListPath, newListPath);

        Path oldPackPath = assetsDir.resolve(basename + ExtensionConstant.PACK);
        Path newPackPath = PathConstant.ENCRYPTED_DIR.resolve(basename + ExtensionConstant.PACK);
        patchByForeach(libnativeByte, oldPackPath, newPackPath);
      }

      // すべてのassetsのパッチが終わったら書き込む
      // これをlibnative-lib.so分繰り返す
      try {
        Files.write(libnativePath, libnativeByte);
        System.out.println("パッチ完了 " + libnativePath);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void patchByForeach(byte[] libnativeByte, Path oldPath, Path newPath) {
    // 「workspace\app\assets」と「workspace\encrypted」に存在しない場合は中断
    if (Files.notExists(oldPath) || Files.notExists(newPath)) {
      // System.out.println(oldPath + " or " + newPath + "doesn't exist.");
      return;
    }

    byte[] oldByte = null;
    byte[] newByte = null;

    try {
      oldByte = Files.readAllBytes(oldPath);
      newByte = Files.readAllBytes(newPath);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // MD5ハッシュ値の計算
    byte[] oldHash = md.digest(oldByte);
    byte[] newHash = md.digest(newByte);

    // MD5ハッシュ値を16進数文字列に変換
    String oldHash2 = hexFormat.formatHex(oldHash);
    String newHash2 = hexFormat.formatHex(newHash);

    // 16進数文字列を検索するバイト配列に変換
    byte[] oldHash3 = oldHash2.getBytes(StandardCharsets.UTF_8);
    byte[] newHash3 = newHash2.getBytes(StandardCharsets.UTF_8);

    for (int i = 0; i < libnativeByte.length - oldHash3.length; i++) {
      // if (i == 0) {
      // System.out.println("\nj == 0");
      // System.out.println(oldPath.getFileName());
      // System.out.println("MD5ハッシュ値(oldHash → newHash): " + Arrays.toString(oldHash) + " "
      // + Arrays.toString(newHash));
      // System.out.println("16進数文字列(oldHash2 → newHash2): " + oldHash2 + " " + newHash2);
      // System.out.println("検索するバイト配列(oldHash3 → newHash3): " + Arrays.toString(oldHash3) + " "
      // + Arrays.toString(newHash3));
      // System.out.println();
      // }

      // パターンが一致したかどうかのフラグ
      boolean match = true;

      // assetsと一致しているか確認
      for (int j = 0; j < oldHash3.length; j++) {
        if (libnativeByte[i + j] != oldHash3[j]) {
          // 一致しない場合はフラグをfalseにしてループを抜ける
          match = false;
          break;
        }
      }

      // パターンが完全に一致した場合は置き換えを実行する
      if (match) {
        for (int j = 0; j < newHash3.length; j++) {
          libnativeByte[i + j] = newHash3[j];
        }

        // System.out.println();
        // System.out.println(oldPath.getFileName());
        // System.out.println("MD5ハッシュ値(oldHash → newHash): " + Arrays.toString(oldHash) + " "
        // + Arrays.toString(newHash));
        // System.out.println("16進数文字列(oldHash2 → newHash2): " + oldHash2 + " " + newHash2);
        // System.out.println("検索するバイト配列(oldHash3 → newHash3): " + Arrays.toString(oldHash3) + " "
        // + Arrays.toString(newHash3));
        // System.out.println(
        // "VSCode: " + hexFormat.formatHex(oldHash3) + " → " + hexFormat.formatHex(newHash3));
        // System.out.println();
      }

    }

  }

}
