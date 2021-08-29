package com.deco2800.game.rendering;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
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

    // Get actual viewport width and height, not screen width and height (not always fullscreen)
    float screenWidth = cam.viewportWidth;
    float screenHeight = cam.viewportHeight;

    // Draw texture
    batch.draw(texture, position.x-(screenWidth/2), position.y-(screenHeight/2), screenWidth, screenHeight);
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
