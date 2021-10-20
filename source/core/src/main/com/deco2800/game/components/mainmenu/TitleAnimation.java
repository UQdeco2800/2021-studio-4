package com.deco2800.game.components.mainmenu;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


public class TitleAnimation extends Image {

    public TitleAnimation(Texture texture, float currentWidth, float currentHeight,
                          float centreWidth, float centreHeight, float duration) {
        super(texture);

        // These are magic numbers as the centre width equations does not centre the image
        // nicely.
        float scale = 25f;
        //setBounds(300,375,width,height);
        setBounds(centreWidth,centreHeight,currentWidth,currentHeight);

        // Scales the runtime logo. The image is scaled from the bottom left corner,
        // meaning that image won't be centred as it is being scaled.
        ScaleByAction scaleImage = new ScaleByAction();
        scaleImage.setAmount(scale);
        scaleImage.setDuration(duration);

        // Moves the runtime logo while it is scaling in order to keep it centred.
        MoveByAction centreImage = new MoveByAction();
        centreImage.setAmount(-0.5f * scale * currentWidth, -0.5f * scale * currentHeight);
        centreImage.setDuration(duration);

        /*
        Rotates the runtime logo. Does not work at the moment because the rotation rotates
        the image from the bottom left corner. As a result, when rotating the image is not moving.
        */
//        RotateToAction rotateImage = new RotateToAction();
//        rotateImage.setRotation(360f);
//        rotateImage.setDuration(duration);

        /* Uncomment this and the RotateToAction above to activate the rotation effect */
        //ParallelAction pa = new ParallelAction(scaleImage,centreImage, rotateImage);
        // Runs both actions above.
        ParallelAction pa = new ParallelAction(scaleImage,centreImage);
        TitleAnimation.this.addAction(pa);
    }
}
