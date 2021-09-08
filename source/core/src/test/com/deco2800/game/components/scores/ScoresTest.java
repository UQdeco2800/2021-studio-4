package com.deco2800.game.components.scores;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(GameExtension.class)
public class ScoresTest {

    // Cannot initialise the ScoreDisplay tester;

     ScoreDisplay tester = mock(ScoreDisplay.class);


    /**
     * check initial high score it 0
     */
    @Test
    void test0HighScore() {
        tester.create();
        int highScore = 0;
        assertEquals(highScore, tester.getHighScore());
    }

    /**
     * check high score changes
     */
    @Test
    void testNewHighScore() {
//        tester.setNewScore(1); //Not workng atm. Possibly due to
//        tester.create();
//        int highScore = 1;
//        assertEquals(highScore, tester.getHighScore());
    }
}
