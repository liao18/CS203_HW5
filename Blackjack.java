import java.io.*;
import java.util.*;

/**
 * Blackjack
 *
 * This text-based game plays a simplified version of Blackjack with the user.
 *
 * @author Andrew Nuxoll
 * @author Jonathan Liao
 *
 * @version 2.16.2015
 */
public class Blackjack
{
    /** this constant defines how many cards are in the deck.  Always use this
     * constant in lieu of a literal 52. */
    public static final int NUM_CARDS = 52;

    /** This array will contain the names of each card in the deck */
    public String[] cardNames = new String[NUM_CARDS];

    /** This array specifies whether each card has been dealt or not */
    public boolean[] dealt = new boolean[NUM_CARDS];

    /** This array specifies the value of each card in the deck.  Arrays are
     * initialized to a value of '11' but that value may be treated as a '1'
     * during play. */
    public int[] cardValues = new int[NUM_CARDS];

    /**
     * initNames
     * this method initializes the card names
     */
    public void initNames()
    {
        String[] ranks = {"Ace", "King", "Queen", "Jack", "Ten",
                "Nine", "Eight", "Seven", "Six", "Five",
                "Four", "Trey", "Deuce"};
        String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};

        int index = 0;
        for(int i = 0; i < suits.length; ++i)
        {
            for(int j = 0; j < ranks.length; ++j)
            {
                cardNames[index] = ranks[j] + " of " + suits[i];
                index++;
            }
        }
    }//initNames

    /**
     * shuffle
     *
     * this method "shuffles" the deck by setting each entry in the dealt array
     * to 'false'
     */
    public void shuffle()
    {
        for(int i = 0; i < dealt.length; i++){
            dealt[i] = false;
        }
    }//shuffle

    /**
     * initValues
     * 
     * This method initializes the entries in the cardValues array.  A value of
     * '11' is assigned for the aces.  The getHandValue() method may later treat
     * this as a '1' to prevent the player from exceeding 21.
     */
    public void initValues()
    {
        int[] ranks = {11, 10, 10, 10, 10, 9, 8, 7, 6, 5, 4, 3, 2};
        

        int index = 0;
        for(int i = 0; i < 4; ++i) //four different suites, so we do this sequence of assigning 11, 10, 10, 10, 10, 9, 8, 7, 6, 5, 4, 3, 2 values FOUR times in the 52-element array
        {
            for(int j = 0; j < ranks.length; ++j)
            {
                cardValues[index] = ranks[j];
                index++;
            }
        }        
    }

    /**
     * cardsDealt
     *
     * This method returns the total number of cards in the deck that have been
     * dealt so far
     */
    private int cardsDealt()
    {
        int total = 0;
        for(int i = 0; i < dealt.length; ++i)
        {
            if (dealt[i])
            {
                total ++;
            }
        }

        return total;
    }//cardsDealt

    /**
     * drawCard
     * 
     * draws a card from the deck.  It selects randomly from cards that have not
     * been dealt yet.  The selcted card is marked as 'dealt' and its index is
     * returned to the caller.
     */
    private int drawCard()
    {
        while(true){ //infinite loop to keep drawing cards until an undrawn is selected
            int ran = (int)(Math.random() * (NUM_CARDS)); //Math.random produces numbers from [0,1). Because 1 is not included, we do not need to subtract one since there is no 52 element in the array

            if(dealt[ran] == false) {
                return ran;  
            }
            else { //if "ran" int produced an already drawn card from the pile array, then we draw a different card
                continue; 
            }
        }
    }//drawCard

    /**
     * getHandValue
     *
     * calculates the value of a player's current hand.  If the hand contains
     * any aces, the total calculated by this method may be adjusted to treat
     * the ace as having a value of 1 instead of 11.  This should be done only
     * if necessary and only the minimum necessary number of aces should get
     * this treatment.
     * 
     * Under no circumstances should the cardValues array be altered by this method.
     *
     * @param hand      an array of indexes indicating which cards are in the hand
     *
     * @return the hand's value
     */
    private int getHandValue(int[] hand)
    {
        int playersSum = 0;
        int aces = 0;
        for(int i = 0; i < hand.length; i++) {//calculate initial sum of cards
            playersSum += cardValues[hand[i]];
        }
        
        for(int i = 0; i < hand.length; i++) {//count how many aces the player has
            if (cardValues[hand[i]] == 11) {//if there is a card value that is 11, which is an ace
                aces++;
            }
        }
        
        //if players sum is greater than 21 and there is at least one ace in hand, we change the values of aces
        //so we avoid the player from busting.
        while(aces > 0) {
            if (playersSum > 21 && aces > 0) {
                playersSum -= 10;
            }
            aces--;
        }
        
        return playersSum; 

    }//getHandValue

    /**
     * announceWinner
     *
     * given arrays representing the player's hand and the dealer's hand, this
     * method announces the outcome to the player by printing a message to the
     * console.  The following outcomes are possible: player busts, dealer
     * busts, dealer wins, player wins and a push (tie).
     *
     * @param playersHand    an array of indexes indicating which cards are in
     *                       the player's hand
     * @param dealersHand    an array of indexes indicating which cards are in
     *                       the dealer's hand
     */
    private void announceWinner(int[] playersHand, int[] dealersHand)
    {
        int playersSum = getHandValue(playersHand);
        int dealersSum = getHandValue(dealersHand);

        if (playersSum > 21 && dealersSum > 21) 
            System.out.println("I don't know who won?!");
        else if(playersSum > 21) //player busts
            System.out.println("Player busts. You lose");
        else if(dealersSum > 21) //dealer busts
            System.out.println("Dealer busts. You win");
        else if(21-playersSum > 21-dealersSum ) //player is farther from 21 than computer
            System.out.println("Dealer wins. You lose");
        else if(21-dealersSum > 21-playersSum) //dealer is farther from 21 than player
            System.out.println("Dealer loses. You win");
        else //if something weird happened
            System.out.println("I don't know who won?!");
            
    }//announceWinner

    /**
     * mainLoop
     *
     * this method contains the main loop that plays Blackjack with the player
     */
    public void mainLoop()
    {
        Scanner kbd = new Scanner(System.in); //for getting user input

        //Invite the player to play a hand
        System.out.println("Would you like to play a hand of Blackjack?");
        String answer = kbd.nextLine();
        answer = answer.trim().toLowerCase();
        //Get the user's answer to (would you like to play?)
        if ( (answer.length() == 0) || (answer.charAt(0) != 'y') )
        {
            return;
        }
        boolean quit = false; //the user isn't ready to quit yet

        while(!quit)
        {
            //Shuffle the deck if needed
            if (cardsDealt() > NUM_CARDS / 2)
            {
                shuffle();
            }

            //initialize the hands
            int[] playerHand = new int[13]; //hand will never have more than 13 cards
            playerHand[0] = drawCard();
            playerHand[1] = drawCard();
            int playerNumCards = 2;
            int playerTotal = getHandValue(Arrays.copyOf(playerHand, playerNumCards));
            int[] dealerHand = new int[13];
            dealerHand[0] = drawCard();
            int dealerNumCards = 1;
            int dealerTotal = getHandValue(Arrays.copyOf(dealerHand, dealerNumCards));

            //Tell the user about the hands
            System.out.println("Your hand contains these cards:");
            System.out.println("\t" + cardNames[playerHand[0]]);
            System.out.println("\t" + cardNames[playerHand[1]]);
            System.out.println("for a total value of: " 
                + getHandValue(Arrays.copyOf(playerHand, playerNumCards)) + ".");
            System.out.println("The dealer has drawn the " +
                cardNames[dealerHand[0]]);

            //Allow the player to add cards to his hand
            System.out.println("Would you like a hit?");
            answer = kbd.nextLine();
            answer = answer.trim().toLowerCase();
            while ( (answer.length() > 0) && (answer.charAt(0) == 'y') )
            {
                //Add a new card to player's hand
                int newCard = drawCard();
                playerHand[playerNumCards] = newCard;
                playerNumCards++;
                System.out.println("You have drawn the " + cardNames[newCard]);
                playerTotal = getHandValue(Arrays.copyOf(playerHand, playerNumCards));
                System.out.println("Your hand's value is now " + playerTotal);

                //Did player bust?
                if (playerTotal > 21)
                {
                    break;
                }

                //Another hit?
                System.out.println("Would you like another hit?");
                answer = kbd.nextLine();
                answer = answer.trim().toLowerCase();
            }//while

            //Dealer only plays if player didn't bust
            if (playerTotal <= 21)
            {
                //Dealer must draw a second card
                dealerHand[1] = drawCard();
                dealerNumCards = 2;
                dealerTotal = getHandValue(Arrays.copyOf(dealerHand, dealerNumCards));
                System.out.println("The dealer draws the " + cardNames[dealerHand[1]]);
                System.out.println("Dealer's current total is: " + dealerTotal);

                //Dealer draws up to 17
                while(dealerTotal < 17)
                {
                    int newCard = drawCard();
                    dealerHand[dealerNumCards] = newCard;
                    dealerNumCards++;
                    dealerTotal = getHandValue(Arrays.copyOf(dealerHand, dealerNumCards));
                    System.out.println("The dealer draws the " + cardNames[newCard]);
                    System.out.println("Dealer's current total is: " + dealerTotal);
                }
            }//if

            //Announce the outcome
            announceWinner(Arrays.copyOf(playerHand, playerNumCards),
                Arrays.copyOf(dealerHand, dealerNumCards));

            //Play again?
            System.out.println("Would you like to play again?");
            answer = kbd.nextLine();
            answer = answer.trim().toLowerCase();
            if ( (answer.length() == 0) || (answer.charAt(0) != 'y') )
            {
                quit = true;
            }

        }//while
    }//mainLoop

    /** kicks off the game */
    public static void main(String[] args)
    {
        Blackjack myGame = new Blackjack();
        myGame.initNames();
        myGame.shuffle();
        myGame.initValues();
        myGame.mainLoop();
    }//main

}//class Blackjack
