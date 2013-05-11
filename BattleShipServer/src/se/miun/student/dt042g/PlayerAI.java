package se.miun.student.dt042g;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class PlayerAI implements PlayerInterface{
	
	Random rand = new Random(System.currentTimeMillis()); 
	GameBoard board = new GameBoard();
	ShipPlacement placement = null;
	EnumRequestType lastRequest = EnumRequestType.ABORTGAME;
	EnumHeader lastMess = EnumHeader.LOBBYCHOICE;
	MessageMove lastMove = null;
	List<Integer> moveList = null;
			
	public PlayerAI(){
		moveList = createGridList();
	}

	/*
	 * Svarar med rätt meddelande beroende 
	 * på vad senaste meddelandet var
	 */
	@Override
	public Message sendMessage() {
		
		if (lastMess == EnumHeader.SERVERREQUEST 
			&& lastRequest == EnumRequestType.PLACEMENT) {
			return new MessagePlacement(placement);	
		}
		
		if (lastMess == EnumHeader.SERVERREQUEST
			&& lastRequest == EnumRequestType.MOVE) {
			return lastMove;
		}
		
		return null;
	}

	@Override
	public void getMessage(Message mess) {
		
		switch (mess.getHeader()) {
		case LOBBYSTATUS:
			lastMess = EnumHeader.LOBBYSTATUS;
			break;
		case PLACEMENT:
			lastMess = EnumHeader.PLACEMENT;
			break;
		case MOVE:
			lastMess = EnumHeader.MOVE;
			break;
		case MOVERESPONSE:
			lastMess = EnumHeader.MOVERESPONSE;
			break;
		case SERVERREQUEST:
			lastMess = EnumHeader.SERVERREQUEST;
			MessageServerRequest tmpMess = (MessageServerRequest) mess;
			serverRequest(tmpMess.getRequest(), tmpMess.getMessage());
			break;
		case LOBBYCHOICE:
			lastMess = EnumHeader.LOBBYCHOICE;
			break;

		default:
			break;
		}							
	}	


	private void serverRequest(EnumRequestType request, String message) {
		switch (request) {
		case PLACEMENT:
			lastRequest = EnumRequestType.PLACEMENT;
			createPlacement();
			break;
		case MOVE:
			lastRequest = EnumRequestType.MOVE;
			createMove();
			break;

		default:
			break;
		}
	}

	/*
	 * Skapar ett slumpat drag
	 */
	private void createMove() {
		int value = rand.nextInt(moveList.size());			
		
		Integer currentMove = moveList.remove(value);
		
		int xPlace = currentMove / 10;
		int yPlace = currentMove % 10;
		
		lastMove = new MessageMove(xPlace, yPlace);
	}

	/*
	 * Skapar en slumpad utsättning av fartyg.
	 */
	private void createPlacement() {
		ShipPlacementBuilder shipBuilder = new ShipPlacementBuilder();
		List<Integer> gridList = createGridList();
		
		placementSubs(shipBuilder, gridList);
		placementDestroyer(shipBuilder, gridList);
		placementCarrier(shipBuilder, gridList);
		
		placement = shipBuilder.getShipPlacement();
		board.setupPlacement(placement);
	}
	
	
	private void placementCarrier(ShipPlacementBuilder shipBuilder, List<Integer> gridList) {
		boolean placementOK = false;
		
		do {
			int value = rand.nextInt(gridList.size());			
			boolean xAlign = rand.nextBoolean();
			
			Integer currentPlacement = gridList.remove(value);
			
			int xPlace = currentPlacement / 10;
			int yPlace = currentPlacement % 10;
			
			if (!shipBuilder.addCarrier(xPlace, yPlace, xAlign)
					&& !shipBuilder.addCarrier(xPlace, yPlace, !xAlign)) {				
					placementOK = false;
				} else {
					placementOK = true;
				}
		} while (!placementOK);
	}

	private void placementDestroyer(ShipPlacementBuilder shipBuilder, List<Integer> gridList) {
		
		for (int i = 0; i < 3; i++) {
			int value = rand.nextInt(gridList.size());			
			boolean xAlign = rand.nextBoolean();
			
			Integer currentPlacement = gridList.remove(value);
			
			int xPlace = currentPlacement / 10;
			int yPlace = currentPlacement % 10;
		
			if (!shipBuilder.addDestroyer(xPlace, yPlace, xAlign)
				&& !shipBuilder.addDestroyer(xPlace, yPlace, !xAlign)) {				
				i--;
			}											
		}		
	}

	private void placementSubs(ShipPlacementBuilder placeBuilder, List<Integer> gridList) {
				
		for (int i = 0; i < 5; i++) {
			
			int value = rand.nextInt(gridList.size()); 
			
			Integer currentPlacement = gridList.remove(value);
			
			int xPlace = currentPlacement / 10;
			int yPlace = currentPlacement % 10;
			
			if (!placeBuilder.addSub(xPlace, yPlace, false)) {				
				i--;
			}			
		}		
	}

	private List<Integer> createGridList() {
		List<Integer> returnList = new ArrayList<Integer>();
		
		for (int i = 0; i < 100; i++) {
			returnList.add(i);
		}
		
		return returnList;
	}

	@Override
	public void close() {
		
	}

	@Override
	public void setPlacement(GameBoard p) {
		this.board = p;		
	}

	@Override
	public GameBoard getPlacement() {		
		return board;
	}
}
