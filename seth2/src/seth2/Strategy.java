package seth2;

public class Strategy {

	public static String strategy(int value, int dCard, boolean isSoft,
			boolean isSplittable, boolean SurrOk, boolean ddOk) {
		String advice = "Let's Figure This Out.";
		while (true) {
			// TREAT 5-5 AS A 10.
			if (value == 10 && isSplittable) {
				isSplittable = false;
			}
			
			// YOU ONLY GET TO SPLIT ACES ONCE.
			if ((Main.getpHands().size()>1) && (value == 12 && isSoft)) {
				isSplittable = false;
			}
			
			
			if (Main.getpHands().size() > Main.getSPLITNUM()){
				isSplittable = false;
			}
			
/*			System.out.println("SIZE TESTER!!!!!!!!!!!!!!" + Main.pHands.size());
			if ((Main.pHands.size()==2) && (value == 12 && isSoft)) {
				System.out.println("TESTER!!!!!!!!!!!!!!!!");
				Main.splitAces = true;
				break;
			}*/

			// FIRST, WE DEAL WITH SPLITTABLE LOGIC.
			if (isSplittable) {
				
				if (value == 20) {
					advice = "Stand";
					break;
				}
				if (value == 12 && isSoft) {
					advice = "Split";
					break;
				}

				if (value == 18) {
					advice = "Split";
					if (dCard == 7 || dCard == 10 || dCard == 1) {
						advice = "Stand";
					}
					break;
				}

				if (dCard <= 6 && dCard != 1) {
					advice = "Split";
					if (value == 8 && dCard < 5 && dCard != 1) {
						advice = "Hit";
					}
				}

				if (dCard == 7) {
					if (value < 8 || value > 12) {
						advice = "Split";
					} else {
						advice = "Hit";
					}
				}

				if (dCard >= 8 || dCard == 1) {
					if (value < 16) {
						advice = "Hit";
					}
					if (value == 16) {
						advice = "Split";
					}
				}

				break;
			}

			// NEXT, WE DEAL WITH SOFT HANDS - STILL ABLE TO BE A SIMULTANEOUS
			// HIGH AND LOW NUMBER.
			if (isSoft) {
				
				if (value == 22) {
					advice = "Split";
					break;
				}
				
				if (value >= 19) {
					advice = "Stand";
					break;
				}

				if (dCard >= 7 || dCard == 1) {
					advice = "Hit";
					if (value == 18 && (dCard == 7 || dCard == 8)) {
						advice = "Stand";
					}
				}

				if (dCard < 7 && dCard != 1) {
					advice = "Hit";

					if ((dCard >= 5) || (dCard == 4 && value > 14)
							|| (dCard == 3 && value == 17)) {
						advice = "Doublehit";
					}

					if (value == 18) {
						advice = "Doublestand";
						if (dCard == 2) {
							advice = "Stand";
						}
					}
				}
				break;
			}

			// WITH THAT OUT OF THE WAY, WE RESOLVE UNSPLITTABLE, HARD HANDS.

			if (isSplittable == false && isSoft == false) {
				advice = "Hit";

				if (value >= 17) {
					advice = "Stand";
				}

				if (value >= 12 && value < 17 && dCard < 7 && dCard != 1) {
					if (value != 12 && (dCard != 2 || dCard != 3)) {
						advice = "Stand";
					}
				}
				if ((value == 16 && (dCard > 8 || dCard == 1))
						|| (value == 15 && dCard == 10)) {
					advice = "Surrenderhit";
				}
				if ((value == 11 && (dCard != 1))
						|| (value == 10 && (dCard < 10 && dCard != 1))
						|| (value == 9 && (dCard < 7 && dCard > 2))) {
					advice = "Doublehit";
				}

				break;
			}
			

			

		}
		if (advice == "Doublestand") {
			if (ddOk == false){
				advice = "Stand";
			}
			else {
			advice = "DoubleDown";
			}
		}
		if (advice == "Doublehit") {
			if (ddOk == false){
				advice = "Hit";
			}
			else {
			advice = "DoubleDown";
			}
		}
		if (advice == "Surrenderhit"){
			if (SurrOk == false){
				advice = "Hit";
			}
			else {
			advice = "Surrender";
			}
		}
/*
		if (advice == "Hit") {
			advice = "Madden says: Hit this hand, son.";
		}
		if (advice == "Stand") {
			advice = "Madden says: Stand, son. Stand.";
		}
		if (advice == "Split") {
			advice = "Madden says: Split those babies, give em' the Blitz.";
		}
*/
		return advice;
	}
	
}
