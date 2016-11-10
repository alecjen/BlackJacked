package com.test;

import java.util.ArrayList;

/**
 * Created by ajen1 on 11/7/16.
 */
public class Dealer extends Player {


    public Dealer( String name ) {
        super( name, true );
        this.hands.add( new Hand( new ArrayList() ) );
    }
/*
    public int getShowingCardValue() {
        return this.hand.get(0).getCardValue();
    }

    public boolean shouldHit( int dealerCardValue ) {
        if( this.handTotal <= 16 ) {
            return true;
        }
        if( this.handTotal == 17 && this.softHand ) {
            return true;
        }
        return false;
    }
*/
    @Override
    public void endHand() {
        super.endHand();
        this.hands.add( new Hand( new ArrayList() ) );
    }
}
