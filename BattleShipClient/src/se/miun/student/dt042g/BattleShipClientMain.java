package se.miun.student.dt042g;

public class BattleShipClientMain {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		MessageHandlerGUI messageHandler = null;
		
		//Eftersom det �r tv� argument, s� har man valt att k�ra
		//med konsol-f�nstret, detta eftersom det grafiska �r default,
		if (args.length == 2) {
			//Vi m�ste hitta vilket av argumenten som �r ip-addressen.
			if (args[0].toLowerCase().equals("-console") || args[0].toLowerCase().equals("-c")) {
				messageHandler = new MessageHandlerGUI(args[1], true);
			} else {
				messageHandler = new MessageHandlerGUI(args[0], true);
			}
			
		} else if (args.length == 1) {
			//Man har valt att antingen k�ra med konsol eller
			//s� har man valt att skicka med ip-nummer.
			
			if (args[0].toLowerCase().equals("-console") || args[0].toLowerCase().equals("-c")) {
				messageHandler = new MessageHandlerGUI("127.0.0.1", true);
			} else {
				messageHandler = new MessageHandlerGUI(args[0], false);
			}
			
		} else if (args.length == 0) {
			//Man har inte valt n�got, d� blir det default
			//v�rde p� b�da, allts� man k�r med grafiskt och 
			//servern ligger p� samma maskin, ip = 127.0.0.1
			messageHandler = new MessageHandlerGUI("127.0.0.1", false);			
		}
		
		messageHandler.run();
		
		/*
		if (args.length > 0) {
			MessageHandlerGUI messageHandler = new MessageHandlerGUI(args[0]);
			messageHandler.run();
			System.out.println("Hej d�!");
		} else {
			MessageHandlerGUI messageHandler = new MessageHandlerGUI();
			messageHandler.run();
		}
		*/
	}
}
