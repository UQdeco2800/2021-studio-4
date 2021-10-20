package com.deco2800.game.areas.terrain;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.deco2800.game.services.ServiceLocator;

/**
 * Class containing details for generating tiles, including what actions they can perform (ex, whether they can be
 * rotated)
 */
public enum TerrainTileDefinition {
  TILE_FULL_MIDDLE("middlePlatform", false, true),
  TILE_FULL_TOP("platform", true, true),
  TILE_HALF_TOP("halfTop", true, true),
  TILE_HALF_BOTTOM("halfBottom", true, true);

  public static final int TILE_X = 500;
  public static final int TILE_Y = 500;

  TerrainTileDefinition(String regionName, boolean rotateable, boolean flipable) {
    this.regionName = regionName;
    this.rotateable = rotateable;
    this.flipable = flipable;
  }

  private final String regionName;
  private final boolean rotateable;
  private final boolean flipable;

  private TextureAtlas atlas;

  public void setAtlas(TextureAtlas atlas) {
    this.atlas = atlas;
  }

  public Sprite getSprite() {
    TextureRegion textureRegion = atlas.findRegion(regionName);

    return new Sprite(textureRegion);
  }

  public boolean isRotateable() {
    return rotateable;
  }

  public boolean isFlipable() {
    return flipable;
  }
}
