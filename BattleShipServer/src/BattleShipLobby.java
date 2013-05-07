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
	 * Konstruktor för klassen
	 * 
	 * Tar en socket som indata och skapar strömmar
	 * 
	 * @param s En socketanslutning
	 */
	public BattleShipLobby(Socket s){
		sock = s; //Initierar strömmar och sockets
		try { 
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			in = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e1) {
			out = null;
			in = null;
		}
	}
	
	public void run(){
		System.out.println("Lobby tråd nr:" + this.getId() + " skapad");
		System.out.println("För anslutning från ip: " + sock.getInetAddress().getHostAddress());
		
		
		
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
