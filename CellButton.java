package othelloSystem;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import javax.swing.JButton;
import javax.swing.event.ChangeEvent;


public class CellButton extends JButton {



	//private static final String uiClassID = "ButtonUI";


	/******************************************************************************
	  フィールド
	******************************************************************************/
	  /**
	   * マウス・クリック時の座標のインスタンス変数です．
	   */
	  //private Point pressedPoint;
	  /**
	   * 自身を描画するための円のインスタンス変数です．
	   */
	  private Ellipse2D.Double circle;
	  /**
	   * グラデーションのインスタンス変数です．
	   */
	  //private GradientPaint gradient;
	  /**
	   * 透明度のインスタンス変数です．
	   */
	  private Composite comp;


	  private Graphics2D g2d;

	  private Color color;

	  private float transparency;


	  protected transient ChangeEvent changeEvent;

	  //private test testObject = null;

	/******************************************************************************
	  コンストラクタ
	******************************************************************************/
	  /**
	   * コンストラクタです．
	   */
	  public CellButton() {


	    // 親クラスのコンストラクタ呼び出し
	    super();


	    // マウス・イベント・リスナの登録
	    //addMouseListener(this);
	    //addMouseMotionListener(this);


	    //背景色の指定
	    setBackground(Color.GREEN);

	    // インスタンス変数の初期化
	    //pressedPoint=null;
	    circle=null;
	    //gradient=null;
	    comp=null;

	    color = Color.BLACK;

	    transparency = (float) 1.0;


	  }




	/******************************************************************************
	  メソッド
	******************************************************************************/


	  public void RepaintColor(Color n){

		  transparency = (float) 1.0;

		  color = n;

		  repaint();
	  }


	/******************************************************************************
	  ビュー
	******************************************************************************/
	  /**
	   * 背景を描画するメソッドです．
	   *
	   * @param g グラフィックス
	   */
	  protected void paintComponent(Graphics g) {
	    // 親クラスのメソッドを呼び出し
	    super.paintComponent(g);


	    // 描画時に使用する円の生成
	    circle=new Ellipse2D.Double(0,0,getWidth(),getHeight());


	    // 描画時に使用する透明度の生成
	    comp=AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float) transparency);


	    // 描画用グラフィックスの取得
	    g2d=(Graphics2D)g;


	    //色設定
	    g2d.setColor(color);


	    // 透明度設定
	    g2d.setComposite(comp);


	    // 円の描画（塗りつぶし）
	    g2d.fill(circle);
	  }
}
