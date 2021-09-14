package com.deco2800.game.areas.terrain;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.deco2800.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.services.ServiceLocator;

import java.util.ArrayList;

/** Factory for creating game terrains. */
public class TerrainFactory {
  private static final TerrainOrientation ORIENTATION = TerrainOrientation.ORTHOGONAL;
  private static final float TILE_SIZE = 0.5f;

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
    OrthoCachedTiledMapRenderer renderer = new OrthoCachedTiledMapRenderer(tiledMap, TILE_SIZE/tilePixelSize.x);
    generateBodies(tiledMap);
    return new TerrainComponent(camera, tiledMap, renderer, ORIENTATION, TILE_SIZE);
  }

  /**
   * Since a tiled map is one single entity, we can't simply apply the collidable component to it to allow collisions
   * with the tiles.
   *
   * To handle collisions, this function generates Box2D Bodies for each solid tile within the map, then it generates
   * and creates appropriate fixtures for each of the bodies.
   *
   * Notes: Currently there is no good way to delete/change these bodies in the event of a map tile change. Additionally
   * this function would need to be further changed to consider the tile definition if, ie a corner tile was to have
   * the correct hitbox.
   * @param tiledMap The map which the boxes should be generated from.
   */
  public static void generateBodies(TiledMap tiledMap) {
    TiledMapTileLayer mapTileLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);

    for (int x = 0; x < mapTileLayer.getWidth(); x++) {
      for (int y = 0; y < mapTileLayer.getHeight(); y++) {
        Cell cell = mapTileLayer.getCell(x, y);

        if (cell != null) {
          // Create a rectangle at the location of the tile
          int twidth = mapTileLayer.getTileWidth(), theight =  mapTileLayer.getTileHeight();
          Rectangle rectangle = new Rectangle(x * TILE_SIZE, y * TILE_SIZE,  TILE_SIZE,  TILE_SIZE);

          // Create a body for this map object
          BodyDef bodyDef = new BodyDef();
          bodyDef.type = BodyDef.BodyType.StaticBody; // haha dynamic body makes it have gravity hahahahahahahaha
          bodyDef.fixedRotation = true;
          bodyDef.linearDamping = 5f;
          bodyDef.angle = 0f;
          bodyDef.active = true;
          Body body = ServiceLocator.getPhysicsService().getPhysics().createBody(bodyDef);

          // Create a shape for the fixture
          PolygonShape bbox = new PolygonShape();
          bbox.setAsBox(rectangle.width/2, rectangle.height/2);

          // Create a fixture for the body
          FixtureDef fixtureDef = new FixtureDef();
          fixtureDef.shape = bbox;
          fixtureDef.filter.categoryBits = PhysicsLayer.TILE;
          fixtureDef.filter.groupIndex = 0;
          body.createFixture(fixtureDef);

          // Set body position
          Vector2 center = new Vector2();
          rectangle.getCenter(center);
          body.setTransform(center,0);

          // Dispose of unneeded shape
          bbox.dispose();
        }
      }
    }
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
    tiles.add(new TerrainTile(TerrainTileDefinition.TILE_FULL_TOP, 90, false, false));
    tiles.add(new TerrainTile(TerrainTileDefinition.TILE_HALF_BOTTOM));
    tiles.add(new TerrainTile(TerrainTileDefinition.TILE_HALF_BOTTOM, 90, false, false));
    tiles.add(new TerrainTile(TerrainTileDefinition.TILE_HALF_TOP));



    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  public static void loadTilesFromFile(TiledMapTileLayer layer, TerrainTileDefinition definition, int rotation, int x, int y) {
    TerrainTile tile = new TerrainTile(definition,rotation,false,false);
    Cell cell = tile.generateCell();
    layer.setCell(x,y,cell);
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
