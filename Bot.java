import java.util.ArrayList;
import java.util.Random;

public class Bot extends Player{
    private Random random;

    public Bot(String name){
        super(name);
        this.random = new Random();
    }

    public Card chooseCard(Board board){            // Logic for bot to choose suitable card
        Card topCard = board.getTopCard();
        ArrayList<Card> playableCards = getPlayableCards(board, topCard);

        if(playableCards.isEmpty()){
            return playRandomCard();                // if there are no suitable cards
        } else{
            return playableCards.get(0);
        }
    }
    
    private ArrayList<Card> getPlayableCards(Board board, Card topCard){             // Creates a list of cards that match the top card in the Priority order: Pisti > Matching Rank > Jack > Random card
        ArrayList<Card> playableCards = new ArrayList<>();

        if(topCard != null && board.isPisti()){                         // Checks if top card is pisti then places card from hand at top of priority
            for(Card card : getHand()){
                if(card.getRank() == topCard.getRank()){
                    playableCards.add(card);
                    return playableCards;
                }
            }
        }

        if(topCard != null){                                           // looks for matching rank between top card and hand
            for(Card card: getHand()){
                if(card.getRank() == topCard.getRank()){
                    playableCards.add(card);
                }
            }
        }

        for(Card card : getHand()){                                          // looks for jack
            if (card.isJack()){
                playableCards.add(card);
            }
        }
        return playableCards;
    }
    
    private Card playRandomCard(){                                    // initiated when there are no valid plays
        ArrayList<Card> hand = getHand();
        if(hand.isEmpty()){
            throw new IllegalStateException("Bot has no cards to play");
        }
        return playCard(random.nextInt(hand.size()));
    }

}
