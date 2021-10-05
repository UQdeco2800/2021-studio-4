package com.deco2800.game.components.scores;

import com.deco2800.game.extensions.GameExtension;
import com.sun.source.tree.AssertTree;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(GameExtension.class)
public class CalcScoreTest {

    /**
     * Tests when the time is over 3 minutes (180s) that the
     * score should always be 100
     */
    @Test
    public void lowestScoreTest() {
        CalcScore calcScore = new CalcScore();

        int time1 = calcScore.calculateScore(400);
        int time2 = calcScore.calculateScore(180);
        int expectedScore = 100;

        assertEquals(expectedScore, time1);
        assertEquals(expectedScore, time2);
    }

    /**
     * Tests when the time is over 0 seconds score should always be 100*180
     */
    @Test
    public void highestScoreTest() {
        CalcScore calcScore = new CalcScore();

        int time1 = calcScore.calculateScore(0);
        int expectedScore = 100*180;

        assertEquals(expectedScore, time1);
    }

    /**
     * Tests individual scores
     * Checks the score is inversely proportional to the clock time
     */
    @Test
    public void individualScoreTest() {
        CalcScore calcScore = new CalcScore();

        int time1 = calcScore.calculateScore(6);
        int time2 = calcScore.calculateScore(40);
        int time3 = calcScore.calculateScore(160);
        int expectedScore1 = 100*(180-6);
        int expectedScore2 = 100*(180-40);
        int expectedScore3 = 100*(180-160);

        assertEquals(expectedScore1, time1);
        assertEquals(expectedScore2, time2);
        assertEquals(expectedScore3, time3);
    }

    /**
     * Tests that a IllegalArgumentException is thrown
     * when the time clock is negative
     */
    @Test
    public void negativeClockTest() {
        CalcScore calcScore = new CalcScore();

        try {
            int time1 = calcScore.calculateScore(-5);
            int time2 = calcScore.calculateScore(-40);

            Assert.fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
}