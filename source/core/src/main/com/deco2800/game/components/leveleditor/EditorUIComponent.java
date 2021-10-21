package com.deco2800.game.components.leveleditor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.GdxGame;
import com.deco2800.game.levels.LevelDefinition;
import com.deco2800.game.levels.LevelInfo;
import com.deco2800.game.ui.UIComponent;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.areas.LevelGameArea;
import com.deco2800.game.services.ServiceLocator;

/**
 * Represents the level editor UI
 */
public class EditorUIComponent extends UIComponent {
    private Table rootTable;
    private Stage stage;
    private LevelGameArea levelGameArea;
    private TextField levelName;
    private GdxGame game;

    /**
     * Constuctor for when only loading a file is required
     */
    public EditorUIComponent(GdxGame game) {
        super();
        this.stage = ServiceLocator.getRenderService().getStage();
        this.game = game;
    }

    /**
     * The constructor for the UI component
     * @param levelGameArea The area to save
     */
    public EditorUIComponent(LevelGameArea levelGameArea, GdxGame game) {
        super();
        this.stage = ServiceLocator.getRenderService().getStage();
        this.levelGameArea = levelGameArea;
        this.game = game;
    }

    public void generateSavePopup() {
        generateSavePopup(false, false);
    }

    /**
     * Generates a "saving level" popup which asks for a level name
     */
    public void generateSavePopup(boolean load, boolean play) {
        LevelInfo levelInfo = null;
        if (levelGameArea != null) {
            levelInfo = levelGameArea.getLevelInfo();
        }

        String existingLevelName = "";

        if (!load) {
            if (levelInfo != null && levelInfo.getLevelFileName() == null) {
                levelInfo.setLevelFileName("mylevels/mylevel.json");
            }
            existingLevelName = levelInfo.getLevelFileName();
        }

        Label titleLabel;
        if (!load) {
            titleLabel = new Label("Enter a new level name", skin, "title");
        } else {
            titleLabel = new Label("Enter the name of the level to load", skin, "title");
        }
        Table menuBtns = makeBtns(load, play);
        Table inputField = new Table();
        levelName = new TextField(existingLevelName, skin);

        rootTable = new Table();
        rootTable.setFillParent(true);

        // Title
        rootTable.add(titleLabel).expandX().top().padTop(10f);

        // Input
        rootTable.row();
        inputField.add(levelName).width(400f);
        rootTable.add(inputField);

        // Buttons
        rootTable.row();
        rootTable.add(menuBtns).fillX();

        stage.addActor(rootTable);
    }

    /**
     * Creates buttons to interact with the level save popup
     * @return A Table containing the buttons
     */
    private Table makeBtns(boolean load, boolean play) {
        TextButton saveLoadBtn;
        if (!load) {
            saveLoadBtn = new TextButton("Save", skin);
        } else {
            saveLoadBtn = new TextButton("Load", skin);
        }

        TextButton cancelBtn = new TextButton("Cancel", skin);

        saveLoadBtn.addListener(
          new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {
                  if (!load) {
                      levelGameArea.getLevelInfo().setLevelFileName(levelName.getText());
                      levelGameArea.writeAll();
                      dispose();
                  } else {
                      // Check if is inbuilt level
                      LevelInfo levelInfo = null;
                      for (LevelDefinition levelDef : LevelDefinition.values()) {
                          if (levelDef.getLevelInfo().getLevelFileName().equals(levelName.getText())) {
                              levelInfo = levelDef.getLevelInfo();
                              break;
                          }
                      }

                      if (levelInfo == null) {
                          levelInfo = new LevelInfo("Level 1", "game_level_1", levelName.getText());
                      }

                      game.setLevel(play ? GdxGame.ScreenType.MAIN_GAME : GdxGame.ScreenType.LEVEL_EDITOR, levelInfo);
                  }
              }
          }
        );

        cancelBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    dispose();
                }
            }
        );

        Table table = new Table();
        table.add(saveLoadBtn).pad(10f);
        table.add(cancelBtn).pad(10f);
        return table;
    } 

    @Override
    protected void draw(SpriteBatch batch) {
        // Drawing handled by stage
    }    

    @Override
    public void dispose() {
        if (rootTable != null) {
            rootTable.clear();
        }
        super.dispose();
    }
}