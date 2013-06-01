package se.miun.student.dt042g;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Player implements PlayerInterface {

	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket player;
	private Message mess;
	private GameBoard board;
	
	public Player(Socket s){
		this.player = s;
		try {
			out = new ObjectOutputStream(player.getOutputStream());
			in = new ObjectInputStream(player.getInputStream());
		} catch (IOException e) {
			System.out.println("Undantag i Player.java konstruktor. Kunde inte skapa objektstr�mmar.");
		}
	}
	
	@Override
	public Message sendMessage() throws Exception {
		mess = (Message)in.readObject();
		
		return mess;
	}

	@Override
	public void getMessage(Message mess) throws Exception {
		out.writeObject(mess);
		out.flush();
	}

	@Override
	public void close() {
		try{
			in.close();
		} catch(IOException e){
			System.out.println("Undantag i Player.java::close.");
			System.out.println("Servern kunde inte st�nga instr�m.");
		}
		try {
			out.close();
		} catch (IOException e) {
			System.out.println("Undantag i Player.java::close.");
			System.out.println("Servern kunde inte st�nga utstr�m, detta h�nder om spelare st�ngt ner sin anslutning");
		}
		try {
			player.close();
		} catch (IOException e) {
			System.out.println("Undantag i Player.java::close.");
			System.out.println("Servern kunde inte st�nga socket.");
		}
	}

	@Override
	public void setPlacement(GameBoard board) {
		this.board = board;
	}

	@Override
	public GameBoard getPlacement() {
		return board;
	}
}
