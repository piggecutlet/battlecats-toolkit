package piggecutlet.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class ProcessBuilderHelper {

  public static boolean execute(List<String> command) {
    // ローカル変数のためスレッドセーフ
    ProcessBuilder processBuilder = new ProcessBuilder(command);

    // 標準出力にエラー出力を結合する
    processBuilder.redirectErrorStream(true);

    Process process = null;
    try {
      process = processBuilder.start();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      String line;
      while ((line = br.readLine()) != null) {
        System.out.println(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    int exitValue = 1;
    try {
      exitValue = process.waitFor();
    } catch (InterruptedException e) {
      e.printStackTrace();
      return false;
    }

    if (0 == exitValue) {
      return true;
    } else {
      return false;
    }
  }

  @Deprecated
  public static boolean execute(String... command) {
    return execute(Arrays.asList(command));
  }

}
