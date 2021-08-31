package com.deco2800.game.components.MainMenu;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.mainmenu.MainMenuActions;
import com.deco2800.game.components.mainmenu.MainMenuDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

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
