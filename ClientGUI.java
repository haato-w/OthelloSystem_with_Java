package othelloSystem;




public class ClientGUI extends OthelloGUI{

	public OthelloSocket socket;

	public ClientGUI(String role) {
		// TODO 自動生成されたコンストラクター・スタブ
		super(role);
	}




	public void InitialSetting(){


		try{
			socket = new OthelloSocket();
		}catch(Exception e){
			System.out.println("ClientGUI:InitialSetting:通信エラーが発生しました");
		}
		System.out.println("OthelloSocketインスタンスを生成しました");

		infotextArea.append("通信が確立しました\n");

		infotextArea.append("先攻です\n");

	}



	public void DataSender(int y, int x){
		int data = y*10+x;
		try{
			socket.sendData(data);
		}catch(Exception e){
			System.out.println("ClientGUI:DataSender:通信エラーが発生しました");
		}

	}

	public int DataGetter(){
		int data;

		try{
			data = socket.getData();
			System.out.println(data+" を受信");
		}catch(Exception e){
			System.out.println("ClientGUI:DataGetter:通信エラーが発生しました");
			data = -1;
		}

		return data;

	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

		new ClientGUI("client");

	}

}


