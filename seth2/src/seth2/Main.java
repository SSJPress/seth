package seth2;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {

	/**
	 * THIS IS WHERE THE USER CAN CUSTOMIZE SETTINGS.
	 */
	private static Hand dealerHand;
	private static List<Hand> pHands = new ArrayList<Hand>();
	private static List<Integer> pScores = new ArrayList<Integer>();
	private static List<String> pChoices = new ArrayList<String>();
	private static double wallet = 1000;
	private static double betnum = 0;
	private static boolean buyInsurance;
	private static final int MINBET = 25; // This is the minimum bet amount at
											// our tables.
	private static int SPLITNUM = 2;
	private static final int DECKNUM = 6;
	private static boolean SURRENDER = true; // If you want to permit the
											// player to Surrender
											// hands, set this to true.
	private static final boolean SOFT17HIT = true; // If you want the dealer to
													// hit on Soft 17, set this
													// to true.
	private static boolean AUTOMODE = false;

	// These are other variables.
	// public static Scanner scan;
	private static Scanner scan = new Scanner(System.in);
	private static boolean thisHandDone;
	private static boolean allHandsDone;
	private static boolean splitAces;
	private static String result;
	// public static Shoe deck;
	private static Shoe deck = new Shoe(DECKNUM);
	
	
	/*
	private static boolean tester(int playerValue, int dealerValue,
			double expectedWallet, boolean surrYes, boolean insureYes,
			Integer... splits) {
		setSURRENDER(surrYes);
		setAUTOMODE(true);
		setBetnum(10);
		setBuyInsurance(insureYes);
		boolean playerRequest = true;
		autoPlay();

		if (getpScores().get(0) == 0) {
			if (expectedWallet != getWallet()) {
				playerRequest = false;
			}
			return playerRequest;
		}

		dealerTurn(getpScores(), getpChoices());

		if (playerValue != 0) {
			if (playerValue != getpScores().get(0)
					|| dealerValue != getDealerHand().getValueInt()) {
				playerRequest = false;
			}
		}

		// FOR SPLIT HAND CHECKS.
		if (playerValue == 0) {
			int turn = 0;
			if (getpScores().size() != splits.length) {
				playerRequest = false;
			}

			if (getpScores().size() == splits.length) {
				for (int x : splits) {
					// System.out.println("Turn: " + turn + ". PlayerScore: " +
					// pScores.get(turn) + ". ExpectedScore: " + x + ".");
					if (getpScores().get(turn) != x) {
						playerRequest = false;
					}
					turn++;
				}
			}

		}
		results(getpScores(), getpChoices(), getDealerHand().getValueInt());

		if (expectedWallet != getWallet()) {
			playerRequest = false;
		}
		if (expectedWallet == getWallet()) {
			playerRequest = true;
		}

		return playerRequest;
	}
	*/
	
	

	public static void main(String[] args) {
		System.out.println("Welcome to Blackjack.");
		if (isAUTOMODE()) {
			setBetnum(bet());
		}

		while (true) {

			if (isAUTOMODE()) {
				if (getWallet() < getBetnum()) {
					System.out
							.println("Can't afford another bet! Get him outta here!!!");
					System.exit(0);
				}
				autoPlay();
			} else {
				setBetnum(bet());
				play();
			}
			dealerTurn(getpScores(), getpChoices());
			results(getpScores(), getpChoices(), getDealerHand().getValueInt());
			System.out.println();

		}
	}

	public static double bet() {
		setBetnum(0);
		while (getBetnum() <= 0) {
			try {

				while (true) {
					System.out
							.println("How much would you like to bet of your "
									+ getWallet() + " chips?");
					setBetnum(Double.parseDouble(scan.nextLine()));

					if (getBetnum() < MINBET || getBetnum() > getWallet()) {
						System.out.println("Not a viable bet, try again.");
					}
					if (getBetnum() >= MINBET && getBetnum() <= getWallet()) {
						return getBetnum();
					}
				}
			} catch (NumberFormatException nfe) {
				System.out.println("Please enter a bid");
				setBetnum(0);
			} catch (InputMismatchException e) {
				System.out.println("Enter an actual bet number, numbskull.");
				setBetnum(0);
			}
			return getBetnum();
		}
		return getBetnum();
	}

	public static void test() {
		System.out.println("Test.");
		String decision = scan.nextLine();
		System.out.println(decision);
		decision = scan.nextLine();
		System.out.println(decision);
	}

	public static List<Integer> play() {
		// This is the initial play phase. When this phase ends, we loop back to
		// the Main Method, which will bring us back here.

		setWallet(getWallet() - getBetnum());

		System.out.println("Play Phase. You bet " + getBetnum() + ". Wallet now at "
				+ getWallet() + ".");

		// The betting phase concludes. Now we deal two face-up cards to the
		// player, and one face-up / one face-down card to the dealer.

		allHandsDone = false;
		thisHandDone = false;
		result = "NEUTRAL";
		Hand playerHand = new Hand();
		setDealerHand(new Hand());
		playerHand.getHand().add(getDeck().draw());
		getDealerHand().getHand().add(getDeck().draw());
		playerHand.getHand().add(getDeck().draw());
		getDealerHand().getHand().add(getDeck().hiddenDraw());

		System.out.println("Your Hand: " + playerHand.getHand()
				+ ". Card count is at " + getDeck().getCount() + ".");
		playerHand.valueDisp();

		System.out.println("Dealer showing " + getDealerHand().getHand().get(0)
				+ ".");

		// BJ CHECK:
		// 1 - CHECK FOR PLAYER BJ. IF PLAYER HAS BJ -> CHECK IF CPU HAS ACE FOR
		// EVEN MONEY / IF CPU HAS BJ.
		// RESOLVE.
		// 2 - CHECK FOR CPU CARD #0 FOR ACE. IF ACE SHOWING -> OFFER INSURANCE.
		// IF NO ACE SHOWING -> CHECK FOR BJ. IF CPU HAS BJ -> CHECK IF PLAYER
		// HAS BJ. RESOLVE.
		// 3 - PLAYER / CPU HAND IS EITHER CONLCUDED VIA PLAYER/CPU BJ, OR
		// NEITHER HAS BJ AND PLAY CONTINUES AS NORMAL.

		if (playerHand.getValueInt() == 21) {
			allHandsDone = true;
			// IF DEALER IS SHOWING AN ACE, PRESENT EVEN MONEY.
			if (getDealerHand().getHand().get(0).getRank() == 1) {
				System.out
						.println("Dealer flashes his face-up Ace. Do you wish to walk away with Even Money? (Y)ES / (N)O.");
				while (true) {
					String decision = scan.nextLine();
					if (decision.toUpperCase().equals("Y")) {
						setWallet((getBetnum() * 2) + getWallet());
						result = "EMONEYYES";
						System.out
								.println("You walk away with Even Money. Chip count up to "
										+ getWallet() + "!");
						break;
					}
					if (decision.toUpperCase().equals("N")) {
						if (getDealerHand().getValueInt() == 21) {
							result = "EMONEYNO";
							setWallet(getBetnum() + getWallet());
							System.out
									.println("Should have taken that Even Money - Dealer Had Blackjack! Chip count still  "
											+ getWallet() + ".");
							break;
						}
						if (getDealerHand().getValueInt() != 21) {
							result = "PBJ";
							System.out.println("Dealer don't got that BJ!");
							break;
						}
					}
					System.out.println("Gonna need a Y or N, butthorn.");
				}
			}
			// IF DEALER IS NOT SHOWING AN ACE, GO STRAIGHT TO CALCULATIONS.
			if (getDealerHand().getHand().get(0).getRank() != 1) {
				if (getDealerHand().getValueInt() == 21) {
					result = "PDBJ";
				} else if (getDealerHand().getValueInt() != 21) {
					result = "PBJ";
				}
			}

		}

		// WARNING:: UNSAFE INPUT.
		else if (playerHand.getValueInt() != 21) {
			if (getDealerHand().getHand().get(0).getRank() == 1) {
				while (true) {
					System.out
							.println("Dealer showing Ace. Purchase Insurance? (Y)es or (N)o.");
					String decision = scan.nextLine();
					if (decision.toUpperCase().equals("Y")) {
						if (getDealerHand().getValueInt() == 21) {
							allHandsDone = true;
							result = "INSDBJ";
						} else if (getDealerHand().getValueInt() != 21) {
							setWallet(getWallet() - (getBetnum() / 2));
							System.out
									.println("Dealer does not have Blackjack, awful call. Wallet dropped to "
											+ getWallet());

						}
					}
					if (decision.toUpperCase().equals("N")) {
						if (getDealerHand().getValueInt() == 21) {
							allHandsDone = true;
							result = "NOINSDBJ";
						} else if (getDealerHand().getValueInt() != 21) {
							System.out
									.println("Dealer does not have Blackjack. Play continues.");

						}
					}
					if (decision.toUpperCase().equals("Y")
							|| decision.toUpperCase().equals("N")) {
						break;
					}

					System.out.println("Need a Y or an N here, bucko.");
				}
			}
			if (getDealerHand().getHand().get(0).getRank() != 1
					&& getDealerHand().getValueInt() == 21) {
				allHandsDone = true;
				result = "DBJ";
			}

		}

		// HERE, THE RESULTS OF ANY EARLY BLACKJACKERY OCCURS.
		if (result.equals("PBJ")) {
			setWallet(getWallet() + (getBetnum() + (getBetnum() * 1.5)));
			System.out.println("Dat Blackjack pays 3:2. Chip count grows to "
					+ getWallet() + "!");
		}
		if (result.equals("DBJ")) {
			System.out.println("You lose this hand! Chip count drops to "
					+ getWallet() + ".");
		}
		if (result.equals("PDBJ")) {
			setWallet(getWallet() + getBetnum());
			System.out.println("Push! Chip count returns to " + getWallet() + "!");
		}

		if (result.equals("INSDBJ")) {
			setWallet(getWallet() + getBetnum());
			System.out
					.println("You correctly insured yourself from Dealer's Blackjack. Chip count returns to "
							+ getWallet() + ".");
		}

		if (result.equals("NOINSDBJ")) {

			System.out
					.println("You failed to insure from Dealer's Blackjack. Chip count drops to "
							+ getWallet() + ".");
		}

		// ASSUMING NO BLACKJACKERY FUNNY BUSINESS OCCURRED, THIS IS WHERE THE
		// MEAT AND POTATOES OF A PLAYER'S GAME GETS PLAYED OUT.

		getpHands().add(playerHand);
		int handPos = 0;

		while (allHandsDone == false) {

			// This is where we move forward one hand, establish which hand is
			// the "Active Hand", and begin a new round of play.

			while (getpHands().size() > handPos) {
				thisHandDone = false;
				handPos++;
				Hand activeHand = getpHands().get(handPos - 1);
				if (getpHands().size() != 1) {
					System.out.println("New Hand Round begins!");
				}

				while (thisHandDone == false) {

					if (getpHands().size() != 1 || activeHand.getHand().size() != 2) {
						System.out
								.println("Your Hand: " + activeHand.getHand()
										+ ". Card count is at "
										+ getDeck().getCount() + ".");
						activeHand.valueDisp();
					}

					StringBuilder baseOptions = new StringBuilder();
					baseOptions.append("Acceptable moves: (H)IT. (S)TAND. ");

					if (activeHand.getHand().size() == 2) {
						baseOptions.append("(D)OUBLE DOWN. ");
					}

					if (activeHand.getHand().size() == 2 && isSURRENDER()) {
						baseOptions.append("SU(R)RENDER. ");
					}
					if (activeHand.hasSplit() == true
							&& activeHand.getHand().size() == 2
							&& getpHands().size() <= getSPLITNUM()) {
						baseOptions.append("S(P)LIT. ");
					}

					System.out.println(baseOptions);
					System.out.println((Strategy.strategy(
							activeHand.getValueInt(),
							getDealerHand().getHand().get(0).getRank(),
							activeHand.soft17Check(), activeHand.hasSplit(),
							activeHand.surrCheck(), activeHand.ddCheck())));

					String decision = scan.nextLine();

					// SPLIT
					if (decision.toUpperCase().equals("P")) {
						if (activeHand.getHand().size() != 2
								|| activeHand.hasSplit() == false
								|| getpHands().size() > getSPLITNUM()) {

							System.out.println("Cannot Split now.");
						}
						if (activeHand.getHand().size() == 2
								&& activeHand.hasSplit() == true
								&& getpHands().size() <= getSPLITNUM()) {
							Hand splitHand = new Hand();
							setWallet(getWallet() - getBetnum());
							splitHand.getHand()
									.add(activeHand.getHand().get(1));
							activeHand.getHand().remove(1);
							activeHand.getHand().add(getDeck().draw());
							splitHand.getHand().add(getDeck().draw());
							getpHands().add(splitHand);
							System.out.println("Split Hands. "
									+ activeHand.getHand()
									+ splitHand.getHand());
							thisHandDone = true;
							handPos--;
						}
					}

					// HIT
					if (decision.toUpperCase().equals("H")) {
						activeHand.getHand().add(getDeck().draw());
						System.out.println("You Hit and draw "
								+ (activeHand.getHand().get((activeHand
										.getHand().size()) - 1)) + ".");

						if (activeHand.getValueInt() > 21) {
							System.out.println("You bust with "
									+ activeHand.getValueInt() + "!");
							thisHandDone = true;
							getpScores().add(activeHand.getValueInt());
							getpChoices().add("N");
						}
					}

					// STAND
					else if (decision.toUpperCase().equals("S")) {
						System.out.println("You Stand with "
								+ activeHand.getValueInt() + ".");
						thisHandDone = true;
						getpScores().add(activeHand.getValueInt());
						getpChoices().add("N");
					}

					// DOUBLE DOWN
					else if (decision.toUpperCase().equals("D")) {
						if (activeHand.getHand().size() == 2) {
							activeHand.getHand().add(getDeck().draw());
							setWallet(getWallet() - getBetnum());
							System.out.println("You Double Down with "
									+ activeHand.getValueInt() + ".");
							thisHandDone = true;
							if (activeHand.getValueInt() > 21) {
								System.out.println("You bust with "
										+ activeHand.getValueInt() + "!");
							}
							getpScores().add(activeHand.getValueInt());
							getpChoices().add("D");
						} else if (activeHand.getHand().size() != 2) {
							System.out.println("Cannot Double Down this turn.");
						}
					}

					// SURRENDER
					else if (isSURRENDER() && decision.toUpperCase().equals("R")) {
						if (activeHand.getHand().size() == 2) {
							System.out.println("You Surrender this Hand.");
							thisHandDone = true;
							getpScores().add(0);
							getpChoices().add("R");
						} else if (activeHand.getHand().size() != 2
								|| isSURRENDER() == false) {
							System.out.println("Cannot Surrender.");
						}
					}

				}
			}
			System.out.println("Your turn is over.");
			allHandsDone = true;

		}
		return getpScores();
	}

	// PLAYER'S TURN IS OVER; IT IS NOW THE DEALER'S TURN.

	public static void dealerTurn(List<Integer> pScores, List<String> pChoices) {

		boolean dealerTurn = true;
		int turnCount = -1;
		for (String x : pChoices) {
			turnCount++;
			// IF THIS HAND WAS NOT A SURRENDER, THIS HAND WAS NOT A BUST, AND
			// THIS HAND WAS NOT INVOLVED IN BLACKJACKERY;
			if (!x.equals("R") && (pScores.get(turnCount)) <= 21
					&& result == "NEUTRAL") {
				// THEN, THE DEALER'S TURN MUST NOT ALREADY BE LOOKED OVER, AND
				// WE WILL CONSUME CARDS FROM THE SHOE FOR A DEALER SCORE.
				dealerTurn = false;
			}
		}

		if (dealerTurn == false) {
			System.out.println("Dealer's Turn. Dealer has "
					+ getDealerHand().getHand() + " for a value of "
					+ getDealerHand().getValueInt() + ".");
			// RIGHT HERE WE COUNT THE PREVIOUSLY HIDDEN DEALER CARD.
			getDeck().cardCounter(getDealerHand().getHand().get(1).getRank());
			System.out.println("Card Count is now at " + getDeck().getCount() + ".");
		}

		while (dealerTurn == false) {
			// If the Dealer busts, then he is done his turn.
			if (getDealerHand().getValueInt() > 21) {
				System.out.println("Dealer Busts with "
						+ getDealerHand().getValueInt() + "!");
				dealerTurn = true;
			}
			// If the Dealer didn't bust yet, then he needs to stand on 17+
			// (Depending on house rules) and hit on lower numbers.
			else if (getDealerHand().getValueInt() < 22) {
				if (getDealerHand().getValueInt() >= 17) {
					if (getDealerHand().soft17Check() == false
							|| getDealerHand().getValueInt() != 17
							|| SOFT17HIT == false) {
						System.out.println("Dealer Stands with "
								+ getDealerHand().getValueInt() + ".");
						dealerTurn = true;
					}
				}
				if (getDealerHand().getValueInt() < 17
						|| (getDealerHand().getValueInt() == 17
								&& getDealerHand().soft17Check() == true && SOFT17HIT)) {
					if (getDealerHand().getValueInt() == 17
							&& getDealerHand().soft17Check() == true) {
						System.out
								.println("Dealer hits on Soft 17 at our tables.");
					}
					getDealerHand().getHand().add(getDeck().draw());
					System.out.println("Dealer hits and draws "
							+ getDealerHand().getHand().get(
									(getDealerHand().getHand().size() - 1)) + ".");
				}

			}
		}
	}

	@SuppressWarnings("unused")
	public static void results(List<Integer> scores, List<String> choices,
			int dScore) {
		int turnCount = -1;

		// The integer B represents the score of each of the player's hands; the
		// corresponding choice (DD/Surrender/Nothing) is also referenced for
		// impact.
		for (int b : scores) {
			turnCount++;
			System.out.println("Hand #" + (turnCount + 1) + ":");
			// System.out.println("B=" + b + ". dScore=" + dScore + ". Result="
			// + result + ".");
			if (b > 21) {
				System.out
						.println("You Busted this Hand for a loss! Chip count is "
								+ getWallet() + ".");
			} else if (choices.get(turnCount).toUpperCase().equals("R")) {
				setWallet((getWallet() + getBetnum() * .5));
				System.out.println("You Surrendered this Hand. Chip count is "
						+ getWallet() + ".");

			} else if (dScore > 21) {
				if (choices.get(turnCount).equals("D")) {
					setWallet(getWallet() + (getBetnum() * 2));
				}
				setWallet(getWallet() + (getBetnum() * 2));
				System.out
						.println("Dealer Busted! You win this hand! Chip count grows to "
								+ getWallet() + "!");

			} else if (b > dScore) {
				if (choices.get(turnCount).equals("D")) {
					setWallet(getWallet() + (getBetnum() * 2));
				}
				setWallet(getWallet() + (getBetnum() * 2));
				System.out.println("You win this hand! Chip count grows to "
						+ getWallet() + "!");
			}

			else if (b == dScore) {
				if (choices.get(turnCount).equals("D")) {
					setWallet(getWallet() + getBetnum());
				}
				setWallet(getWallet() + getBetnum());
				System.out.println("Push! Chip count returns to " + getWallet()
						+ "!");
			}

			else if (b < dScore) {
				System.out.println("You lose this hand! Chip count is at "
						+ getWallet() + ".");
			}
		}
		if (isAUTOMODE() == false) {
			System.out.println("TYPE O(K) TO CONTINUE.");

			while (true) {
				String decision = scan.nextLine();
				if (decision.toUpperCase().equals("K")) {
					break;
				}
				System.out.println("Gonna need you to type that (K) sir.");
			}
		}
//clear();
	}

	public static void clear (){
	getpHands().clear();
	getpScores().clear();
	getpChoices().clear();
	System.out.println("Cleared");
}

	public static List<Integer> setDeck(Integer... integers) {
		Shoe deck = new Shoe(integers);
		Main.deck = deck;
		return play();
	}

	public static List<Integer> autoPlay() {
		// This is the initial play phase. When this phase ends, we loop back to
		// the Main Method, which will bring us back here.

		setWallet(getWallet() - getBetnum());

		System.out.println("AutoPlay Phase. You bet " + getBetnum() + ". Wallet now at "
				+ getWallet() + ".");

		// The betting phase concludes. Now we deal two face-up cards to the
		// player, and one face-up / one face-down card to the dealer.

		allHandsDone = false;
		thisHandDone = false;
		result = "NEUTRAL";
		Hand playerHand = new Hand();
		setDealerHand(new Hand());
		playerHand.getHand().add(getDeck().draw());
		getDealerHand().getHand().add(getDeck().draw());
		playerHand.getHand().add(getDeck().draw());
		getDealerHand().getHand().add(getDeck().hiddenDraw());

		System.out.println("Your Hand: " + playerHand.getHand()
				+ ". Card count is at " + getDeck().getCount() + ".");
		playerHand.valueDisp();

		System.out.println("Dealer showing " + getDealerHand().getHand().get(0)
				+ ".");

		if (playerHand.getValueInt() == 21) {
			allHandsDone = true;
			// IF DEALER IS SHOWING AN ACE, PRESENT EVEN MONEY.
			if (getDealerHand().getHand().get(0).getRank() == 1) {
				System.out
						.println("Dealer flashes his face-up Ace. Do you wish to walk away with Even Money? (Y)ES / (N)O.");
				while (true) {
					if (isBuyInsurance()) {
						setWallet((getBetnum() * 2) + getWallet());
						result = "EMONEYYES";
						System.out
								.println("You walk away with Even Money. Chip count up to "
										+ getWallet() + "!");
						break;
					}
					if (isBuyInsurance() == false) {
						if (getDealerHand().getValueInt() == 21) {
							result = "EMONEYNO";
							setWallet(getBetnum() + getWallet());
							System.out
									.println("Should have taken that Even Money - Dealer Had Blackjack! Chip count still  "
											+ getWallet() + ".");
							break;
						}
						if (getDealerHand().getValueInt() != 21) {
							result = "PBJ";
							System.out.println("Dealer don't got that BJ!");
							break;
						}
					}
					System.out.println("Gonna need a Y or N, butthorn.");
				}
			}
			// IF DEALER IS NOT SHOWING AN ACE, GO STRAIGHT TO CALCULATIONS.
			if (getDealerHand().getHand().get(0).getRank() != 1) {
				if (getDealerHand().getValueInt() == 21) {
					result = "PDBJ";
				} else if (getDealerHand().getValueInt() != 21) {
					result = "PBJ";
				}
			}

		}

		// WARNING:: UNSAFE INPUT.
		else if (playerHand.getValueInt() != 21) {
			if (getDealerHand().getHand().get(0).getRank() == 1) {
				while (true) {
					System.out
							.println("Dealer showing Ace. Purchase Insurance? (Y)es or (N)o.");
				//	String decision = scan.nextLine();
					if (isBuyInsurance()) {
						if (getDealerHand().getValueInt() == 21) {
							allHandsDone = true;
							result = "INSDBJ";
						} else if (getDealerHand().getValueInt() != 21) {
							setWallet(getWallet() - (getBetnum() / 2));
							System.out
									.println("Dealer does not have Blackjack, awful call. Wallet dropped to "
											+ getWallet());

						}
					}
					if (isBuyInsurance() == false) {
						if (getDealerHand().getValueInt() == 21) {
							allHandsDone = true;
							result = "NOINSDBJ";
						} else if (getDealerHand().getValueInt() != 21) {
							System.out
									.println("Dealer does not have Blackjack. Play continues.");

						}
					}

						break;
					}

				}
			}
			if (getDealerHand().getHand().get(0).getRank() != 1
					&& getDealerHand().getValueInt() == 21) {
				allHandsDone = true;
				result = "DBJ";
			}

		

		/*
		 * if (dealerHand.getValueInt() == 21) { allHandsDone = true; System.out
		 * .
		 * println("You lose this hand to Dealer Blackjack! Chip count drops to "
		 * + wallet + "."); }
		 */
		// HERE, THE RESULTS OF ANY EARLY BLACKJACKERY OCCURS.
		if (result.equals("PBJ")) {
			setWallet(getWallet() + (getBetnum() + (getBetnum() * 1.5)));
			System.out.println("Dat Blackjack pays 3:2. Chip count grows to "
					+ getWallet() + "!");
		}
		if (result.equals("DBJ")) {
			System.out.println("You lose this hand! Chip count drops to "
					+ getWallet() + ".");
		}
		if (result.equals("PDBJ")) {
			setWallet(getWallet() + getBetnum());
			System.out.println("Push! Chip count returns to " + getWallet() + "!");
		}

		if (result.equals("INSDBJ")) {
			setWallet(getWallet() + getBetnum());
			System.out
					.println("You correctly insured yourself from Dealer's Blackjack. Chip count returns to "
							+ getWallet() + ".");
		}

		if (result.equals("NOINSDBJ")) {

			System.out
					.println("You failed to insure from Dealer's Blackjack. Chip count drops to "
							+ getWallet() + ".");
		}
		

		// ASSUMING NO BLACKJACKERY FUNNY BUSINESS OCCURRED, THIS IS WHERE THE
		// MEAT AND POTATOES OF A PLAYER'S GAME GETS PLAYED OUT.

		getpHands().add(playerHand);
		int handPos = 0;
		if (allHandsDone == true) {
			getpScores().add(0);
			getpChoices().add("N");
		}
		splitAces = false;
		
		while (allHandsDone == false) {

			// This is where we move forward one hand, establish which hand is
			// the "Active Hand", and begin a new round of play.

			while (getpHands().size() > handPos) {
				thisHandDone = false;
				handPos++;
				Hand activeHand = getpHands().get(handPos - 1);
				if (getpHands().size() != 1) {
					System.out.println("New Hand Round begins!");
				}
				if (activeHand.getHand().size() == 1) {
					activeHand.getHand().add(getDeck().draw());
				}

				while (thisHandDone == false) {

					if (getpHands().size() != 1 || activeHand.getHand().size() != 2) {
						System.out
								.println("Your Hand: " + activeHand.getHand()
										+ ". Card count is at "
										+ getDeck().getCount() + ".");
						activeHand.valueDisp();
					}

					StringBuilder baseOptions = new StringBuilder();
					baseOptions.append("Acceptable moves: (H)IT. (S)TAND. ");

					if (activeHand.getHand().size() == 2) {
						baseOptions.append("(D)OUBLE DOWN. ");
					}

					if (activeHand.getHand().size() == 2 && isSURRENDER()) {
						baseOptions.append("SU(R)RENDER. ");
					}
					if (activeHand.hasSplit() == true
							&& activeHand.getHand().size() == 2
							&& getpHands().size() <= getSPLITNUM()) {
						baseOptions.append("S(P)LIT. ");
					}

					// System.out.println(baseOptions);

					String decision = (Strategy.strategy(
							activeHand.getValueInt(),
							getDealerHand().getHand().get(0).getRank(),
							activeHand.soft17Check(), activeHand.hasSplit(),
							activeHand.surrCheck(), activeHand.ddCheck()));
					
					if (splitAces){
					System.out.println("Can't Split Aces Again; Forced Stand.");
					decision="Stand";
					}
					
					System.out.println(decision);

					// SPLIT
					if (decision.toUpperCase().equals("SPLIT")) {
						if (activeHand.getHand().size() != 2
								|| activeHand.hasSplit() == false
								|| getpHands().size() > getSPLITNUM()) {
							System.out.println("Cannot Split now.");
						}
					
						if (activeHand.getHand().size() == 2
								&& activeHand.hasSplit() == true
								&& getpHands().size() <= getSPLITNUM()) {
							if (activeHand.getValueInt()==12){
							System.out.println("YOU DID IT!!!");
							splitAces=true;
							}
							Hand splitHand = new Hand();
							setWallet(getWallet() - getBetnum());
							splitHand.getHand()
									.add(activeHand.getHand().get(1));
							activeHand.getHand().remove(1);
							activeHand.getHand().add(getDeck().draw());
							// splitHand.getHand().add(deck.draw());
							getpHands().add(splitHand);
							System.out.println("Split Hands. New Active Hand: "
									+ activeHand.getHand() + ". "
									+ splitHand.getHand()
									+ " set aside for future play.");
							thisHandDone = true;
							handPos--;
							
						}
					}

					// HIT
					if (decision.toUpperCase().equals("HIT")) {
						activeHand.getHand().add(getDeck().draw());
						System.out.println("You Hit and draw "
								+ (activeHand.getHand().get((activeHand
										.getHand().size()) - 1)) + ".");

						if (activeHand.getValueInt() > 21) {
							System.out.println("You bust with "
									+ activeHand.getValueInt() + "!");
							thisHandDone = true;
							getpScores().add(activeHand.getValueInt());
							getpChoices().add("N");
						}
					}

					// STAND
					else if (decision.toUpperCase().equals("STAND")) {
						System.out.println("You Stand with "
								+ activeHand.getValueInt() + ".");
						thisHandDone = true;
						getpScores().add(activeHand.getValueInt());
						getpChoices().add("N");
					}

					// DOUBLE DOWN
					else if (decision.toUpperCase().equals("DOUBLEDOWN")) {
						if (activeHand.getHand().size() == 2) {
							activeHand.getHand().add(getDeck().draw());
							setWallet(getWallet() - getBetnum());
							System.out.println("You Double Down with "
									+ activeHand.getValueInt() + ".");
							thisHandDone = true;
							if (activeHand.getValueInt() > 21) {
								System.out.println("You bust with "
										+ activeHand.getValueInt() + "!");
							}
							getpScores().add(activeHand.getValueInt());
							getpChoices().add("D");
						} else if (activeHand.getHand().size() != 2) {
							System.out.println("Cannot Double Down this turn.");
						}
					}

					// SURRENDER
					else if (isSURRENDER()
							&& decision.toUpperCase().equals("SURRENDER")) {
						if (activeHand.getHand().size() == 2) {
							System.out.println("You Surrender this Hand.");
							thisHandDone = true;
							getpScores().add(0);
							getpChoices().add("R");
						} else if (activeHand.getHand().size() != 2
								|| isSURRENDER() == false) {
							System.out.println("Cannot Surrender.");
						}
					}

				}
			}
			System.out.println("Your turn is over.");
			allHandsDone = true;

		}
		/*
		 * // THIS IS A SIMPLE CALCULATION FOR BLACKJACK PAYOUTS. if
		 * (pScores.size()==1 && pScores.get(0)==21 &&
		 * pHands.get(0).getHand().size()==2){ wallet = (wallet + (betnum +
		 * (betnum * 1.5)) - (betnum * 2)); }
		 */
		System.out.println(getpScores());
		return getpScores();
	}




	public static boolean isAUTOMODE() {
		return AUTOMODE;
	}




	public static void setAUTOMODE(boolean aUTOMODE) {
		AUTOMODE = aUTOMODE;
	}




	public static boolean isSURRENDER() {
		return SURRENDER;
	}




	public static void setSURRENDER(boolean sURRENDER) {
		SURRENDER = sURRENDER;
	}




	public static int getSPLITNUM() {
		return SPLITNUM;
	}




	public static void setSPLITNUM(int sPLITNUM) {
		SPLITNUM = sPLITNUM;
	}




	public static boolean isBuyInsurance() {
		return buyInsurance;
	}




	public static void setBuyInsurance(boolean buyInsurance) {
		Main.buyInsurance = buyInsurance;
	}




	public static double getWallet() {
		return wallet;
	}




	public static void setWallet(double wallet) {
		Main.wallet = wallet;
	}




	public static double getBetnum() {
		return betnum;
	}




	public static void setBetnum(double betnum) {
		Main.betnum = betnum;
	}




	public static Shoe getDeck() {
		return deck;
	}




	public static void setDeck(Shoe deck) {
		Main.deck = deck;
	}




	public static List<Integer> getpScores() {
		return pScores;
	}




	public static void setpScores(List<Integer> pScores) {
		Main.pScores = pScores;
	}




	public static List<String> getpChoices() {
		return pChoices;
	}




	public static void setpChoices(List<String> pChoices) {
		Main.pChoices = pChoices;
	}




	public static Hand getDealerHand() {
		return dealerHand;
	}




	public static void setDealerHand(Hand dealerHand) {
		Main.dealerHand = dealerHand;
	}




	public static List<Hand> getpHands() {
		return pHands;
	}




	public static void setpHands(List<Hand> pHands) {
		Main.pHands = pHands;
	}
}

//

