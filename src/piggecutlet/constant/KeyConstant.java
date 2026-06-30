package piggecutlet.constant;

import java.util.Map;
import piggecutlet.util.GenerateUtil;

/** パスを定義する定数クラス. */
public class KeyConstant {

  /**
   * インスタンス化を禁止するプライベートコンストラクタ.
   */
  private KeyConstant() {}

  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

  /** リストファイルのMD5鍵. */
  public static final String LIST_MD5_KEY = "pack";

  /** リストファイルのAES鍵. */
  public static final byte[] LIST_AES_KEY = GenerateUtil.generateSecretKey(LIST_MD5_KEY);

  /** パックファイルのMD5鍵. */
  public static final String SERVER_PACK_MD5_KEY = "battlecats";

  /** サーバーパックファイルのAES鍵. */
  public static final byte[] SERVER_PACK_AES_KEY =
      GenerateUtil.generateSecretKey(SERVER_PACK_MD5_KEY);

  /** ローカルパックファイルのAES鍵. */
  public static final Map<String, String> LOCAL_PACK_AES_KEY = Map.of(
      // 日本版
      "jp", "d754868de89d717fa9e7b06da45ae9e3",
      // 韓国版
      "kr", "bea585eb993216ef4dcb88b625c3df98",
      // グローバル版
      "en", "0ad39e4aeaf55aa717feb1825edef521",
      // 台湾版
      "tw", "313d9858a7fb939def1d7d859629087d");

  /** ローカルパックファイルの初期化ベクトル. */
  public static final Map<String, String> LOCAL_PACK_AES_IV = Map.of(
      // 日本版
      "jp", "40b2131a9f388ad4e5002a98118f6128",
      // 韓国版
      "kr", "9b13c2121d39f1353a125fed98696649",
      // グローバル版
      "en", "d1d7e708091941d90cdf8aa5f30bb0c2",
      // 台湾版
      "tw", "0e3743eb53bf5944d1ae7e10c2e54bdf");

}
