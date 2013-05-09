package se.miun.student.dt042g;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class BattleShipLobby{
	
	private Socket playerOneSocket;
	private Socket playerTwoSocket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private ServerSocket ss;
	private Message inputData;
	private EnumLobbyChoice choice;
	private EnumLobbyState state;

	/**
	 * Konstruktor för klassen
	 * 
	 * Tar tar in en port som den ska lyssna på
	 * 
	 * @param port int
	 */
	public BattleShipLobby(int port){
		
		state = EnumLobbyState.EMPTY;
		
		try {
			ss = new ServerSocket(port); //Skapar en serversocket
			System.out.println("BattlShip Server lyssnar på port: " + port);
		} catch (IOException e) {
			System.out.println("BattlShip Server failed to create ServerSocket on: " + port);
			return;
		}
	}
	
	public void run(){
		while(true){
			
			playerOneSocket = getPlayer();
			state = EnumLobbyState.PLAYERWAITING;
			playerTwoSocket = getPlayer();
			state = EnumLobbyState.EMPTY;
			
			new BattleShipGameThred(playerOneSocket, playerTwoSocket);
		}
	}

	private Socket getPlayer() {
		
		while(true){ //Evig loop, tar emot nya spelare
			try {
				Socket player = ss.accept();
				out = new ObjectOutputStream(player.getOutputStream());
				in = new ObjectInputStream(player.getInputStream());
				
				System.out.println("Anslutning etablerad från: " + player.getInetAddress().getHostAddress());

				//Skicka lobbyinfo till spelare
				out.writeObject(new MessageLobbyStatus(state));
				
				while(true){
					inputData = (Message)in.readObject();
					if(inputData.getHeader() != EnumHeader.LOBBYCHOICE){
						out.writeObject(new MessageLobbyStatus(state));
						continue;
						//Varför skickar klienten nåt annat än den blev ombedd att skicka???
						//På nåt sätt får vi försöka igen här.... Lägga allt i en while loop till???
					}else{
						choice = ((MessageLobbyChoice)inputData).getChoice();
						break;
					}
				}
				
				//om spelare vill spela mot ai så skapas spel och continue körs för att forsätta lyssna efter player one
				if(choice == EnumLobbyChoice.WAIT_FOR_PLAYER){
					System.out.println("Spelare har valt att vänta i lobby på en annan spelare.");
					return player; //För att gå ur while loopen och ta emot nästa spelare.
				}else{
					System.out.println("Spelare har valt att spela mot AI.");
					new BattleShipGameThred(player, null).start();
					continue; //Går till början, av while loopen för att hämta en ny player 
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}