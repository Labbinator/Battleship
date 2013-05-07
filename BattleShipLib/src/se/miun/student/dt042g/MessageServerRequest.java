package se.miun.student.dt042g;

public class MessageServerRequest extends Message {
	
	public final EnumRequestType request;
	public final String message;
	public MessageServerRequest( EnumRequestType request, String message) {
		super(EnumHeader.SERVERREQUEST);
		
		this.request = request;
		this.message = message;
	}
	
	public EnumRequestType getRequest(){
		return request;
	}
}
