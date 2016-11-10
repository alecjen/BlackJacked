package com.test;

import java.util.Arrays;
import java.util.ArrayList;

/**
 * Created by ajen1 on 11/4/16.
 */
public class Better extends Player {

    private int chipCount;
    private int coldBet;
    private int betAmount;

    public Better( String name, int chipCount, int minBet ) {
        super( name, false );
        this.handTotal = 0;
        this.chipCount = chipCount;
        this.coldBet = Math.max( minBet, this.chipCount / 10 );
        this.betAmount = coldBet;
        this.hands.add( new Hand( new ArrayList(), this, this.betAmount ) );
    }
/*
    public int getBetAmount() {
        return this.betAmount;
    }
*/
    public int getChipCount() { return this.chipCount; }

    public void winChips( int numChips ) {
        this.chipCount += numChips;
    }

    public void loseChips( int numChips ) {
        this.chipCount -= numChips;
    }
/*
    public boolean shouldHit( int dealerCardValue ) {
        if( this.doublingDown ) {
            this.currentHand++;
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
            this.currentHand++;
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
            this.currentHand++;
        }
        return willHit;
    }

    private void shouldDoubleDown( int dealerCardValue ) {
        if( this.hand.size() != 2 ) {
            return;
        }
        if ( this.chipCount < 2 * this.betAmount ) {
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
*/
    public void split( Shoe shoe ) {
        Hand handToSplit = this.hands.get( this.currentHand );
        Card firstCard = handToSplit.getCards().get(0);
        Card secondCard = handToSplit.getCards().get(1);
        ArrayList<Card> handOne = new ArrayList();
        handOne.add(firstCard);
        ArrayList<Card> handTwo = new ArrayList();
        handTwo.add(secondCard);

        handToSplit.setCards(handOne);
        shoe.deal( 1, handToSplit );
        System.out.print( this.name + "\'s first split: ");
        handToSplit.printHand(true);
        this.hands.add( new Hand( handTwo, this, handToSplit.getBetAmount() ) );
    }

    public void getNextHand() { this.currentHand++; }

    public boolean hasBettingHands() { return this.currentHand < this.hands.size(); }

    @Override
    public void endHand() {
        super.endHand();
        this.betAmount = coldBet;
        this.hands.add( new Hand( new ArrayList(), this, this.betAmount ) );
    }


}
