package com.deco2800.game.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.deco2800.game.services.ServiceLocator;

/** Render a static background texture, similar to TextureRenderComponent however image is at z=0 and is scaled to fit
 * the screen
 */
public class BackgroundRenderComponent extends RenderComponent {
  private Texture texture;
  private String texturePath;




  public BackgroundRenderComponent(String texturePath) {
    this.texturePath = texturePath;
  }

  @Override
  public void create() {
    super.create();
    texture = ServiceLocator.getResourceService().getAsset(texturePath, Texture.class);


  }
  @Override
  public void draw(SpriteBatch batch) {
    // The background must follow the camera
    Camera cam = ServiceLocator.getCamera().getCamera();
    Vector3 position = cam.position;
    // Camera lock is removed
    // Get actual viewport width and height, not screen width and height (not always fullscreen)
    float screenWidth = cam.viewportWidth;
    float screenHeight = cam.viewportHeight;
    /* Adjust the background screen based on the level*/
    if (texturePath.equals("backgrounds/background_level1.png")) {
      batch.draw(texture, -3, -1, screenWidth * 14f, screenHeight * 1.9f);
    } else if (texturePath.equals("backgrounds/background_level2.png")){
      batch.draw(texture, -3, -2, screenWidth *10f, screenHeight * 2f);
    } else if (texturePath.equals("backgrounds/background_level3.png")) {
      batch.draw(texture, -3, -3, screenWidth *10f, screenHeight * 2f);
    } else if (texturePath.equals("backgrounds/background_level4.png")) {
      batch.draw(texture, -3, -1, screenWidth * 5f, screenHeight * 1.9f);
    } else {
      System.out.println("Not a valid texturePath");
      //default
      batch.draw(texture, -3, -2, screenWidth *10f, screenHeight * 2f);
    }


  }


  @Override
  public int getLayer() {
    return 0;
  }

  @Override
  public float getZIndex() {
    return 0;
  }



}
