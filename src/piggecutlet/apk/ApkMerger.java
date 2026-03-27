package piggecutlet.apk;

import java.util.ArrayList;
import java.util.List;
import piggecutlet.constant.PathConstant;
import piggecutlet.helper.ProcessBuilderHelper;

public class ApkMerger {

  public void execute() {
    System.out.println("XAPK を APK にマージします。");

    if (!merge()) {
      System.err.println("XAPK のマージに失敗しました。");
      System.exit(1);
    }

    System.out.println("XAPK のマージが完了しました。");
  }

  private boolean merge() {
    List<String> command = new ArrayList<>();

    // java -jar $env:tool/APKEditor.jar m -f -i app.xapk -o app.apk

    command.add("java");
    command.add("-jar");
    command.add(PathConstant.APKEDITOR.toString());
    command.add("merge");
    command.add("-f");
    command.add("-i");
    command.add(PathConstant.APP_XAPK.toString());
    command.add("-o");
    command.add(PathConstant.APP_APK.toString());

    if (ProcessBuilderHelper.execute(command)) {
      return true;
    } else {
      return false;
    }
  }

}
