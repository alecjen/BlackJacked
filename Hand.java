// TODO: splitting Aces only grants 1 hit per split
// Blackjack on a split pays 1.0x, not 1.5x

package com.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
/**
 * Created by ajen1 on 11/9/16.
 */
public class Hand {
    private ArrayList<Card> cards;
    private boolean softHand;
    private int handTotal;
    private boolean doublingDown;
    private Better better;
    private int betAmount;
    private boolean shouldSplit;
    private boolean isDealer;

    public Hand( ArrayList<Card> cards ) {
        this.cards = cards;
        this.isDealer = true;
        this.softHand = false;
        this.handTotal = 0;
    }

    public Hand( ArrayList<Card> cards, Better better, int betAmount ) {
        this.cards = cards;
        this.softHand = false;
        this.handTotal = 0;
        this.doublingDown = false;
        this.better = better;
        this.betAmount = betAmount;
        this.shouldSplit = false;
        this.isDealer = false;
    }

    public int getBetAmount() { return this.betAmount; }

    public void setCards( ArrayList<Card> cards ) {
        this.cards = cards;
    }

    public int getShowingCardValue() {
        return this.cards.get(0).getCardValue();
    }

    public ArrayList<Card> getCards() {
        return this.cards;
    }

    public String printHand( boolean shouldReveal ) {
        String out = "";
        if( shouldReveal ) {
            out += this.handTotal + " |";
            System.out.print( this.handTotal + " |" );
            for (int i = 0; i < this.cards.size(); i++) {
                out += " " + this.cards.get(i).getCardFace();
                System.out.print( " " + this.cards.get(i).getCardFace() );
            }
            System.out.println("");
        } else {
            out = this.cards.get(0).getCardFace();
            System.out.println( this.cards.get(0).getCardFace() );
        }
        return out;
    }

    public int calculateBestHand() {
        ArrayList<Integer> possibleTotals = new ArrayList<>();
        Stack<Card> cardStack = new Stack();
        for(int i = 0; i < this.cards.size(); i++) {
            cardStack.push( this.cards.get(i) );
        }
        calculateHand( cardStack, 0, possibleTotals );
        int bestTotal = -1;
        int numValidHands = 0;
        for(int i = 0; i < possibleTotals.size(); i++) {
            if( possibleTotals.get(i) > 21 )
                continue;
            numValidHands++;
            bestTotal = Math.max( bestTotal, possibleTotals.get(i) );
        }
        if( numValidHands > 1 ) {
            this.softHand = true;
        } else {
            this.softHand = false;
        }
        bestTotal = bestTotal == -1 ? 22 : bestTotal;

        this.handTotal = bestTotal;
        return bestTotal;
    }

    private void calculateHand( Stack<Card> hand, int cur, ArrayList<Integer> possibleTotals ) {
        if( hand.isEmpty() ) {
            possibleTotals.add(cur);
            return;
        }
        Card curCard = hand.pop();
        if( curCard.getCardFace() == "A" ) {
            Stack<Card> cl = (Stack<Card>)hand.clone();
            calculateHand( hand, cur + 1, possibleTotals );
            calculateHand( cl, cur + 11, possibleTotals );
        }
        else {
            calculateHand( hand, cur + curCard.getCardValue(), possibleTotals );
        }
    }

    public boolean isBlackJack() {
        this.calculateBestHand();
        if( this.cards.size() == 2 && this.handTotal == 21 ) {
            return true;
        }
        return false;
    }

    public int getHandTotal() { return this.handTotal; }

    public boolean hit( Card hitCard ) {
        this.cards.add(hitCard);
        this.calculateBestHand();
        return !this.didBust();
    }

    public boolean didBust() {
        return this.handTotal > 21;
    }

    public boolean shouldHit( int dealerCardValue, Shoe shoe ) {
        if( this.isDealer ) {
            return this.dealerShouldHit(dealerCardValue);
        }

        this.shouldSplitHand(dealerCardValue);
        if( this.shouldSplit ) {
            System.out.println( "SPLITTING!" );
            this.better.split(shoe);
            this.shouldSplit = false;
        }

        if( this.doublingDown ) {
            this.better.getNextHand();
            return false;
        }
        this.shouldDoubleDown(dealerCardValue);
        if( this.doublingDown ) {
            return true;
        }

        if( this.softHand ) {
            if( this.handTotal == 13 && this.handTotal <= 17 ) {
                return true;
            }
            if( this.handTotal == 18 && dealerCardValue >= 9 ) {
                return true;
            }
        }
        if( this.handTotal > 16 ) {
            this.better.getNextHand();
            return false;
        }

        boolean hitChart[][] = new boolean[15][10];
        int playerOffset = -2;
        int dealerOffset = -2;
        for( int i = 2; i <= 11; i++ ) {
            Arrays.fill( hitChart[i+playerOffset], true );
        }
        hitChart[12+playerOffset][2+dealerOffset] = true;
        hitChart[12+playerOffset][3+dealerOffset] = true;
        for( int i = 12; i <= 16; i++ ) {
            for( int j = 7; j <= 11; j++ ) {
                hitChart[i+playerOffset][j+dealerOffset] = true;
            }
        }

        boolean willHit = hitChart[ this.handTotal+playerOffset ][ dealerCardValue+dealerOffset ];
        if( !willHit ) {
            this.better.getNextHand();
        }
        return willHit;
    }

    private boolean shouldSplitHand( int dealerCardValue ) {
        if( this.cards.size() != 2 ) {
            return false;
        }
        if( this.cards.get(0).getCardFace() != this.cards.get(1).getCardFace() ) {
            return false;
        }
        Card splitCard = this.cards.get(0);
        int playerOffset = -2;
        int dealerOffset = -2;
        boolean splitChart[][] = new boolean[10][10];
        Arrays.fill( splitChart[8+playerOffset], true );
        Arrays.fill( splitChart[11+playerOffset], true );
        for( int i = 0; i < 10; i++ ) {
            for( int j = 0; j < 10; j++ ) {
                if( i == ( 2 + playerOffset ) || i == ( 3 + playerOffset ) ) {
                    if( j >= (4 + dealerOffset ) && j <= 7 + dealerOffset ) {
                        splitChart[i][j] = true;
                    }
                }
                if( i == ( 6 + dealerOffset ) ) {
                    if( j >= ( 3 + dealerOffset ) && j <= ( 6 + dealerOffset ) ) {
                        splitChart[i][j] = true;
                    }
                }
                if( i == ( 7 + playerOffset ) ) {
                    if( j <= ( 7 + dealerOffset ) ) {
                        splitChart[i][j] = true;
                    }
                }
                if( i == ( 9 + playerOffset ) ) {
                    if( j <= ( 9 + dealerOffset ) && j != ( 7 + dealerOffset ) ) {
                        splitChart[i][j] = true;
                    }
                }
            }
        }
        this.shouldSplit = splitChart[splitCard.getCardValue()+playerOffset][dealerCardValue+dealerOffset];
        return this.shouldSplit;
    }

    private void shouldDoubleDown( int dealerCardValue ) {
        if( this.cards.size() != 2 ) {
            return;
        }
        if ( this.better.getChipCount() < 2 * this.betAmount ) {
            return;
        }

        if( this.softHand ) {
            if( this.handTotal >= 13 ) {
                if( this.handTotal <= 19 && dealerCardValue == 6 ) {
                    this.doublingDown = true;
                } else if( this.handTotal <= 18 && dealerCardValue == 5 ) {
                    this.doublingDown = true;
                }
            }
            if( this.handTotal >= 15 && this.handTotal <= 18 && dealerCardValue == 4 ) {
                this.doublingDown = true;
            }
            if( this.handTotal == 17 || this.handTotal == 18 && dealerCardValue == 3 ) {
                this.doublingDown = true;
            }
            if( this.handTotal == 18 && dealerCardValue == 2 ) {
                this.doublingDown = true;
            }
        }
        else {
            if (this.handTotal == 11) {
                if (dealerCardValue < 11) {
                    this.doublingDown = true;
                }
            } else if (this.handTotal == 10) {
                if (dealerCardValue < 10) {
                    this.doublingDown = true;
                }
            } else if (this.handTotal == 9) {
                if (dealerCardValue >= 3 && dealerCardValue <= 7) {
                    this.doublingDown = true;
                }
            }
        }
        if ( this.doublingDown ) {
            this.betAmount *= 2;
            System.out.println("DOUBLING DOWN");
        }
    }

    private boolean dealerShouldHit( int dealerCardValue ) {
        if( this.handTotal <= 16 ) {
            return true;
        }
        if( this.handTotal == 17 && this.softHand ) {
            return true;
        }
        return false;
    }

    public static void main( String [] args ) {
        Better better = new Better( "Alec", 100, 10 );
        ArrayList<Card> cards = new ArrayList();
        cards.add( new Card( 2, Suit.CLUBS ) );
        cards.add( new Card( 2, Suit.CLUBS  ) );
        Hand hand = new Hand( cards, better, 10 );
        System.out.println( hand.shouldSplitHand(4) );
    }
}
