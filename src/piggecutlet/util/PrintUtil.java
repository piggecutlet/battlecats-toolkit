package piggecutlet.util;

import java.nio.file.Path;

/** System.err.printlnのユーティリティクラス. */
public class PrintUtil {

  /** 終了. */
  public static void complete(String text) {
    System.out.println("完了: " + text);
  }

  /** 失敗. */
  public static void fail(String text) {
    System.err.println("失敗: " + text);
  }

  /** 失敗: xxx が存在しません. */
  public static void notExist(String text) {
    fail(text + " が存在しません。");
  }

  /** 失敗: xxx が存在しません. */
  public static void notExist(Path path) {
    fail(path.toString() + " が存在しません。");
  }

  /** 開始. */
  public static void start(String text) {
    System.out.println("開始: " + text);
  }

  /** リクエスト. */
  public static void request(String text) {
    System.out.println("リクエスト: " + text);
  }

  public static void statusCode(String text) {
    System.err.println("ステータスコード: " + text);
  }

}
