package com.deco2800.game.components.scores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.GdxGame;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.Clock;
import java.util.ArrayList;
import java.util.StringJoiner;

//import static com.deco2800.game.screens.MainGameScreen.timeScore;

public class ScoreDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ScoreDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private Sprite sprite;
    private Label scoreLabel; // Shows the score.
    private Label levelLabel; // Shows the current level.
    //int newScore = timeScore;
    int newScore = 0; // Will need to be set using GameTime
    //int newScore = 1;
    int highScore;
    private ArrayList<Integer> levels = new ArrayList<>();; // The current Level. Levels need to be implemented later in development
                           // when multiple levels are available. For now, it will be 0.

    private ArrayList<Integer> highScores = new ArrayList<>();

    @Override
    public void create() {
        for (int i = 0; i < 4; i++) { // Adds the number of levels to an arrayList of that size
            levels.add(i, i+1);
        }
//        System.out.println(levels);

        readHighScores();
        highScore = highScores.get(levels.get(0) - 1);
        if (newScore > highScore) { // For now, this only works for level 1
            highScore = newScore;
            // System.out.println(highScore);  // CHANGE TO A LOGGER
            writeHighScores();  // Implement in sprint 2 to store high score in file
        } else {
            highScore = highScores.get(levels.get(0) - 1);
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
        StringJoiner sjLevels = new StringJoiner("\n");
        StringJoiner sjText = new StringJoiner("\n");
        for (int level : levels) {
            try {
                String numLevel = String.format("Level %d", level);
                sjLevels.add(numLevel);
                String levelScores = String.format("%d", highScores.get(level-1));
                sjText.add(levelScores);
            } catch (IndexOutOfBoundsException e) {
                sjText.add("-"); // Added '-' if uncompleted level
            }
        }

        CharSequence levelText = sjLevels.toString();
        CharSequence scoreText = sjText.toString();
        scoreLabel = new Label(scoreText, skin, "large");
        levelLabel = new Label(levelText, skin, "large");
        levelLabel.getStyle().fontColor.add(Color.GOLD);

        int CenterScoreTextWidth = Math.round(centreWidth1 - scoreLabel.getWidth()/2);
        int CenterLevelTextWidth = Math.round(centreWidth1 - levelLabel.getWidth()/2);
        int textDimenstionHeight = (int) Math.round(Gdx.graphics.getHeight()*0.1);
        int textDimenstionWidth = (int) Math.round(Gdx.graphics.getWidth()*0.1);

        /**
         * Sets the position of the label.
         */
        levelLabel.setBounds(CenterLevelTextWidth - 100,400,
                textDimenstionWidth,textDimenstionHeight);
        scoreLabel.setBounds(CenterScoreTextWidth + 100,400,
                textDimenstionWidth,textDimenstionHeight);

        /**
         * Creates the 'SCOREDISPLAY' title texture.
         */
        Texture scoreDisplay = new Texture(Gdx.files.internal("images/ScoreTitlePlaceHolder.png"));
        Drawable scoreDisplayDrawable = new TextureRegionDrawable(new TextureRegion(scoreDisplay));
        Image scoreDisplayTitle = new Image(scoreDisplayDrawable);
        scoreDisplayTitle.setBounds(0,Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/5,
                Gdx.graphics.getWidth(),Gdx.graphics.getHeight()/6);
//        Image image1 = new Image(new Texture("images/ScoreTitlePlaceHolder.png"));
        //image1.setBounds(200, 80, 70, 40);
//        image1.setBounds(400, 200, 700, 200);
//        image1.setSize(50, 50);

        stage.addActor(table);
        stage.addActor(exitBtn);
        stage.addActor(levelLabel);
        stage.addActor(scoreLabel);
        stage.addActor(scoreDisplayTitle);
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
                // Removes NullPointerException
                assert myScoresReader != null;
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
            // Goes to the correct levels indentations
            for (int i = 0; i < levels.get(0); i++) {
                scoresWriter.write(highScores.get(i) + "\r\n"); // writes a blank line
            }
            scoresWriter.write(String.valueOf(highScore));

        } catch (IOException | NullPointerException e) {
            System.err.println("High_Scores.txt is corrupted or missing.");
        }

        finally {
            try {
                // Removes NullPointerException
                assert scoresWriter != null;
                scoresWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
