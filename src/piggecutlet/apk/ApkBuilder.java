package piggecutlet.apk;

import java.util.ArrayList;
import java.util.List;
import piggecutlet.constant.PathConstant;
import piggecutlet.helper.ProcessBuilderHelper;

public class ApkBuilder {



  public void execute() {
    System.out.println("APK をビルドします。");

    if (!build()) {
      System.err.println("APK のビルドに失敗しました。");
      System.exit(1);
    }
  }

  private boolean build() {
    List<String> command = new ArrayList<>();

    // java -jar %tool%/apktool.jar b app -d -f -o app.apk
    // java -jar $env:tool/apktool.jar b app -d -f -o app.apk

    command.add("java");
    command.add("-jar");
    command.add(PathConstant.APKTOOL.toString());
    command.add("build");
    command.add(PathConstant.APP_DIR.toString());
    command.add("--debug");
    command.add("--force");
    command.add("--output");
    command.add(PathConstant.OUTPUT_APK.toString());

    if (ProcessBuilderHelper.execute(command)) {
      return true;
    } else {
      return false;
    }
  }

}
