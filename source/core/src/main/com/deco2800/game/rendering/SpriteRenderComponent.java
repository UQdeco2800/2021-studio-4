package com.deco2800.game.rendering;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.services.ServiceLocator;

/** Render a static texture. */
public class SpriteRenderComponent extends RenderComponent {
  private Sprite sprite;

  /**
   * @param spritePath internal path of sprite to render. Will be scaled to the entity's
   *     scale.
   */
  public SpriteRenderComponent(String spritePath) {
    this(ServiceLocator.getResourceService().getAsset(spritePath, Sprite.class));
  }

  /** @param sprite Sprite to render. Will be scaled to the entity's scale. */
  public SpriteRenderComponent(Sprite sprite) {
    this.sprite = sprite;
  }

  /** Scale the entity to a width of 1 and a height matching the texture's ratio */
  public void scaleEntity() {
    entity.setScale(1f, sprite.getHeight() / sprite.getWidth());
  }

  /**
   * Rotate the sprite by a set number of degrees
   * @param degrees degrees to rotate
   */
  public void setRotation(float degrees) {
    sprite.setRotation(degrees);
  }

  /**
   * Rotate the sprite by 90 degrees clockwise
   */
  public void rotate90() {
    sprite.rotate(90);
  }

  /**
   * Flip sprite in the X direction
   */
  public void flipX() {
    sprite.setFlip(!sprite.isFlipX(), sprite.isFlipY());
  }

  /**
   * Flip sprite in the Y direction
   */
  public void flipY() {
    sprite.setFlip(sprite.isFlipX(), !sprite.isFlipY());
  }

  public boolean getFlipX() {
    return sprite.isFlipX();
  }

  public boolean getFlipY() {
    return sprite.isFlipY();
  }

  /**
   * Get sprites rotation amount
   * @return the rotation in degrees
   */
  public float getRotation() {
    return sprite.getRotation();
  }

  public void setSprite(Sprite sprite) {
    this.sprite = sprite;
  }

  @Override
  public void draw(SpriteBatch batch) {
    Vector2 position = entity.getPosition();
    Vector2 scale = entity.getScale();

    float num90 = (sprite.getRotation() % 360) / 90;

    // If rotated 1 or 2 times
    float posDeltaX = num90 > 0  && num90 <= 2 ? 0.5f : 0;
    // If rotated 2 or 3 times
    float posDeltaY = num90 >= 2 ? 0.5f : 0;

    batch.draw(sprite,
      position.x + posDeltaX, position.y + posDeltaY,
      0, 0,
      1, 1,
      scale.x, scale.y,
      sprite.getRotation());
  }
}
