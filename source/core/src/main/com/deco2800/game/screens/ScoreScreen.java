package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.scores.ScoreActions;
import com.deco2800.game.components.scores.ScoreDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.levels.LevelDefinition;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.MusicServiceDirectory;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScoreScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ScoreScreen.class);
    private final GdxGame game;
    private final Renderer renderer;
    private static final String[] scoreScreenTextures = {"ui-elements/title_screen_clean.png"};
    private static final MusicServiceDirectory mainMenuSong = new MusicServiceDirectory();
    private static final String[] MainMenuMusic = {mainMenuSong.main_menu};
    private final LevelDefinition levelDefinition;
    private final int completionTime;

    public ScoreScreen (GdxGame game, LevelDefinition levelDefinition, int completionTime) {
        this.game = game;
        this.levelDefinition = levelDefinition;
        this.completionTime = completionTime;
        logger.debug("Initialising score screen");
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
        logger.debug("Disposing score screen");

        renderer.dispose();
        unloadAssets();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getEntityService().dispose();

        ServiceLocator.clear();

    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(scoreScreenTextures);
        ServiceLocator.getResourceService().loadAll();
        resourceService.loadMusic(MainMenuMusic);

        while (resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen

            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(scoreScreenTextures);
        resourceService.unloadAssets(MainMenuMusic);
    }

    /**
     * Creates the score screen's ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new ScoreDisplay(levelDefinition, completionTime))
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(new ScoreActions(game));
        ServiceLocator.getEntityService().register(ui);

    }
}
