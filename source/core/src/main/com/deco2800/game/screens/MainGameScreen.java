package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.LevelGameArea;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.loading.LoadingScreenDisplay;
import com.deco2800.game.components.loading.PauseScreenDisplay;
import com.deco2800.game.components.maingame.MainGameActions;
import com.deco2800.game.components.player.KeyboardPlayerInputComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.levels.LevelDefinition;
import com.deco2800.game.levels.LevelInfo;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.terminal.Terminal;
import com.deco2800.game.ui.terminal.TerminalDisplay;
import com.deco2800.game.components.maingame.MainGameExitDisplay;
import com.deco2800.game.components.gamearea.PerformanceDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Timer;
import java.util.TimerTask;

import static com.deco2800.game.components.player.PlayerStatsDisplay.gameOver;

/**
 * The game screen containing the main game.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class MainGameScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);
  private static final String[] mainGameTextures = {};
  private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);

  private long start = System.currentTimeMillis();
  private long end = start + 2*1000;
  private final GdxGame game;
  private final Renderer renderer;
  private final PhysicsEngine physicsEngine;
  private Entity ui = new Entity();
  private Entity ui2 = new Entity();
  private Entity ui3 = new Entity();
  private PauseScreenDisplay pauseScreenDisplay = new PauseScreenDisplay();
//  private final LevelDefinition levelDefinition;
  private LevelGameArea levelGameArea;

  //private final long timeStarted = System.currentTimeMillis();
  public static long timeScore;
  public static boolean levelComplete = false;
  public GameTime gameTime;

  public MainGameScreen(GdxGame game, LevelInfo levelInfo) {
    this.game = game;
//    this.levelDefinition = levelDefinition;
    this.gameTime = new GameTime();

    logger.debug("Initialising main game screen services");
    ServiceLocator.registerTimeSource(new GameTime());

    PhysicsService physicsService = new PhysicsService();
    ServiceLocator.registerPhysicsService(physicsService);
    physicsEngine = physicsService.getPhysics();

    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());

    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());

    renderer = RenderFactory.createRenderer();
    renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

    ServiceLocator.registerCamera(renderer.getCamera());

    loadAssets();
    /* modified with changes https://www.tabnine.com/code/java/methods/java.util.Timer/schedule */


    createUI();


    logger.debug("Initialising main game screen entities");
    TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());
    levelGameArea = new LevelGameArea(terrainFactory, levelInfo);
    levelGameArea.create();
  }

  public static void setLevelComplete() {
    levelComplete = true;
  }

  @Override
  public void render(float delta) {
    physicsEngine.update();
    ServiceLocator.getEntityService().update();
    renderer.render();

    if (gameOver) {
      gameOver = false;
      playerDied();
    }

    if (levelComplete)   {
      playerWon();
    }

    timeScore = gameTime.getTime(); // Doesn't change the time????
    //timeScore = (int) ((System.currentTimeMillis() - timeStarted) / 1000);
  }


  public void playerWon() {
    logger.info("Level completed");
    game.setScreen(GdxGame.ScreenType.SCORE_SCREEN); // Must go to scoreScreen
                                                     // to change levelComplete to false
  }

  public void playerDied() {
      logger.info("Show Death Screen");
      game.setScreen(GdxGame.ScreenType.DEATH_SCREEN);
  }

  @Override
  public void resize(int width, int height) {
    renderer.resize(width, height);
    logger.trace("Resized renderer: ({} x {})", width, height);
  }

  @Override
  public void pause() {
    createPauseUI(true);
    logger.info("Game paused");
    levelGameArea.getPlayer().getComponent(KeyboardPlayerInputComponent.class).pause();
  }

  @Override
  public void resume() {
    createPauseUI(false);
    logger.info("Game resumed");
    levelGameArea.getPlayer().getComponent(KeyboardPlayerInputComponent.class).resume();
  }

  @Override
  public void dispose() {
    logger.debug("Disposing main game screen");

    renderer.dispose();
    unloadAssets();

    ServiceLocator.getEntityService().dispose();
    ServiceLocator.getRenderService().dispose();
    ServiceLocator.getResourceService().dispose();

    ServiceLocator.clear();
  }

  private void loadAssets() {
    logger.debug("Loading assets in main game screen");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(mainGameTextures);
    ServiceLocator.getResourceService().loadAll();
    while (!resourceService.loadForMillis(10)) {
      logger.info("LOOOK AT ME! I'M MAKING A LOADING SCREEN");

    }
  }

  private void unloadAssets() {
    logger.debug("Unloading assets in main game screen");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(mainGameTextures);

  }

  /**
   * Creates the main game's ui including components for rendering ui elements to the screen and
   * capturing and handling ui input.
   */

  private void createUI()  {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();
    boolean displayLoading = false;
    InputComponent inputComponent =
            ServiceLocator.getInputService().getInputFactory().createForTerminal();

   /* ui.addComponent(new InputDecorator(stage, 10))
        .addComponent(new PerformanceDisplay())
        .addComponent(new MainGameActions(this.game))
        .addComponent(new MainGameExitDisplay())
        .addComponent(new Terminal())
        .addComponent(inputComponent)
        .addComponent(new TerminalDisplay());*/

      System.out.println("Yoodole");
      logger.debug("It's loading!");


       // ui.dispose();
       // Entity ui2 = new Entity();
        ui3.addComponent(new InputDecorator(stage, 10))
                .addComponent(new PerformanceDisplay())
                .addComponent(new MainGameActions(this.game))
                .addComponent(new MainGameExitDisplay())
                .addComponent(new Terminal())
                .addComponent(inputComponent)
                .addComponent(new TerminalDisplay());
        ServiceLocator.getEntityService().register(ui3);
      //  ui.dispose();

      }


  private void createPauseUI(boolean loading) {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();
    boolean displayLoading = false;
    InputComponent inputComponent =
            ServiceLocator.getInputService().getInputFactory().createForTerminal();
    Entity ui2 = new Entity();

    if (loading == true) {
      ui.addComponent(pauseScreenDisplay);
      pauseScreenDisplay.add();
      ui.create();
      ServiceLocator.getEntityService().register(ui);
    }
    if (loading == false) {
      //ui.dispose();
      pauseScreenDisplay.remove();
      ui2.addComponent(new InputDecorator(stage, 10))
              .addComponent(new PerformanceDisplay())
              .addComponent(new MainGameActions(this.game))
              .addComponent(new MainGameExitDisplay())
              .addComponent(new Terminal())
              .addComponent(inputComponent)
              .addComponent(new TerminalDisplay());
      ServiceLocator.getEntityService().register(ui2);
    }
  }
  }



