package se.miun.student.dt042g;

import java.io.Serializable;

public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 69055617416881282L;
	public final EnumHeader header;
	// public final Double version // Vid eventuell expandering av projektet
	
	public Message(EnumHeader header){
		this.header = header;
	}
	
	public EnumHeader getHeader(){
		return this.header;
	}
}
