package se.miun.student.dt042g;

public class MessagePlacement extends Message {
	
	public final ShipPlacement shipPlacement;
	
	
	
	
	public MessagePlacement(ShipPlacement shipPlacement){
		super(EnumHeader.PLACEMENT);
		
		this.shipPlacement = shipPlacement;
	}
	
}
