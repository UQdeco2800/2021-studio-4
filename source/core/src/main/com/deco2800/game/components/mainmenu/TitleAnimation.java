package com.deco2800.game.components.mainmenu;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


public class TitleAnimation extends Image {

    public TitleAnimation(Texture texture, float currentWidth, float currentHeight,
                          int centreWidth, int centreHeight, float duration) {
        super(texture);

        // These are magic numbers as the centre width equations does not centre the image
        // nicely.
        float width = 600;
        float height = 200;
        float scale = 2f;
        setBounds(300,375,width,height);
        //setBounds(centreWidth,centreHeight,currentWidth,currentHeight);

        if (centreWidth == 0 && centreHeight == 0) {
            centreWidth = (int) width;
            centreHeight = (int) height;
        }


        // Scales the runtime logo. The image is scaled from the bottom left corner,
        // meaning that image won't be centred as it is being scaled.
        ScaleByAction sba = new ScaleByAction();
        sba.setAmount(scale);
        sba.setDuration(duration);

        // Moves the runtime logo while it is scaling in order to keep it centred.
        MoveByAction mba = new MoveByAction();
        mba.setAmount(-1f * 0.5f * scale * width, -1f * 0.5f * scale * height);
        mba.setDuration(duration);

        // Runs both actions above.
        ParallelAction pa = new ParallelAction(sba,mba);
        TitleAnimation.this.addAction(pa);
    }
}
