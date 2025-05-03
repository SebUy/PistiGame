import java.util.ArrayList;

public class Player{
    private String name;        // Either player or bot 
    private ArrayList<Card> hand;  // was made protected due to visibility issues with bot subclass
    private ArrayList<Card> collected;
    private int score;
    private int regularCardCount;

    public Player(String name){
        this.name = name;
        this.hand = new ArrayList<>();
        this.collected = new ArrayList<>();
        this.score = 0;
    }

    public void addToHand(Card card){
        hand.add(card);
    }

    public Card playCard(int index){
        if(index < 0 || index >= hand.size()){
            throw new IllegalArgumentException("Invalid card index");
        }
        return hand.remove(index);
    }

    public void collectCards(ArrayList<Card> cards){        // for when the same rank or jack is played
        collected.addAll(cards);
    }

    public void calculateScore(){
        score = 0;
        int pistiCount = 0;
        int regularCardCount = 0;
       
        for(Card card : collected){
            if(card.isPisti()){
                pistiCount ++;                                  // keeps track of pisti count
            }else{
                score += card.getPoints();                      // Sum points of the regular cards
                regularCardCount ++;
            }
        }
        
        score += pistiCount*10;                                 // each pisti is worth 10 points
    }

    public void addToScore(int bonus) {                         // for player with most cards
        score += bonus;
    }


    public boolean hasCardOfRank(int rank){                     // for use in bot and validation
        for(Card card : hand){
            if(card.getRank() == rank){
                return true;
            }
        }
        return false;
    }

    public void displayHand(){                                  // displays each card in hand while asigning it to a number
        System.out.print(name + "'s hand: ");
        for(int i=0; i< hand.size(); i++){
            System.out.print("[" + i +"]" + hand.get(i)+ " ");
        }
        System.out.println("");
    }


    //Getters
    public int getHandSize() { return hand.size(); }
    public int getScore() { return score; }
    public String getName() { return name; }
    public ArrayList<Card> getCollectedCards() {
        return collected;
    }
    public ArrayList<Card> getHand() {
        return new ArrayList<>(hand);
    }
    public int getRegularCardCount(){return regularCardCount;}
}