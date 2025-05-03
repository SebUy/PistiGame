import java.util.*;

public class PistiGame {
    private static final Map<String, Integer> CARD_VALUES = new HashMap<>();
    static {
        CARD_VALUES.put("D10", 3);  // ♦10 becomes D10
        CARD_VALUES.put("C2", 2);   // ♣2 becomes C2
    }


    private List<String> deck;
    private List<String> board;
    private List<String> playerHand;
    private List<String> computerHand;
    private List<String> playerCollected;
    private List<String> computerCollected;
    private int playerPointCount;
    private int computerPointCount;
    private boolean isPlayerTurn;
    private Scanner scanner;

    public PistiGame() {
        initializeDeck();
        scanner = new Scanner(System.in);
        playerCollected = new ArrayList<>();
        computerCollected = new ArrayList<>();
        playerPointCount = 0;
        computerPointCount = 0;
    }

    private void initializeDeck() {
        String[] suits = {"S", "C", "H", "D"}; // Spades, Clubs, Hearts, Diamonds

        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        
        deck = new ArrayList<>();
        for (String suit : suits) {
            for (String rank : ranks) {
                deck.add(suit + rank);
            }
        }
    }

    public void startGame() {
        System.out.println("Welcome to Pisti Card Game!");
        System.out.println("===========================");
        
        shuffleDeck();
        cutDeck();
        dealInitialCards();
        
        isPlayerTurn = true; // Player starts first
        
        while (!deck.isEmpty() || !playerHand.isEmpty() || !computerHand.isEmpty()) {
            if (playerHand.isEmpty() && computerHand.isEmpty() && !deck.isEmpty()) {
                dealCards();
            }
            
            if (isPlayerTurn) {
                if (!playerHand.isEmpty()) {
                    playerTurn();
                }
            } else {
                if (!computerHand.isEmpty()) {
                    computerTurn();
                }
            }
            
            isPlayerTurn = !isPlayerTurn;
        }
        
        // Collect remaining cards on board
        if (!board.isEmpty()) {
            if (isPlayerTurn) {
                playerCollected.addAll(board);
            } else {
                computerCollected.addAll(board);
            }
            board.clear();
        }
        
        endGame();
    }

    private void shuffleDeck() {
        Collections.shuffle(deck);
        System.out.println("Deck has been shuffled.");
    }

    private void cutDeck() {
        Random random = new Random();
        int cutPoint = random.nextInt(deck.size() - 10) + 5; // Cut between 5 and size-5
        
        List<String> top = deck.subList(0, cutPoint);
        List<String> bottom = deck.subList(cutPoint, deck.size());
        
        List<String> newDeck = new ArrayList<>(bottom);
        newDeck.addAll(top);
        deck = newDeck;
        
        System.out.println("Deck has been cut at position " + cutPoint);
    }

    private void dealInitialCards() {
        // Place 4 cards on board
        board = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            board.add(deck.remove(0));
        }
        
        // Deal 4 cards to each player
        playerHand = new ArrayList<>();
        computerHand = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            playerHand.add(deck.remove(0));
            computerHand.add(deck.remove(0));
        }
        
        System.out.println("Initial cards have been dealt.");
        printGameState();
    }

    private void dealCards() {
        System.out.println("\nDealing new cards...");
        for (int i = 0; i < 4; i++) {
            if (!deck.isEmpty()) {
                playerHand.add(deck.remove(0));
            }
            if (!deck.isEmpty()) {
                computerHand.add(deck.remove(0));
            }
        }
        printGameState();
    }

    private void playerTurn() {
        System.out.println("\nYour turn!");
        System.out.println("Your hand: " + String.join(" ", playerHand));
        System.out.println("Board: " + formatBoard());
        
        int choice = -1;
        while (choice < 0 || choice >= playerHand.size()) {
            System.out.print("Choose a card to play (0-" + (playerHand.size() - 1) + "): ");
            try {
                choice = scanner.nextInt();
                if (choice < 0 || choice >= playerHand.size()) {
                    System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number.");
                scanner.next(); // Clear the invalid input
            }
        }
        
        String playedCard = playerHand.remove(choice);
        System.out.println("You played: " + playedCard);
        
        handleCardPlayed(playedCard, true);
    }

    private void computerTurn() {
        System.out.println("\nComputer's turn...");
        
        // Computer strategy:
        // 1. Look for matching card to top of board
        // 2. Look for Jack
        // 3. Play random card
        
        String cardToPlay = null;
        
        if (!board.isEmpty()) {
            String topCard = board.get(board.size() - 1);
            String topRank = topCard.substring(1); // Get rank part
            
            // Check for matching rank
            for (String card : computerHand) {
                if (card.substring(1).equals(topRank)) {
                    cardToPlay = card;
                    break;
                }
            }
            
            // Check for Jack if no match found
            if (cardToPlay == null) {
                for (String card : computerHand) {
                    if (card.substring(1).equals("J")) {
                        cardToPlay = card;
                        break;
                    }
                }
            }
        }
        
        // If no special card found, play random
        if (cardToPlay == null) {
            Random random = new Random();
            cardToPlay = computerHand.remove(random.nextInt(computerHand.size()));
        } else {
            computerHand.remove(cardToPlay);
        }
        
        System.out.println("Computer played: " + cardToPlay);
        handleCardPlayed(cardToPlay, false);
    }

    private void handleCardPlayed(String playedCard, boolean isPlayer) {
        board.add(playedCard);
        
        if (board.size() == 1) {
            // Single card on board - potential pigit
            String playedRank = playedCard.substring(1);
            if ((isPlayer && computerHand.stream().anyMatch(c -> c.substring(1).equals(playedRank))) ||
                (!isPlayer && playerHand.stream().anyMatch(c -> c.substring(1).equals(playedRank)))) {
                // Other player could have made pigit
                System.out.println("Warning: Other player could have made pisti with this play!");
            }
            return;
        }
        
        String topCard = board.get(board.size() - 2);
        String topRank = topCard.substring(1);
        String playedRank = playedCard.substring(1);
        
        // Check for pigit (single card on board before play)
        if (board.size() == 2 && playedRank.equals(topRank)) {
            System.out.println("Pisti!");
            if (isPlayer) {
                playerPointCount++;
            } else {
                computerPointCount++;
            }
            board.clear();
            return;
        }
        
        // Check for Jack
        if (playedRank.equals("J")) {
            collectCards(isPlayer);
            return;
        }
        
        // Check for matching rank
        if (playedRank.equals(topRank)) {
            collectCards(isPlayer);
        }
    }

    private void collectCards(boolean isPlayer) {
        if (isPlayer) {
            playerCollected.addAll(board);
            System.out.println("You collected " + board.size() + " cards!");
        } else {
            computerCollected.addAll(board);
            System.out.println("Computer collected " + board.size() + " cards!");
        }
        board.clear();
    }

    private void endGame() {
        System.out.println("\nGame Over!");
        System.out.println("==========");
        
        // Calculate scores
        int playerScore = calculateScore(playerCollected) + (playerPointCount * 10);
        int computerScore = calculateScore(computerCollected) + (computerPointCount * 10);
        
        // Additional 3 points for more cards
        if (playerCollected.size() > computerCollected.size()) {
            playerScore += 3;
        } else if (computerCollected.size() > playerCollected.size()) {
            computerScore += 3;
        }
        
        System.out.println("Your cards: " + playerCollected.size() + " (" + String.join(" ", playerCollected) + ")");
        System.out.println("Computer's cards: " + computerCollected.size() + " (" + String.join(" ", computerCollected) + ")");
        System.out.println("Your points: " + playerPointCount);
        System.out.println("Computer's pigits: " + computerPointCount);
        System.out.println("Your score: " + playerScore);
        System.out.println("Computer's score: " + computerScore);
        
        if (playerScore > computerScore) {
            System.out.println("You win!");
        } else if (computerScore > playerScore) {
            System.out.println("Computer wins!");
        } else {
            System.out.println("It's a tie!");
        }
    }

    private int calculateScore(List<String> cards) {
        int score = 0;
        for (String card : cards) {
            score += CARD_VALUES.getOrDefault(card, 1);
        }
        return score;
    }

    private String formatBoard() {
        if (board.isEmpty()) return "[Empty]";
        return String.join(" ", board);
    }

    private void printGameState() {
        System.out.println("\nCurrent Game State:");
        System.out.println("Cards remaining in deck: " + deck.size());
        System.out.println("Your hand: " + String.join(" ", playerHand));
        System.out.println("Board: " + formatBoard());
        // Computer hand is hidden
    }

    public static void main(String[] args) {
        PistiGame game = new PistiGame();
        game.startGame();
    }
}