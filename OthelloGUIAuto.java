package othelloSystem;




import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Othelloシステムの中で画面の表示とオセロのコマ管理、操作処理を行うクラス
 *
 * @author heart watanabe
 * @verison 1.0
 *
 */
// @SuperWarningは気にしなくて良いです
@SuppressWarnings("serial")
public class OthelloGUIAuto extends JFrame implements ActionListener {

	Container contentpane;

	JTextArea infotextArea;

	CellButton[][] Cells = new CellButton[8][8];
	int[][] CellsInfo = new int[8][8];

	//石を置けるかを探索する際に使うベクトル
	int[][] directions = {{-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}};
	//自分と相手の石を記録 nonecolor:-1、黒:0、白:1
	int mycolor, nonecolor, opponentcolor;

	//コマを置くマス目の優先順位
	int[][] priorityofcells = {
			{ 4, 0, 3, 3, 3, 3, 0, 4},
			{ 0, 0, 1, 1, 1, 1, 0, 0},
			{ 3, 1, 2, 2, 2, 2, 1, 3},
			{ 3, 1, 2,-1,-1, 2, 1, 3},
			{ 3, 1, 2,-1,-1, 2, 1, 3},
			{ 3, 1, 2, 2, 2, 2, 1, 3},
			{ 0, 0, 1, 1, 1, 1, 0, 0},
			{ 4, 0, 3, 3, 3, 3, 0, 4}
	};


	/**
	 * GameLoop
	 * GUI上のボタンの有効、無効を制御してactionPerformedが
	 * 呼び出されるタイミングを調整する
	 * 石の色が変わったタイミングで終了またはパスの判定を行い、終了またはパスの場合は相手に通知を送る
	 */
	 void GameLoop(){


		 while(true){
			 int data = DataGetter();
			 if(data == -1){
				 System.out.println("OthelloGUI:actionPerformed:DataGetter:エラーが発生しました");
			 }

			//相手がパスをした場合石の色を変えずに自分のターンになる
			 if(data == -2){
				 infotextArea.append("相手がパスをしました\n");

			//相手から終了の通知(-3)を受け取った場合GameLoopを出る
			 }else if(data == -3){
				 infotextArea.append("ゲーム終了です\n");
				 countCellsNum();
				 infotextArea.append("お疲れ様でした\n");
				 break;

			//相手からの通知がパスでも終了でもない場合相手からのデータを解析して石の色に反映させる
			 }else{
				 int _x = data%10, _y = (data/10)%10;
				 checkCanPut(_y, _x, true, opponentcolor);
				 changeCellsColor();
			 }


			 //自分の石を置けるところがあるかの確認
			if(!((checkCanPutAll(mycolor)))){

				ButtonEnable(false);

				//相手の石を置けるところがあるかの確認
				//相手の石を置ける場合はパスの通知(-2)を送る
				if(checkCanPutAll(opponentcolor)){
					infotextArea.append("置ける所が無いためパスをします\n");
					DataSender(0,-2);//-2が送られる

					//相手の石も置けない場合はゲームを終了する通知を送りGameLoopを抜ける
				}else{
					DataSender(0,-3);
					infotextArea.append("ゲーム終了です\n");
					countCellsNum();
					infotextArea.append("お疲れ様でした\n");
					break;
				}

			}


			try {
				 Thread.sleep(1000); // 1秒(千ミリ秒)間だけ処理を止める
			} catch (InterruptedException e) {
			}
			PlacePiece();

		 }


	}



	/**
	 *PlacePiece
	 *自動的にコマを置く
	 *石を置けるかを判定し、可であればCellsInfoの内容を改変しボタンの色を変えて、データを送信する
	 */
	public void PlacePiece() {


		System.out.println("PlacePieceが呼び出されました");


		int candidate_x = 3, candidate_y = 3;
		Random random = new Random();


		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(priorityofcells[i][j] > priorityofcells[candidate_y][candidate_x]){
					if(checkCanPut(i, j, false, mycolor)){
						candidate_y = i;
						candidate_x = j;
					}
				}else if(priorityofcells[i][j] == priorityofcells[candidate_y][candidate_x]){
					if(checkCanPut(i, j, false, mycolor)){
						if(random.nextInt(2) == 1){//0以上2未満
							candidate_y = i;
							candidate_x = j;
						}
					}
				}
			}
		}


		System.out.println("checkCanPutが呼び出されました");
		checkCanPut(candidate_y, candidate_x, true, mycolor);
		DataSender(candidate_y, candidate_x);
		changeCellsColor();


	}






	/**
	 *actionPerformed
	 *ボタンは常に無効なので押されることはないが実装しなければならないので書いてある
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

	}



	/**
	 *石を置けるかのチェックと石を置いたときのひっくり返し処理を行うメソッド
	 *
	 * @param row 石を置く行
	 * @param column 石を置く列
	 * @param turnOver ひっくり返しを行うかの判断をするための引数 falseでチェックのみ trueでひっくり返しを行う
	 * @param turncolor 置く石の色の引数 0が黒 1が白に対応
	 * @return
	 */
	public boolean checkCanPut(int row, int column, boolean turnOver, int turncolor){

		int reversecolor;

		reversecolor = 1 - turncolor;


		//既にコマが置かれているならfalse
		if(CellsInfo[row][column] != nonecolor)
			return false;

		for(int i = 0; i < 8; i++){
			int y = row, x = column;
			y += directions[i][0];
			x += directions[i][1];
			if((x<0) || (x>=8) || (y<0) || (y>=8))
				continue;
			if(CellsInfo[y][x] != reversecolor)//隣が相手の色か判定
				continue;
			while(true){
				y += directions[i][0];
				x += directions[i][1];

				if((x<0) || (x>=8) || (y<0) || (y>=8))
					break;
				if(CellsInfo[y][x] == nonecolor)
					break;
				if(CellsInfo[y][x] == turncolor){

					if(!turnOver)
						return true;

					int y2 = row, x2 = column;
					while(true){
						CellsInfo[y2][x2] = turncolor;

						y2 += directions[i][0];
						x2 += directions[i][1];

						if((x2 == x) && (y2 == y))
							break;
					}

					break;

				}
			}

		}

		return false;

	}



	/**
	 * 石を置ける場所があるか確認するメソッド
	 * 石を置けるところがある場合はtrue、ない場合はfalseを返す。
	 *
	 */
	public boolean checkCanPutAll(int turnColor){

		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(checkCanPut(i, j, false, turnColor)){
					return true;
				}
			}
		}

		return false;
	}



	/**
	 * ゲーム終了後に石の数を数えて表示するメソッド
	 */
	public void countCellsNum(){

		int black = 0;
		int white = 0;

		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(CellsInfo[i][j] == 0){
					black++;
				}else if(CellsInfo[i][j] == 1){
					white++;
				}
			}
		}


		infotextArea.append("黒：" + black + " 白：" + white + "\n");

		if(black == white){
			infotextArea.append("引き分けです\n");
		}else if(black > white){
			if(mycolor == 0){
				infotextArea.append("You win!\n");
			}else{
				infotextArea.append("You lose\n");
			}
		}else{
			if(mycolor == 1){
				infotextArea.append("You win!\n");
			}else{
				infotextArea.append("You lose\n");
			}
		}


	}



	/**
	 * CellsInfoの情報に基づいてグリッド上の全てのボタンの背景色を更新するメソッド
	 */
	public void changeCellsColor(){


		for(int i=0; i<8; i++){
			for(int j = 0; j<8; j++){
				if(CellsInfo[i][j] == -1){
					Cells[i][j].RepaintColor(Color.GREEN);
				}else if(CellsInfo[i][j] == 0){
					Cells[i][j].RepaintColor(Color.BLACK);
				}else{
					Cells[i][j].RepaintColor(Color.WHITE);
				}
			}
		}

	}



	/**
	 * セルの全ボタンの有効無効を指定するメソッド
	 * @param enable
	 */
	public void ButtonEnable(boolean enable){


		for(int i=0; i<8; i++){
			for(int j = 0; j<8; j++){
				Cells[i][j].setEnabled(enable);
			}
		}


	}


/**
	public abstract void DataSender(int y, int x);


	public abstract int DataGetter();


	public abstract void InitialSetting();
*/


	/**
	 * OthelloGUIのコンストラクタ
	 * GUIを生成する
	 * roleによって石の色が決定する
	 *
	 */
	public OthelloGUIAuto(String role) {
		// コンストラクタ
		super();

		nonecolor = -1;
		if(role.equals("client")){
			mycolor = 0;
			opponentcolor = 1;
		}else if(role.equals("server")){
			mycolor = 1;
			opponentcolor = 0;
		}

		// gui情報を更新
		this.setTitle("OthelloGUIAuto");

		this.setSize(800, 450);

		this.setLayout(new GridLayout(1, 2));

		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(Color.GREEN);

		JPanel rightPanel = new JPanel();

		leftPanel.setLayout(new GridLayout(8, 8));

		rightPanel.setLayout(new GridLayout(1, 1));


		//オセロのボタン8*8を生成し配列Cellsに格納、初期設定は緑
		for(int i = 0; i<8; i++){
			for(int j = 0; j<8; j++){
				Cells[i][j] = new CellButton();
				Cells[i][j].addActionListener(this);
				leftPanel.add(Cells[i][j]);
			}
		}


		for(int i = 0; i<8; i++){
			for(int j = 0; j<8; j++){
				CellsInfo[i][j] = -1;
			}
		}


		CellsInfo[3][3] = 0;
		CellsInfo[3][4] = 1;
		CellsInfo[4][3] = 1;
		CellsInfo[4][4] = 0;

		changeCellsColor();



		infotextArea = new JTextArea(30, 40);
		JScrollPane scrollpane = new JScrollPane(infotextArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollpane.setVisible(true);

		rightPanel.add(scrollpane);

		contentpane = getContentPane();
		// レイアウトを実装するとFrameのサイズを可変にした時に対応したサイズにしてくれる。
		contentpane.setLayout(new GridLayout(1, 2));
		contentpane.add(leftPanel);
		contentpane.add(rightPanel);

		this.addWindowListener(new ClosedListener());

		this.setVisible(true);

		// 自身のIPアドレスを表示する
		/**
		try {
			infotextArea.append("このマシンのIPアドレス:："
					+ java.net.InetAddress.getLocalHost().toString() + "\n");
		} catch (UnknownHostException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}*/


		if(mycolor==0){
			infotextArea.append("あなたの色は黒です\n");
		}else if(mycolor==1){
			infotextArea.append("あなたの色は白です\n");
		}


		InitialSetting();
		GameLoop();

	}




	public OthelloServer server;


	public void InitialSetting(){

		ButtonEnable(false);//ボタンを無効にする

		try{
			server = new OthelloServer();
		}catch(Exception e){
			System.out.println("ServerGUI:InitialSetting:通信エラーが発生しました");
		}
		System.out.println("OthelloServerインスタンスを生成しました");

		infotextArea.append("通信が確立しました\n");

		infotextArea.append("後攻です\n");


	}


	public void DataSender(int y, int x){
		int data = y*10+x;
		try{
			server.sendData(data);
		}catch(Exception e){
			System.out.println("ClientGUI:DataSender:通信エラーが発生しました");
		}

	}

	public int DataGetter(){
		int data;

		try{
			data = server.getData();
			System.out.println(data+" を受信");
		}catch(Exception e){
			System.out.println("ClientGUI:DataGetter:通信エラーが発生しました");
			data = -1;
		}

		return data;

	}



	public static void main(String[] args) {
		// TODO Auto-generated method stub

		new OthelloGUIAuto("server");

	}



}


/**
class ClosedListener extends WindowAdapter {
	public void windowClosing(WindowEvent event) {
		System.out.println("closed window");



		System.exit(0);

	}
}
*/

