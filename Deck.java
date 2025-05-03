import java.util.Random;
import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> cards; 
    private String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};


    public Deck(){
        cards = new ArrayList<>(52); // cap the array at 52 objects to represent a deck
        initializeDeck();
        shuffle();
    }

    private void initializeDeck() {             // private method since only used in deck class
        for(String suit: suits) {
            for(int rank = 1; rank <= 13; rank++){
                cards.add(new Card(suit, rank));        // nested for loop to create cards of ranks 1-13 for each suit
            }
        }
    }

    public Card drawCard() {    // must return object of card class
        if(cards.isEmpty()){
            throw new IllegalStateException("Deck is empty!");      // error detection method to prevent draw of null
        }
        return cards.remove(0);     
    }

    public void shuffle(){          // uses the Fisher-Yates Shuffle Algorithm
        System.out.println("Shuffling deck...");
        Random random = new Random();
        for(int i = cards.size() - 1; i > 0; i--){
            int j = random.nextInt(i + 1);      // (i+1) since it is non-inclusive; chooses a random num from 0 to i
            Card temp = cards.get(i);       // stores the card at the end of the array as temporary
            cards.set(i, cards.get(j));     // replaces the last card with the card at random index j
            cards.set(j, temp);             // puts the card from i at j
        }
    }

    public void cut() {
        Random rand = new Random();
        int cutPoint = rand.nextInt(cards.size());
        ArrayList<Card> newDeck = new ArrayList<>();

        for (int i = cutPoint; i < cards.size(); i++) {
            newDeck.add(cards.get(i));      // puts the bottom half of the deck on top
        }

        for (int i = 0; i < cutPoint; i++) {
            newDeck.add(cards.get(i));      // puts the top half of the deck on the bottom
        }
        cards.clear();              // removes the old deck
        cards.addAll(newDeck);      // replaces the cards array with the temporary array in the instance
    }

    public boolean isEmpty()  { return cards.isEmpty();}

    public int remainingCards() {return cards.size();}


    public ArrayList<Card> dealCardsBoard() {
        ArrayList<Card> boardCards = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            boardCards.add(drawCard());
        }
        return boardCards;       // returns 4 cards
    }

}
