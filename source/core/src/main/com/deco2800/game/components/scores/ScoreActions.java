package com.deco2800.game.components.scores;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.mainmenu.MainMenuActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScoreActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuActions.class);
    private final GdxGame game;

    public ScoreActions (GdxGame game){
        this.game = game;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("exit", this::onExit);
    }

    /**
     * Exits the game.
     */
    private void onExit() {
        logger.info("Exit game");
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }
}
