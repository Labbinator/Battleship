package se.miun.student.dt042g;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ShipPlacement implements Serializable{
	
	/*
	 * Ful konstruktor men ger rigid klass och publika Ship-objekt
	 *  
	 */
	
	private final int NO_SHIPS = 9;
	private Ship[] ships;
	//private final int gridW = 10;
	//private final int gridH = 10;
	
	private int currentNoOfShips = 0;
	
	public ShipPlacement() {
		ships = new Ship[NO_SHIPS];
	}
	
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
		return currentNoOfShips;
	}
	
	public void addShip(Ship ship, EnumCellStatus shipType) {
		
		ships[currentNoOfShips++] = ship;		
		
	}
	
	
	//public boolean isGood(){
	//	boolean[][] grid = new boolean[gridW][gridH];
	//	
	//	for(int i=0; i<gridW;i++){
	//		for(int j=0;j<gridH;j++){
	//			grid[i][j] = false;
	//		}
	//	}
		
	//	for(int i=0; i < NO_SHIPS; i++){
			/*
			 * Check if ship[i] all positions are available, ie FALSE
			 */
	//		for(int j=0; j<ships[i].getLength(); j++){
	//			if(ships[i].getXAligned()){
	//				if( grid[ships[i].getStartX()+j] [ships[i].getStartY()] ){
	//					
	//					return false;
	//				}
	//			}else{
	//				if( grid[ships[i].getStartX()] [ships[i].getStartY()+j] ){
						/*DEBUG
						printGrid(grid);
						System.out.println("Illegal: index: " + i
								+ ", start x: " + ships[i].getStartX()
								+ ", start y: " + ships[i].getStartY()
								+ ", length: " + ships[i].getLength());
						*/
	//					return false;
	//				}
	//			}				
	//		}
			/*
			 *  Paint ships and positions around to true in grid
			 */
			
	//		int x_;
	//		int y_;
	//		if( ships[i].getXAligned() ){
	//			x_ = ships[i].getLength() + 1 ;
	//			y_ = 2;
	//		}else{
	//			x_ = 2;
	//			y_ = ships[i].getLength() + 1;
	//		}
			
			
	//		for(int j=-1; j < x_ ; j++){
	//			for(int k=-1; k < y_; k++){
	//				paintBool( ships[i].getStartX()+j, 
	//						ships[i].getStartY()+k,
	//						grid );
	//			}
	//		}
	//	}
		/* DEBUG
		System.out.println("Placement is good");
		printGrid(grid);
		*/
	//	return true;
	//}
	
	/*
	private static void paintBool(int x, int y, boolean[][] grid){
		if( x >= 0 
				&& x < grid[0].length 
				&& 	y >= 0 
				&& y < grid.length){
			grid[x][y] = true;
		}
	}
	*/
	/* 
	 * printGrid only used for debug
	 */
	/*
	private void printGrid(boolean[][] grid){
		for( int i=0; i < grid.length; i++){
			for(int j=0; j<grid.length; j++){
				if(grid[j][i]){
					System.out.print(" O ");
				}else{
					System.out.print(" X ");
				}
			}
			System.out.println("");
		}
	}
	*/
}
