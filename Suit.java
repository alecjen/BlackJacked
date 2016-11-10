package com.test;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

/**
 * Created by ajen1 on 11/3/16.
 */
public enum Suit {
    SPADES, HEARTS, CLUBS, DIAMONDS;

    private static final List<Suit> VALUES = Collections.unmodifiableList(Arrays.asList(values() ) );
    private static final Random RANDOM = new Random();

    public static Suit getRandomSuit() {
        return VALUES.get( RANDOM.nextInt( VALUES.size() ) );
    }
}
