package com.deco2800.game.files;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.deco2800.game.areas.terrain.TerrainTile;
import com.deco2800.game.areas.terrain.TerrainTileDefinition;
import com.deco2800.game.entities.ObstacleEntity;
import com.deco2800.game.levels.LevelTexture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelFile {
  public GridPoint2 playerStartPos;
  public Terrain terrain;
  public Obstacles obstacles;
  public LevelTexture levelTexture = LevelTexture.LEVEL_ONE;

  public static class PositionedTerrainTile implements Json.Serializable {
    public TerrainTile tile;
    public int x;
    public int y;

    private JsonValue rawTileData;

    public PositionedTerrainTile(){};

    public PositionedTerrainTile(TerrainTile tile, int x, int y) {
      this.tile = tile;
      this.x = x;
      this.y = y;
    }

    @Override
    public void write(Json json) {
      json.writeValue("x", this.x);
      json.writeValue("y", this.y);
      json.writeValue("def", this.tile.definition.toString());
      json.writeValue("rot", this.tile.rotation);
      json.writeValue("flipX", this.tile.flipX);
      json.writeValue("flipY", this.tile.flipY);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
      this.x = jsonData.getInt("x");
      this.y = jsonData.getInt("y");
      this.rawTileData = jsonData;
    }

    /**
     * This method generates the actual tile object, which must be done separately from loading the file
     * as the file contains the atlas, which is required for loading.
     *
     * @param atlas - the atlas
     */
    public void generateTile(TextureAtlas atlas){
      TerrainTileDefinition definition = TerrainTileDefinition.valueOf(rawTileData.getString("def"));
      definition.setAtlas(atlas);
      this.tile = new TerrainTile(definition);
      this.tile.rotation = rawTileData.getInt("rot");
      this.tile.flipY = rawTileData.getBoolean("flipY");
      this.tile.flipX = rawTileData.getBoolean("flipX");
    }
  }

  public static class TileLayerData {
    public List<PositionedTerrainTile> tiles = new ArrayList<>();
    public Integer width = 0;
    public Integer height = 40;
  }

  public static class Terrain {
    public TileLayerData mapLayer;
  }

  public static class Obstacles {
    public List<ObstacleEntity> obstacleEntities;
    public Map<Integer, List<Integer>> interactablesMap = new HashMap<Integer, List<Integer>>();
  }
}
