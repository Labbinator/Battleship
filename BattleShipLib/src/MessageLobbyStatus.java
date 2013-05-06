
public class MessageLobbyStatus extends Message {
	public final EnumLobbyState state;
	
	public MessageLobbyStatus(EnumLobbyState state){
		super(EnumHeader.LOBBYSTATUS);
		this.state = state;
	}
	
	public EnumLobbyState getState(){
		return state;
	}
}
