import java.util.ArrayList;
import java.util.Scanner;

public class Game {                             // majority of methods are private since they do not need to be referenced in other classes
    private Player player;
    private Bot bot;
    private Deck deck;
    private Board board;
    private boolean isPlayerTurn;

    public Game(){
        this.deck = new Deck();
        this.board = new Board();
        this.player = new Player("Player");
        this.bot = new Bot("Computer");
        this.isPlayerTurn = true; // Player starts first
    }

    // logic for the gameplay itself
    public void start(){
        dealInitialCards();
        while(! isGameOver()){
            playTurn();
        }
        endGame();
    }

    private void dealInitialCards(){                    //deals 4 cards to player, bot and board 1by1 in that order
        for(int i = 0; i < 4; i++){
            player.addToHand(deck.drawCard());
            bot.addToHand(deck.drawCard());
            board.placeCard(deck.drawCard());
        }
    }

    private void playTurn(){
        if(isPlayerTurn){
            playerTurn();
        } else {
            botTurn();
        }
        isPlayerTurn = !isPlayerTurn;
    }

    // logic governing turns

    private void playerTurn(){
        System.out.println("\n=== Your Turn ===");
        displayGame();
        int cardIndex = getPlayerCardChoice();
        Card playedCard = player.playCard(cardIndex);
        processMove(playedCard, player);
    }

    private void botTurn(){
        System.out.println("\n=== Bot's Turn ===");
        Card botCard = bot.chooseCard(board);
        System.out.println("Bot plays: " + botCard);
        processMove(botCard, bot);
    }

    private void processMove(Card playedCard, Player currentPlayer) {
        boolean willCapture = board.willCapture(playedCard);            //check whether the card is capturable
        board.placeCard(playedCard);

        if (willCapture) {
            ArrayList<Card> captured = board.captureCard(playedCard);
            currentPlayer.collectCards(captured);
            
            if (captured.stream().anyMatch(Card::isPisti)) {
                System.out.println("PISTI! +10 points");
            }
        }
        
        // Deal new cards if needed
        if (player.getHand().isEmpty() && bot.getHand().isEmpty() && !deck.isEmpty()) {
            dealNewCards();
        }
    }

    // Methods used in the other methods
    private void dealNewCards() {
        for (int i = 0; i < 4 && !deck.isEmpty(); i++) {
            player.addToHand(deck.drawCard());
            bot.addToHand(deck.drawCard());
        }
    }

    private boolean isGameOver(){
        return deck.isEmpty() && player.getHand().isEmpty() && bot.getHand().isEmpty();
    }

    private void displayGame(){
        System.out.print("Board: ");
        if (board.getPile().isEmpty()) {
            System.out.println("[Empty]");
        } else {
            for (Card card : board.getPile()) {
                System.out.print(card + "  ");
            }
            System.out.println();
        }
    
        // Display player's hand
        player.displayHand();
    }

    private int getPlayerCardChoice(){
        int handSize = player.getHand().size();
        Scanner sc = new Scanner(System.in);
        
        System.out.printf("Enter card index to play (0 - %d): ", handSize - 1);

        while(true){
            try {
                int choice = sc.nextInt();
                if (choice >= 0 && choice < handSize) {
                    return choice;
                } else {
                    System.out.printf("Invalid index! Please enter 0-%d: ", handSize - 1);
                }
            } catch (Exception e) {
                System.out.printf("Invalid input! Please enter a number between 0-%d: ", handSize - 1);
                sc.next();
            }
        }
    }


    private void endGame(){
        if(!board.getPile().isEmpty()) {
            Player lastCapturer = isPlayerTurn ? bot : player; // Give remaining cards to last person who captured
            lastCapturer.collectCards(board.getPile());
            board.getPile().clear();
            System.out.println(lastCapturer.getName() + " collects remaining cards from board");
        }

        player.calculateScore();
        bot.calculateScore();

            // Add 3-point bonus for player with most regular cards
        if (player.getRegularCardCount() > bot.getRegularCardCount()) {
            player.addToScore(3);
        } else if (bot.getRegularCardCount() > player.getRegularCardCount()) {
            bot.addToScore(3);
        }

        System.out.println("\n === GAME OVER ===");
        System.out.println("Your score: " + player.getScore());
        System.out.println("Computer score: " + bot.getScore());

        if(player.getScore() > bot.getScore()){
            System.out.println("You Win!");
        } else if(bot.getScore() > player.getScore()){
            System.out.println("Computer wins!");
        } else{
            System.out.println("It's a tie!");
        }
    }
}
