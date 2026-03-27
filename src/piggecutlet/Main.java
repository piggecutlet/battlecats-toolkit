package piggecutlet;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import piggecutlet.apk.ApkBuilder;
import piggecutlet.apk.ApkDecoder;
import piggecutlet.apk.ApkMerger;
import piggecutlet.apk.ApkSigner;
import piggecutlet.assets.AssetsDecryptor;
import piggecutlet.assets.AssetsEncryptor;
import piggecutlet.constant.PathConstant;

public class Main {


  public static void main(String[] args) throws NoSuchFileException {
    // { } : 必須
    // [ ] : 任意
    String usage =
        "Usage: java -jar battlecats-toolkit.jar { decrypt | encrypt } { jp | kr | en | tw }";

    if (args.length < 2) {
      System.out.println(usage);
      return;
    }

    String lang = "jp";

    switch (args[1]) {
      case "jp":
        lang = "jp";
        break;
      case "kr":
        lang = "kr";
        break;
      case "en":
        lang = "en";
        break;
      case "tw":
        lang = "tw";
        break;
      default:
        System.out.println(usage);
        return;
    }

    if ("decrypt".equals(args[0])) {
      // XAPK の場合は APK に変換
      if (Files.exists(PathConstant.APP_XAPK)) {
        new ApkMerger().execute();
        System.out.println();
      }

      if (Files.exists(PathConstant.APP_APK)) {
        new ApkDecoder().execute();
        System.out.println();
      }

      new AssetsDecryptor(lang).main();

      return;
    }

    if ("encrypt".equals(args[0])) {
      new AssetsEncryptor(lang).main();

      // app ディレクトリが存在する場合は再ビルドする
      if (Files.exists(PathConstant.APP_DIR)) {
        new ApkBuilder().execute();
        System.out.println();

        new ApkSigner().execute();
      }

      return;
    }

  }

}
