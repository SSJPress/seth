package seth2;

public class Card {
	private int rank;
	private String suit;
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		if (rank == 1){
			sb.append("The Ace");
		}
		if (rank != 1 && rank <= 10){
		    sb.append("The ").append(rank);
		}
		if (rank == 11){
			sb.append("The Jack");
		}
		if (rank == 12){
			sb.append("The Queen");
		}
		if (rank == 13){
			sb.append("The King");
		}
		
		sb.append(" of ").append(suit);
		
		return sb.toString();
	}
	

	public Card(String cardSuit, int cardRank){
		
		this.rank=cardRank;
		this.suit=cardSuit;
	}


	public int getRank() {
		return rank;
	}

	public String getSuit() {
		return suit;
	}
	
}
