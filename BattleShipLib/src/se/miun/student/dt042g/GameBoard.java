package se.miun.student.dt042g;

public class GameBoard {	
	
	private ShipPlacement ships;
	private EnumCellStatus[][] board ;
	private final int WIDTH = 10;
	private final int HEIGHT = 10;
	public GameBoard(){
		board = new EnumCellStatus[WIDTH][HEIGHT];
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[i].length; j++){
				board[i][j] = EnumCellStatus.EMPTY;
			}
		}
	}
	
	/*
	 * Returnera false om setup är felaktig?? ej implementerad
	 */
	public boolean setupPlacement(ShipPlacement p){
		ships = p;
		
		for(int i=0; i<p.getNoShips();i++){
			Ship s = p.getShip(i);
			
			for(int j=0; j<s.getLength(); j++){
				if(s.getXAligned()){
					board[s.getStartX() + j][s.getStartY() ] = s.getType();
				}else{
					board[s.getStartX() ][s.getStartY() + j] = s.getType();
				}				
			}
		}
		
		return true;
	}

	public EnumCellStatus getPositionValue(int x, int y) {
		
		return board[x][y];
	}
	
	public EnumMoveResult checkShot(int x, int y){
		EnumCellStatus cell = board[x][y];
		if( cell == EnumCellStatus.CARRIER_HIT
				|| cell == EnumCellStatus.DESTROYER_HIT
				|| cell == EnumCellStatus.SUBMARINE_HIT
				|| cell == EnumCellStatus.HIT ){
			return EnumMoveResult.FAIL;
		}
		for(int i=0; i < ships.getNoShips(); i++){
			if( ships.getShip(i).checkShot(x, y) ){
				board[x][y] = EnumCellStatus.values()[ships.getShip(i).getType().ordinal()+1 ];
				if(ships.getShip(i).isSunk()){
					return EnumMoveResult.SINK;
				}else{
					return EnumMoveResult.HIT;
				}
			}
		}
		return EnumMoveResult.MISS;
	}
	
	public void setShot(int x, int y){
		
	}
	public int getHeight(){
		return HEIGHT;
	}
	
	public int getWidth(){
		return WIDTH;
	}
}
