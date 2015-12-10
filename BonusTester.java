
public class BonusTester {
	public static void main(String[] args) {
		CardPile pile = CardPile.makeFullDeck(4);
		int roundResult = Blackjack.playRound(pile);
		System.out.println("playRound() returned score of " + roundResult);
	}
}
