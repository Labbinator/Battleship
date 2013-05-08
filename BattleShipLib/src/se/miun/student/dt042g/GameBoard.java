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

	public EnumCellStatus getPositionValue(int outer, int inner) {
		
		return board[outer][inner];
	}
	
	public int getHeight(){
		return HEIGHT;
	}
	
	public int getWidth(){
		return WIDTH;
	}
}
