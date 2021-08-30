package com.deco2800.game.areas.terrain;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;

/**
 * Custom terrain tile implementation for tiled map terrain that stores additional properties we
 * may want to have in the game, such as audio, walking speed, traversability by AI, etc.
 */
public class TerrainTile implements TiledMapTile {
  private int id;
  private BlendMode blendMode = BlendMode.ALPHA;
  private TerrainTileDefinition definition;
  private Sprite sprite;
  private float offsetX;
  private float offsetY;

  public TerrainTile(TerrainTileDefinition definition) {
    this(definition, 0);
  }

  public TerrainTile(TerrainTileDefinition definition, int rotation) {
    this.definition = definition;
    this.sprite = definition.getSprite();

    if (definition.isRotateable()) {
      this.sprite.setRotation(rotation);
    }
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public BlendMode getBlendMode() {
    return blendMode;
  }

  @Override
  public void setBlendMode(BlendMode blendMode) {
    this.blendMode = blendMode;
  }

  @Override
  public TextureRegion getTextureRegion() {
    return sprite;
  }

  @Override
  public void setTextureRegion(TextureRegion textureRegion) {
    this.sprite = (Sprite)textureRegion;
  }

  public Sprite getSprite() {
    return sprite;
  }

  public void setSprite(Sprite sprite) {
    this.sprite = sprite;
  }

  @Override
  public float getOffsetX() {
    return offsetX;
  }

  @Override
  public void setOffsetX(float offsetX) {
    this.offsetX = offsetX;
  }

  @Override
  public float getOffsetY() {
    return offsetY;
  }

  @Override
  public void setOffsetY(float offsetY) {
    this.offsetY = offsetY;
  }

  /**
   * Not required for game, unimplemented
   * @return null
   */
  @Override
  public MapProperties getProperties() {
    return null;
  }

  /**
   * Not required for game, unimplemented
   * @return null
   */
  @Override
  public MapObjects getObjects() {
    return null;
  }
}
