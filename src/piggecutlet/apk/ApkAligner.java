package piggecutlet.apk;

import java.util.ArrayList;
import java.util.List;
import piggecutlet.constant.PathConstant;
import piggecutlet.helper.ProcessBuilderHelper;

public class ApkAligner {

  public void execute() {
    if (!align()) {
      System.exit(1);
    }
  }

  private boolean align() {
    List<String> command = new ArrayList<>();

    // %tool%/zipalign -P 16 -f -v 4 infile.apk outfile.apk
    // $env:tool/zipalign

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
