package com.deco2800.game.components.scores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;

import static com.deco2800.game.screens.MainGameScreen.timeScore;

public class ScoreDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ScoreDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private Sprite sprite;
    private Label scoreLabel; // Shows the score.
    private Label levelLabel; // Shows the current level.
    int newScore = timeScore;
    int highScore;
    private int level = 0; // The current Level. Levels need to be implemented later in development
                           // when multiple levels are available. For now, it will be 0.

    private ArrayList<Integer> highScores = new ArrayList<>();

    @Override
    public void create() {
        readHighScores();
        highScore = highScores.get(level);
        if (newScore > highScore) { // For now, this only works for level 1
            highScore = newScore;
            System.out.println(highScore);
            writeHighScores();  // Implement in sprint 2 to store high score in file
        } else {
            highScore = highScores.get(level);
        }
        super.create();
        addActors();
    }

    /**
     * Added Background image and initialised buttons
     */
    private void addActors() {
        table = new Table();
        table.setFillParent(true);
        sprite = new Sprite(new Texture("images/title_screen_clean.png"));
        table.setBackground(new SpriteDrawable(sprite)); // Set background

        int centreWidth1 = Gdx.graphics.getWidth()/2;
        int centreHeight1 = Gdx.graphics.getHeight()/2;
        int buttonDimensionsWidth = (int) Math.round(centreWidth1*0.35);
        int buttonDimensionsHeight = (int) Math.round(centreHeight1*0.31);
        int centreWidth = centreWidth1 - buttonDimensionsWidth/2; // Moves middle of button to Centre
        int centreHeight = centreWidth1 - buttonDimensionsHeight/2;
        int height105Percent = (int) Math.round(centreHeight*0.98);


        // This button takes us back to the main menu.
        /**
         * Creates the button texture for the Exit Button.
         */
        Texture exitTexture = new Texture(Gdx.files.internal("images/button_exit.png"));
        Texture exitHoverTexture = new Texture(Gdx.files.internal("images/button_exit_hover.png"));
        Drawable exitDrawing = new TextureRegionDrawable(new TextureRegion(exitTexture));
        ImageButton exitBtn = new ImageButton(exitDrawing);
        exitBtn.getStyle().imageOver = new TextureRegionDrawable(exitHoverTexture);
        /**
         * Sets the size and position of the button after texture applied.
         */
        exitBtn.setBounds(centreWidth,centreHeight-height105Percent,
                buttonDimensionsWidth, buttonDimensionsHeight);

        // Exit button event.
        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        logger.debug("Exit button from score screen is clicked");
                        entity.getEvents().trigger("exit");
                    }
                });

        // Text to display the score for the current Level.
        CharSequence levelText = String.format("Level %d Highest Score ever", level);
        CharSequence scoreText = String.format("%d", highScore);
        scoreLabel = new Label(scoreText, skin, "large");
        levelLabel = new Label(levelText, skin, "large");

        int widthLabel = (int) Math.round(Gdx.graphics.getWidth()*0.3);
        int centreScreenLevelWidth = (int) Math.round(scoreLabel.getWidth());
        int centreScreenScoreWidth = (int) Math.round(levelLabel.getWidth());
        int levelHeight = (int) Math.round(Gdx.graphics.getHeight()*0.8);
        int scoreHeight = (int) Math.round(Gdx.graphics.getHeight()*0.75);
        int textDimenstionWidth = (int) Math.round(Gdx.graphics.getWidth()*0.1);
        int textDimenstionHeight = (int) Math.round(Gdx.graphics.getHeight()*0.1);

        /**
         * Sets the position of the label.
         */
        levelLabel.setBounds(widthLabel + centreScreenLevelWidth,levelHeight,
                textDimenstionWidth,textDimenstionHeight);
        scoreLabel.setBounds(widthLabel + centreScreenScoreWidth,scoreHeight,
                textDimenstionWidth,textDimenstionHeight);

        stage.addActor(table);
        stage.addActor(exitBtn);
        stage.addActor(levelLabel);
        stage.addActor(scoreLabel);
        //stage.
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }



    /**
     * Reads the high scores recorded in a file
     */
    private void readHighScores() {
        BufferedReader myScoresReader = null;
        try {
            File scoresReader = new File("High_Scores.txt");
            myScoresReader = new BufferedReader(new FileReader(scoresReader));
            String str;
            while ((str = myScoresReader.readLine()) != null) {
                int score = Integer.parseInt(str);
                highScores.add(score);
            }
            myScoresReader.close();
        } catch (IOException | NullPointerException e) {
            System.err.println("High_Scores.txt is corrupted or missing.");
        }
        finally {
            try {
                myScoresReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Writes the high score to the text file. Only works for level 0
     */
    private void writeHighScores() {
        FileWriter scoresWriter = null;
        try {
            scoresWriter = new FileWriter("High_Scores.txt");
            scoresWriter.write(String.valueOf(highScore));
        } catch (IOException | NullPointerException e) {
            System.err.println("High_Scores.txt is corrupted or missing.");
        }
        finally {
            try {
                scoresWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public int getHighScore() {
        return highScore;
    }

    public void setNewScore(int input) {
        newScore = input;
    }
}
