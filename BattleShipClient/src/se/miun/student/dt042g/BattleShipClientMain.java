package se.miun.student.dt042g;

public class BattleShipClientMain {
	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		
		String ipAddress = "127.0.0.1";
		
		if (args.length == 1){
			ipAddress = args[0];
		}
		
		MessageHandler messageHandler = new MessageHandler(ipAddress);
		messageHandler.run();
		System.out.println("Hej då!");
	}
}
