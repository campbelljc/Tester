import java.util.ArrayList;
import java.lang.reflect.Field;

public class Tester extends CardPile{
	public static void main(String [] args){
		try {
			test_cardpile();
			testNewMethods();
			testBlackjack();
		}
		catch(NoSuchFieldException | IllegalAccessException f){
			System.out.println("Access exception.");
			f.printStackTrace();
		}
		System.out.println();
		//test_game();
	}

	public static ArrayList<Card> getCards(CardPile p) throws NoSuchFieldException, IllegalAccessException{
		Field field = CardPile.class.getDeclaredField("cards");
		field.setAccessible(true);
		ArrayList<Card> cardArray = (ArrayList<Card>) field.get(p);
		return cardArray;
	}


	// CardPile Tester
	public static void test_cardpile() throws NoSuchFieldException, IllegalAccessException{
		System.out.println("Testing Q3: CardPile (40 pts)");

		Suit s = Suit.HEARTS;
		Value v = Value.TWO;
		Card testCard0 = new Card(s, v);

		boolean cardPilePass = true;
		CardPile cardpile = new CardPile();
		if (cardpile.getNumCards() != 0) {
			System.out.println("new cardPile has " + cardpile.getNumCards() + " cards, should have 0");
			cardPilePass = false;
		}
		if (!cardpile.isEmpty()) {
			System.out.println("cardpile.isEmpty() returns false when cardpile *is* empty");
			cardPilePass = false;	  
		}
		ArrayList<Card> cardArray = getCards(cardpile); 
		if (cardArray == null || cardArray.size() > 0) {
			System.out.println("Initial card array is not empty");
			cardPilePass = false;
		}

		// Test addToBottom
		cardpile.addToBottom(testCard0);
		if (cardpile.getNumCards() != 1) {
			System.out.println("new cardPile has " + cardpile.getNumCards() + " cards, should have 1");
			cardPilePass = false;
		}
		if (cardpile.isEmpty()) {
			System.out.println("cardpile.isEmpty() returns true when cardpile is *not* empty");
			cardPilePass = false;	  
		}
		cardArray = getCards(cardpile); 
		Card firstCard = cardArray.get(0);
		if (firstCard == null || firstCard.getSuit() != Suit.HEARTS || firstCard.getValue() != Value.TWO) {
			cardPilePass = false;
			System.out.println("Card 0 is " + firstCard.toString());
		}

		// get(): 2 points
		Card gotten = cardpile.get(0);
		try{
			if (gotten == null || gotten.getSuit() != Suit.HEARTS || gotten.getValue() != Value.TWO) {
				cardPilePass = false;
				System.out.println("Gotten card is " + gotten.toString());
			}
		}
		catch (Exception ex){
			ex.printStackTrace();
			System.out.println("CardPile.get(0) throws exception");
			cardPilePass = false;
		}

		// System.out.print("remove(): 8 points - ");
		
		Card testCard1 = new Card(Suit.SPADES, Value.ACE);
		Card testCard2 = new Card(Suit.DIAMONDS, Value.NINE);
		cardpile.addToBottom(testCard1);
		cardpile.addToBottom(testCard2);
		
		Card removed = cardpile.remove(1);
		try{
			if (removed == null || removed.getSuit() != testCard1.getSuit() || removed.getValue() != testCard1.getValue()) {
				System.out.println("remove(1) removed the wrong card");		  
				cardPilePass = false;
			}
			cardArray = getCards(cardpile);
			Card card0 = cardArray.get(0);
			Card card1 = cardArray.get(1);
			Card card2 = null;
			try {
				card2 = cardArray.get(2);
			} catch (Exception e) {
				// It's ok if this causes an exception.
			}
			if (cardArray.get(0) == null || 
					card0.getSuit() != testCard0.getSuit() || card0.getValue() != testCard0.getValue() ||
					card1 == null ||
					card1.getSuit() != testCard2.getSuit() || card1.getValue() != testCard2.getValue() ||
					card2 != null || cardpile.getNumCards() != 2) {
				System.out.println("array after removal is incorrect: (" + cardpile.getNumCards() + ") " + 
					getCards(cardpile).toString());
				cardPilePass = false;
			}
		}
		catch (Exception ex){
			cardPilePass = false;
			ex.printStackTrace();
		}
		
		// Test find
		cardpile.addToBottom(testCard0);
		Card testCard3 = new Card(Suit.CLUBS, Value.THREE);
		cardpile.addToBottom(testCard3);
		int foundIndex = cardpile.find(s, v);
		if (foundIndex == -1) {
			System.out.println("Did not find existent card: " + getCards(cardpile).toString());
			cardPilePass = false;
		} else {
			Card found = cardpile.get(foundIndex);
			if (found.getSuit() != s || found.getValue() != v) {
				System.out.println("Wrong card found");
				cardPilePass = false;
			}
		}

		cardpile = makeFullDeck();
		if (cardpile.getNumCards() != 52) {
			System.out.println("makeFullDeck produces deck with " + cardpile.getNumCards() + " cards.");
			cardPilePass = false;
		}
		
		if (!hasAllCards(cardpile)) {
			cardPilePass = false;			
		}

		if (cardPilePass) {
			System.out.println("CardPile class passes all tests!");
		}
	}

	public static void testNewMethods(){
		// test makeFullDeck
		boolean makeFullDeckPass = true;
		try{
			CardPile pile0 = CardPile.makeFullDeck(0);
			if (pile0 == null || pile0.getNumCards() > 0) {
				System.out.println("makeFullDeck(0) returns non-empty deck: " + getCards(pile0).toString());
				makeFullDeckPass = false;
			}
		}
		catch (Exception ex){
			System.out.println("makeFullDeck(0) throws exception:");			
			ex.printStackTrace();
			makeFullDeckPass = false;
		}
		
		int n = 4; // Test makeFullDeck(n) with n = 1 to 100
		for (int i = 1; i < n; i++) {
			try{
				CardPile pile = CardPile.makeFullDeck(i);
				if (pile == null || pile.getNumCards() != i*52 || !hasAllCards(pile)) {
					System.out.println("makeFullDeck(" + i + 
							") returns deck of wrong size (" + pile.getNumCards() 
							+ "): " + getCards(pile).toString());
					makeFullDeckPass = false;
					break;
				}
			}
			catch (Exception ex){
				System.out.println("makeFullDeck(" + i + ") throws exception:");			
				ex.printStackTrace();
				makeFullDeckPass = false;
				break;
			}
		}
		
		if (makeFullDeckPass) {
			System.out.println("makeFullDeck(int n) passes all tests!");
		}
		
		// getNumCards() is adequately tested by every other method.
	}
	
	public static void testBlackjack() {
		boolean getScorePass = true;
		// test getScore
		int[] scores = {2,3,4,5,6,7,8,9,10,10,10,10,11};
		for (Suit suit : Suit.values()) {
			for (Value value: Value.values()) {
				Card c = new Card(suit, value);
				int score = scores[value.ordinal()];
				if (Blackjack.getScore(c) != score) {
					System.out.println("The card " + c.toString() + " gets the wrong score: " + 
										Blackjack.getScore(c));		  
					getScorePass = false;
				}
			}
		}
		if (getScorePass) {
			System.out.println("getScore() passes!");
		}
		
		// test countValues
		boolean countValuesPass = true;

		// empty cardpile
		CardPile pile = new CardPile();
		if (Blackjack.countValues(pile) != 0) {
			System.out.println("countValues() gives the empty cardpile the score " + Blackjack.countValues(pile));
			countValuesPass = false;
		}
		
		// 1-card cardpile
		for (Suit suit : Suit.values()) {
			for (Value value: Value.values()) {
				pile = new CardPile();
				Card c = new Card(suit, value);
				pile.addToBottom(c);
				int score = scores[value.ordinal()];
				if (Blackjack.countValues(pile) != score) {
					System.out.println("countValues() gives the cardpile with only " + c.toString() 
							+ " the wrong score: " + Blackjack.getScore(c));		  
					countValuesPass = false;
				}
			}
		}
		
		// cardpile with several cards, less than 21
		pile = new CardPile();
		pile.addToBottom(new Card(Suit.CLUBS, Value.EIGHT));
		pile.addToBottom(new Card(Suit.HEARTS, Value.THREE));
		countValuesPass = countValuesPass && checkPileValue(pile, 11);

		// cardpile with several cards, more than 21
		pile.addToBottom(new Card(Suit.SPADES, Value.KING));
		pile.addToBottom(new Card(Suit.SPADES, Value.FIVE));
		countValuesPass = countValuesPass && checkPileValue(pile, 26);
		
		// cardpile with 1 ace that goes over 21 with ace as 11, under with ace as 1
		pile = new CardPile();
		pile.addToBottom(new Card(Suit.SPADES, Value.KING));
		pile.addToBottom(new Card(Suit.SPADES, Value.FIVE));
		pile.addToBottom(new Card(Suit.DIAMONDS, Value.ACE));
		countValuesPass = countValuesPass && checkPileValue(pile, 16);
		
		// cardpile with 2 aces that goes over 21 with both aces as 11, under with one as 1
		pile = new CardPile();
		pile.addToBottom(new Card(Suit.SPADES, Value.SEVEN));
		pile.addToBottom(new Card(Suit.HEARTS, Value.ACE));
		pile.addToBottom(new Card(Suit.DIAMONDS, Value.ACE));
		countValuesPass = countValuesPass && checkPileValue(pile, 19);		
		
		// cardpile with 2 aces that goes over 21 with either ace as 11, under with both as 1
		pile = new CardPile();
		pile.addToBottom(new Card(Suit.SPADES, Value.SEVEN));
		pile.addToBottom(new Card(Suit.SPADES, Value.NINE));
		pile.addToBottom(new Card(Suit.HEARTS, Value.ACE));
		pile.addToBottom(new Card(Suit.DIAMONDS, Value.ACE));
		countValuesPass = countValuesPass && checkPileValue(pile, 18);		
		
		// cardpile with 1 ace that is always over 21
		pile = new CardPile();
		pile.addToBottom(new Card(Suit.SPADES, Value.KING));
		pile.addToBottom(new Card(Suit.SPADES, Value.FIVE));
		pile.addToBottom(new Card(Suit.CLUBS, Value.JACK));
		pile.addToBottom(new Card(Suit.DIAMONDS, Value.ACE));
		countValuesPass = countValuesPass && checkPileValue(pile, 26);
		
		// cardpile with 1 ace that is always under 21
		pile = new CardPile();
		pile.addToBottom(new Card(Suit.DIAMONDS, Value.FOUR));
		pile.addToBottom(new Card(Suit.HEARTS, Value.FIVE));
		pile.addToBottom(new Card(Suit.HEARTS, Value.ACE));
		countValuesPass = countValuesPass && checkPileValue(pile, 20);
		
		// cardpile with 1 ace that is exactly 21 with ace as 1
		pile = new CardPile();
		pile.addToBottom(new Card(Suit.DIAMONDS, Value.FOUR));
		pile.addToBottom(new Card(Suit.HEARTS, Value.FIVE));
		pile.addToBottom(new Card(Suit.DIAMONDS, Value.SEVEN));
		pile.addToBottom(new Card(Suit.CLUBS, Value.FOUR));
		pile.addToBottom(new Card(Suit.DIAMONDS, Value.ACE));
		countValuesPass = countValuesPass && checkPileValue(pile, 21);
		
		// cardpile with 1 ace that is exactly 21 with ace as 11
		pile = new CardPile();
		pile.addToBottom(new Card(Suit.DIAMONDS, Value.QUEEN));
		pile.addToBottom(new Card(Suit.DIAMONDS, Value.ACE));
		countValuesPass = countValuesPass && checkPileValue(pile, 21);
		
		// makeFullDeck cardpile
		pile = CardPile.makeFullDeck();
		countValuesPass = countValuesPass && checkPileValue(pile, 340);
		if (countValuesPass) {
			System.out.println("countValues() passes!");
		}
		
		// test playRound
		System.out.println("Calling playRound() - you'll have to test this one by hand.");
		Blackjack.playRound(pile.makeFullDeck(3));
		
		System.out.println("Now you should actually play their blackjack game.");
	}

	
	private static boolean hasAllCards(CardPile pile) {
		for (Suit suit : Suit.values()) {
			for (Value value: Value.values()) {
				int foundIndex = pile.find(suit, value);
				if (foundIndex == -1) {
					System.out.println("pile does not include " + suit.toString() + ", " + value.toString());			  
					return false;
				}
			}
		}
		return true;
	}
	
	private static boolean checkPileValue(CardPile pile, int correctScore) {
		int score = Blackjack.countValues(pile);
		if (score != correctScore) {
			System.out.println("countValues() returns " + score 
					+ ", but correct score is " + correctScore);
			return false;
		}
		return true;
	}

}
