package com.deco2800.game.components.levelselect;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.leveleditor.EditorUIComponent;
import com.deco2800.game.components.levelselect.LevelDisplayActions;
import com.deco2800.game.levels.LevelDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class responds to user input in the level select screen and reacts by triggering the corresponding event.
 */
public class LevelDisplayActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(LevelDisplayActions.class);
    private GdxGame game;
    private PreviousLevel previousLevel = new PreviousLevel();

    public LevelDisplayActions(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("exit", this::onExit);
        entity.getEvents().addListener("start", this::startGame);
        entity.getEvents().addListener("levelEditor", this::startLevelEditor);
        entity.getEvents().addListener("loadLevel", this::onLoadLevel);
    }

    /**
     * Starts the game with the selected level (if applicable)
     */
    private void startGame(LevelDefinition levelDefinition) {
        logger.info("Start game level: " + levelDefinition);
        game.setLevel(GdxGame.ScreenType.MAIN_GAME, levelDefinition.getLevelInfo());
        previousLevel.updatePreviousLevel(levelDefinition.getLevelInfo());
    }

    /**
     * Starts the game with the selected level (if applicable)
     */
    private void startLevelEditor(LevelDefinition levelDefinition) {
        logger.info("Level editor for level level: " + levelDefinition);
        game.setLevel(GdxGame.ScreenType.LEVEL_EDITOR, levelDefinition.getLevelInfo());
    }

    /**
     * Swaps to the Main Menu screen.
     */
    private void onExit() {
        logger.info("Exiting to main game screen");
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    private void onLoadLevel() {
        new EditorUIComponent(game).generateSavePopup(true, true);
    }
}
