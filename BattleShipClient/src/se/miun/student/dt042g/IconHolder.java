package se.miun.student.dt042g;

import javax.swing.ImageIcon;
	
public class IconHolder {

	private final ImageIcon water;
	private final ImageIcon miss;
	private final ImageIcon hit;
	private final ImageIcon sub;
	private final ImageIcon[] destroyer;
	private final ImageIcon[] destroyer_h;
	private final ImageIcon[] carrier;
	private final ImageIcon[] carrier_h;
	
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
	
	public ImageIcon getWater() {
		return water;
	}
	
	public ImageIcon getMiss() {
		return miss;
	}

	public ImageIcon getHit() {
		return hit;
	}

	public ImageIcon getSub() {
		return sub;
	}
	
	public ImageIcon getDestroyer(Integer index) {
		if (index >= 0 && index < destroyer.length) {
			return destroyer[index];
		}
		return null;
	}
	
	public ImageIcon getDestroyer_h(Integer index) {
		if (index >= 0 && index < destroyer_h.length ) {
			return destroyer_h[index];
		}
		return null;
	}

	public ImageIcon getCarrier(Integer index) {
		if (index >= 0 && index < carrier.length) {
			return carrier[index];
		}
		return null;
	}
	
	public ImageIcon getCarrier_h(Integer index) {
		if (index >= 0 && index < carrier_h.length) {
			return carrier_h[index];
		}
		return null;
	}
}
