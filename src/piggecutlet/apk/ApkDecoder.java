package piggecutlet.apk;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import piggecutlet.constant.ExtensionConstant;
import piggecutlet.constant.PathConstant;
import piggecutlet.helper.ProcessBuilderHelper;
import piggecutlet.util.FilesUtil;

public class ApkDecoder {

  public void execute() {
    System.out.println("APK をデコードします。");

    if (!decode()) {
      System.err.println("APK のデコードに失敗しました。");
      System.exit(1);
    }

    System.out.println("APK のデコードが完了しました。");

    // 成功時は assets をコピー
    copyAssets(PathConstant.ASSETS_DIR_BY_APKTOOL);
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
    command.add(PathConstant.APP_DIR.toString());

    if (ProcessBuilderHelper.execute(command)) {
      return true;
    } else {
      return false;
    }
  }

  /** リストファイルとパックファイルをコピーするメソッド. */
  private void copyAssets(Path assets) {
    System.err.println();
    System.out.println("assets をコピーします。");

    FilesUtil.recreateDir(PathConstant.ENCRYPTED_DIR);

    List<Path> listFilePathList = FilesUtil.getFilePathList(assets, ExtensionConstant.LIST);
    FilesUtil.copy(listFilePathList, PathConstant.ENCRYPTED_DIR);

    List<Path> packFilePathList = FilesUtil.getFilePathList(assets, ExtensionConstant.PACK);
    FilesUtil.copy(packFilePathList, PathConstant.ENCRYPTED_DIR);
  }

}
