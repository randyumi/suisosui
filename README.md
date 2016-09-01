Suisosui
==
Android端末をペットボトルの上とかに置くと何故か**無料で**水素水ができるアプリ。
水素水は無料で作れる時代になった。

# 対応バージョン
\> Android 2.3

# インストール
```
git clone git@github.com:randyumi/suisosui.git
cd suisosui
./gradlew build
adb install app/build/outputs/apk/app-debug.apk
```


# 使用方法
1. アプリを起動する(この時なぜかバッテリーが50%以上必要)
2. 「SUISOSUIを作る」みたいな感じのボタンをタップ
3. 作るボタンみたいなやつをタップして寝かせたペットボトルの上に画面が向かうように置く、または画面をそのままペットに向かうように置く
4. 任意の時間放置する
5. ペットボトルの水が水素水になりそう


# TODO
* 波動の放出エリアに広告を表示、課金すると広告が非表示になり波動がより多く放出できると煽るようにする。

# ライセンス
[NYSL Version0.9982](http://www.kmonos.net/nysl/)
