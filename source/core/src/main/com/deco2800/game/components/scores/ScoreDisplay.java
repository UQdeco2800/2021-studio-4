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
import com.deco2800.game.components.InsertImageButton;
import com.deco2800.game.levels.LevelDefinition;
import com.deco2800.game.levels.LevelInfo;
import com.deco2800.game.services.MusicService;
import com.deco2800.game.services.MusicServiceDirectory;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.StringJoiner;
import static com.deco2800.game.screens.MainGameScreen.levelComplete;


public class ScoreDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ScoreDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private Label scoreLabel; // Shows the score.
    private Label levelLabel; // Shows the current level.
    private Label congratsLabel; // Shows the congratulations text.
    private LevelInfo levelInfo;
    private boolean newBest;
    private int newScore; // Will need to be set using GameTime
    private final int completionTime; // Will need to be set using GameTime
    private int highScore;
    private final boolean isSuccessful;
    private final ArrayList<Integer> levels = new ArrayList<>(); // The current Level
    private final ArrayList<Integer> highScores = new ArrayList<>();

    public ScoreDisplay(LevelInfo levelInfo, int completionTime) {
        this.levelInfo = levelInfo;
        this.completionTime = completionTime;
        isSuccessful = levelComplete;
        levelComplete = false;
        newBest = false;
    }

    @Override
    public void create() {
        for (int i = 0; i < 4; i++) { // Adds the number of levels to an arrayList of that size
            levels.add(i, i+1);
        }

        readHighScores(); // Bug note: Doesn't show new high score straight away

        int level = selectLevel();

        if (isSuccessful) {
            // Sets the newScore
            getNewScore();
            if (newScore > highScore) {
                highScore = newScore;
                // System.out.println(highScore);  // CHANGE TO A LOGGER
                writeHighScores(level);  // Implement in sprint 2 to store high score in file
                newBest = true;
            }
        }

        super.create();
        addActors();
        playTheMusic();
    }

    /**
     * creates the score based off of the calculation done in the CalcScore class
     */
    private void getNewScore() {
        CalcScore calcScore = new CalcScore();
        newScore = calcScore.calculateScore(completionTime);
    }

    /**
     * Added Background image and initialised buttons
     */
    private void addActors() {
        InsertImageButton insImage = new InsertImageButton();

        table = insImage.setTable("ui-elements/score_screen_guide.png");

        int centreWidth1 = Gdx.graphics.getWidth()/2;
        int centreHeight1 = Gdx.graphics.getHeight()/2;
        int buttonDimensionsWidth = (int) Math.round(centreWidth1*0.3);
        int buttonDimensionsHeight = (int) Math.round(centreHeight1*0.15);
        int centreWidth = centreWidth1 - buttonDimensionsWidth/2; // Moves middle of button to Centre
        int centreHeight = centreWidth1 - buttonDimensionsHeight/2;
        int height105Percent = (int) Math.round(centreHeight*0.98);

        String exitMainImage = "ui-elements/default_buttons/exit-button.png";
        String exitHoverImage = "ui-elements/hovered-buttons/exit-button-hovered.png";
        ImageButton exitBtn;
        exitBtn = insImage.setImage(exitMainImage, exitHoverImage,
                centreWidth,centreHeight-height105Percent,
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

        // Text to display the score for the current Level. If score is 0 or null '-' os displayed
        StringJoiner sjLevels = new StringJoiner("\n");
        StringJoiner sjScores = new StringJoiner("\n");
        String congratsText = "";

        if (isSuccessful) {
            if (levelInfo != null) {
                if (newBest) {
                    congratsText = "new PB: \n" +
                      levelInfo.getName() + ": " + newScore + "!";
                    sjLevels.add("Previous Scores");
                    sjScores.add(""); // Does a new line for scores string
                    newBest = false;
                } else {
                    congratsText = "most recent score: \n" +
                      levelInfo.getName() + ": " + newScore;
                    sjLevels.add("High Scores");
                    sjScores.add(""); // Does a new line for scores string
                }
            }
        } else {
            sjLevels.add("High Scores");
            sjScores.add(""); // Does a new line for scores string
        }

        for (int level : levels) {
            try {
                String numLevel = String.format("Level %d:", level);
                sjLevels.add(numLevel);
                String levelScores = String.format("%d", highScores.get(level-1));
                if (levelScores.equals("0")) {
                    sjScores.add("-");
                } else {
                    sjScores.add(levelScores);
                }
            } catch (IndexOutOfBoundsException e) {
                sjScores.add("-"); // Added '-' if uncompleted level
            }
        }

        CharSequence levelText = sjLevels.toString();
        CharSequence scoreText = sjScores.toString();
        // Shows the score.
        Label scoreLabel = new Label(scoreText, skin, "large");
        // Shows the current level.
        Label levelLabel = new Label(levelText, skin, "large");
        // Shows the congratulations text.
        Label congratsLabel = new Label(congratsText, skin, "large");
        levelLabel.getStyle().fontColor.add(Color.WHITE); // Other colours
                                                          // default to white is
                                                          // this colour is changed?
        //scoreLabel.getStyle().fontColor.add(Color.GOLDENROD);

        int CenterScoreTextWidth = Math.round(centreWidth1 - scoreLabel.getWidth()/2);
        int CenterLevelTextWidth = Math.round(centreWidth1 - levelLabel.getWidth()/2);
        int textDimensionHeight = (int) Math.round(Gdx.graphics.getHeight()*0.1);
        int textDimensionWidth = (int) Math.round(Gdx.graphics.getWidth()*0.1);

        levelLabel.setBounds((float)(CenterLevelTextWidth - 100),400,
                textDimensionWidth,textDimensionHeight);
        scoreLabel.setBounds((float)(CenterScoreTextWidth + 100),400,
                textDimensionWidth,textDimensionHeight);
        congratsLabel.setBounds((float)(CenterLevelTextWidth - 100), 560,
                textDimensionWidth, textDimensionHeight);

        int imageWidth = Gdx.graphics.getWidth()/2;
        Texture scoreDisplay = new Texture(Gdx.files.internal("ui-elements/scores-title.png"));
        Drawable scoreDisplayDrawable = new TextureRegionDrawable(new TextureRegion(scoreDisplay));
        Image scoreDisplayTitle = new Image(scoreDisplayDrawable);
        scoreDisplayTitle.setBounds((float)Gdx.graphics.getWidth()/2 - (float)imageWidth/2,Gdx.graphics.getHeight()-(float)Gdx.graphics.getHeight()/5,
                imageWidth,(float)Gdx.graphics.getHeight()/6);

        stage.addActor(table);
        stage.addActor(exitBtn);
        stage.addActor(levelLabel);
        stage.addActor(scoreLabel);
        stage.addActor(congratsLabel);
        stage.addActor(scoreDisplayTitle);
        //stage.
    }

    /**
     * Determines the level that the player accomplished
     * @return the level as an integer
     */
    private int selectLevel() {
        if (levelInfo != null) {
            switch (levelInfo.getName()) {
                case ("Level 1"):
                    highScore = highScores.get(0);
                    return 1;
                case ("Level 2"):
                    highScore = highScores.get(1);
                    return 2;
                case ("Level 3"):
                    highScore = highScores.get(2);
                    return 3;
                case ("Level 4"):
                    highScore = highScores.get(3);
                    return 4;
                default:
                    logger.error("not a valid levelName");
                    return 0;
            }
        }
        return 0;
    }

    /**
     * Play the music
     */
    public void playTheMusic() {
        MusicServiceDirectory menuSong = new MusicServiceDirectory();
        MusicService menuMusic = new MusicService(menuSong.main_menu);
        menuMusic.playSong(true, 0.2f);
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

            // Reads the contents of the file. If there is an empty string it sets
            // the HighScore value to 0 to avoid any possibly bugs.
            while ((str = myScoresReader.readLine()) != null) {
                if (!str.equals("")) {
                    int score = Integer.parseInt(str);
                    highScores.add(score);
                } else {
                    highScores.add(0);
                }
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
            } catch (IOException | NullPointerException ignored) {
            }
        }
    }

    /**
     * Writes the high score to the text file.
     */
    private void writeHighScores(int level) {
        FileWriter scoresWriter = null;
        try {

            scoresWriter = new FileWriter("High_Scores.txt");
            // Goes to the correct levels indentations
            for (int i = 0; i < levels.size(); i++) {
                if (levels.get(i) == level) {
                    scoresWriter.write(highScore + "\r\n"); // writes a new line
                } else {
                    scoresWriter.write(highScores.get(i) + "\r\n");
                }
            }

        } catch (IOException | NullPointerException e) {
            System.err.println("High_Scores.txt is corrupted or missing.");
        }

        finally {
            try {
                // Removes NullPointerException possibility
                assert scoresWriter != null;
                scoresWriter.close();
            } catch (IOException | NullPointerException ignored) {
            }
        }
    }
}
