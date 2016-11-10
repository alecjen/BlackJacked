package com.test;

import java.util.ArrayList;

/**
 * Created by ajen1 on 11/4/16.
 */
public class Game {

    private Shoe shoe;
    private ArrayList<Better> players;
    private Dealer dealer;
    private int minBet;

    public Game( int numDecks, ArrayList<Better> players, int minBet ) {
        this.shoe = new Shoe(numDecks);
        this.players = players;
        this.minBet = minBet;
        this.dealer = new Dealer( "Dealer Joe" );
    }

    public void dealHands() {
        for( int i = 0; i < this.players.size(); i++ ) {
            this.shoe.deal( 2, this.players.get(i).getCurrentHand() );
            System.out.print( this.players.get(i).getName() + "\'s HAND: ");
            this.players.get(i).getCurrentHand().printHand(true);
        }
        this.shoe.deal( 2, this.dealer.getCurrentHand() );
        System.out.print( this.dealer.getName() + " SHOWS: ");
        this.dealer.getCurrentHand().printHand(false);
    }

    public void playHand() {
        boolean anyValidHands = false;

        this.dealHands();
        if( this.dealer.getCurrentHand().isBlackJack() ) {
            this.handleDealerBJ();
            return;
        }

        for( int i = 0; i < this.players.size(); i++ ) {
            Better better = this.players.get(i);
            //for( int j = 0; j < better.getHands().size(); j++ ) {
            int j = 0;
            while( j < better.getHands().size() ) {
                Hand currentHand = better.getCurrentHand();
                if( currentHand.getCards().size() == 1 ) {
                    this.shoe.deal(1, better.getCurrentHand() );
                    System.out.print( better.getName() + "\'s second split: ");
                    currentHand.printHand(true);
                }
                while( currentHand.shouldHit( this.dealer.getCurrentHand().getShowingCardValue(), this.shoe ) ) {
                    Card card = this.shoe.deal( 1, currentHand ).get(0);
                    System.out.print( better.getName() + " HITS " + card.getCardFace() + ": ");
                    currentHand.printHand(true);
                }
                if( currentHand.didBust() ) {
                    System.out.println( better.getName() + " BUSTS." );
                } else if( !currentHand.isBlackJack() ) {
                    anyValidHands = true;
                    System.out.println( better.getName() + " STAYS." );
                }
                j++;
            }
        }

        System.out.print( this.dealer.getName() + " HAS: " );
        this.dealer.getCurrentHand().printHand(true);
        if( anyValidHands ) {
            while( this.dealer.getCurrentHand().shouldHit( this.dealer.getCurrentHand().getShowingCardValue(), this.shoe ) ) {
                Card card = this.shoe.deal( 1, this.dealer.getCurrentHand() ).get(0);
                System.out.print( this.dealer.getName() + " HITS " + card.getCardFace() + ": " );
                this.dealer.getCurrentHand().printHand(true);
            }
            if( this.dealer.getCurrentHand().didBust() ) {
                System.out.println( this.dealer.getName() + " BUSTS!" );
            }
        }

        for( int i = 0; i < this.players.size(); i++ ) {
            Better better = players.get(i);
            int winnings = 0;
            for( int j = 0; j < better.getHands().size(); j++ ) {
                Hand hand = better.getHands().get(j);
                if( hand.isBlackJack() ) {
                    winnings += hand.getBetAmount() * 3 / 2;
                    System.out.println( "BLACKJACK!! WON " + ( 3 * hand.getBetAmount() ) / 2 + " CHIPS." );
                } else if( this.handResults(hand) == 1 ) {
                    winnings += hand.getBetAmount();
                    System.out.println( "WON " + hand.getBetAmount() + " CHIPS." );
                } else if( this.handResults(hand) == 0 ) {
                    System.out.println( "PUSH." );
                } else {
                    winnings -= hand.getBetAmount();
                    System.out.println( "LOST " + hand.getBetAmount() + " CHIPS." );
                }
            }
            if( winnings > 0 ) {
                better.winChips(winnings);
                System.out.println( better.getName() + " wins " + winnings + " chips. Total chip count is: " + better.getChipCount() );
            } else if( winnings == 0 ) {
                System.out.println( better.getName() + " pushes. Total chip count is: " + better.getChipCount() );
            } else {
                better.loseChips( Math.abs(winnings) );
                System.out.println( better.getName() + " loses " + Math.abs(winnings) + " chips. Total chip count is: " + better.getChipCount() );
            }
            better.endHand();
        }
        this.dealer.endHand();
    }

    public void handleDealerBJ() {
        System.out.println( this.dealer.getName() + " HAS BLACKJACK :(" );
        for( int i = 0; i < this.players.size(); i++ ) {
            Better better = this.players.get(i);
            if( better.getCurrentHand().isBlackJack() ) {
                System.out.println( "PUSH. TOTAL CHIP COUNT IS : " + better.getChipCount() );
            } else {
                better.loseChips( better.getCurrentHand().getBetAmount() );
                System.out.println( "LOST " + better.getCurrentHand().getBetAmount() + " CHIPS. TOTAL CHIP COUNT IS : " + better.getChipCount() );
            }
            better.endHand();
        }
        this.dealer.endHand();
    }

    public int betterResults( Better better ) {
        if( better.getCurrentHand().didBust() ) {
            return -1;
        }
        if( this.dealer.getCurrentHand().didBust() ) {
            return 1;
        }
        if( better.getCurrentHand().getHandTotal() == this.dealer.getCurrentHand().getHandTotal() ) {
            return 0;
        }
        return better.getCurrentHand().getHandTotal() > this.dealer.getCurrentHand().getHandTotal() ? 1 : -1;
    }

    public int handResults( Hand hand ) {
        if( hand.didBust() ) {
            return -1;
        }
        if( this.dealer.getCurrentHand().didBust() ) {
            return 1;
        }
        if( hand.getHandTotal() == this.dealer.getCurrentHand().getHandTotal() ) {
            return 0;
        }
        return hand.getHandTotal() > this.dealer.getCurrentHand().getHandTotal() ? 1 : -1;
    }

    public boolean canPlay() {
        if( !this.shoe.hasEnoughCards( this.players.size() + 1 ) ) {
            return false;
        }
        int i = 0;
        while( i < this.players.size() ) {
            if( this.players.get(i).getChipCount() < this.minBet ) {
                this.players.remove(i);
            } else {
                i++;
            }
        }
        return this.players.size() > 0;
    }

    public static void main( String [] args ) {
        ArrayList<Integer> results = new ArrayList();
        int minBet = 10;
        int buyin = 100;

        for( int i = 0; i < 10; i++ ) {
            ArrayList<Better> players = new ArrayList();
            players.add( new Better( "Alec", buyin, minBet ) );
            Game game = new Game( 3, players, minBet );

            while (game.canPlay()) {
                game.playHand();
            }
            if ( players.size() == 0 ) {
                results.add(0);
            } else {
                results.add( players.get(0).getChipCount() );
            }
            System.out.println("-------- END OF GAME ----------");
        }
        int average = 0;
        for( int i = 0; i < results.size(); i++ ) {
            average += results.get(i);
        }
        average /= results.size();
        System.out.println( "Average Chip Count: " + average );
        double gain = 100 * ( ( (double)average - (double)buyin ) / (double)buyin );
        System.out.println( "Percent Gain: " + gain + "%" );
        // WORK ON SPLITS (may have to create a separate 'Hand' class)
    }
}
