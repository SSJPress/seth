package seth2;

import java.util.ArrayList;
import java.util.List;

public class Hand {

	private List<Card> handList;
	private Card cardActive;
	private int cardSum;
	private int aceCount;

	
	public Hand() {
			this.handList = new ArrayList<Card>();
	}

	public void setHandList(List<Card> handList) {
		this.handList = handList;
	}

	public List<Card> getHand() {
		return handList;
	}
	
	public boolean hasSplit() {
		int card1 = this.handList.get(0).getRank();
		int card2 = this.handList.get(1).getRank();
		if (card1 > 10){card1 = 10;}
		if (card2 > 10){card2 = 10;}
		//System.out.println("Card1: " + card1 + ". Card2: " + card2 + ".");
		//System.out.println(this.handList.size());
		if (this.handList.size()==2 && (card1 == card2)){
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean surrCheck(){
			if (this.handList.size()==2 && Main.isSURRENDER()==true){
		return true;
		}
		else {
			return false;
		}
	}
	
	public boolean ddCheck(){
		if (this.handList.size()==2){
	return true;
	}
	else {
		return false;
	}
}
	
	public void getValue () {
	getValueInt();
	
	}

	public int getValueIntBAK () {
		this.cardSum = 0;
		this.aceCount=0;
		
		for (int a=0; a<(handList.size()); a++){
			// The Active Card we are dealing with is set to whatever card in the Hand's List "a" points to. 
			this.cardActive = handList.get(a);
			
			// DummyInt is the Number of the Active Card. This is so we can set 11-13 to 10s, and 1 to Ace. 
			int dummyInt = cardActive.getRank();
			
			// Rank check for Face Cards
			if (cardActive.getRank() >= 10) {
				dummyInt = 10;	
			}
			// Rank check for Aces
			if (cardActive.getRank() == 1) {
				dummyInt = 11;
				this.aceCount++;
			}
			
			this.cardSum = dummyInt + this.cardSum;
			

		}
		
		if (aceCount > 0){
		int dummyInt = cardSum - (aceCount*10);
		if (dummyInt <= 11) {dummyInt=dummyInt+10;}
		}
		return cardSum;
		
			// BE PREPARED TO DISPLAY BOTH HAND VALUES WITH AN ACE FOR COUNTING AND DECISIONMAKING PURPOSES
			//while (aceCount > 0 && this.cardSum > 21) {
				
				//if 
				
			//	aceCount--;
			//	this.cardSum = this.cardSum-10;
		//		}	
	}
	
	public int getValueInt () {
		this.cardSum = 0;
		this.aceCount=0;
		
		for (int a=0; a<(handList.size()); a++){
			// The Active Card we are dealing with is set to whatever card in the Hand's List "a" points to. 
			this.cardActive = handList.get(a);
			
			// DummyInt is the Number of the Active Card. This is so we can set 11-13 to 10s, and 1 to Ace. 
			int dummyInt = cardActive.getRank();
			
			// Rank check for Face Cards
			if (cardActive.getRank() >= 10) {
				dummyInt = 10;	
			}
			// Rank check for Aces
			if (cardActive.getRank() == 1) {
				dummyInt = 11;
				this.aceCount++;
			}
			
			this.cardSum = dummyInt + this.cardSum;
			

		}
		
		int value = cardSum;
		
		if (aceCount > 0){
		int dummyInt = cardSum - (aceCount*10);
		if (dummyInt <= 11) {dummyInt=dummyInt+10;}
		value = dummyInt;
		}
		return value;
		
			// BE PREPARED TO DISPLAY BOTH HAND VALUES WITH AN ACE FOR COUNTING AND DECISIONMAKING PURPOSES
			//while (aceCount > 0 && this.cardSum > 21) {
				
				//if 
				
			//	aceCount--;
			//	this.cardSum = this.cardSum-10;
		//		}	
	}
	
	public int valueDisp(){
		getValueInt();
		if (aceCount > 0){
		int dummyInt = cardSum - (aceCount*10);
		if (dummyInt <= 11){
			System.out.println("You've got " + dummyInt + " or " + (dummyInt+10) + ".");
			cardSum=dummyInt+10;
		}
		else{
			System.out.println("You've got " + dummyInt + ".");
		}
		}
		if (aceCount <= 0){
			System.out.println("You've got " + cardSum + ".");
		}
		
		return this.cardSum;
			}

	
	
	public boolean soft17Check(){
			getValueInt();
			boolean soft17Check = false;
			if (aceCount > 0){
			int dummyInt = cardSum - (aceCount*10);
			if (dummyInt <= 11){
			soft17Check = true;
			}
			}
			return soft17Check;
		
	}
	
	public int getCardSum(){
		return this.cardSum;
	}
	
	
}
