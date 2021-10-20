package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.levelselect.LevelDisplay;
import com.deco2800.game.components.levelselect.LevelDisplayActions;
import com.deco2800.game.components.loading.LoadingScreenDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.MusicServiceDirectory;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The game that contains the level select.
 */
public class LevelSelectScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(LevelSelectScreen.class);
    private final GdxGame game;
    private final Renderer renderer;
    private static final String[] levelSelectMenuTextures = {"ui-elements/LevelSelectScreenPlaceholder.png"};
    private static final MusicServiceDirectory mainMenuSong = new MusicServiceDirectory();
    private static final String[] MainMenuMusic = {mainMenuSong.main_menu};

    public LevelSelectScreen(GdxGame game) {
        this.game = game;
        logger.debug("Initialising level selection screen services");
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
        logger.debug("Disposing level select screen");

        renderer.dispose();
        unloadAssets();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getEntityService().dispose();

        ServiceLocator.clear();

    }

    private void loadAssets() {
        logger.debug("This ONE!");
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(levelSelectMenuTextures);
        ServiceLocator.getResourceService().loadAll();
        resourceService.loadMusic(MainMenuMusic);

        while (resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            Entity ui = new Entity();
            ui.addComponent(new LoadingScreenDisplay());
            ServiceLocator.getEntityService().register(ui);
            logger.info("LoadingScreen");
            logger.info("Loading... {}% MMM", resourceService.getProgress());
            logger.info("Loading... {}% MMM", resourceService.getProgress());
            logger.info("Loading... {}% MMM", resourceService.getProgress());

            //logger.info(String.valueOf(LevelDisplay.selected));
        }

    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(levelSelectMenuTextures);
        resourceService.unloadAssets(MainMenuMusic);
    }

    /**
     * Creates the level select menu's ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        LevelDisplay levelDisplay = new LevelDisplay();
        /* global update button event listener passing into a different class */
        ui.addComponent(levelDisplay).
                addComponent(new InputDecorator(stage, 10)).
                addComponent(new LevelDisplayActions(game));
        ServiceLocator.getEntityService().register(ui);

    }



    }


