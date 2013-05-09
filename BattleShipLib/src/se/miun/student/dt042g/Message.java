package se.miun.student.dt042g;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Message implements Serializable{
	
	private final EnumHeader header;
	// public final Double version // Vid eventuell expandering av projektet
	
	public Message(EnumHeader header){
		this.header = header;
	}
	
	public EnumHeader getHeader(){
		return this.header;
	}
}
