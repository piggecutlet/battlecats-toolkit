package piggecutlet.apk;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import piggecutlet.constant.ArchitectureEnum;
import piggecutlet.constant.LocaleEnum;
import piggecutlet.constant.PathConstant;
import piggecutlet.util.PrintUtil;

public class ApkDownloader {

  private final String logMessage = "ダウンロード app.xapk";

  private final HttpClient client = HttpClient.newHttpClient();

  /** armeabi-v7a arm64-v8a x86 x86_64. */
  private final String ajaxFormat = "https://apkcombo.com/downloader/?package=%s&arches=%s&ajax=1";

  public void execute(LocaleEnum locale, ArchitectureEnum architecture) {
    PrintUtil.start(logMessage);

    String packageName = locale.getPackageName();

    String ajax = String.format(ajaxFormat, packageName, architecture);

    String ajaxBody = requestAjax(ajax);
    if (ajaxBody == null) {
      PrintUtil.fail(logMessage);
      return;
    }

    String ztp1 = parseBody(ajaxBody);
    if (ztp1 == null) {
      PrintUtil.fail(logMessage);
      return;
    }

    byte[] ztp1Body = requestZtp1(ztp1);
    if (ztp1Body == null) {
      PrintUtil.fail(logMessage);
      return;
    }

    boolean isDownloadXapk = downloadXapk(ztp1Body);
    if (!isDownloadXapk) {
      PrintUtil.fail(logMessage);
      return;
    }

    PrintUtil.complete(logMessage);
  }

  String requestAjax(String url) {
    PrintUtil.request(url);

    HttpRequest request = createRequest(url);

    HttpResponse<String> response = null;

    try {
      response = client.send(request, BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      return null;
    }

    if (response.statusCode() != 200) {
      PrintUtil.statusCode(String.valueOf(response.statusCode()));
      return null;
    }

    return response.body();
  }

  String parseBody(String body) {
    // ?: 非貪欲マッチ
    String regex = "https://apkcombo\\.com/d\\?u=.*?\"";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(body);

    String match = null;
    if (matcher.find()) {
      match = matcher.group();
    } else {
      PrintUtil.fail("パターン不一致: " + regex + "\n" + body);
      return null;
    }

    // https://apkcombo.com/d?u=aHR0cHM6Ly96dHAxLmFuZHJvaWRjb21iby5jb20v" →
    // aHR0cHM6Ly96dHAxLmFuZHJvaWRjb21iby5jb20v
    String encoded = match.replace("https://apkcombo.com/d?u=", "").replace("\"", "");

    byte[] decodedArray = Base64.getDecoder().decode(encoded);

    // aHR0cHM6Ly96dHAxLmFuZHJvaWRjb21iby5jb20v → https://ztp1.androidcombo.com/
    String decodedStr = new String(decodedArray, StandardCharsets.UTF_8);

    // ?__cache=true など余計なパラメータを取り除く
    return decodedStr.split("\\?")[0];
  }

  byte[] requestZtp1(String url) {
    PrintUtil.request(url);

    HttpRequest request = createRequest(url);

    HttpResponse<byte[]> response = null;

    try {
      response = client.send(request, BodyHandlers.ofByteArray());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      return null;
    }

    if (response.statusCode() != 200) {
      PrintUtil.statusCode(String.valueOf(response.statusCode()));
      return null;
    }

    return response.body();
  }

  boolean downloadXapk(byte[] body) {
    try {
      Files.write(PathConstant.APP_XAPK, body);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  HttpRequest createRequest(String url) {
    return HttpRequest.newBuilder().uri(URI.create(url)).build();
  }

  public String getLogMessage() {
    return logMessage;
  }

}
