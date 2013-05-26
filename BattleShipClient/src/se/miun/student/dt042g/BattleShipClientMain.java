package se.miun.student.dt042g;

public class BattleShipClientMain {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		MessageHandlerGUI messageHandler = null;
		
		//Eftersom det är två argument, så har man valt att köra
		//med konsol-fönstret, detta eftersom det grafiska är default,
		if (args.length == 2) {
			//Vi måste hitta vilket av argumenten som är ip-addressen.
			if (args[0].toLowerCase().equals("-console") || args[0].toLowerCase().equals("-c")) {
				messageHandler = new MessageHandlerGUI(args[1], true);
			} else {
				messageHandler = new MessageHandlerGUI(args[0], true);
			}
			
		} else if (args.length == 1) {
			//Man har valt att antingen köra med konsol eller
			//så har man valt att skicka med ip-nummer.
			
			if (args[0].toLowerCase().equals("-console") || args[0].toLowerCase().equals("-c")) {
				messageHandler = new MessageHandlerGUI("127.0.0.1", true);
			} else {
				messageHandler = new MessageHandlerGUI(args[0], false);
			}
			
		} else if (args.length == 0) {
			//Man har inte valt något, då blir det default
			//värde på båda, alltså man kör med grafiskt och 
			//servern ligger på samma maskin, ip = 127.0.0.1
			messageHandler = new MessageHandlerGUI("127.0.0.1", false);			
		}
		
		messageHandler.run();
		
		/*
		if (args.length > 0) {
			MessageHandlerGUI messageHandler = new MessageHandlerGUI(args[0]);
			messageHandler.run();
			System.out.println("Hej då!");
		} else {
			MessageHandlerGUI messageHandler = new MessageHandlerGUI();
			messageHandler.run();
		}
		*/
	}
}
