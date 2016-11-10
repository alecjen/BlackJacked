package com.test;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Stack;

/**
 * Created by ajen1 on 11/4/16.
 */
public abstract class Player {
    protected String name;
    protected boolean isDealer;
    protected int handTotal;
    protected int numAces;
    protected ArrayList<Card> hand;
    protected boolean softHand;
    protected ArrayList<Hand> hands;
    protected int currentHand;

    public Player( String name, boolean isDealer ) {
        this.name = name;
        this.handTotal = 0;
        this.isDealer = isDealer;
        this.numAces = 0;
        this.hand = new ArrayList();
        this.hands = new ArrayList();
        this.softHand = false;
        this.currentHand = 0;
    }

    public String getName() { return this.name; }

    public ArrayList<Hand> getHands() { return this.hands; }

    public Hand getCurrentHand() { return this.hands.get( this.currentHand ); }
/*
    public boolean hit( Card hitCard ) {
        this.hand.add(hitCard);
        this.calculateBestHand();

        Hand currentHand = this.hands.get(this.currentHand);
        currentHand.calculateBestHand();

        return !this.didBust();


    }

    public boolean didBust() {
        return this.handTotal > 21;
    }

    public int calculateBestHand() {
        ArrayList<Integer> possibleTotals = new ArrayList<>();
        Stack<Card> cardStack = new Stack();
        for(int i = 0; i < this.hand.size(); i++) {
            cardStack.push( this.hand.get(i) );
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

    public boolean hasBlackJack() {
        if( this.hand.size() == 2 && this.handTotal == 21 ) {
            return true;
        }
        return false;
    }

    public int getHandTotal() { return this.handTotal; }

    public ArrayList<Card> getHand() { return this.hand; }
*/
    public void endHand() {
        this.handTotal = 0;
        this.hand.clear();
        this.hands.clear();
        this.softHand = false;
        this.currentHand = 0;
    }

    public static void main( String[] args ) {
//        Player p = new Player(false);
//        p.hand.add( new Card( 9, Suit.SPADES ) );
//        p.hand.add( new Card( 1, Suit.SPADES ) );
//        p.hand.add( new Card( 1, Suit.SPADES ) );
//        p.hand.add( new Card( 1, Suit.SPADES ) );
//        p.calculateBestHand();
    }

}
