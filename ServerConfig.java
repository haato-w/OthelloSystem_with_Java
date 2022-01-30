package othelloSystem;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ServerConfig {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		JFrame frame = new JFrame();
		int option = JOptionPane.showConfirmDialog(frame, "自動応答で起動しますか？");

		switch(option){
			case 0:
				new OthelloGUIAuto("server");
				break;
			case 1:
				new ServerGUI("server");
				break;
			default:
				break;
		}
	}

}
