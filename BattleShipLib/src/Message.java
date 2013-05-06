

public class Message {
	public final EnumHeader header;
	// public final Double version // Vid eventuell expandering av projektet
	
	public Message(EnumHeader header){
		this.header = header;
	}
	
	public EnumHeader getHeader(){
		return this.header;
	}
}
