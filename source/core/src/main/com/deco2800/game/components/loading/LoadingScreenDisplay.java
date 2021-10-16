package com.deco2800.game.components.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.components.InsertImageButton;
import com.deco2800.game.services.*;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * A ui component for displaying the Loading Screen
 */
public class LoadingScreenDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(LoadingScreenDisplay.class);
    private static final float Z_INDEX = 2f;
    private String loadingText;
    private int loadingPercentage;
    private String[] terms;
    Random rand = new Random();


    /**
     * used tp switch between button states
     */
    public LoadingScreenDisplay() {
        terms = new String[]{
            "ただ良くなる",
          "Avoiding Death is the best way to win",
          "If you are ever bored try 'rm -rf .git*' in your Command Prompt",
          "Try pressing 'I c u p', what happens is nothing because they are unassigned keys",
          "The Void represents the emptiness of space, an issue you can easily over by buying extra space on your Google Drive",
          "The Void will listen to you if you stand your ground and make yourself look bigger, or wait was that for a bear?",
          "To survive, try running away from the chasm known as the void, or don't",
          "We guarantee you will have fun, whether its due to playing this game or not is up to you",
          "Mamamia: System 32 was replaced with Mario Odyssey",
          "You are currently on a loading screen, to exit this screen please wait for the game to finish loading.",
          " If you like this game then your will love: Runtime, Runtime, and Runtime",
          "To increase your concentration try drinking some Methyl Benzoylecgonine",
          "Reviewed: 11/10 Starts Comment: 'Yet to try'",
          "Tired of Losing? 1) Don't Die 2) Keep on Moving 3) Just Win",
          "You can lose if you try to use an item",
          "You probably should start making some coffee, you'll be here for a while",
          "You should try winning more",
          "Money can't buy victory (unless you send $100 to 8795 8265 by midnight)",
          "Collect the Clock to stop time and your chance at winning",
          "If you win, you're just playing wrong",
          "Press W, A,S, D to move or use the Arrow Keys",
        };
    }

    @Override
    public void create() {
        super.create();
        addActors();
       // playTheMusic();
    }
  /*  public void playTheMusic() {
        MusicSingleton music = MusicSingleton.getInstance();
        music.playMusicSingleton("sounds/MainMenuMusic.mp3");
    }*/
    public String randomMessage() {
        /* Modified with changes from Stacksoverflow:
        https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java*/
        int maxaximum = 20;
        int minimum = 1;
        int randomNum = rand.nextInt((maxaximum - minimum) + 1) + minimum;
        return terms[randomNum];
    }
    /**
     * Added Background image and initialised buttons
     */
    private void addActors() {
        /* Docs on the stage use: https://www.tabnine.com/code/java/classes/com.badlogic.gdx.scenes.scene2d.ui.Label$LabelStyle */
        Texture runtimeTitleTexture = new Texture(Gdx.files.internal("ui-elements/runtime-title.png"));
        Drawable runtimeTitleDrawable = new TextureRegionDrawable(new TextureRegion(runtimeTitleTexture));
        ImageButton runtimeTitle = new ImageButton(runtimeTitleDrawable);
        String loadingText2 = randomMessage();
        Label label;
        Table table = new Table();
        table.setFillParent(true);
        label = new Label(loadingText2, skin);
        label.setFontScale(2f);
        BitmapFont font = new BitmapFont();
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        label.setStyle(labelStyle);
        label.setAlignment(Align.center);

       // TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(new TextureRegion(bground));
        Sprite sprite = new Sprite(new Texture("ui-elements/loading_screen_background.png"));
        table.setBackground(new SpriteDrawable(sprite));
        loadingText = "Game is loading! Enjoy";
        table.add(label).center().width(100).height(100);;
       // table.addActor(runtimeTitle);
        stage.addActor(table);
        //stage.addActor(label);
      //  stage.addActor(textureRegionDrawable);
       // stage.addActor(runtimeTitle);
    }






    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
