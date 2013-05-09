package se.miun.student.dt042g;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BattleShipGameThred extends Thread {

	Socket playerOne;
	Socket playerTwo;
	ObjectInputStream playerOneIn;
	ObjectOutputStream playerOneOut;
	ObjectInputStream playerTwoIn;
	ObjectOutputStream playerTwoOut;
	BattleShipAI ai = null;
	boolean withAI = false;
	
	public BattleShipGameThred(Socket playerOne, Socket playerTwo){
		this.playerOne = playerOne;
		this.playerTwo = playerTwo;
		
		if ( playerTwo == null ){
			//Spela mot AI
			//ai = new BattleShipAI();
			//withAI = true;
		}else{
			
		}
	}
	
	public void run(){
		//Här startar själva spelet IF playerTwo == NULL så spelas mot AI
		
		//Först vill vi ha uppstälniongen sen kommer spelarna alternera om att skjuta
	}
	
	
	private void dafuq(){/*  Klippte in koden här.... den ska göras om och fixas iordning
	
		while(true){//Evil loop, gjord för testsyfte. Tar emot data och printar lite info.
			Message mess;	
			try {
				mess = (Message)in.readObject();
				
				EnumHeader en = mess.getHeader();
				
				switch (en) {
				case LOBBYSTATUS:
					mess = (MessageLobbyStatus)mess;
					break;
				case PLACEMENT:
					
					break;
				case MOVE:
					break;
				case MOVERESPONSE:
					
					break;
				case SERVERREQUEST:
					MessageServerRequest servermess = (MessageServerRequest)mess;
					System.out.println(servermess.getRequest() + " " + servermess.getMessage());
					 
					break;
				default:
					break;
				}
				
				out.writeObject(mess);
			} catch (ClassNotFoundException | IOException e) {
				break;
			}
			try{ //Efter while loopen stänger sockets och strömmar
				in.close();
				out.close();
				//sock.close();
				System.out.println("Tråd:" + this.getId() + " frånkopplad");
			} catch(IOException e){
				System.out.println("Tråd:" + this.getId() + " kunde inte stänga strömmar och/eller socket." );
			}
			*/
	}
}
