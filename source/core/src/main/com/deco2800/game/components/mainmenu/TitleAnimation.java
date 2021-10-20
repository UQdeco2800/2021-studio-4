package com.deco2800.game.components.mainmenu;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


public class TitleAnimation extends Image {

    public TitleAnimation(Texture texture, float currentWidth, float currentHeight,
                          float centreWidth, float centreHeight, float duration) {
        super(texture);
        // Set origin of the image.
        this.setOrigin(currentWidth/2, currentHeight/2);

        // These are magic numbers as the centre width equations does not centre the image
        // nicely.
        float scale = 25f;
        setBounds(centreWidth,centreHeight,currentWidth,currentHeight);

        ScaleByAction scaleImage = new ScaleByAction();
        scaleImage.setAmount(scale);
        scaleImage.setDuration(duration);

        // Rotates the runtime logo. Does not work at the moment because the rotation rotates
        // the image from the bottom left corner. As a result, when rotating the image is not moving.

        RotateToAction rotateImage = new RotateToAction();
        rotateImage.setRotation(360f * 4f);
        rotateImage.setDuration(duration);

        ParallelAction imageActions = new ParallelAction(scaleImage, rotateImage);
        TitleAnimation.this.addAction(imageActions);
    }
}
