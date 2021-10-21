package com.deco2800.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.deco2800.game.files.LevelFile;

import java.util.ArrayList;

public class LevelInfo {
  private final String name;
  private final String musicPath;

  private String levelFileName;

  /**
   * Creates a blank level without a level file name. If this is used, readLevelFile will return an empty LevelFile
   * and getLevelFileName will return null. You can use setLevelFileName to set the level file.
   * @param name Name of the level
   * @param musicPath Path of the music
   */
  public LevelInfo(String name, String musicPath) {
    this(name, musicPath, null);
  }

  /**
   * Creates a level with a pre-specified level file name. readLevelFile will read that specific file and
   * getLevelFileName will return the specified level file name. setLevelFileName can still be used to change the
   * level file name.
   * @param name
   * @param musicPath
   * @param levelFileName
   */
  public LevelInfo(String name, String musicPath, String levelFileName) {
    this.name = name;
    this.levelFileName = levelFileName;
    this.musicPath = musicPath;
  }

  public String getName() {
    return name;
  }

  public String getLevelFileName() {
    return levelFileName;
  }

  public void setLevelFileName(String levelFileName) {
    this.levelFileName = levelFileName;
  }

  public String getMusicPath() {
    return musicPath;
  }

  public LevelFile readLevelFile() {
    // If the level file name is specified
    if (getLevelFileName() != null) {
      Json json = new Json();

      FileHandle file = Gdx.files.local(getLevelFileName());
      assert file != null;

      return json.fromJson(LevelFile.class, file);
    } else { // No level file name specified, create a blank level
      LevelFile levelFile = new LevelFile();

      levelFile.levelTexture = LevelTexture.LEVEL_ONE;

      levelFile.obstacles = new LevelFile.Obstacles();
      levelFile.obstacles.obstacleEntities = new ArrayList<>();

      levelFile.terrain = new LevelFile.Terrain();
      levelFile.terrain.mapLayer = new LevelFile.TileLayerData();
      levelFile.terrain.mapLayer.width = 1000;
      return levelFile;
    }
  }
}
