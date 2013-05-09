package se.miun.student.dt042g;

import static org.junit.Assert.*;

import org.junit.Test;


public class ServerRequestTest {

	@Test
	public void castAndEnumTest() {
		Object o = new MessageServerRequest(EnumRequestType.PLACEMENT, "Test" );
		
		Message m = (Message) o;
		
		MessageServerRequest sr = (MessageServerRequest) m;
		
		assertEquals(EnumHeader.SERVERREQUEST, m.getHeader() );
		
		assertEquals( EnumRequestType.PLACEMENT, sr.getRequest() );
	}
	
	@Test
	public void testGridPrint(){
		Ship sub1,sub2,sub3, sub4, sub5, dest1, dest2, dest3, carrier;
		sub1 = new Ship(0,0,1,true);
		sub2 = new Ship(0,2,1,true);
		sub3 = new Ship(0,4,1,true);
		sub4 = new Ship(0,6,1,true);
		sub5 = new Ship(0,8,1,true);
		
		dest1 = new Ship(2,0,3,false);
		dest2 = new Ship(4,0,3,false);
		dest3 = new Ship(6,0,3,false);
		
		carrier = new Ship(8,0,5,false);
		
		ShipPlacement p = new ShipPlacement(sub1,sub2,sub3, sub4, sub5, dest1, dest2, dest3, carrier);
		System.out.println("Shipplacement isGood: " + p.isGood());
		GameBoard board = new GameBoard();
		board.setupPlacement(p);
		
		/*
		for(int i=0; i<board.getHeight();i++){
			for(int j=0; j<board.getWidth(); j++){
				System.out.print( board.getPositionValue(j,i) + "\t\t");
				
			}
			System.out.print("\n");
		}
		*/
		
	}
}
