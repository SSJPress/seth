package seth2.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import seth2.Main;
import seth2.Shoe;

// testRedSeven(expectedPlayerValue, expectedDealerValue, expectedBankroll, shoe, surrender, buyInsurance, other... EPV);
public class RedSevenFullTest {
	private final double BET = 10.0;
	private final double START = 1000.0;
	
	@Test
	public void testRegularPlay() {
		Shoe shoe = null;
		
		// player win high card 20vs18
		shoe = new Shoe(10, 10, 10, 8);
		testRedSeven(20, 18, 1010, shoe, false, false);
		
		// player win high card by hitting 19vs18
		shoe = new Shoe(10, 10, 5, 8, 4);
		testRedSeven(19, 18, 1010, shoe, false, false);
		
		// player win by dealer bust 20vs25
		shoe = new Shoe(10, 10, 10, 5, 10);
		testRedSeven(20, 25, 1010, shoe, false, false);
		
		// dealer win high card 17vs19
		shoe = new Shoe(10, 10, 7, 9);
		testRedSeven(17, 19, 990, shoe, false, false);
		
		// dealer win high card by hitting 20vs21
		shoe = new Shoe(10, 6, 10, 5, 10);
		testRedSeven(20, 21, 990, shoe, false, false);
		
		// dealer win by player bust 23vs1
		shoe = new Shoe(10, 10, 3, 4, 10);
		testRedSeven(23, 14, 990, shoe, false, false);
		
		// push 20vs20
		shoe = new Shoe(10, 10, 10, 10);
		testRedSeven(20, 20, 1000, shoe, false, false);
				
		// player surrenders 15vs19
		shoe = new Shoe(10, 10, 5, 9);
		testRedSeven(15, 19, 995, shoe, true, false);
	}
	
	
	@Test
	public void testBlackjackAndInsurance() {
		Shoe shoe = null; 
		
		// player Blackjack 21vs18
		shoe = new Shoe(10, 10, 1, 8);
		testRedSeven(21, 18, 1015, shoe, false, false);
		
		// Dealer Blackjack 18vs21
		shoe = new Shoe(10, 10, 8, 1);
		testRedSeven(18, 21, 990, shoe, false, false);
		
		// Dealer Blackjack with insurance 18vs21
		shoe = new Shoe(10, 1, 8, 10);
		testRedSeven(18, 21, 1000, shoe, false, true);
		
		// Dealer Blackjack without insurance 18vs21
		shoe = new Shoe(10, 1, 8, 10);
		testRedSeven(18, 21, 990, shoe, false, false);
		
		// Player Blackjack with Insurance 21vs13
		shoe = new Shoe(1, 1, 10, 2);
		testRedSeven(21, 13, 1010, shoe, false, true);
		
		// Player Blackjack without Insurance 21vs13
		shoe = new Shoe(1, 1, 10, 2);
		testRedSeven(21, 13, 1015, shoe, false, false);
		
		// Blackjack Push with Insurance 21vs21
		shoe = new Shoe(1, 1, 10, 10);
		testRedSeven(21, 21, 1010, shoe, false, true);
		
		// Blackjack Push without Insurance 21vs21
		shoe = new Shoe(1, 1, 10, 10);
		testRedSeven(21, 21, 1000, shoe, false, false);
		
		// player win high card and buying insurance 20vs18
		shoe = new Shoe(10, 1, 10, 7);
		testRedSeven(20, 18, 1005, shoe, false, true);
		
		// player win high card by hitting and buying insurance 19vs18
		shoe = new Shoe(10, 1, 5, 7, 4);
		testRedSeven(19,18, 1005, shoe, false, true);
		
		// player win by dealer bust and buying insurance 20vs25
		shoe = new Shoe(10, 1, 10, 4, 10);
		testRedSeven(20, 25, 1005, shoe, false, true);
		
		// dealer win high card and buying insurance 17vs19
		shoe = new Shoe(10, 1, 7, 8);
		testRedSeven(17, 19, 985, shoe, false, true);
		
		// dealer win high card by hitting and buying insurance  20vs21
		shoe = new Shoe(10, 1, 10, 4, 6);
		testRedSeven(20, 21, 985, shoe, false, true);
		
		// dealer win by player bust and buying insurance 23vs15
		shoe = new Shoe(10, 1, 3, 4, 10);
		testRedSeven(23, 15, 985, shoe, false, true);
		
		// push and buying insurance 20vs20
		shoe = new Shoe(10, 1, 10, 9);
		testRedSeven(20, 20, 995, shoe, false, true);
				
		// player surrenders and buying insurance 16vs19
		shoe = new Shoe(10, 1, 6, 8);
		testRedSeven(16, 19, 990, shoe, true, true);
	}
	
	
	@Test
	public void testDoubleDown() {
		Shoe shoe = null;
		
		// player win by double down 20vs18
		shoe = new Shoe(5, 9, 5, 9, 10);
		testRedSeven(20, 18, 1020, shoe, false, false);
		
		// player win by double down, dealer bust 16vs25
		shoe = new Shoe(5, 9, 5, 6, 6, 10);
		testRedSeven(16, 25, 1020, shoe, false, false);
		
		// player lose double down 1vs19
		shoe = new Shoe(5, 9, 5, 10, 4);
		testRedSeven(14, 19, 980, shoe, false, false);
		
		// player lose double down by dealer hitting 1vs17
		shoe = new Shoe(5, 4, 5, 10, 4, 3);
		testRedSeven(14, 17, 980, shoe, false, false);
		
		// double down push 18vs18
		shoe = new Shoe(5, 9, 5, 9, 8);
		testRedSeven(18, 18, 1000, shoe, false, false);
	}
	
	@Test
	public void testSoftDoubleDown() {
		Shoe shoe = null;
		
		// player win by double down 20vs18
		shoe = new Shoe(1, 6, 6, 2, 3);
		testRedSeven(20, 18, 1020, shoe, false, false);
		
		// player win by double down, dealer bust 16vs25
		shoe = new Shoe(1, 6, 6, 9, 9);
		testRedSeven(16, 25, 1020, shoe, false, false);
		
		// player lose double down 1vs19
		shoe = new Shoe(1, 6, 6, 3, 7);
		testRedSeven(14, 19, 980, shoe, false, false);
		
		// player lose double down by dealer hitting 1vs17
		shoe = new Shoe(1, 6, 6, 8, 7, 3);
		testRedSeven(14, 17, 980, shoe, false, false);
		
		// double down push 18vs18
		shoe = new Shoe(1, 6, 6, 2, 1);
		testRedSeven(18, 18, 1000, shoe, false, false);
	}
	
	
	@Test
	public void testSplit() {
		Shoe shoe = null;
		/*
		// player win high card by hitting 19,19vs18
		shoe = new Shoe(9, 8, 9, 10, 10, 10);
		testRedSeven(0, 18, 1020, shoe, false, false, 19, 19);
		
		// player win high card by hitting 20,20,20vs19
		shoe = new Shoe(8, 9, 8, 10, 8, 8, 4, 8, 4, 8, 4);
		testRedSeven(0, 19, 1030, shoe, false, false, 20, 20, 20);

		// player win high card by hitting 18,18,18vs26
		shoe = new Shoe(8, 6, 8, 10, 8, 10, 10, 10, 10);
		testRedSeven(0, 26, 1030, shoe, false, false, 18, 18, 18);
		
		// dealer win high card 18,18,18vs19
		shoe = new Shoe(8, 9, 8, 10, 8, 10, 10, 10);
		testRedSeven(0, 19, 970, shoe, false, false, 18, 18, 18);
		
		// dealer win high card by hitting 18,18,18vs21
		shoe = new Shoe(8, 6, 8, 5, 8, 10, 10, 10, 10);
		testRedSeven(0, 21, 970, shoe, false, false, 18, 18, 18);
		
		// dealer win by player bust with insurance 26,26,26vs1
		shoe = new Shoe(8, 1, 8, 3, 8, 8, 10, 8, 10, 8, 10);
		testRedSeven(0, 14, 965, shoe, false, true, 26, 26, 26);
		
		// dealer win by player bust without insurance26,26,26vs1
		shoe = new Shoe(8, 1, 8, 3, 8, 8, 10, 8, 10, 8, 10);
		testRedSeven(0, 14, 970, shoe, false, false, 26, 26, 26);
		
		// push 20,20,20vs20
		shoe = new Shoe(8, 10, 8, 10, 8, 8, 4, 8, 4, 8, 4);
		testRedSeven(0, 20, 1000, shoe, false, false, 20, 20, 20);
		
		// double down push 20,20,20vs20
		shoe = new Shoe(8, 10, 8, 10, 8, 3, 9, 3, 9, 3, 9);
		testRedSeven(0, 20, 1000, shoe, false, false, 20, 20, 20);
				
		// player surrenders 16,16,16vs19
		shoe = new Shoe(8, 9, 8, 10, 8, 8, 8, 8);
		testRedSeven(0, 19, 985, shoe, true, false, 16, 16, 16);
		
		// player win by double down 19,19,19vs17
		shoe = new Shoe(4, 6, 4, 10, 4, 5, 10, 5, 10, 5, 10, 1);
		testRedSeven(0, 17, 1060, shoe, false, false, 19, 19, 19);
		
		// player win by double down 19,19,19vs20
		shoe = new Shoe(4, 6, 4, 10, 4, 5, 10, 5, 10, 5, 10, 4);
		testRedSeven(0, 20, 940, shoe, false, false, 19, 19, 19);
		
		// player win, bust, surrender 18,24,16vs17
		shoe = new Shoe(8, 10, 8, 6, 8, 10, 6, 10, 8, 1);
		testRedSeven(0, 17, 995, shoe, true, false, 18, 24, 16);
		
		// player win, double down, bust 18,21,26vs17
		shoe = new Shoe(8, 10, 8, 6, 8, 10, 3, 10, 8, 10, 1);
		testRedSeven(0, 17, 1020, shoe, false, false, 18, 21, 26);
		
		// player double down, bust, bust 21,25,26vs17
		shoe = new Shoe(8, 10, 8, 6, 8,  3, 10, 7, 10, 8, 10, 1);
		testRedSeven(0, 17, 1000, shoe, false, false, 21, 25, 26);*/
		
		// aces win 21,16vs26
		shoe = new Shoe(1, 10, 1, 6, 10, 5, 10);
		testRedSeven(0, 26, 1020, shoe, false, false, 21, 16);
		
		// aces lose 17,16vs19
		shoe = new Shoe(1, 10, 1, 6, 6, 5, 3);
		testRedSeven(0, 19, 980, shoe, false, false, 17, 16);
		
		// aces win,lose 21,16vs19
		shoe = new Shoe(1, 10, 1, 6, 10, 5, 3);
		testRedSeven(0, 19, 1000, shoe, false, false, 21, 16);
		
		// resplit aces lose,lose 12,12vs19
		shoe = new Shoe(1, 10, 1, 6, 1, 1, 3);
		testRedSeven(0, 19, 980, shoe, false, false, 12, 12);
	}
	
	/*
	// testRedSeven
	private void testRedSeven( int expectedPlayerValue, int expectedDealerValue, double expectedBankroll, Shoe shoe, boolean surrender, boolean buyInsurance, int... expectedPlayerValues) {
		Main.AUTOMODE = true;
		Main.SURRENDER = surrender;
		Main.SPLITNUM = 2;
		Main.buyInsurance = buyInsurance;
		
		Main.wallet = START;
		Main.betnum = BET;
		Main.deck = shoe;
		Main.autoPlay();
		Main.dealerTurn(Main.pScores, Main.pChoices);
		Main.results(Main.pScores, Main.pChoices, Main.dealerHand.getValueInt());
		
		if( expectedPlayerValues.length > 0 ) {
			for( int i = 0; i < expectedPlayerValues.length; i++ ) {
				assertEquals("Wrong PlayerHand[" + (i+1) + "] Value", expectedPlayerValues[i], Main.pHands.get(i).getValueInt());
			}
		} else {
			assertEquals("Wrong PlayerHand Value", expectedPlayerValue, Main.pHands.get(0).getValueInt());
		}
		
		assertEquals("Wrong DealerHand Value", expectedDealerValue, Main.dealerHand.getValueInt());
		assertEquals("Wrong Payout", expectedBankroll, Main.wallet, 0);
		
		Main.clear();
	}
	*/
	
	private void testRedSeven( int expectedPlayerValue, int expectedDealerValue, double expectedBankroll, Shoe shoe, boolean surrender, boolean buyInsurance, int... expectedPlayerValues) {
		Main.setAUTOMODE(true);
		Main.setSPLITNUM(2);
		Main.setDeck(shoe);
		Main.setWallet(START);
		Main.setBetnum(BET);
	/*	Main.tester(expectedPlayerValue, expectedDealerValue,
				expectedBankroll, surrender, buyInsurance,
				expectedPlayerValues);*/
		
		if( expectedPlayerValues.length > 0 ) {
			for( int i = 0; i < expectedPlayerValues.length; i++ ) {
				assertEquals("Wrong PlayerHand[" + (i+1) + "] Value", expectedPlayerValues[i], Main.getpHands().get(i).getValueInt());
			}
		} else {
			assertEquals("Wrong PlayerHand Value", expectedPlayerValue, Main.getpHands().get(0).getValueInt());
		}
		
		assertEquals("Wrong DealerHand Value", expectedDealerValue, Main.getDealerHand().getValueInt());
		assertEquals("Wrong Payout", expectedBankroll, Main.getWallet(), 0);
		
		Main.clear();
	}
}