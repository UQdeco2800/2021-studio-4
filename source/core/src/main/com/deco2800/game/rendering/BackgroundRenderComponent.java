package com.deco2800.game.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.services.ServiceLocator;

/** Render a static background texture, similar to TextureRenderComponent however image is at z=0 and is scaled to fit
 * the screen
 */
public class BackgroundRenderComponent extends RenderComponent {
  private final Texture texture;

  public BackgroundRenderComponent(String texturePath) {
    this(ServiceLocator.getResourceService().getAsset(texturePath, Texture.class));
  }

  /** @param texture Static texture to render. Will be scaled to the entity's scale. */
  public BackgroundRenderComponent(Texture texture) {
    this.texture = texture;
  }

  @Override
  public void draw(SpriteBatch batch) {
    batch.draw(texture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
