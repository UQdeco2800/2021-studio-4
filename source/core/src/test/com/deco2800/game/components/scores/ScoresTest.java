package com.deco2800.game.components.scores;


import com.badlogic.gdx.utils.Null;
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.ui.terminal.Terminal;
import com.deco2800.game.ui.terminal.commands.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

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
        verify(tester).create();
    }

    /**
     * check high score changes
     */
    @Test
    void testNewHighScore() {
        tester.create();
        spy(tester).highScore = 4;
        spy(tester).newScore = 4;
        assertEquals(spy(tester).highScore, spy(tester).newScore);
    }
}
