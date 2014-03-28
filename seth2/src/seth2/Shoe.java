package seth2;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


public class Shoe {

	private int shoeSize;
	private List<Card> deckList;
	private int deckPos = 0;
	private int cardCount;

	public int getShoeSize() {
		return this.shoeSize;
	}

	// Establishing future use of how many decks make up a ShoeClass instance
	// (shoeSize) and a List of Card objects.

	public Shoe() {
		// This Constructor is hit when creating a ShoeClass with no custom Deck
		// amount input, defaulting it to 2 decks, then firing the deckGen
		// method.
		this.shoeSize = 2;
		deckGen();
	}

	public Shoe(int shoeSize) {
		// This Constructor is hit when creating a ShoeClass with custom Deck
		// amount before shipping it off to the deckGen method.
		this.shoeSize = shoeSize;
		this.deckPos = 0;
		deckGen();
	}
	
	// RIGGING YOUR SHOE.
	public Shoe(Integer ... integers ){
		this.shoeSize = 6;
		deckList = new ArrayList<Card>();
		for (int x : integers){
			Card c = new Card("Spades", x);
			deckList.add(c);
		}
		
		for (int i = 0; i < (this.shoeSize*52)-integers.length; i++){
			Card c = new Card("Spades", 10);
			deckList.add(c);
		}
		cardCount = shoeSize * -2;
		deckPos = 0;
	}
	
	private void deckGen() {
		// Deck Generator Method.
		List<Card> deckList = new ArrayList<Card>();
		{
			for (int c = 0; c < shoeSize; c++) {
				for (int a = 0; a <= 3; a++) {

					for (int b = 1; b <= 13; b++) {
						String cardsuit = new String();
						if (a == 0) {
							cardsuit = "Hearts";
						}
						if (a == 1) {
							cardsuit = "Spades";
						}
						if (a == 2) {
							cardsuit = "Clubs";
						}
						if (a == 3) {
							cardsuit = "Diamonds";
						}

						Card bicycle = new Card(cardsuit, b);
						deckList.add(bicycle);
					}
				}

			}
		}

		this.setDeckList(deckList);

		// this.deckList = deckList;

		// System.out.println("You have generated a Shoe with " + this.shoeSize
		// + " Decks.");
		shuffleShoe();
	}

	public int getRemainingSize() {
		return this.deckList.size();
	}

	public void shuffleShoe() {
		cardCount = shoeSize * -2;
		//System.out.println(cardCount);
		Collections.shuffle(this.deckList);
		// This is where we burn.
		deckPos = 1;
		System.out.println("Dealer burns the first card; " + deckList.get(0));
		cardCounter(deckList.get(0).getRank());
		//System.out.println(cardCount);
//		System.out.println(deckList);
	}

	public void setDeckList(List<Card> deckList) {
		this.deckList = deckList;
	}

	public List<Card> getDeckList() {
		return deckList;
	}

	public int getDeckPos() {
		return deckPos;
	}



	public Card draw() {
		if (deckPos >= (shoeSize*52)-8){
			shuffleShoe();
			}
		deckPos++;
		int drawnRank = deckList.get(deckPos - 1).getRank();
		cardCounter(drawnRank);
		return this.deckList.get(deckPos - 1);
	}

	// If you are drawing to the Dealer's Face Down card (HIDDEN);
	public Card hiddenDraw() {
		deckPos++;
		return this.deckList.get(deckPos - 1);
	}
	

	
	
	public void cardCounter(int drawnRank){
		if (drawnRank > 1 && drawnRank <= 6) {
			cardCount = cardCount + 1;
		}
		if (drawnRank == 7
				&& (deckList.get(deckPos - 1).getSuit().equals("Diamonds") || (deckList
						.get(deckPos - 1).getSuit().equals("Hearts")))) {
			cardCount = cardCount + 1;
		}
		if (drawnRank == 1 || drawnRank > 9) {
			cardCount = cardCount - 1;
		}
	}
	
	public int getCount() {
		return this.cardCount;
	}

	public int getPos() {
		return this.deckPos;
	}

}
