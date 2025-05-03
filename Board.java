import java.util.ArrayList;


public class Board {
    private ArrayList<Card> pile = new ArrayList<>();

    public void placeCard(Card card){
        pile.add(card);
    }
    
    public boolean willCapture(Card cardToPlay) {
        if (pile.isEmpty()) return false;
        
        Card currentTop = getTopCard();
        return cardToPlay.isJack() || cardToPlay.getRank() == currentTop.getRank();
    }

    public ArrayList<Card> captureCard(Card playedCard){
        if (pile.isEmpty()) return null;
        
        ArrayList<Card> captured = new ArrayList<>(pile);
        pile.clear();
        
        // Check for Pisti (must be exactly 1 card before playing)
        if (captured.size() == 1 && playedCard.getRank() == captured.get(0).getRank()) {
            captured.get(0).markPisti(); // Mark the CAPTURED card as Pisti
        }
        
        return captured;
    }

    public boolean isPisti(){
        return pile.size() == 1;
    }

    public Card getTopCard(){       // for use in bot
        if(pile.isEmpty()){
            return null;
        }
        else{
            return pile.get(pile.size() - 1);
        }
    }

    public ArrayList<Card> getPile(){
        return pile;
    }
}
