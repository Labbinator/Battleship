import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class BattleShipServerMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ServerSocket ss;
		Socket s;
		
		int port = 5511; //Använder just nu enbart port 5511, tror inte något annat brukar använda den porten.
		
		try {
			ss = new ServerSocket(port); //Skapar en serversocket
			System.out.println("Server lyssnar på port: " + port);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}			
		while(true){ //Evig loop.
			try {
				s = ss.accept();
				new BattleShipLobby(s).start();
				System.out.println("Anslutning etablerad från: " + s.getInetAddress().getHostAddress());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		
		

	}

}
