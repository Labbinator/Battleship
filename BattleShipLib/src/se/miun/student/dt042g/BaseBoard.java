package se.miun.student.dt042g;

public abstract class BaseBoard {
	protected EnumCellStatus[][] board ;
	protected final int WIDTH = 10;
	protected final int HEIGHT = 10;
	public BaseBoard(){
		board = new EnumCellStatus[WIDTH][HEIGHT];
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[i].length; j++){
				board[i][j] = EnumCellStatus.EMPTY;
			}
		}
	}
	
	public EnumCellStatus getPositionValue(int x, int y) {		
		return board[x][y];
	}
	public int getHeight(){
		return HEIGHT;
	}
	
	public int getWidth(){
		return WIDTH;
	}
}
