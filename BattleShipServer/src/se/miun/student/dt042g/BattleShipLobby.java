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
	 * Konstruktor f�r klassen
	 * 
	 * Tar tar in en port som den ska lyssna p�
	 * 
	 * @param port int
	 */
	public BattleShipLobby(int port){
		try {
			ss = new ServerSocket(port); //Skapar en serversocket
			System.out.println("BattlShip Server lyssnar p� port: " + port);
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
					//Varf�r skickar klienten n�t annat �n den blev ombedd att skicka???
					//P� n�t s�tt f�r vi f�rs�ka igen h�r.... L�gga allt i en while loop till???
				}else{
					choice = ((MessageLobbyChoice)inputData).getChoice();
				}
				
				//om spelare vill spela mot ai s� skapas spel och continue k�rs f�r att fors�tta lyssna efter player one
				
				System.out.println("Anslutning etablerad fr�n: " + player.getInetAddress().getHostAddress());

				
				if(choice == EnumLobbyChoice.WAIT_FOR_PLAYER){
					System.out.println("Spelare har valt att v�nta i lobby p� en annan spelare.");
					return player; //F�r att g� ur while loopen och ta emot n�sta spelare.
				}else{
					System.out.println("Spelare har valt att spela mot AI.");
					new BattleShipGameThred(player, null).start();
					continue; //G�r till b�rjan, av while loopen f�r att h�mta en ny player 
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}