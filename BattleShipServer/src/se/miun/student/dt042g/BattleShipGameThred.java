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
		//H�r startar sj�lva spelet IF playerTwo == NULL s� spelas mot AI
		
		//F�rst vill vi ha uppst�lniongen sen kommer spelarna alternera om att skjuta
	}
	
	
	private void dafuq(){/*  Klippte in koden h�r.... den ska g�ras om och fixas iordning
	
		while(true){//Evil loop, gjord f�r testsyfte. Tar emot data och printar lite info.
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
			try{ //Efter while loopen st�nger sockets och str�mmar
				in.close();
				out.close();
				//sock.close();
				System.out.println("Tr�d:" + this.getId() + " fr�nkopplad");
			} catch(IOException e){
				System.out.println("Tr�d:" + this.getId() + " kunde inte st�nga str�mmar och/eller socket." );
			}
			*/
	}
}
