import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class BattleShipLobby extends Thread{
	private Socket sock;
	ObjectInputStream in;
	BufferedWriter out;
	String result;
	
	/**
	 * Konstruktor f�r klassen
	 * 
	 * Tar en socket som indata och skapar str�mmar
	 * 
	 * @param s En socketanslutning
	 */
	public BattleShipLobby(Socket s){
		sock = s; //Initierar str�mmar och sockets
		try { 
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			in = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e1) {
			out = null;
			in = null;
		}
	}
	
	public void run(){
		System.out.println("Lobby tr�d nr:" + this.getId() + " skapad");
		System.out.println("F�r anslutning fr�n ip: " + sock.getInetAddress().getHostAddress());
		
		
		
		try{ //Efter while loopen st�nger sockets och str�mmar
			in.close();
			out.close();
			sock.close();
			System.out.println("Tr�d:" + this.getId() + " fr�nkopplad");
		} catch(IOException e){
			System.out.println("Tr�d:" + this.getId() + " kunde inte st�nga str�mmar och/eller socket." );
		}
	}
}
