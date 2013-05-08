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

	/**
	 * Konstruktor för klassen
	 * 
	 * Tar tar in en port som den ska lyssna på
	 * 
	 * @param port int
	 */
	public BattleShipLobby(int port){
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
			playerTwoSocket = getPlayer();
			
			new BattleShipGameThred(playerOneSocket, playerTwoSocket);
		}
	}

	private Socket getPlayer() {
		
		while(true){ //Evig loop, tar emot nya spelare
			try {
				Socket player = ss.accept();
				out = new ObjectOutputStream(player.getOutputStream());
				in = new ObjectInputStream(player.getInputStream());
				
				//Skicka lobbyinfo till spelare
				out.writeObject(new MessageLobbyStatus(EnumLobbyState.EMPTY));
				inputData = (Message)in.readObject();
				
				if(inputData.getHeader() != EnumHeader.LOBBYCHOICE){
					//Varför skickar klienten nåt annat än den blev ombedd att skicka???
					//På nåt sätt får vi försöka igen här.... Lägga allt i en while loop till???
				}else{
					choice = ((MessageLobbyChoice)inputData).getChoice();
				}
				
				//om spelare vill spela mot ai så skapas spel och continue körs för att forsätta lyssna efter player one
				
				System.out.println("Anslutning etablerad från: " + player.getInetAddress().getHostAddress());

				
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