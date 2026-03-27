package piggecutlet.apk;

import java.util.ArrayList;
import java.util.List;
import piggecutlet.constant.PathConstant;
import piggecutlet.helper.ProcessBuilderHelper;

public class ApkDecoder {

  public void execute() {
    System.out.println("APK をデコードします。");

    if (!decode()) {
      System.err.println("APK のデコードに失敗しました。");
      System.exit(1);
    }
  }

  private boolean decode() {
    List<String> command = new ArrayList<>();

    // java -jar $env:tool/apktool.jar d app.xapk -f -o app -s

    command.add("java");
    command.add("-jar");
    command.add(PathConstant.APKTOOL.toString());
    command.add("decode");
    command.add(PathConstant.APP_APK.toString());
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
