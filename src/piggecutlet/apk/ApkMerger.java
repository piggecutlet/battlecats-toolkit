package piggecutlet.apk;

import java.util.ArrayList;
import java.util.List;
import piggecutlet.constant.PathConstant;
import piggecutlet.helper.ProcessBuilderHelper;

public class ApkMerger {

  public void execute() {
    if (!merge()) {
      System.exit(1);
    }
  }

  private boolean merge() {
    List<String> command = new ArrayList<>();

    // java -jar %tool%/APKEditor.jar m -f -i app.xapk -o app.apk
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
