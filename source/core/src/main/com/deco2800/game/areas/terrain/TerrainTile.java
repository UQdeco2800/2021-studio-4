package com.deco2800.game.areas.terrain;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

/**
 * Custom terrain tile implementation for tiled map terrain that stores additional properties we
 * may want to have in the game, such as audio, walking speed, traversability by AI, etc.
 */
public class TerrainTile implements TiledMapTile{
  private int id;
  private BlendMode blendMode = BlendMode.ALPHA;
  private Sprite sprite;
  private float offsetX;
  private float offsetY;

  public TerrainTileDefinition definition;
  public boolean flipX;
  public boolean flipY;
  public int rotation;

  public TerrainTile(TerrainTileDefinition definition) {
    this(definition, 0, false, false);
  }

  public TerrainTile(TerrainTileDefinition definition, int rotation, boolean flipX, boolean flipY) {
    this.definition = definition;
    sprite = definition.getSprite();

    this.rotation = rotation;
    this.flipX = flipX;
    this.flipY = flipY;
  }

  /**
   * Generate a cell to this TerrainTile's cell definitions. Includes setting texture, rotation and flipX/Y
   * @return the generated cell
   */
  public Cell generateCell(){
    Cell cell = new Cell();

    cell.setTile(this);

    if (definition.isRotateable()) {
      cell.setRotation(this.rotation / 90);
    }

    if (definition.isFlipable()) {
      cell.setFlipHorizontally(flipX);
      cell.setFlipVertically(flipY);
    }

    return cell;
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
    return getSprite();
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

  public String serialize(int x, int y) {
    // X -> x coordinate
    // Y -> y coordinate
    return String.format("%s:%s:%s:%s\n",this.definition,this.rotation,x,y);
  }
}
