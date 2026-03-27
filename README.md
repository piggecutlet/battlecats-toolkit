# Battle Cats Toolkit

## 要件

- `java.util.HexFormat` を使用しているため Java 17 以上が必要です

## 使い方

1. https://github.com/piggecutlet/battlecats-toolkit/releases で Zip をダウンロード
1. ZIP を展開
1. `app.apk` か `app.xapk` を `battlecats-toolkit.jar` と同じフォルダーに配置
1. コマンドを実行

- Usage

```
java -jar battlecats-toolkit.jar { decrypt | encrypt } { jp | kr | en | tw }
```

- 例

```
java -jar battlecats-toolkit.jar decrypt jp
```

```
java -jar battlecats-toolkit.jar encrypt jp
```

- decrypt

1. `app.xapk` がある場合は `app.apk` に変換します
1. `app.apk` がある場合は `workspace/app` に展開します
1. `workspace/encrypted` にあるファイルを復号し `workspace/decrypted` に配置します

※ ファイル名に `Local` が付くファイルと `Server` が付くファイルは復号処理が異なります

- encrypt

1. `workspace/decrypted` にあるファイルを暗号化し `workspace/encrypted` に配置します
1. `workspace/app` がある場合は `app.apk` にビルドし、署名します

- JAR だと `encrypt` に数分かかりますが Eclipse だと数秒で完了するため Eclipse をお持ちの方はリポジトリをクローンし Eclipse で実行することをオススメします

## 外部ライブラリ

### APKEditor.jar

- バージョン

  `1.4.8`

- GitHub

  https://github.com/REAndroid/APKEditor

### apksigner.jar

- バージョン

  `Android SDK Build Tool 36.1.0`

- パス

  `C:\Users\%USERNAME%\AppData\Local\Android\Sdk\build-tools\36.1.0\lib\apksigner.jar`

### apktool.jar

- バージョン

  `2.12.1`

- GitHub
  https://github.com/iBotPeaches/Apktool

## コーディング規約

- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)

- [Semantic Versioning 2.0.0](https://semver.org)
