package piggecutlet.eventdata;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/** イベントデータをダウンロードするクラス. */
public class EventDataDownloader {

  private static final String CREATE_ACCOUNT_URL =
      "https://nyanko-backups.ponosgames.com/?action=createAccount&referenceId=";

  // private static final String ACCOUNT_ID = "58c84e1ea";

  // private static final String USERS_URL = "https://nyanko-auth.ponosgames.com/v1/users";

  /** イベントデータをダウンロードするメソッド. */
  public static void main(String[] args) {
    getRequest(CREATE_ACCOUNT_URL);
  }

  private static void getRequest(String link) {
    try {
      // The constructor URL(String) is deprecated since version
      // URI経由でURLインスタンスを生成
      URI uri = new URI(link);
      URL url = uri.toURL();

      // コネクションを開く
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      // リクエストメソッドの設定
      connection.setRequestMethod("GET");

      // リクエストヘッダーの設定
      // connection.setRequestProperty("Accept", "application/json");

      // レスポンスヘッダーの設定
      connection.setRequestProperty("Content-Type", "application/json");

      // レスポンスコードの取得
      int responseCode = connection.getResponseCode();

      // レスポンスの読み取り
      if (responseCode == HttpURLConnection.HTTP_OK) {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
        in.close();

        // レスポンスの表示
        System.out.println(response.toString());
      } else {
        System.out.println("responseCode: " + responseCode);
      }

      // コネクションを閉じる
      connection.disconnect();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // private static void postRequest() {}

}
