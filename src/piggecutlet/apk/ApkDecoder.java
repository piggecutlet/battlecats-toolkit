package piggecutlet.apk;

import java.util.ArrayList;
import java.util.List;
import piggecutlet.constant.PathConstant;
import piggecutlet.helper.ProcessBuilderHelper;

public class ApkDecoder {

  public void execute() {
    if (!decode()) {
      System.exit(1);
    }
  }

  private boolean decode() {
    List<String> command = new ArrayList<>();

    // java -jar %tool%/apktool.jar d app.xapk -f -o app -s
    // java -jar $env:tool/apktool.jar d app.xapk -f -o app -s

    command.add("java");
    command.add("-jar");
    command.add(PathConstant.APKTOOL.toString());
    command.add("decode");
    command.add(PathConstant.APP_DIR.toString());
    command.add("--force");
    command.add("--no-src");
    command.add("--output");
    command.add(PathConstant.OUTPUT_APK.toString());

    if (ProcessBuilderHelper.execute(command)) {
      return true;
    } else {
      return false;
    }
  }

}
