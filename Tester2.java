import java.util.ArrayList;
import java.lang.reflect.Field;

public class Tester extends CardPile{
  
  static boolean printAll = false;
  
  static void log(String msg)
  {
    if (printAll)
      System.out.println(msg);
  }
 
 public static void main(String [] args){
  try {
   test_cardpile();
   test_new_methods();
   test_blackjack();
  }
  catch(NoSuchFieldException | IllegalAccessException f){
   System.out.println("Access exception (missing method?)");
   f.printStackTrace();
  }
  System.out.println();
 }

 public static ArrayList<Card> getCards(CardPile p) throws NoSuchFieldException, IllegalAccessException{
  Field field = CardPile.class.getDeclaredField("cards");
  field.setAccessible(true);
  @SuppressWarnings("unchecked")
  ArrayList<Card> cardArray = (ArrayList<Card>) field.get(p);
  return cardArray;
 }

 // CardPile Tester
 public static void test_cardpile() throws NoSuchFieldException, IllegalAccessException{
  System.out.println("Testing Q1: CardPile (30 pts)");
  
  // Test constructor, isEmpty.
  log("Q1: constructor");
  boolean cardPilePass = true;
  CardPile cardpile = new CardPile();
  log(getCards(cardpile).toString());
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

  // Test addToBottom, isEmpty.
  log("Q1: addToBottom");
  Suit s = Suit.HEARTS;
  Value v = Value.TWO;
  Card testCard0 = new Card(s, v);
  cardpile.addToBottom(testCard0);
  log(getCards(cardpile).toString());
  if (cardpile.getNumCards() != 1) {
   System.out.println("new cardPile has " + cardpile.getNumCards() + " cards, should have 1");
   cardPilePass = false;
  }
  
  log("Q1: isEmpty");
  log(cardpile.isEmpty() ? "true" : "false");
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

  // test get
  log("Q1: get");
  Card gotten = cardpile.get(0);
  try{
   if (gotten == null || gotten.getSuit() != Suit.HEARTS || gotten.getValue() != Value.TWO) {
    cardPilePass = false;
    System.out.println("Gotten card is " + gotten.toString());
   }
    log(gotten.toString());
  }
  catch (Exception ex){
   ex.printStackTrace();
   System.out.println("CardPile.get(0) throws exception");
   cardPilePass = false;
  }

  log("Q1: remove");  
  Card testCard1 = new Card(Suit.SPADES, Value.ACE);
  Card testCard2 = new Card(Suit.DIAMONDS, Value.NINE);
  cardpile.addToBottom(testCard1);
  cardpile.addToBottom(testCard2);
  
   log(getCards(cardpile).toString());
  Card removed = cardpile.remove(1);
  try{
   if (removed == null || removed.getSuit() != testCard1.getSuit() || removed.getValue() != testCard1.getValue()) {
    System.out.println("remove(1) removed the wrong card");    
    cardPilePass = false;
   }
   log(getCards(cardpile).toString());
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
  log("Q1: find");  
  cardpile.addToBottom(testCard0);
  Card testCard3 = new Card(Suit.CLUBS, Value.THREE);
  cardpile.addToBottom(testCard3);
  int foundIndex = cardpile.find(s, v);
  log(Integer.toString(foundIndex));
  if (foundIndex == -1) {
   System.out.println("Did not find existing card: " + getCards(cardpile).toString());
   cardPilePass = false;
  } else {
   Card found = cardpile.get(foundIndex);
   if (found.getSuit() != s || found.getValue() != v) {
    System.out.println("Wrong card found");
    cardPilePass = false;
   }
  }
  Card testCard4 = new Card(Suit.HEARTS, Value.JACK);
  foundIndex = cardpile.find(Suit.HEARTS, Value.JACK);
  log(Integer.toString(foundIndex));
  if (foundIndex != -1)
  {
   System.out.println("Found a card that doesn't exist.");
   cardPilePass = false;
  }
  
  log("Q1: toString");  
  log(cardpile.toString());
  
  log("Q1: makeFullDeck");
  CardPile fullDeck = makeFullDeck();
  log(fullDeck.toString());
  if (fullDeck.getNumCards() != 52) {
   System.out.println("makeFullDeck produces deck with " + fullDeck.getNumCards() + " cards.");
   cardPilePass = false;
  }
  if (!hasAllCards(fullDeck)) {
   System.out.println("makeFullDeck did not fill cards correctly.");
   cardPilePass = false;
  }
  
  log("Methods on full deck...");
  Card gc = fullDeck.get(0);
  Card rc = fullDeck.remove(0);
  if (fullDeck.getNumCards() != 51)
  {
   System.out.println("Couldn't remove last card.");
   cardPilePass = false;
  }
  if (gc.getSuit() != rc.getSuit() || gc.getValue() != rc.getValue())
  {
   System.out.println("Card removed incorrectly.");
   cardPilePass = false;
  }
  
  fullDeck.addToBottom(rc);
  if (fullDeck.getNumCards() != 52)
  {
   System.out.println("Couldn't add 52nd card.");
   cardPilePass = false;
  }
  gc = fullDeck.get(51);
  if (gc.getSuit() != rc.getSuit() || gc.getValue() != rc.getValue())
  {
   System.out.println("Card added incorrectly.");
   cardPilePass = false;
  }
  
  foundIndex = fullDeck.find(rc.getSuit(), rc.getValue());
  if (foundIndex != 51)
  {
   System.out.println("Couldn't find the last card.");
   cardPilePass = false;
  }

  if (cardPilePass) {
   System.out.println("CardPile class passes all tests!");
  }
 }

 public static void test_new_methods(){
  System.out.println("Testing Q2: CardPile (new methods) (10 pts)");
  
  // test getnumcards
  log("Q2: getNumCards (2 pts)");
  CardPile cardpile = CardPile.makeFullDeck();
  int size1 = cardpile.getNumCards();
  cardpile.remove(4);
  cardpile.remove(0);
  cardpile.remove(4);
  int size2 = cardpile.getNumCards();
  log(size1 + " -3 : " + size2 + " (52, 49)");
  if (size1 != 52 || size2 != 49)
  {
   System.out.println("getNumCards incorrect value.");
  }

  // test makeFullDeck
  log("Q2: makeFullDeck(n) (8 pts)");
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
  
  for (int i = 1; i < 4; i++) {
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
 }
 
 public static void test_blackjack() {
  System.out.println("Testing Q3: Blackjack (60 pts)");
  
  // test getScore
  log("Q3: getScore (5 pts)");
  boolean getScorePass = true;
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
  log("Q3: countValues (15 pts)");
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
  
  // cardpile with 21 aces should give score of 21 :D
  pile = new CardPile();
  for (int i = 0; i < 21; i ++)
   pile.addToBottom(new Card(Suit.DIAMONDS, Value.ACE));
  countValuesPass = countValuesPass && checkPileValue(pile, 21);
  
  // makeFullDeck cardpile
  pile = CardPile.makeFullDeck();
  countValuesPass = countValuesPass && checkPileValue(pile, 340);
  if (countValuesPass) {
   System.out.println("countValues() passes!");
  }
  
  // test playRound
  System.out.println("Q3: playRound() (30 pts) - you'll have to test this one by hand.");
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
