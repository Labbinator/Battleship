package se.miun.student.dt042g;

import javax.swing.ImageIcon;
	
public class IconHolder {

	public final ImageIcon water;
	public final ImageIcon miss;
	public final ImageIcon hit;
	public final ImageIcon sub;
	public final ImageIcon[] destroyer;
	public final ImageIcon[] destroyer_h;
	public final ImageIcon[] carrier;
	public final ImageIcon[] carrier_h;
	
	private static final IconHolder inst = new IconHolder();
	
	private IconHolder(){
		water = new ImageIcon("img/water.png");
		miss = new ImageIcon("img/miss_trans.png");
		hit = new ImageIcon("img/hit.png");
		sub = new ImageIcon("img/sub.png");
		
		destroyer = new ImageIcon[3];
		destroyer[0] = new ImageIcon("img/destroyer_0.png");
		destroyer[1] = new ImageIcon("img/destroyer_1.png");
		destroyer[2] = new ImageIcon("img/destroyer_2.png");
		
		carrier = new ImageIcon[5];
		carrier[0] = new ImageIcon("img/carrier_0.png");
		carrier[1] = new ImageIcon("img/carrier_1.png");
		carrier[2] = new ImageIcon("img/carrier_2.png");
		carrier[3] = new ImageIcon("img/carrier_3.png");
		carrier[4] = new ImageIcon("img/carrier_4.png");
		
		destroyer_h = new ImageIcon[3];
		destroyer_h[0] = new ImageIcon("img/destroyer_0_h.png");
		destroyer_h[1] = new ImageIcon("img/destroyer_1_h.png");
		destroyer_h[2] = new ImageIcon("img/destroyer_2_h.png");
		
		carrier_h = new ImageIcon[5];
		carrier_h[0] = new ImageIcon("img/carrier_0_h.png");
		carrier_h[1] = new ImageIcon("img/carrier_1_h.png");
		carrier_h[2] = new ImageIcon("img/carrier_2_h.png");
		carrier_h[3] = new ImageIcon("img/carrier_3_h.png");
		carrier_h[4] = new ImageIcon("img/carrier_4_h.png");
		
				
	}
	
	public static IconHolder getInstance(){
			return inst;
	}
}
