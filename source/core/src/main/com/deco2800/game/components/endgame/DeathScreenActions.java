package com.deco2800.game.components.endgame;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.levelselect.LevelDisplayActions;
import com.deco2800.game.components.levelselect.PreviousLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class responds to user input in the death screen and reacts by triggering the corresponding event.
 */
public class DeathScreenActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(LevelDisplayActions.class);
    private GdxGame game;
    private PreviousLevel previousLevel = new PreviousLevel();

    public DeathScreenActions(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("exit", this::onExit);
        entity.getEvents().addListener("retry", this::onRestart);
    }

    /**
     * Swaps to the Main Menu screen.
     */
    private void onExit() {
        logger.info("Exiting main game screen");
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    /**
     * Restarts the level.
     */
    private void onRestart() {
        logger.info("Restart the level");
        game.setLevel(GdxGame.ScreenType.MAIN_GAME, previousLevel.getPreviousLevel());
    }
}
