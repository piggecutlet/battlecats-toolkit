# Battle Cats Toolkit

## 使い方

```
java -jar battlecats-toolkit.jar { decrypt | encrypt } { jp | kr | en | tw }
```

- Java 17 で追加された `java.util.HexFormat` を使用しているため最低でも Java 17 が必要です

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

### debug.keystore

- 生成

```
keytool -genkeypair -v -alias androiddebugkey -keyalg RSA -keysize 2048 -sigalg SHA256withRSA -dname "C=US, O=Android, CN=Android Debug" -validity 36525 -storetype PKCS12 -keystore debug.keystore -storepass android
```

- 確認

```
keytool -list -v -keystore debug.keystore -storepass android
```

## ルール

### コーディング規約

- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) に従う

### バージョニング

- [セマンティック バージョニング](https://semver.org/lang/ja/) に従う
