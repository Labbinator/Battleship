package se.miun.student.dt042g;
/*
 * Används som bräde för egna skott
 * där man inte känner till motståndarens skepp, bara skotten
 */
public class BlindBoard extends BaseBoard{
	public BlindBoard(){
		
	}
	/*
	 * 
	 * @return true om skottet registrerats, false om skottet är dubblet sedan tidigare
	 */
	public boolean setShot(int x, int y, EnumCellStatus cellStatus){
		
		if(board[x][y] == EnumCellStatus.EMPTY){
			board[x][y] = cellStatus;
			return true;
		}else{
			return false;
		}
		
		
		/*
		if(board[x][y] == EnumCellStatus.EMPTY){
			if(hit){
				board[x][y] = EnumCellStatus.HIT;
			}else{
				board[x][y] = EnumCellStatus.MISS;
			}
			return true;
		}else{
			return false;
		}
		*/
	}
	
}
