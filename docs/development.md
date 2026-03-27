# 開発メモ

## ToDo

- ログの改善 `lib/Logger.java` とか？

- apktool v3 系に移行する
  --debug が --debugable になったり変更が色々あるので確認する

## 未使用

- パッケージ `piggecutlet.eventdata`

- クラス `ApkAligner`
- クラス `ApkDownloader`

## debug.keystore

- 生成

```
keytool -genkeypair -v -alias androiddebugkey -keyalg RSA -keysize 2048 -sigalg SHA256withRSA -dname "C=US, O=Android, CN=Android Debug" -validity 36525 -storetype PKCS12 -keystore debug.keystore -storepass android
```

- 確認

```
keytool -list -v -keystore debug.keystore -storepass android
```
