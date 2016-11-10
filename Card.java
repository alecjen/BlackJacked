package com.test;
import java.util.Objects;

/**
 * Created by ajen1 on 11/3/16.
 */
public class Card {
    private int value;
    private Suit suit;

    public Card( int value, Suit suit ) {
        this.value = value;
        this.suit = suit;
    }

    public int rawValue() {
        return this.value;
    }

    public Suit getSuit() {
        return this.suit;
    }

    public String getCardFace() {
        switch( this.value ) {
            case 1: return "A";
            case 2: return "2";
            case 3: return "3";
            case 4: return "4";
            case 5: return "5";
            case 6: return "6";
            case 7: return "7";
            case 8: return "8";
            case 9: return "9";
            case 10: return "10";
            case 11: return "J";
            case 12: return "Q";
            case 13: return "K";
            default: return " ";
        }
    }

    public int getCardValue() {
        if( this.value == 1 ) {
            return 11;
        }
        if( this.value < 10 ) {
            return this.value;
        }
        return 10;
    }

    @Override
    public boolean equals( Object ob ) {
        if( ob == null ) {
            return false;
        }
        if( ob.getClass() != getClass() ) {
            return false;
        }
        Card other = (Card)ob;
        if( this.value == other.value && this.suit == other.suit ) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash( this.value, this.suit );
    }
}
