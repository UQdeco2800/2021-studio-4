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
  TILE_FULL_MIDDLE("mapTextures_Middle-Platform", false, true),
  TILE_FULL_TOP("mapTextures_Platforms", true, true),
  TILE_HALF_TOP("mapTextures_Half-Top", true, true),
  TILE_HALF_BOTTOM("mapTextures_Half-Bottom", true, true);

  public static final int TILE_X = 500;
  public static final int TILE_Y = 500;

  TerrainTileDefinition(String regionName, boolean rotateable, boolean flipable) {
    TextureAtlas atlas = ServiceLocator.getResourceService()
      .getAsset("map-spritesheets/mapTextures.atlas", TextureAtlas.class);
    this.textureRegion = atlas.findRegion(regionName);
    this.rotateable = rotateable;
    this.flipable = flipable;
  }

  private final TextureRegion textureRegion;
  private final boolean rotateable;
  private final boolean flipable;

  public Sprite getSprite() {
    return new Sprite(this.textureRegion);
  }

  public boolean isRotateable() {
    return rotateable;
  }

  public boolean isFlipable() {
    return flipable;
  }
}
