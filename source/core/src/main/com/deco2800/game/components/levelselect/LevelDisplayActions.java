package com.deco2800.game.components.levelselect;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.levelselect.LevelDisplayActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class responds to user input in the level select screen and reacts by triggering the corresponding event.
 */
public class LevelDisplayActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(LevelDisplayActions.class);
    private GdxGame game;

    public LevelDisplayActions(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("exit", this::onExit);
        entity.getEvents().addListener("start", this::level1);
    }

    /**
     * Swaps to the Main Game screen.
     * Note that this only goes to the main Game screen at the moment as
     * there were no levels past level1 created at time of implementation
     */
    private void level1() {
        logger.info("Start game");
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

    /**
     * Swaps to the Main Menu screen.
     */
    private void onExit() {
        logger.info("Exiting to main game screen");
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }
}
