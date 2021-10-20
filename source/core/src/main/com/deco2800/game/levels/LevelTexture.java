package com.deco2800.game.levels;

public enum LevelTexture {
  LEVEL_ONE("map-spritesheets/level1Textures.atlas");

  private final String atlasName;

  LevelTexture(String atlasName){
    this.atlasName = atlasName;
  }

  public String getAtlasName() {
    return atlasName;
  }
}
