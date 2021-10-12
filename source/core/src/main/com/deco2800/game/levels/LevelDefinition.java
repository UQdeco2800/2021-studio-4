package com.deco2800.game.levels;

public enum LevelDefinition {
  LEVEL_1("Level 1","levels/level1.json", "map-spritesheets/level1Textures.atlas"),
  LEVEL_2("Level 2","levels/level2.json", "map-spritesheets/level2Textures.atlas"),
  LEVEL_3("Level 3","levels/level3.json", "map-spritesheets/level3Textures.atlas"),
  LEVEL_4("Level 4","levels/level4.json", "map-spritesheets/level4Textures.atlas");

  private final String name;
  private final String levelFileName;
  private final String levelAtlasName;

  LevelDefinition(String name, String levelFileName, String levelAtlasName) {
    this.name = name;
    this.levelFileName = levelFileName;
    this.levelAtlasName = levelAtlasName;
  }

  public String getName() {
    return name;
  }

  public String getLevelAtlasName() {
    return levelAtlasName;
  }

  public String getLevelFileName() {
    return levelFileName;
  }

}
