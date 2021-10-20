package com.deco2800.game.rendering;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.services.ServiceLocator;

/** Render a static background texture, similar to TextureRenderComponent however image is at z=0 and is scaled to fit
 * the screen
 */
public class BackgroundRenderComponent extends RenderComponent {
  private Texture texture;
  private final String texturePath;




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
    // Camera lock is removed
    // Get actual viewport width and height, not screen width and height (not always fullscreen)
    float screenWidth = cam.viewportWidth*3.5f;
    //float screenHeight = cam.viewportHeight;
    float screenHeight = cam.viewportHeight*1.3f;


    // Draw texture
    batch.draw(texture, -4, -2, screenWidth * 1.5f, screenHeight * 1.5f);


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
