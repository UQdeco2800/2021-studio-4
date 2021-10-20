package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.pausegame.PauseScreenActions;
import com.deco2800.game.components.pausegame.PauseScreenDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PauseScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(PauseScreen.class);
  private final GdxGame game;
  private final Renderer renderer;
  private static final String[] PauseScreenTextures = {"ui-elements/death-screen-background.png"};

  public PauseScreen(GdxGame game) {
    this.game = game;
    logger.debug("Initialising pause screen");
    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());
    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());
    renderer = RenderFactory.createRenderer();
    loadAssets();
    createUI();

  }

  @Override
  public void render(float delta) {
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
    logger.debug("Disposing pause screen");

    renderer.dispose();
    unloadAssets();
    ServiceLocator.getRenderService().dispose();
    ServiceLocator.getEntityService().dispose();

    ServiceLocator.clear();

  }
  private void loadAssets() {

    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(PauseScreenTextures);
    ServiceLocator.getResourceService().loadAll();

    while (resourceService.loadForMillis(10)) {
      // This could be upgraded to a loading screen
      logger.info("Loading... {}%", resourceService.getProgress());
    }

  }
  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(PauseScreenTextures);
  }

  /**
   * Creates the death screen ui including buttons to restart the level or exit to main menu.
   */
  private void createUI() {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();
    Entity ui = new Entity();
    ui.addComponent(new PauseScreenDisplay())
      .addComponent(new InputDecorator(stage, 10))
      .addComponent(new PauseScreenActions(game));
    ServiceLocator.getEntityService().register(ui);
  }
}