package seth2.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import seth2.Shoe;
import seth2.Strategy;
import seth2.Main;

public class countTest {
	
//	@Test
	// THIS ONE TESTS FOR PLAYER GOT BLACKJACK, DEALER SHOWING ACE.
	// NOTE; ASSERT WALLET TOTAL AT END TO BE 1100 IF YOU WANT TO TEST FOR TAKING EVEN MONEY.
	public void testInputStream() {
		ByteArrayInputStream in = new ByteArrayInputStream("Y".getBytes());
		System.setIn(in);
		System.out.println(Main.getWallet());
		Main.setBetnum(100);
		Main.setDeck(1, 1, 10, 1);
		System.out.println(Main.getWallet());
		assertEquals(Main.getWallet(), 1100, .01);
	}
	
	//@Test
	public void inputRigger(){

		ByteArrayInputStream in = new ByteArrayInputStream("My string\nStill My string".getBytes());
		System.setIn(in);
		Main.test();
	}
	
	//@Test
	public void testSplits(){
	List<Integer> testList = new ArrayList<Integer>();
	List<Integer> mainList = new ArrayList<Integer>();
	
	// Rig what you believe the final set of hand scores should be.
	testList.add(10);
	testList.add(10);
	testList.add(10);
	testList.add(10);
	
	// Rig what you want the deck to be. 
	mainList = Main.setDeck(5,5,5,5,5,5,5,5,5,5,5,5,5,5,5);
	
	// Should you play the hand to Max Splits (4 hands total,) and you stand if you can't hit, you will end up with 4 hands with value 10 each.
	assertEquals (mainList, testList);
	
	}

	
	//@Test
	// THIS ONE TESTS FOR PLAYER/DEALER INITIAL BLACKJACK STIUATIONS. RIGHT NOW, PROGRAMMED TO PROVE-
	// PURCHASING INSURANCE AGAINST A DEALER FACE-UP ACE HIDING BLACKJACK RETAINS WALLET'S ORIGINAL AMOUNT.
	public void testDealerShowingAcePayouts(){
		System.out.println(Main.getWallet());
		Main.setBetnum(100);
	Main.setDeck(5,1,5,10);
	System.out.println(Main.getWallet());
	assertEquals(Main.getWallet(), 1000, .01);
	}
	
	//@Test
	// THIS ONE TESTS FOR DEALER FACE DOWN ACE, HIDING BLACKJACK.
	public void testDealerNotShowingAceBJPayouts(){
		System.out.println(Main.getWallet());
		Main.setBetnum(100);
	Main.setDeck(5,10,5,1);
	System.out.println(Main.getWallet());
	assertEquals(Main.getWallet(), 900, .01);
	}
	
	//@Test
	// THIS ONE TESTS FOR PLAYER GOT BLACKJACK, DEALER CLEARLY DOESN'T.
	public void testPlayerBJPayouts(){
		System.out.println(Main.getWallet());
		Main.setBetnum(100);
	Main.setDeck(1,5,10,1);
	System.out.println(Main.getWallet());
	assertEquals(Main.getWallet(), 1150, .01);
	}
	
	//@Test
	// THIS ONE TESTS FOR PLAYER GOT BLACKJACK, DEALER SHOWING ACE.
	// NOTE; ASSERT WALLET TOTAL AT END TO BE 1100 IF YOU WANT TO TEST FOR TAKING EVEN MONEY.
	public void testPlayerBJDealerMaybeBJPayouts(){
		System.out.println(Main.getWallet());
		Main.setBetnum(100);
	Main.setDeck(1,1,10,1);
	System.out.println(Main.getWallet());
	assertEquals(Main.getWallet(), 1150, .01);
	}
	
	//@Test
	// THIS ONE TESTS FOR EVERYONE GOT BLACKJACK. ASSERTING YOU DO NOT TAKE EVEN MONEY.
	public void testEveryoneGotItPayouts(){
		System.out.println(Main.getWallet());
		Main.setBetnum(100);
	Main.setDeck(1,1,10,10);
	System.out.println(Main.getWallet());
	assertEquals(Main.getWallet(), 1000, .01);
	}
	
	
	//@Test
	public void testResults() {
		System.out.println(Main.getWallet());
	// ADD SCORES HERE! (0 FOR SURRENDER)
	List<Integer> scores = new ArrayList<Integer>();
	scores.add(20);
	scores.add(5);
	scores.add(15);

	// ADD DECISIONS HERE! (N FOR NORMAL, D FOR DOUBLE DOWN, R FOR SURRENDER)
	List<String> choices = new ArrayList<String>();
	choices.add("D");
	choices.add("R");
	choices.add("D");
	
	// We remove the bet amounts the players would have taken with the above choices;
	// The Scores.size accounts for the hands bet, the for loop accounts for all the Double Down bets.
	
	Main.setBetnum(100);
	int totalNum = scores.size();
	
	for (String x : choices){
		if (x.equals("D")){
		totalNum++;
		}
	}
	Main.setWallet(Main.getWallet() - (Main.getBetnum()*totalNum));
	
	System.out.println(Main.getWallet());
	Main.results(scores, choices, 15);
	assertEquals (Main.getWallet(), 1150, .01);
	}
	
	
	




@Test
	public void testMaddens() {
//		private static String askMadden(int value, int dCard, boolean isSoft,
//				boolean isSplittable, boolean ddSurrOk)
//		System.out.println(Strategy.strategy(18, 5, true, false));
	//for (int x=4; x<22; x++){
	//	System.out.println("VALUE of " + x + ". " + Strategy.strategy(x, 9, false, false));	
	//}
	System.out.println(Strategy.strategy(15, 10, false, false, true, false));	
		boolean condition = false;
		if ((Strategy.strategy(15, 4, true, false, true, false) == "Madden says: Double Down if you can, Stand if you can't, kid.")){
		condition = true;
		}
	//	assertTrue(condition);
	}


public void testMadden() {
//	private static String askMadden(int value, int dCard, boolean isSoft,
//			boolean isSplittable)
//	System.out.println(Strategy.strategy(18, 5, true, false));
	System.out.println(Strategy.strategy(15, 4, true, false, true, true));		
	boolean condition = false;
	if ((Strategy.strategy(15, 4, true, false, true, true) == "Madden says: Double Down if you can, Stand if you can't, kid.")){
	condition = true;
	}
//	assertTrue(condition);
}

	
	public void testShoeCreator() {
Shoe deck = new Shoe(5,1,5,13);
System.out.println(deck.getDeckList());
	
	}
	
	public void testCount() {
		
		Shoe deck = new Shoe();
		System.out.println(deck.getCount());
		while (deck.getPos() < deck.getDeckList().size()) {
			deck.draw();
			System.out.println(deck.getCount());
			
		}
		assertEquals(deck.getCount(), 0);
	}


}
