package com.deco2800.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * A class designed to minimise code duplication when creating imageButtons across screens.
 * Note: The Listener will still need to be call in the respective class that this
 * method has been called from
 */
public class InsertImageButton {

    /**
     * Creates the buttonsImage of the
     * @param texturePathName - the texture for the button
     * @param hoverTexturePathName the texture for the button when hovered over
     * @param posX - X-Coord on the screen
     * @param posY - Y-Coord on the screen
     * @param Width - Width of the button
     * @param height - Height of the button
     * @return ImageButton: The button to be displayed
     */
    public ImageButton setImage(String texturePathName, String hoverTexturePathName,
                                int posX, int posY, int Width, int height) {

        Texture mainTexture = new Texture(Gdx.files.internal(texturePathName));
        Texture hoverTexture = new Texture(Gdx.files.internal(hoverTexturePathName));
        Drawable drawing = new TextureRegionDrawable(new TextureRegion(mainTexture));
        ImageButton buttonImage = new ImageButton(drawing);
        buttonImage.getStyle().imageOver = new TextureRegionDrawable(hoverTexture);

        buttonImage.setBounds((posX),(posY),
                Width, height);

        return buttonImage;
    }

    public Table setTable(String backgroundImage) {
        Table table = new Table();
        table.setFillParent(true);
        Sprite sprite = new Sprite(new Texture(backgroundImage));
        table.setBackground(new SpriteDrawable(sprite)); // Set background
        return table;
    }
}
