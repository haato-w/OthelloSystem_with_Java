# OthelloSystem_with_Java

通信ででオセロ対戦を行うプログラムです。<br>
人との対戦か、自動で打ち返してくるシステムとの対戦かを選ぶことができます。<br>
対戦終了後、プレイヤーそれぞれのコマの数と勝敗結果がGUIに表示されます。<br>
<br>
![Othello_with_auto_cropped](https://user-images.githubusercontent.com/64346215/151690341-c9ad3236-87c3-4258-881b-17fe757ce02d.gif)

<br><br>
## 【実行方法】
1. Javaが実行できる環境にプログラムを設置する<br>
2. ServerConfig.java を実行<br>
3. 自動応答か否かを選択<br>
4. ClientGUI.java を実行<br>
5. ゲーム開始<br>
6. ゲーム終了後 結果が表示される<br>
<br>
サーバー機能選択画面<br>
<img width="250" alt="ServerConfigGUI" src="https://user-images.githubusercontent.com/64346215/151689460-2c18c141-de72-4801-a06f-5c108762a565.PNG">
ゲーム終了時の画面<br>
<img width="460" alt="勝ち画面" src="https://user-images.githubusercontent.com/64346215/151689479-e6cdbc62-e49c-4f75-833b-dce2418cdc4e.PNG">
<br><br>

## 【詳細】
クライアントとサーバーの通信を行います。（サーバーは一人しか同時に相手できません）<br>
最初に人間対戦か自動応答のどちらのサーバーを選ぶかを設定します。<br>

言語：Java<br>
GUI：Swing<br>
<br>

CellButton.java     ：各セルのボタンGUIを実装したクラス<br>
ClientGUI.java      ：クライアント側に必要な機能を呼び出すクラス<br>
OthelloGUI.java     ：オセロのGUI画面とゲーム処理全般を実装したクラス<br>
OthelloGUIAuto.java ：オセロのGUI画面と自動応答のゲーム処理全般を実装したクラス<br>
OthelloServer.java  ：サーバー側の通信処理を実装したクラス<br>
OthelloSocket.java  ：クライアント側の通信処理を実装したクラス<br>
ServerConfig.java   ：人間対戦か自動応答のどちらのサーバー側処理を呼び出すかを確認するGUIを実装<br>
ServerGUI.java      ：サーバー側に必要な機能を呼び出すクラス<br>

