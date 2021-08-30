package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainTile;
import com.deco2800.game.areas.terrain.TerrainTileDefinition;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.BackgroundRenderComponent;
import com.deco2800.game.utils.math.RandomUtils;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class LevelGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(LevelGameArea.class);
  private static final int NUM_TREES = 7;
  private static final int NUM_GHOSTS = 2;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);
  private static final float WALL_WIDTH = 0.1f;
  private static final String[] gameTextures = {
    "images/box_boy_leaf.png",
    "images/tree.png",
    "images/ghost_king.png",
    "images/ghost_1.png",
    "images/grass_1.png",
    "images/grass_2.png",
    "images/grass_3.png",
    "images/hex_grass_1.png",
    "images/hex_grass_2.png",
    "images/hex_grass_3.png",
    "images/iso_grass_1.png",
    "images/iso_grass_2.png",
    "images/iso_grass_3.png",
    "images/the_void.png",
    "images/basicenemysprite.png",
    "images/chasingenemy.png",
    "images/enemyspritehsee.png",
    "images/game_background.png",
    "map-textures/mapTextures_Platforms.png",
    "map-textures/mapTextures_Middle-Platform.png",
    "map-textures/mapTextures_Button-On.png",
    "map-textures/mapTextures_bridge.png",
    "map-textures/mapTextures_door.png"
    
  };
  private static final String[] gameTextureAtlases = {
    "images/terrain_iso_grass.atlas",
    "images/ghost.atlas",
    "images/ghostKing.atlas",
    "images/the_void.atlas",
    "images/testingenemy.atlas",
    "map-spritesheets/mapTextures.atlas",
  };
  private static final String[] gameSounds = {"sounds/Impact4.ogg"};
  private static final String backgroundMusic = "sounds/BackingMusicWithDrums.mp3";
  private static final String[] gameMusic = {backgroundMusic};

  private final TerrainFactory terrainFactory;

  private Entity player;

  public LevelGameArea(TerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;
  }

  /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
  @Override
  public void create() {
    loadAssets();

    displayBackground(); // Display background at the back first
    displayUI();

    spawnTerrain();
    //spawnTrees();
    spawnLevel();
    player = spawnPlayer();
//    spawnGhosts();
//    spawnGhostKing();
//    spawnTheVoid();

    playMusic();
  }

  private void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay("Box Forest"));
    spawnEntity(ui);
  }

  private void displayBackground() {
    Entity background = new Entity();
    background.addComponent(new BackgroundRenderComponent("images/game_background.png"));
    spawnEntity(background);
  }


  public static Shape getShapeFromRectangle(Rectangle rectangle){
    PolygonShape polygonShape = new PolygonShape();
    polygonShape.setAsBox(rectangle.width*0.5F / TerrainTileDefinition.TILE_X,rectangle.height*0.5F/ TerrainTileDefinition.TILE_Y);
    return polygonShape;
  }

  public static Vector2 getTransformedCenterForRectangle(Rectangle rectangle){
    Vector2 center = new Vector2();
    rectangle.getCenter(center);
    return center.scl(1/TerrainTileDefinition.TILE_X);
  }

  private void spawnTerrain() {
    // Generate terrain
    terrain = terrainFactory.createTerrain();
    MapObjects objects = terrain.getMap().getLayers().get(0).getObjects();

    for (MapObject object : objects) {
      Rectangle rectangle = ((RectangleMapObject)object).getRectangle();

      //create a dynamic within the world body (also can be KinematicBody or StaticBody
      BodyDef bodyDef = new BodyDef();
      bodyDef.type = BodyDef.BodyType.DynamicBody;
      Body body = ServiceLocator.getPhysicsService().getPhysics().createBody(bodyDef);

      Fixture fixture = body.createFixture(getShapeFromRectangle(rectangle),1);

      body.setTransform(getTransformedCenterForRectangle(rectangle),0);
    }

    Entity terrainEntity = new Entity();
    terrainEntity
      .addComponent(terrain)
      .addComponent(new PhysicsComponent())
      .addComponent(new ColliderComponent().setLayer(PhysicsLayer.ALL));

    terrainEntity.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);

    spawnEntity(terrainEntity);
  }

  private void spawnTrees() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_TREES; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity tree = ObstacleFactory.createTree();
      spawnEntityAt(tree, randomPos, true, false);
    }
  }

  private void spawnPlatform(int posX, int posY, int blocks) {
    Entity platform = ObstacleFactory.createPlatform(blocks);
    GridPoint2 position = new GridPoint2(posX,posY);
    spawnEntityAt(platform, position, false, true);
  }

  private void spawnMiddlePlatform(int posX, int posY, int blocks) {
    Entity middlePlatform = ObstacleFactory.createMiddlePlatform(blocks);
    GridPoint2 position = new GridPoint2(posX,posY);
    spawnEntityAt(middlePlatform, position, true, false);
  }

  private void spawnButton(int posX, int posY) {
    Entity button = ObstacleFactory.createButton();
    GridPoint2 position = new GridPoint2(posX,posY);
    spawnEntityAt(button, position, false, true);
  }

  private void spawnBridge(int posX, int posY, int blocks) {
    Entity bridge = ObstacleFactory.createBridge(blocks);
    GridPoint2 position = new GridPoint2(posX,posY);
    spawnEntityAt(bridge, position, false, true);
  }

  private void spawnDoor(int posX, int posY) {
    Entity door = ObstacleFactory.createDoor();
    GridPoint2 position = new GridPoint2(posX,posY);
    spawnEntityAt(door, position, false, true);
  }

  private void spawnLevel() {
    int c;
    int i;
    for (c = 0; c < 4; c++) {
      spawnPlatform(c*8,c+5,8);
      spawnMiddlePlatform(c*8+8,c+5,1);
    }
    int x = 32;
    int y = 8;
    spawnLadderPlatforms(x,y);
    spawnPlatform(32,22,8);
    spawnMiddlePlatform(39,30,2);
    spawnPlatform(40,14,8);
    for (i = 0; i < 8; i++) {
      spawnMiddlePlatform(39,22-i-1,1);
    }
    spawnButton(40,15);
    spawnBridge(48,14,8);
    spawnPlatform(56,14,6);
    spawnDoor(58,15);
  }

  private void spawnLadderPlatforms(int x, int y) {
    for (int i = 0; i < 13; i++) {
      spawnMiddlePlatform(x,y+1+i,1);
      spawnMiddlePlatform(x-9,y+6+i,1);
    }
    spawnPlatform(x-3,y+3,3);
    spawnPlatform(x-8,y+6,3);
    spawnPlatform(x-3,y+9,3);
    spawnPlatform(x-8,y+12,3);
  }

  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer();
    spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    return newPlayer;
  }

  private void spawnGhosts() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_GHOSTS; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity ghost = NPCFactory.createGhost(player);
      spawnEntityAt(ghost, randomPos, true, true);
    }
  }

  private void spawnGhostKing() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    Entity ghostKing = NPCFactory.createGhostKing(player);
    spawnEntityAt(ghostKing, randomPos, true, true);
  }

  private void spawnTheVoid() {
    int startPosY = terrain.getMapBounds(0).y;
    GridPoint2 startPos = new GridPoint2();
    startPos.set(-20, startPosY/2 - 1);

    Entity theVoid = NPCFactory.createTheVoid(player);
    spawnEntityAt(theVoid, startPos, true, true);

  }

  private void playMusic() {
    Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
    music.setLooping(true);
    music.setVolume(0.3f);
    music.play();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(gameTextures);
    resourceService.loadTextureAtlases(gameTextureAtlases);
    resourceService.loadSounds(gameSounds);
    resourceService.loadMusic(gameMusic);

    while (!resourceService.loadForMillis(10)) {
      // This could be upgraded to a loading screen
      logger.info("Loading... {}%", resourceService.getProgress());
    }
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(gameTextures);
    resourceService.unloadAssets(gameTextureAtlases);
    resourceService.unloadAssets(gameSounds);
    resourceService.unloadAssets(gameMusic);
  }

  @Override
  public void dispose() {
    super.dispose();
    ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
    this.unloadAssets();
  }
}
