package com.deco2800.game.components.levelselect;

import com.badlogic.gdx.Screen;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.levelselect.LevelDisplayActions;
import com.deco2800.game.levels.LevelDefinition;
import com.deco2800.game.screens.LoadingScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This class responds to user input in the level select screen and reacts by triggering the corresponding event.
 */
public class LevelDisplayActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(LevelDisplayActions.class);
    private static GdxGame game;
    private static PreviousLevel previousLevel = new PreviousLevel();

    public LevelDisplayActions(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("exit", this::onExit);
        entity.getEvents().addListener("start", this::startGame);
        entity.getEvents().addListener("levelEditor", this::startLevelEditor);
    }
    private static void addGameScreen(LevelDefinition levelDefinition) {

        game.setLevel(GdxGame.ScreenType.MAIN_GAME,  levelDefinition);
        previousLevel.updatePreviousLevel(  levelDefinition);

    }

    /**
     * Starts the game with the selected level (if applicable)
     */
   private void startGame(LevelDefinition levelDefinition) {
       game.setLevel(GdxGame.ScreenType.MAIN_GAME,  levelDefinition);
       previousLevel.updatePreviousLevel(  levelDefinition);
       // game.setScreen(GdxGame.ScreenType.LOADING);
       // Screen testscreen = new LoadingScreen(game);
       // logger.info(game.getScreen().getClass().toString());
       // logger.info(testscreen.getClass().toString());
      //  try {
      //  Thread.sleep(10000);
      //  } catch (InterruptedException e) {
        //    e.printStackTrace();
      //  }
       // if (!game.getScreen().getClass().equals(testscreen.getClass())) {
        //        logger.info("IT WORKS! BY GEORGE WE DID IT! ");
                  //game.setLevel(GdxGame.ScreenType.MAIN_GAME, levelDefinition);
                    //previousLevel.updatePreviousLevel(levelDefinition);
         //       addGameScreen(levelDefinition);
          //     final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
          //    executorService.scheduleAtFixedRate(LevelDisplayActions::addGameScreen, 0, 1, TimeUnit.SECONDS);

              // final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
              // executorService.scheduleAtFixedRate(LevelDisplayActions::addGameScreen, 0, 1, TimeUnit.SECONDS);
          //  ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
          //  exec.schedule(() -> addGameScreen(levelDefinition), 5, TimeUnit.SECONDS);
        }


   // }
       // game.setLevel(GdxGame.ScreenType.MAIN_GAME,  levelDefinition);
       // previousLevel.updatePreviousLevel(levelDefinition);*/

    //}


    //


    /**
     * Starts the game with the selected level (if applicable)
     */
    private void startLevelEditor(LevelDefinition levelDefinition) {
        logger.info("Level editor for level level: " + levelDefinition);
        game.setLevel(GdxGame.ScreenType.LEVEL_EDITOR, levelDefinition);
    }

    /**
     * Swaps to the Main Menu screen.
     */
    private void onExit() {
        logger.info("Exiting to main game screen");
        //
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }
}

