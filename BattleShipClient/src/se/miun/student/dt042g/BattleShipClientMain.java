package se.miun.student.dt042g;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class BattleShipClientMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String hostname = "127.0.0.1"; //F�r att testa i b�rjan (localhost)
		int port = 5511;
		
		Message mess = null;
		Socket s = null;
		ObjectOutputStream out;
		ObjectInputStream in;
		
		try { //�ppnar sockets och str�mmar
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
				mess = new MessageServerRequest(EnumRequestType.ABORTGAME, "Testar allt detta :p");
				out.writeObject(mess); //Skickar object till server.
				out.flush();
				mess = (Message)in.readObject();
				break;
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Kunde skriva eller ta emot data fr�n servern. Avbryter");
				break;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {//St�nger alla str�mmar och sockets
			out.close();
			in.close();
			s.close();
		} catch (IOException e) {
			System.out.println("Kunde inte st�nga str�mmar/sockets. Detta kan bli ett problem, f�r servern d�.");
		}

	}

}
