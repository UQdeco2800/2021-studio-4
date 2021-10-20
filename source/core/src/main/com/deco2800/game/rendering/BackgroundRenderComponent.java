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
    // Camera lock is removed
    // Get actual viewport width and height, not screen width and height (not always fullscreen)
    float screenWidth = cam.viewportWidth*3.5f;
    //float screenHeight = cam.viewportHeight;
    float screenHeight = cam.viewportHeight*1.3f;


    // Draw texture
    //batch.draw(texture, screenWidth/6, screenHeight/6, screenWidth, screenHeight);
    //batch.begin();
    batch.draw(texture, -4, -2, screenWidth * 1.5f, screenHeight * 1.5f);
    //batch.draw(texture, -screenWidth/10, -screenHeight/10, screenWidth, screenHeight);
    //batch.end();


    /*sprite = new Sprite(texture);
    sprite.setPosition(Gdx.graphics.getWidth()/2 - texture.getWidth()/2,
            Gdx.graphics.getHeight()/2 - texture.getHeight()/2);
    //sprite.setRotation(90f);
    sprite.setScale(0.3f);//scale down by 30%*/

  /*
  // The background must follow the camera
    Camera cam = ServiceLocator.getCamera().getCamera();
    Vector3 position = cam.position;

    // Get actual viewport width and height, not screen width and height (not always fullscreen)
    float screenWidth = cam.viewportWidth;
    float screenHeight = cam.viewportHeight;

    // Draw texture
    batch.draw(texture, position.x-(screenWidth/2), position.y-(screenHeight/2), screenWidth, screenHeight);*/
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
