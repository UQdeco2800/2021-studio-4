package com.deco2800.game.leveleditor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.ui.UIComponent;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.areas.LevelGameArea;
import com.deco2800.game.services.ServiceLocator;

public class EditorUIComponent extends UIComponent {
    private Table rootTable;
    private Stage stage;
    private LevelGameArea levelGameArea;
    private TextField levelName;
    
    public EditorUIComponent(LevelGameArea levelGameArea) {
        super();
        this.stage = ServiceLocator.getRenderService().getStage();
        this.levelGameArea = levelGameArea;
    }

    public void generatePopup() {
        Label titleLabel = new Label("Enter a new level name", skin, "title");
        Label extensionLabel = new Label(".txt", skin, "title");
        Table menuBtns = makeBtns();
        Table inputField = new Table();
        levelName = new TextField("level", skin);

        rootTable = new Table();
        rootTable.setFillParent(true);

        // Title
        rootTable.add(titleLabel).expandX().top().padTop(10f);

        // Input
        rootTable.row();
        levelName.setWidth(20f);
        inputField.add(levelName);
        inputField.add(extensionLabel);
        rootTable.add(inputField);

        // Buttons
        rootTable.row();
        rootTable.add(menuBtns).fillX();

        stage.addActor(rootTable);
    }

    private Table makeBtns() {
        TextButton saveBtn = new TextButton("Save", skin);
        TextButton cancelBtn = new TextButton("Cancel", skin);

        saveBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    levelGameArea.saveAll(levelName.getText());
                    dispose();
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
        table.add(saveBtn)/*.expandX().left().pad(0f, 20f, 0f, 0f)*/;
        table.add(cancelBtn)/*.expandX().right().pad(0f, 0f, 20f, 0f)*/;
        return table;
    } 

    @Override
    protected void draw(SpriteBatch batch) {
        System.out.println("Drawing");
        // Drawing handled by stage
    }    

    @Override
    public void dispose() {
        rootTable.clear();
        super.dispose();
    }
}