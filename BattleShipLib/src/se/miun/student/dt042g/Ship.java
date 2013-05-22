package se.miun.student.dt042g;

import java.io.Serializable;

/*
 *  Ship-klassens logik för checkShot mm är fullt beroende av att 
 *  	startX samt startY är de lägre värdena relativt på griden, ej ubåt.
 * 	
 *  length är inte hårdkodat någonstans och är beroende av korrekt användande:
 *  	-> submarine 1, destroyer 3, carrier 5.
 */
@SuppressWarnings("serial")
public class Ship implements Serializable{
	private int startX;
	private int startY;
	private int length;
	private boolean xAligned; // Sätts till true om skeppet ligger på X-axel
	private final EnumCellStatus type;
	private int hits; // Antal träffar skeppet tål, stegas ner till 0
	
	public Ship( int startX, int startY, int length, boolean xAligned ){
		this.startX = startX;
		this.startY = startY;
		this.length = length;
		this.xAligned = xAligned;
		
		hits = length;
		switch (length){
		case 1:
			type  = EnumCellStatus.SUBMARINE;
			break;
		case 3:
			type = EnumCellStatus.DESTROYER;
			break;
		case 5:
			type = EnumCellStatus.CARRIER;
			break;
		default:
			type = EnumCellStatus.MISS; // Trasig initiering				
		}
	}

	public boolean checkShot(int x, int y){
	
		for(int i=0; i<length; i++){
			
			if( xAligned && x == startX + i && y == startY){
				hits--;
				return true;
			}
			if( !xAligned && x == startX && y == startY + i){
				hits--;
				return true;
			}
		}	
		
		return false;
	}
	
	public boolean isSunk(){
		return ( hits <= 0 );
	}
	
	public int getLength(){
		return length;
	}
	
	public boolean getXAligned(){
		return xAligned;
	}
	
	public int getStartX(){
		return startX;
	}
	
	public int getStartY(){
		return startY;
	}
	
	public EnumCellStatus getType(){
		return type;
	}
	
	public void setStartX(int startX){
		this.startX = startX;
	}
	
	public void setStartY(int startY){
		this.startY = startY;
	}
	
	public void setXAligned(boolean xAligned){
		this.xAligned = xAligned;
	}
	
}
