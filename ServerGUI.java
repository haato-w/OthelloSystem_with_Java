package othelloSystem;




public class ServerGUI extends OthelloGUI {

	public OthelloServer server;

	public ServerGUI(String role) {
		// TODO 自動生成されたコンストラクター・スタブ
		super(role);

	}



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

		new ServerGUI("server");

	}

}


