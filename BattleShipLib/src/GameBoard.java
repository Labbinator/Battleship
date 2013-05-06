
public class GameBoard {
	
	public ShipPlacement ships;
	public EnumCellStatus[][] board ;
	public final int WIDTH = 10;
	public final int HEIGHT = 10;
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
		
		for(int i=0; i<p.NO_SHIPS;i++){
			Ship s = p.getShip(i);
			
			for(int j=0; j<s.length; j++){
				if(s.xAligned){
					board[s.startX + j][s.startY ] = s.type;
				}else{
					board[s.startX ][s.startY + j] = s.type;
				}				
			}
		}
		
		return true;
	}
	
}
