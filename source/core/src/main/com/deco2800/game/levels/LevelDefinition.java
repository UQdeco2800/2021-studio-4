package com.deco2800.game.levels;

/**
 * Contains info relating to the inbuilt levels. Any info not specifically required for the inbuilt levels are stored in
 * the relevant LevelInfo file
 */
public enum LevelDefinition {
  LEVEL_1(new LevelInfo("Level 1", "game_level_1","levels/level1.json"), "level-1.png", "level-1-hovered.png"),
  LEVEL_2(new LevelInfo("Level 2", "level_2","levels/level2.json"), "level-2.png", "level-2-hovered.png"),
  LEVEL_3(new LevelInfo("Level 3", "level_3","levels/level3.json"), "level-3.png", "level-3-hovered.png"),
  LEVEL_4(new LevelInfo("Level 4", "level_1_2","levels/level4.json"), "level-4.png", "level-4-hovered.png");

  public static final String LEVEL_UI_ELEMENT_DIR = "ui-elements/levels-screen-buttons/";

  private final LevelInfo levelInfo;
  private final String menuButtonName;
  private final String menuButtonHoverName;

  LevelDefinition(LevelInfo levelInfo, String menuButtonName, String menuButtonHoverName) {
    this.levelInfo = levelInfo;
    this.menuButtonName = menuButtonName;
    this.menuButtonHoverName = menuButtonHoverName;
  }

  public LevelInfo getLevelInfo() {
    return levelInfo;
  }

  public String getMenuButtonName() {
    return LEVEL_UI_ELEMENT_DIR + menuButtonName;
  }

  public String getMenuButtonHoverName() {
    return LEVEL_UI_ELEMENT_DIR + menuButtonHoverName;
  }
}
