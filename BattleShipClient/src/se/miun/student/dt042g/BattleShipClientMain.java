package se.miun.student.dt042g;

public class BattleShipClientMain {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length > 0){
			MessageHandler messageHandler = new MessageHandler(args[0]);
			messageHandler.run();
			System.out.println("Hej d�!");
		}else{
			MessageHandlerGUI messageHandler = new MessageHandlerGUI();
			messageHandler.run();
		}
	}
}
