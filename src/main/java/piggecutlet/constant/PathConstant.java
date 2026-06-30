package piggecutlet.constant;

import java.nio.file.Path;
import java.nio.file.Paths;

/** パスを定義する定数クラス. */
public class PathConstant {

  /**
   * インスタンス化を禁止するプライベートコンストラクタ.
   */
  private PathConstant() {}

  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

  /** 「tool」ディレクトリ. */
  private static final Path TOOL_DIR = Paths.get("lib");

  /** 「tool\APKEditor.jar」. */
  public static final Path APKEDITOR = TOOL_DIR.resolve("APKEditor.jar");

  /** %tool%/apksigner.jar. */
  public static final Path APKSIGNER = TOOL_DIR.resolve("apksigner.jar");

  /** 「tool\apktool.jar」. */
  public static final Path APKTOOL = TOOL_DIR.resolve("apktool.jar");

  /** 「tool\.keystore」. */
  public static final Path KEYSTORE = TOOL_DIR.resolve("debug.keystore");

  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

  /** 「workspace\app」ディレクトリ名. */
  private static final String APP_DIR_NAME = "app";

  /** 「app.apk」. */
  public static final Path APP_APK = Paths.get(APP_DIR_NAME + ExtensionConstant.APK);

  /** 「app.xapk」. */
  public static final Path APP_XAPK = Paths.get(APP_DIR_NAME + ExtensionConstant.XAPK);

  /** 「workspace」ディレクトリ. */
  public static final Path WORKSPACE_DIR = Paths.get("workspace");

  /** 「workspace\app」ディレクトリ. */
  public static final Path APP_DIR = WORKSPACE_DIR.resolve(APP_DIR_NAME);

  /** 「workspace\app\assets」ディレクトリ(apktool.jar). */
  public static final Path ASSETS_DIR_BY_APKTOOL = APP_DIR.resolve("assets");

  /** 「workspace\app\root\assets」ディレクトリ(APKEditor.jar). */
  public static final Path ASSETS_DIR_BY_APKEDITOR = APP_DIR.resolve("root").resolve("assets");

  /** 「workspace\decrypt」ディレクトリ. */
  public static final Path DECRYPT_DIR = WORKSPACE_DIR.resolve("decrypt");

  /** 「workspace\encrypt」ディレクトリ. */
  public static final Path ENCRYPT_DIR = WORKSPACE_DIR.resolve("encrypt");

  /** 「workspace\temp」ディレクトリ. */
  public static final Path TEMP_DIR = WORKSPACE_DIR.resolve("temp");

  /** 「output.apk」. */
  public static final Path OUTPUT_APK = Paths.get("output" + ExtensionConstant.APK);

  /** 「workspace\app\lib」ディレクトリ(apktool.jar). */
  public static final Path LIB_DIR_BY_APKTOOL = APP_DIR.resolve("lib");

  /** 「workspace\app\root\lib」ディレクトリ(APKEditor.jar). */
  public static final Path LIB_DIR_BY_APKEDITOR = APP_DIR.resolve("root").resolve("lib");

}
