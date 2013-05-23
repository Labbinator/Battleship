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
	//List<bombsPlacement> bombList = null;
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
	
	//MessageMove lastHitMove = null;
	//MessageMove nextMove = null;
	//boolean searchedTop = false;
	//boolean searchedBottom = false;
	//boolean searchedLeft = false;
	//boolean searchedRight = false;

	
	public PlayerAISmart(){
		
		//Måste vara i denna ordning annars så funkar inte
		//createGridList eftersom den använder listan som 
		//är skapad i createBombList.
		//bombList = createBombList();
		//moveList = createGridList();

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
			//checkMoveResponse((MessageMoveResponse)mess);
			
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
		
		//Vi försöker inte sänka ett skepp och missade igen
		//så därför behöver vi inte gör något.
		if (firstHit == null && (moveResult == EnumMoveResult.MISS || moveResult == EnumMoveResult.FAIL)) {
			return;
		}
		
		//Om vi kommer in här, så har vi träffat en ubåt
		//detta vet vi eftersom vi inte har träffat 
		//skeppet innan och nu sänkte vi det, så då
		//kan vi ta bort alla rutor runt om träffen.
		if (firstHit == null && moveResult == EnumMoveResult.SINK) {
			if (lastMove != null) {
				removeAllMovesAroundSink();
			}
			return;
		}
		
		//Vi har sänkt ett annat fartyg
		if (firstHit != null && moveResult == EnumMoveResult.SINK) {
			if (lastMove != null) {
				removeAllMovesAroundSink();
				defaultSetAllValues();
			}
			return;
		}
		
		//Hit kommer vi om vi träffar något annat skepp än en ubåt
		if (firstHit == null && moveResult == EnumMoveResult.HIT) {
			firstHit = lastMove;
			lowestHit = lastMove;
			highestHit = lastMove;
			removeAllDiagonalMoves();
			return;
		}
				
		//Här går vi in om vi inte vet hur skeppet som
		//vi har träffat ligger och har träffat det igen.
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
		
		//Här går vi in om vi vet hur skeppet ligger
		//Vi börjar att gå mot det lägsta värdet på skeppet
		//alltså mindre och mindre x tills vi missar.
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

/*
	private void checkMoveResponse(MessageMoveResponse mess) {
		EnumMoveResult moveResult = mess.getResponse();
		
		//Skottet missade eller så är det något annat fel och då
		//behöver vi inte göra något här.
		if (moveResult == EnumMoveResult.MISS || moveResult == EnumMoveResult.FAIL) {
			return;
		}
		
		//Om vi sänkte något så har vi redan tagit bort
		//alla andra onödig bombplatser, så nu tar vi bort
		//alla onödiga bombplatser runt skottet som sänkte
		//skeppet, eftersom vi inte kollar vad det är vi har 
		//sänkt så kanske vi tar bort en massa platser som 
		//redan är borttagna, men lättare än att kolla det.
		if (moveResult == EnumMoveResult.SINK) {
			removeAllHorizontalVerticalBombPlacesAroundMove();
		}
		
		if (moveResult == EnumMoveResult.HIT) {
			if (lastHitMove == null) {
				lastHitMove = new MessageMove(lastMove.getX(), lastMove.getY());
				if (lastHitMove.getY() > 0) {
					nextMove = new MessageMove(lastHitMove.getX(), lastHitMove.getY() - 1);
					searchedTop = true;
				} else {
					nextMove = new MessageMove(lastHitMove.getX(), lastHitMove.getY() + 1);
					searchedTop = true;
					searchedBottom = true;
				}
				
				return;
			}
			
			if (searchedTop || searchedBottom) {
				isVertical = true;
				if (!searchedBottom && lastMove.getY() < 9) {
					nextMove = new MessageMove(lastMove.getX(), lastMove.getY() + 1);
					searchedBottom = true;
				}
			}
			
		}
		
	}
	*/

	/*
	private void removeAllHorizontalVerticalBombPlacesAroundMove() {
		int xPos = nextMove.getX();
		int yPos = nextMove.getY() - 1;
		
		removeUnusedBombPlace(xPos, yPos);
		xPos--;
		yPos++;
		removeUnusedBombPlace(xPos, yPos);
		xPos += 2;
		removeUnusedBombPlace(xPos, yPos);
		xPos--;
		yPos++;
		removeUnusedBombPlace(xPos, yPos);							
	}
	*/

	/*
	private void removeUnusedBombPlace(int xPos, int yPos) {
		//Kollar så att x och y positionerna är på spelplanen
		//i annat fall så kan vi inte markera postionen som used.
		if (xPos > -1 && xPos < 10 && yPos > -1 && yPos < 10) {
			for (bombsPlacement tmpBombPlace : bombList) {
				if (tmpBombPlace.getX() == xPos && tmpBombPlace.getY() == yPos) {
					tmpBombPlace.setUsed(true);
					break;
				}
			}
		}
	}
	*/

	@Override
	public void close() {
		// TODO Auto-generated method stub

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
		
		//Skapar en lista med alla drag som kan göras.
		moveList = createGridList();
		
		if (firstHit == null) {
			int value = rand.nextInt(moveList.size());			
			
			Integer currentMove = moveList.remove(value);
			
			int xPlace = currentMove / 10;
			int yPlace = currentMove % 10;
			
			usedPlaceList[currentMove] = true;
			
			/*
			for (bombsPlacement tmpBombPlace : bombList) {
				if (tmpBombPlace.getX() == xPlace && tmpBombPlace.getY() == yPlace) {
					tmpBombPlace.setUsed(true);
					break;
				}
			}
			*/
			
			nextMove = new MessageMove(xPlace, yPlace);						
		} else if (isHotizontal) {
			//Om vi kommer hit, så har vi skjutit minst två skott som träffat ett skepp
			//och har upptäckt att skeppet ligger horisontellt
			createHorizontalMove();
		} else if (isVertical) {
			//Om vi kommer hit, så har vi skjutit minst två skott som träffat ett skepp
			//och har upptäckt att skeppet ligger vertikalt
			createVerticalMove();
		} else {
			//Om vi kommer hit, så har i träffat med ett skott
			//och har inte kommit på hur skeppet ligger
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
				//Om draget var utanför brädat, så räknas det som en miss
				//eftersom vi inte kan gå längre åt det hållet.
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
				
				//Om draget var utanför brädat, så räknas det som en miss
				//eftersom vi inte kan gå längre åt det hållet.
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
			
			//Första är när vi nyss har hittat ett skepp,
			//men inte börjat skjuta runt omkring det förräns nu
			//Vi börjar med att skjuta klockan 12 om det går.
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
		//Kollar så att värdena håller sig inom spelbrädat
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
	
	/*
	private List<bombsPlacement> createBombList() {
		List<bombsPlacement> tmpBombList = new ArrayList<bombsPlacement>();
		
		for (int i = 0; i < 100; i++) {
			int x = i / 10;
			int y = i % 10;
			
			bombsPlacement tmpBombPlace = new bombsPlacement(x, y);
			
			tmpBombList.add(tmpBombPlace);
		}
		return tmpBombList;
	}
	*/
	
	/*
	private class bombsPlacement {
		private int x;
		private int y;
		private boolean used;
		
		public bombsPlacement(int inX, int inY) {
			x = inX;
			y = inY;
			used = false;
			// TODO Auto-generated constructor stub
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public boolean getUsed() {
			return used;
		}
		
		public void setUsed(boolean inUsed) {
			used = inUsed;
		}
	}
	*/
}
