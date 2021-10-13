package com.deco2800.game.levels;

public enum LevelTexture {
  LEVEL_ONE("map-spritesheets/mapTextures.atlas"),
  LEVEL_TWO("map-spritesheets/level2textures.atlas"),
  LEVEL_THREE("map-spritesheets/level3Textures.atlas"),
  LEVEL_FOUR("map-spritesheets/level4Textures.atlas");

  private final String atlasName;

  LevelTexture(String atlasName){
    this.atlasName = atlasName;
  }

  public String getAtlasName() {
    return atlasName;
  }
}
