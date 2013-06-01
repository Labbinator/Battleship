package se.miun.student.dt042g;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerAISmart implements PlayerInterface {

	Random rand = new Random(System.currentTimeMillis()); 
	GameBoard board = new GameBoard();
	ShipPlacement placement = null;
	EnumRequestType lastRequest = EnumRequestType.ABORTGAME;
	EnumHeader lastMess = EnumHeader.LOBBYCHOICE;
	MessageMove nextMove = null;
	MessageMove lastMove = null;
	List<Integer> moveList = null;
	boolean[] usedPlaceList = new boolean[100];
	MessageMove firstHit = null;
	MessageMove lowestHit = null;
	MessageMove highestHit = null;
	boolean isVertical = false;
	boolean isHotizontal = false;
	boolean firstTryDone = false;
	boolean secondTryDone = false;
	boolean thirdTryDone = false;
	boolean fourthTryDone = false;
	boolean lowestMiss = false;

	public PlayerAISmart(){
	}

	@Override
	public Message sendMessage() {
		if (lastMess == EnumHeader.SERVERREQUEST 
				&& lastRequest == EnumRequestType.PLACEMENT) {
				return new MessagePlacement(placement);	
			}
			
			if (lastMess == EnumHeader.SERVERREQUEST
				&& lastRequest == EnumRequestType.MOVE) {
				lastMove = nextMove;
				return nextMove;
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
			checkMoveResponse((MessageMoveResponse)mess);
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
	
	private void checkMoveResponse(MessageMoveResponse mess) {
		EnumMoveResult moveResult = mess.getResponse();
		
		//Vi f�rs�ker inte s�nka ett skepp och missade igen
		//s� d�rf�r beh�ver vi inte g�r n�got.
		if (firstHit == null && (moveResult == EnumMoveResult.MISS || moveResult == EnumMoveResult.FAIL)) {
			return;
		}
		
		//Om vi kommer in h�r, s� har vi tr�ffat en ub�t
		//detta vet vi eftersom vi inte har tr�ffat 
		//skeppet innan och nu s�nkte vi det, s� d�
		//kan vi ta bort alla rutor runt om tr�ffen.
		if (firstHit == null && moveResult == EnumMoveResult.SINK) {
			if (lastMove != null) {
				removeAllMovesAroundSink();
			}
			return;
		}
		
		//Vi har s�nkt ett annat fartyg
		if (firstHit != null && moveResult == EnumMoveResult.SINK) {
			if (lastMove != null) {
				removeAllMovesAroundSink();
				defaultSetAllValues();
			}
			return;
		}
		
		//Hit kommer vi om vi tr�ffar n�got annat skepp �n en ub�t
		if (firstHit == null && moveResult == EnumMoveResult.HIT) {
			firstHit = lastMove;
			lowestHit = lastMove;
			highestHit = lastMove;
			removeAllDiagonalMoves();
			return;
		}
				
		//H�r g�r vi in om vi inte vet hur skeppet som
		//vi har tr�ffat ligger och har tr�ffat det igen.
		if (firstHit != null && !isHotizontal && !isVertical && moveResult == EnumMoveResult.HIT) {
			if (fourthTryDone) {
				isHotizontal = true;
				highestHit = firstHit;
			}else if (thirdTryDone) {
				isVertical = true;
				highestHit = firstHit;
			} else if (secondTryDone) {
				isHotizontal = true;
				lowestHit = firstHit;
			} else if (firstTryDone) {
				isVertical = true;
				lowestHit = firstHit;
			} 
			removeAllDiagonalMoves();
		}
		
		//H�r g�r vi in om vi vet hur skeppet ligger
		//Vi b�rjar att g� mot det l�gsta v�rdet p� skeppet
		//allts� mindre och mindre x tills vi missar.
		if (firstHit != null && isHotizontal && moveResult == EnumMoveResult.HIT) {
			if (!lowestMiss) {
				lowestHit = lastMove;
			} else {
				highestHit = lastMove;
			}
			removeAllDiagonalMoves();
		}
		
		if (firstHit != null && isHotizontal && moveResult == EnumMoveResult.MISS) {
			lowestMiss = true;
		}
		
		if (firstHit != null && isVertical && moveResult == EnumMoveResult.HIT) {
			if (!lowestMiss) {
				lowestHit = lastMove;
			} else {
				highestHit = lastMove;
			}
			removeAllDiagonalMoves();
		}
		
		if (firstHit != null && isVertical && moveResult == EnumMoveResult.MISS) {
			lowestMiss = true;
		}
	}

	private void defaultSetAllValues() {
		firstHit = null;
		lowestHit = null;
		highestHit = null;
		isVertical = false;
		isHotizontal = false;
		firstTryDone = false;
		secondTryDone = false;
		thirdTryDone = false;
		fourthTryDone = false;
		lowestMiss = false;		
	}

	private void removeAllDiagonalMoves() {
		if (checkIfMoveInsideBoard(lastMove.getX() - 1, lastMove.getY() - 1)) {
			usedPlaceList[((lastMove.getX() - 1) * 10) + (lastMove.getY() - 1)] = true;
		}
		
		if (checkIfMoveInsideBoard(lastMove.getX() + 1, lastMove.getY() - 1)) {
			usedPlaceList[((lastMove.getX() + 1) * 10) + (lastMove.getY() - 1)] = true;
		}
		
		if (checkIfMoveInsideBoard(lastMove.getX() - 1, lastMove.getY() + 1)) {
			usedPlaceList[((lastMove.getX() - 1) * 10) + (lastMove.getY() + 1)] = true;
		}
		
		if (checkIfMoveInsideBoard(lastMove.getX() + 1, lastMove.getY() + 1)) {
			usedPlaceList[((lastMove.getX() + 1) * 10) + (lastMove.getY() + 1)] = true;	
		}		
	}

	private void removeAllMovesAroundSink() {
		
		if (checkIfMoveInsideBoard(lastMove.getX() - 1, lastMove.getY() - 1)) {
			usedPlaceList[((lastMove.getX() - 1) * 10) + (lastMove.getY() - 1)] = true;
		}
		
		if (checkIfMoveInsideBoard(lastMove.getX(), lastMove.getY() - 1)) {
			usedPlaceList[((lastMove.getX()) * 10) + (lastMove.getY() - 1)] = true;
		}
		
		if (checkIfMoveInsideBoard(lastMove.getX() + 1, lastMove.getY() - 1)) {
			usedPlaceList[((lastMove.getX() + 1) * 10) + (lastMove.getY() - 1)] = true;
		}
		
		if (checkIfMoveInsideBoard(lastMove.getX() - 1, lastMove.getY())) {
			usedPlaceList[((lastMove.getX() - 1) * 10) + lastMove.getY()] = true;
		}
		
		if (checkIfMoveInsideBoard(lastMove.getX() + 1, lastMove.getY())) {
			usedPlaceList[((lastMove.getX() + 1) * 10) + lastMove.getY()] = true;
		}
		
		if (checkIfMoveInsideBoard(lastMove.getX() - 1, lastMove.getY() + 1)) {
			usedPlaceList[((lastMove.getX() - 1) * 10) + (lastMove.getY() + 1)] = true;
		}
		
		if (checkIfMoveInsideBoard(lastMove.getX(), lastMove.getY() + 1)) {
			usedPlaceList[((lastMove.getX()) * 10) + (lastMove.getY() + 1)] = true;
		}
		
		if (checkIfMoveInsideBoard(lastMove.getX() + 1, lastMove.getY() + 1)) {
			usedPlaceList[((lastMove.getX() + 1) * 10) + (lastMove.getY() + 1)] = true;	
		}			
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
	
	private void createMove() {
		
		//Skapar en lista med alla drag som kan g�ras.
		moveList = createGridList();
		
		if (firstHit == null) {
			int value = rand.nextInt(moveList.size());			
			
			Integer currentMove = moveList.remove(value);
			
			int xPlace = currentMove / 10;
			int yPlace = currentMove % 10;
			
			usedPlaceList[currentMove] = true;			
			
			nextMove = new MessageMove(xPlace, yPlace);						
		} else if (isHotizontal) {
			//Om vi kommer hit, s� har vi skjutit minst tv� skott som tr�ffat ett skepp
			//och har uppt�ckt att skeppet ligger horisontellt
			createHorizontalMove();
		} else if (isVertical) {
			//Om vi kommer hit, s� har vi skjutit minst tv� skott som tr�ffat ett skepp
			//och har uppt�ckt att skeppet ligger vertikalt
			createVerticalMove();
		} else {
			//Om vi kommer hit, s� har i tr�ffat med ett skott
			//och har inte kommit p� hur skeppet ligger
			CreateMoveFromOneHit();
			
		}
	}
	
	private void createVerticalMove() {
		int xPlace = -1;
		int yPlace = -1;
		boolean foundMove = false;
		
		while (!foundMove) {
			if (!lowestMiss) {
				xPlace = lastMove.getX();
				yPlace = lastMove.getY() - 1;
				foundMove = checkIfMoveInsideBoard(xPlace, yPlace);
				//Om draget var utanf�r br�dat, s� r�knas det som en miss
				//eftersom vi inte kan g� l�ngre �t det h�llet.
				if (!foundMove) {
					lowestMiss = true;
				}
			} else {
				xPlace = lastMove.getX();
				yPlace = lastMove.getY() + 1;
				foundMove = checkIfMoveInsideBoard(xPlace, yPlace);
			}
		}
		
		nextMove = new MessageMove(xPlace, yPlace);
	}

	private void createHorizontalMove() {
		int xPlace = -1;
		int yPlace = -1;
		boolean foundMove = false;
		
		while (!foundMove) {
			if (!lowestMiss) {
				xPlace = lastMove.getX() - 1;
				yPlace = lastMove.getY();
				foundMove = checkIfMoveInsideBoard(xPlace, yPlace);
				
				//Om draget var utanf�r br�dat, s� r�knas det som en miss
				//eftersom vi inte kan g� l�ngre �t det h�llet.
				if (!foundMove) {
					lowestMiss = true;
				}
			} else {
				xPlace = lastMove.getX() + 1;
				yPlace = lastMove.getY();
				foundMove = checkIfMoveInsideBoard(xPlace, yPlace);
			}
		}
		
		nextMove = new MessageMove(xPlace, yPlace);			
	}

	private void CreateMoveFromOneHit() {
		int xPlace = -1;
		int yPlace = -1;
		boolean foundMove = false;
		
		//Vi loppar runt tills vi hittar ett giltigt drag.
		while (!foundMove) {
			
			//F�rsta �r n�r vi nyss har hittat ett skepp,
			//men inte b�rjat skjuta runt omkring det f�rr�ns nu
			//Vi b�rjar med att skjuta klockan 12 om det g�r.
			if (!firstTryDone) {
				xPlace = firstHit.getX();
				yPlace = firstHit.getY() - 1;
				firstTryDone = true;				
			} else if (!secondTryDone) {
				xPlace = firstHit.getX() - 1;
				yPlace = firstHit.getY();
				secondTryDone = true;				
			} else if (!thirdTryDone) {
				xPlace = firstHit.getX();
				yPlace = firstHit.getY() + 1;
				thirdTryDone = true;
				lowestMiss = true;
			} else {
				xPlace = firstHit.getX() + 1;
				yPlace = firstHit.getY();
				fourthTryDone = true;
				lowestMiss = true;
			}
			foundMove = checkIfMoveInsideBoard(xPlace, yPlace);
		}
		
		nextMove = new MessageMove(xPlace, yPlace);			
	}

	private boolean checkIfMoveInsideBoard(int xPlace, int yPlace) {
		//Kollar s� att v�rdena h�ller sig inom spelbr�dat
		return xPlace < 10 && xPlace > -1 && yPlace < 10 && yPlace > -1;
	}

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
		
		for (int i = 0; i < 100; i++){
			if (!usedPlaceList[i]) {				
				returnList.add(i);
			}
		}
		return returnList;
	}
}
