package piggecutlet.util;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import piggecutlet.constant.KeyConstant;

/** 秘密鍵やインスタンスを生成するクラス. */
public class GenerateUtil {

  /** MessageDigest. */
  private static MessageDigest md;

  // staticイニシャライザ
  static {
    try {
      md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }

  /**
   * リストファイルとサーバーパックファイルのAESキーを生成して返します.
   */
  public static byte[] generateSecretKey(String key) {
    // ハッシュ値に変換する
    // pack: -76 -124 -123 121 1 116 42 -4 -98 -99 78 -104 83 89 108 -30
    // battlecats: -119 -96 -7 -112 120 65 -100 40 -108 74 116 44 -71 -40 -73 -68
    byte[] byteList = md.digest(key.getBytes(StandardCharsets.UTF_8));

    // 16進数に変換する
    // pack: b4 84 85 79 01 74 2a fc 9e 9d 4e 98 53 59 6c e2
    // battlecats: 89 a0 f9 90 78 41 9c 28 94 4a 74 2c b9 d8 b7 bc
    StringBuilder sb = new StringBuilder();
    for (byte b : byteList) {
      sb.append(String.format("%02x", b));
    }

    // 最初の16文字返す
    // pack: b4 84 85 79 01 74 2a fc
    // battlecats: 89 a0 f9 90 78 41 9c 28
    String md5HashString = sb.toString().substring(0, 16);

    // バイト配列に変換する
    // pack: 98 52 56 52 56 53 55 57 48 49 55 52 50 97 102 99
    // battlecats: 56 57 97 48 102 57 57 48 55 56 52 49 57 99 50 56
    return md5HashString.getBytes(StandardCharsets.UTF_8);
  }

  /** リストファイルのCipherインスタンスを生成して返します. */
  public static Cipher generateListCipher(int opmode) {
    SecretKeySpec secretKeySpec = new SecretKeySpec(KeyConstant.LIST_AES_KEY, "AES");

    Cipher cipher = null;
    try {
      cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(opmode, secretKeySpec);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
      e.printStackTrace();
    }

    return cipher;
  }

  /** サーバーパックファイルのCipherインスタンスを生成して返します. */
  public static Cipher generateServerPackCipher(int opmode, String lang) {
    SecretKeySpec secretKeySpec = new SecretKeySpec(KeyConstant.SERVER_PACK_AES_KEY, "AES");

    Cipher cipher = null;
    try {
      cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(opmode, secretKeySpec);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
      e.printStackTrace();
    }

    return cipher;
  }

  /** ローカルパックファイルのCipherインスタンスを生成して返します. */
  public static Cipher generateLocalePackCipher(int opmode, String lang) {
    HexFormat hexFormat = HexFormat.of();

    byte[] key = hexFormat.parseHex(KeyConstant.LOCAL_PACK_AES_KEY.get(lang));
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

    byte[] iv = hexFormat.parseHex(KeyConstant.LOCAL_PACK_AES_IV.get(lang));
    IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

    Cipher cipher = null;
    try {
      cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(opmode, secretKeySpec, ivParameterSpec);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
        | InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    }

    return cipher;
  }

}
