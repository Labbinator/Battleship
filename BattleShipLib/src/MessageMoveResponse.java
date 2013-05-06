
public class MessageMoveResponse extends Message {
	public final EnumMoveResult result;
	
	MessageMoveResponse(EnumMoveResult result){
		super(EnumHeader.MOVERESPONSE);		
		this.result = result;
	}
}
