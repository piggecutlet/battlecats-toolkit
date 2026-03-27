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
