package se.miun.student.dt042g;

public class MessageLobbyChoice extends Message{ 

	private final EnumLobbyChoice choice;

	public MessageLobbyChoice(EnumLobbyChoice choice) {
		super(EnumHeader.LOBBYCHOICE);
		
		this.choice = choice;
	}
	
	public EnumLobbyChoice getChoice(){
		return choice;
	}

	
}
