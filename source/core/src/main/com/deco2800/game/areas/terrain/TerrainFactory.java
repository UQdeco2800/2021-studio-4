package com.deco2800.game.areas.terrain;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.deco2800.game.components.CameraComponent;

import java.util.ArrayList;

/** Factory for creating game terrains. */
public class TerrainFactory {
  private static final TerrainOrientation ORIENTATION = TerrainOrientation.ORTHOGONAL;

  private final OrthographicCamera camera;

  /**
   * Create a terrain factory.
   *
   * @param cameraComponent Camera to render terrains to. Must be orthographic.
   */
  public TerrainFactory(CameraComponent cameraComponent) {
    this.camera = (OrthographicCamera) cameraComponent.getCamera();
  }

  /**
   * Generates the terraincomponent which will contain the terrain data
   * @return
   */
  public TerrainComponent createTerrain() {
    GridPoint2 tilePixelSize = new GridPoint2(TerrainTileDefinition.TILE_X, TerrainTileDefinition.TILE_Y);
    TiledMap tiledMap = loadTiles(tilePixelSize);
    TiledMapRenderer renderer = new OrthogonalTiledMapRenderer(tiledMap, 0.5f/tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, ORIENTATION, 0.5f);
  }

  /**
   * Loads in the tiles, places them on the tile layer and returnes a tiledmap
   * @param tileSize
   * @return
   */
  private TiledMap loadTiles(GridPoint2 tileSize) {
    // These values determine the size of the base layer, these could be calculated by taking the largest coordinates
    // from the map save, or could be saved with the map
    int map_size_x = 100;
    int map_size_y = 40;

    TiledMap tiledMap = new TiledMap();
    TiledMapTileLayer layer = new TiledMapTileLayer(map_size_x, map_size_y, tileSize.x, tileSize.y);

    // NOTE: THIS IS A PLACEHOLDER
    // This should be dynamically loaded from the save file.
    // note to self - could use this to load definitions: https://stackoverflow.com/questions/604424/how-to-get-an-enum-value-from-a-string-value-in-java
    ArrayList<TerrainTile> tiles = new ArrayList<>();

    tiles.add(new TerrainTile(TerrainTileDefinition.TILE_FULL_MIDDLE));
    tiles.add(new TerrainTile(TerrainTileDefinition.TILE_FULL_TOP));
    tiles.add(new TerrainTile(TerrainTileDefinition.TILE_FULL_TOP, 90));
    tiles.add(new TerrainTile(TerrainTileDefinition.TILE_HALF_BOTTOM));
    tiles.add(new TerrainTile(TerrainTileDefinition.TILE_HALF_BOTTOM, 90));
    tiles.add(new TerrainTile(TerrainTileDefinition.TILE_HALF_TOP));

    int x = 0;
    for (TerrainTile tile : tiles) {
      Cell cell = new Cell();
      cell.setTile(tile);
      layer.setCell(x, 20, cell);

      x++;
    }

    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  private static void fillTiles(TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile) {
    for (int x = 0; x < mapSize.x; x++) {
      for (int y = 0; y < mapSize.y; y++) {
        Cell cell = new Cell();
        cell.setTile(tile);
        layer.setCell(x, y, cell);
      }
    }
  }

  /**
   * This enum should contain the different terrains in your game, e.g. forest, cave, home, all with
   * the same oerientation. But for demonstration purposes, the base code has the same level in 3
   * different orientations.
   */
  public enum TerrainType {
    FOREST_DEMO,
    FOREST_DEMO_ISO,
    FOREST_DEMO_HEX
  }
}
