package com.test;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;

/**
 * Created by ajen1 on 11/4/16.
 */
public class Shoe {

    private Map<Card, Integer> cards;
    private ArrayList<Card> cardList;
    private int numDecks;

    public Shoe( int numDecks ) {
        this.cards = new HashMap();
        this.cardList = new ArrayList();
        this.numDecks = numDecks;
        this.shuffle();
    }

    public void shuffle() {
        this.cards.clear();
        this.cardList.clear();

        // Cut leaves 0.5 to 0.75 of the total cards left for the shoe
        int maxCards = this.numDecks * 39; // 39 = 75% of 52 card deck
        int minCards = this.numDecks * 26; // 26 = 50% of 52 card deck
        Random shoeCut = new Random();
        int numCards = shoeCut.nextInt( maxCards - minCards ) + minCards;
        System.out.println( "Shoe size: " + numCards );
        int count = 0;
        while( count < numCards ) {
            Card c = this.generateRandomCard();
            int cardCount = this.cards.containsKey(c) ? this.cards.get(c) : 0;
            if( cardCount < this.numDecks ) {
                this.cards.put( c, cardCount + 1 );
                this.cardList.add(c);
                count++;
            }
        }
    }
/*
    public ArrayList<Card> deal( int numCards, Player player ) {

        ArrayList<Card> outputCards = new ArrayList();
        for( int i = 0; i < numCards; i++ ) {
            Random rand = new Random();
            int cardIndex = rand.nextInt( this.cardList.size() );
            Card out = this.cardList.get(cardIndex);
            this.cardList.remove(cardIndex);
            int cardCount = this.cards.get(out);
            cardCount--;
            this.cards.put( out, cardCount );
            if( cardCount == 0 ) {
                this.cards.remove(out);
            }
            outputCards.add(out);
            player.hit(out);
        }
        return outputCards;
    }
*/
    public ArrayList<Card> deal( int numCards, Hand hand ) {

        ArrayList<Card> outputCards = new ArrayList();
        for( int i = 0; i < numCards; i++ ) {
            Random rand = new Random();
            int cardIndex = rand.nextInt( this.cardList.size() );
            Card out = this.cardList.get(cardIndex);
            this.cardList.remove(cardIndex);
            int cardCount = this.cards.get(out);
            cardCount--;
            this.cards.put( out, cardCount );
            if( cardCount == 0 ) {
                this.cards.remove(out);
            }
            outputCards.add(out);
            hand.hit(out);
        }
        return outputCards;
    }

    public boolean hasEnoughCards( int numPlayers ) {
        int cardsNeeded = numPlayers * 8;
        System.out.println( "shoe size: " + this.cardList.size() );
        return this.cardList.size() > cardsNeeded;
    }

    public void print() {
        for( Map.Entry<Card, Integer> entry : this.cards.entrySet() ) {
            System.out.println( entry.getKey().getCardFace() + " " + entry.getKey().getSuit() + ": " + entry.getValue() );
        }
        System.out.println("____________________");
    }

    private Card generateRandomCard() {
        Random randValue = new Random();
        int value = randValue.nextInt( 13 ) + 1;
        Suit suit = Suit.getRandomSuit();
        Card c = new Card( value, suit );
        return c;
    }

    public static void main( String [] args ) {
        Shoe s = new Shoe(2);
        Better b = new Better( "Alec", 100, 10);
        //s.deal(2, b);
        //System.out.println( "Hand count is: " + b.getHandTotal() );
    }


}
