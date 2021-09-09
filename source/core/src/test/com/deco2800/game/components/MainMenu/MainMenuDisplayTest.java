package com.deco2800.game.components.MainMenu;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.mainmenu.MainMenuDisplay;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class MainMenuDisplayTest extends MainMenuDisplay {

    private GdxGame game;

    @BeforeAll
    static void beforeAll() {
        // Mock any calls to OpenGL
        GdxGame game = mock(GdxGame.class);
    }

    /**
     * when switcher is 0 mute button is displayed test
     */
    @Test
    void testInitialSwitcherIs0() {
        int switcher = getSwitcher();
        int expected = 0;
        assertEquals(expected, switcher, "Switcher Value was not 0. Mute button text incorrect");
    }

    /**
     * when switcher is 0 unmute button is displayed test
     */
    @Test
    void testInitialSwitcherIs1() {
        System.out.println(getMuteBtn());
        int expected = 0;
        assertEquals(expected, getSwitcher(), "Switcher Value was not 0. Mute button text incorrect");
    }
}
