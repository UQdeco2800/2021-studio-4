package com.deco2800.game.areas.terrain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.deco2800.game.services.ServiceLocator;

/**
 * Class containing details for generating tiles, including what actions they can perform (ex, whether they can be
 * rotated)
 */
public enum TerrainTileDefinition {
  TILE_FULL_MIDDLE("mapTextures_Middle-Platform", false),
  TILE_FULL_TOP("mapTextures_Platforms", true),
  TILE_HALF_TOP("mapTextures_Middle-Platform", true),
  TILE_HALF_BOTTOM("mapTextures_Middle-Platform", true);

  public static final int TILE_X = 500;
  public static final int TILE_Y = 500;

  TerrainTileDefinition(String regionName, boolean rotateable) {
    TextureAtlas atlas = ServiceLocator.getResourceService()
      .getAsset("map-spritesheets/mapTextures.atlas", TextureAtlas.class);
    System.out.println(atlas.getRegions());
    this.textureRegion = atlas.findRegion(regionName);
    this.rotateable = rotateable;
  }

  private final TextureRegion textureRegion;
  private final boolean rotateable;

  public Sprite getSprite() {
    return new Sprite(this.textureRegion);
  }

  public boolean isRotateable() {
    return rotateable;
  }
}
