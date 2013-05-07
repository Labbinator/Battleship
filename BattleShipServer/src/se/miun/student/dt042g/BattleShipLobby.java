package se.miun.student.dt042g;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class BattleShipLobby extends Thread{
	private Socket sock;
	ObjectInputStream in;
	ObjectOutputStream out;

	
	/**
	 * Konstruktor för klassen
	 * 
	 * Tar en socket som indata och skapar strömmar
	 * 
	 * @param s En socketanslutning
	 */
	public BattleShipLobby(Socket s){
		sock = s; //Initierar strömmar och sockets
		try { 
			out = new ObjectOutputStream(sock.getOutputStream());
			in = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e1) {
			out = null;
			in = null;
		}
	}
	
	public void run(){
		System.out.println("Lobby tråd nr:" + this.getId() + " skapad");
		System.out.println("För anslutning från ip: " + sock.getInetAddress().getHostAddress());
		
		while(true){//Evil loop, gjord för testsyfte. Tar emot data och printar lite info.
			Message mess;	
			try {
				mess = (Message)in.readObject();
				
				EnumHeader en = mess.header;
				
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
					System.out.println(servermess.request + " " + servermess.message);
					 
					break;
				default:
					break;
				}
				
				out.writeObject(mess);
			} catch (ClassNotFoundException | IOException e) {
				break;
			}
			
		}
		try{ //Efter while loopen stänger sockets och strömmar
			in.close();
			out.close();
			sock.close();
			System.out.println("Tråd:" + this.getId() + " frånkopplad");
		} catch(IOException e){
			System.out.println("Tråd:" + this.getId() + " kunde inte stänga strömmar och/eller socket." );
		}
	}
}
