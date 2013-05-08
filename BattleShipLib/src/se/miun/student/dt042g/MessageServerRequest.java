package se.miun.student.dt042g;

public class MessageServerRequest extends Message {
	
	private final EnumRequestType request;
	private final String message;
	public MessageServerRequest( EnumRequestType request, String message) {
		super(EnumHeader.SERVERREQUEST);
		
		this.request = request;
		this.message = message;
	}
	
	public EnumRequestType getRequest(){
		return request;
	}
	
	public String getMessage(){
		return message;
	}
}
