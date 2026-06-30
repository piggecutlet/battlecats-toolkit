package piggecutlet.apk;

import java.util.ArrayList;
import java.util.List;
import piggecutlet.constant.PathConstant;
import piggecutlet.helper.ProcessBuilderHelper;

public class ApkSigner {

  public void execute() {
    System.out.println("APK に署名します。");

    if (!sign()) {
      System.err.println("APK の署名に失敗しました。");
      System.exit(1);
    }
  }

  private boolean sign() {
    List<String> command = new ArrayList<>();

    // @formatter:off
    // java -jar %tool%/apksigner.jar sign --ks %tool%\debug.keystore --ks-pass pass:android build.apk
    // java -jar $env:tool/apksigner.jar sign --ks %tool%\debug.keystore --ks-pass pass:android build.apk
    // @formatter:on

    command.add("java");
    command.add("-jar");
    command.add(PathConstant.APKSIGNER.toString());
    command.add("sign");
    command.add("--ks");
    command.add(PathConstant.KEYSTORE.toString());
    command.add("--ks-pass");
    command.add("pass:android");
    command.add(PathConstant.OUTPUT_APK.toString());

    if (ProcessBuilderHelper.execute(command)) {
      return true;
    } else {
      return false;
    }
  }

}
