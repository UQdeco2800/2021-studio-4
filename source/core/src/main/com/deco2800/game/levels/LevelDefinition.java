package com.deco2800.game.levels;

public enum LevelDefinition {
  LEVEL_1("Level 1","levels/level1.json"),
  LEVEL_2("Level 2","levels/level2.json"),
  LEVEL_3("Level 3","levels/level3.json"),
  LEVEL_4("Level 4","levels/level4.json");

  private final String name;
  private final String levelFileName;

  LevelDefinition(String name, String levelFileName) {
    this.name = name;
    this.levelFileName = levelFileName;
  }

  public String getName() {
    return name;
  }

  public String getLevelFileName() {
    return levelFileName;
  }

}
