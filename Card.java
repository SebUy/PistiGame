public class Card {
    //comments written to help with report afterwards
    private String suit;
    private int rank;   // 1 to 13 (1 is ace, 11 is jack, 12 is queen and 13 is king)
    private int points;
    private boolean isPisti;


    public Card(String suit, int rank){
        this.suit = suit;
        this.rank = rank;
        this.points = calculatePoints();
        this.isPisti = false;
    }

    public String getSuit() {return suit;}
    public int getRank() {return rank;}
    public int getPoints() {return points;}

    public boolean isJack(){
        return rank == 11;  // if the rank is 11, it will return true
    }

    public boolean rankMatch(Card card2) {
        return this.rank == card2.rank;   // so we can compare the ranks of the card in hand with card in pile
    }

    public String getRankName() {  // to change the number rank to their names for 1, 11, 12 and 13
        switch(rank){ //originally done with ifelse but changed to switch for simplicity
            case 1:
                return "A";
            case 11:
                return "J";
            case 12:
                return "Q";
            case 13:
                return "K";
            default:
                return String.valueOf(rank);
        }
    }

    public String getSuitSymbol(){              // Unicode did not work in the vscode terminal so defaulted to initial letter
        switch (suit){
            case "Spades":
                return "S";
            case "Clubs":
                return "C";
            case "Hearts":
                return "H";
            case "Diamonds":
                return "D";
            default:
                return "";
        }
    }


    public int calculatePoints(){
        if(isPisti()){
            return 0;       //since pisti cards dont have values
        }
        if(suit.equals("Diamonds") && rank == 10) {return 3;}
        if(suit.equals("Clubs") && rank == 2) {return 2;}
        return 1;
    }

    public void markPisti(){
        this.isPisti = true;
    }

    public boolean isPisti(){
        return isPisti;
    }

    // to display the cards in the form of (Ace of Clubs, etc.)
    @Override 
    public String toString(){
        return getRankName() + getSuitSymbol();
    } 


}
