package othelloSystem;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
// Socketクラスを使用するため
// OutputStreamWriter, BufferedReaderを使用するため

public class OthelloSocket {

	Socket sok = null; // 接続用Socket
	OutputStreamWriter out; // 出力用ストリーム
	BufferedReader in; // 入力用ストリーム
	String from = null; // 相手から受けるデータの入力用
	String to = null; // 相手へ送るデータの入力用
	String host = "localhost"; // ホスト名
	int port_number = 15000; // ポートナンバー


	public OthelloSocket() throws Exception {

		// hostマシン上のport_number番号で開いているソケットへ
		// 接続するためのソケットを作成する．
		sok = new Socket(host, port_number);


		/* 準備：データ入力ストリームの定義--ソケットからデータを
		   取ってくる．sok -> in  */
		in = new BufferedReader
				(new InputStreamReader(sok.getInputStream(), "UTF8"));


		/* 準備：データ出力ストリームの定義--ソケットにデータを
		   書き込む．  sok <- out */
		out = new OutputStreamWriter(sok.getOutputStream(),"UTF8");


	}



	//サーバーにインデックスを送信するメソッド
	public int getData() throws Exception {

		from = in.readLine();

		return Integer.parseInt(from);

	}



	//サーバーからインデックスを受信するメソッド
	public void sendData(int data) throws Exception {

		to = data+""+"\n";
		out.write(to);
		out.flush();

	}



	public static void main(String[] args) {
		// TODO Auto-generated method stub

		OthelloSocket socket = null;
		int data;

		try{
			socket = new OthelloSocket();
		}catch(Exception e){
			System.out.println("OthelloSocket:エラー1");
		}


		try{
			data = socket.getData();
			System.out.println(data);
		}catch(Exception e){
			System.out.println("OthelloSocket:エラー2");
		}


		try{
			socket.sendData(34);
		}catch(Exception e){
			System.out.println("OthelloSocket:エラー3");
		}

	}

}
