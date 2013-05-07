import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class BattleShipClientMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String hostname = "localhost"; //För att testa i början
		int port = 5511;
		
		Object obj = null;
		Socket s = null;
		ObjectOutputStream out;
		ObjectInputStream in;
		
		try { //Öppnar sockets och strömmar
			s = new Socket(hostname, port);
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
		} catch (UnknownHostException e) {
			System.out.println("Kunde inte ansluta till server. Avbryter");
			return;
		} catch (IOException e) {
			System.out.println("Kunde inte ansluta till server. Avbryter");
			return;
		}
		
		while(true){				
			try {
				out.writeObject(new Object()); //Skickar object till server.
				out.flush();
				obj = in.readObject();
			} catch (IOException e) {
				System.out.println("Kunde inte ansluta till server. Server sockets stängda?");
				break;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {//Stänger alla strömmar och sockets
			out.close();
			in.close();
			s.close();
		} catch (IOException e) {
			System.out.println("Kunde inte stänga strömmar/sockets. Detta kan bli ett problem, för servern då.");
		}

	}

}
