package se.miun.student.dt042g;

public class ShipPlacement {
	
	/*
	 * Ful konstruktor men ger rigid klass och publika Ship-objekt
	 *  
	 */
	
	private final int NO_SHIPS = 9;
	private Ship[] ships;
	
	
	public ShipPlacement(
			Ship sub1,Ship sub2,Ship sub3,Ship sub4,Ship sub5,
			Ship destroyer1,Ship destroyer2,Ship destroyer3,
			Ship carrier
			){
		
		ships = new Ship[NO_SHIPS];
		
		ships[0] = sub1;
		ships[1] = sub2;
		ships[2] = sub3;
		ships[3] = sub4;
		ships[4] = sub5;
				
		ships[5] = destroyer1;
		ships[6] = destroyer2;
		ships[7] = destroyer3;
		
		ships[8] = carrier;
		
	}
	
	public Ship getShip(int i){
		if(i<NO_SHIPS){
			return ships[i];		
		}else{			
			return null;
		}
	}
	
	public int getNoShips(){
		return NO_SHIPS;
	}	
}
