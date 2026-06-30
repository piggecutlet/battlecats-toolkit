package piggecutlet.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilesUtil {

  /**
   * 指定したディレクトリ内にあり、指定した拡張子で終わるファイルのパスを取得します.
   * 
   * @param dir 検索するディレクトリのパス
   * @param extension フィルタリングする拡張子
   * @return 条件に一致したファイルのパスのリスト
   */
  public static List<Path> getFilePathList(Path dir, String extension) {
    try (Stream<Path> paths = Files.list(dir)) {
      return paths
          // ファイルか判定する
          // 「!isDirectory」だとショートカットなども含まれてしまう
          .filter(Files::isRegularFile)
          // 引数で指定した拡張子で終わるファイルだけ取得する
          .filter(path -> path.getFileName().toString().endsWith(extension))
          // デバッグ
          // .peek(path -> System.out.println("getFileName: " + path.getFileName()))
          // 結果をListにまとめる
          .collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  /**
   * 指定したディレクトリ内にあるファイルのパスを取得します.
   * 
   * @param dir 検索するディレクトリのパス
   * @return 条件に一致したファイルのパスのリスト
   */
  public static List<Path> getFilePathList(Path dir) {
    try (Stream<Path> paths = Files.list(dir)) {
      return paths
          // ファイルか判定する
          // 「!isDirectory」だとショートカットなども含まれてしまう
          .filter(Files::isRegularFile).collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  /**
   * 拡張子を除いたファイル名を取得します.
   * 
   * @param fileName 拡張子を除くファイル名
   * @return 拡張子を除いたファイル名
   */
  public static String getBaseName(String fileName) {
    // splitメソッドの引数は正規表現のため、特別な意味を持つ「.」はエスケープする必要がある
    return fileName.split("\\.")[0];
  }

  /**
   * ディレクトリを削除します.
   * 
   * @param dir 削除するディレクトリのパス
   */
  public static void deleteDir(Path dir) {
    if (Files.notExists(dir)) {
      return;
    }

    try (Stream<Path> walk = Files.walk(dir)) {
      // sorted(Comparator.reverseOrder())で再帰的に削除する
      walk.sorted(Comparator.reverseOrder()).forEach(path -> {
        try {
          Files.delete(path);
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * ディレクトリを再作成します.
   *
   * @param dir 再作成するディレクトリのパス
   */
  public static void recreateDir(Path dir) {
    // ディレクトリを削除する
    deleteDir(dir);

    try {
      // ディレクトリを作成する
      Files.createDirectories(dir);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static boolean exists(Path path) {
    if (Files.exists(path)) {
      return true;
    } else {
      PrintUtil.notExist(path.toString());
      return false;
    }
  }



  /**
   * ファイルをコピーします.
   *
   * @param filePathList コピーするファイルのパス.
   * @param targetDir コピー先のディレクトリのパス.
   */
  public static void copy(List<Path> filePathList, Path targetDir) {
    for (Path source : filePathList) {
      Path target = targetDir.resolve(source.getFileName());
      try {
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
