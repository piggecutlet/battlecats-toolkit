package piggecutlet;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import piggecutlet.apk.ApkDecoder;
import piggecutlet.apk.ApkMerger;
import piggecutlet.constant.PathConstant;

public class Main {

  public static void main(String[] args) throws NoSuchFileException {
    if (Files.exists(PathConstant.APP_XAPK)) {
      new ApkMerger().execute();
      System.out.println();
    }

    if (Files.exists(PathConstant.APP_APK)) {
      new ApkDecoder().execute();
      System.out.println();
    }

  }

}
