package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.LevelGameArea;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainTileDefinition;
import com.deco2800.game.components.gamearea.PerformanceDisplay;
import com.deco2800.game.components.maingame.MainGameActions;
import com.deco2800.game.components.maingame.MainGameExitDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.leveleditor.CameraMoveComponent;
import com.deco2800.game.leveleditor.LinkingToolComponent;
import com.deco2800.game.leveleditor.ObstacleToolComponent;
import com.deco2800.game.leveleditor.TileToolComponent;
import com.deco2800.game.levels.LevelDefinition;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.rendering.SpriteRenderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.terminal.Terminal;
import com.deco2800.game.ui.terminal.TerminalDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen for editing the level. This is essentially a more basic version of the MainGameScreen,
 * with various tools for editing the level
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class LevelEditorScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(LevelEditorScreen.class);
  private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);

  private final GdxGame game;
  private final Renderer renderer;
  private final PhysicsEngine physicsEngine;
  private final LevelDefinition levelDefinition;

  private LevelGameArea levelGameArea;

  private Entity hand;

  public LevelEditorScreen(GdxGame game, LevelDefinition levelDefinition) {
    this.game = game;
    this.levelDefinition = levelDefinition;

    logger.debug("Initialising level editor screen services");
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
    createUI();

    logger.debug("Initialising main game screen entities");
    TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());
    levelGameArea = new LevelGameArea(terrainFactory, levelDefinition);
    levelGameArea.init();

    selectTileHand();
  }

  @Override
  public void render(float delta) {
    physicsEngine.update();
    ServiceLocator.getEntityService().update();
    renderer.render();
  }

  @Override
  public void resize(int width, int height) {
    renderer.resize(width, height);
    logger.trace("Resized renderer: ({} x {})", width, height);
  }

  @Override
  public void pause() {
    logger.info("Game paused");
  }

  @Override
  public void resume() {
    logger.info("Game resumed");
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
    logger.debug("Loading assets");
    ServiceLocator.getResourceService().loadAll();
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
  }

  /**
   * Creates an entity which will follow the cursor and allow the user to place items
   */
  public void selectTileHand() {
    if (hand != null) hand.dispose();

    hand = new Entity();
    hand
      .addComponent(new SpriteRenderComponent(TerrainTileDefinition.TILE_FULL_MIDDLE.getSprite()))
      .addComponent(new TileToolComponent(levelGameArea, this));
    hand.scaleHeight(0.5f);

    ServiceLocator.getEntityService().register(hand);
  }

  /**
   * Creates an entity which will follow the cursor and allow the user to place obstacles
   */
  public void selectObstacleHand() {
    if (hand != null) hand.dispose();

    hand = new Entity();
    hand
      .addComponent(new ObstacleToolComponent(levelGameArea, this));
    hand.scaleHeight(0.5f);

    ServiceLocator.getEntityService().register(hand);
  }

  public void selectLinkingHand() {
    if (hand != null) hand.dispose();

    hand = new Entity();
    hand
      .addComponent(new LinkingToolComponent(levelGameArea, this));
    hand.scaleHeight(0.5f);

    ServiceLocator.getEntityService().register(hand);
  }

  /**
   * Creates the main game's ui including components for rendering ui elements to the screen and
   * capturing and handling ui input.
   */
  public void createUI() {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();
    InputComponent inputComponent =
        ServiceLocator.getInputService().getInputFactory().createForTerminal();

    Entity ui = new Entity();
    ui.addComponent(new InputDecorator(stage, 10))
        .addComponent(new PerformanceDisplay())
        .addComponent(new MainGameActions(this.game))
        .addComponent(new MainGameExitDisplay())
        .addComponent(new Terminal())
        .addComponent(inputComponent)
        .addComponent(new CameraMoveComponent())
        .addComponent(new TerminalDisplay());

    ServiceLocator.getEntityService().register(ui);
  }
}
