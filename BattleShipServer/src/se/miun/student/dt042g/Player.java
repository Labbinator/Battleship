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
			e.printStackTrace();
		}
	}
	
	@Override
	public Message sendMessage() {
		try {
			mess = (Message)in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			close();
			//e.printStackTrace();
		}
		
		return mess;
	}

	@Override
	public void getMessage(Message mess) {
		try {
			out.writeObject(mess);
			out.flush();
		} catch (IOException e) {
			close();
			//e.printStackTrace();
			
		}
	}

	@Override
	public void close() {
		try{
			in.close();
			out.close();
			player.close();
		} catch(IOException e){
			e.printStackTrace();
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
