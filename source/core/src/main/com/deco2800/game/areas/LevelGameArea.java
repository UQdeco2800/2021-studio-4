package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainTile;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.rendering.BackgroundRenderComponent;
import com.deco2800.game.utils.math.RandomUtils;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class LevelGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(LevelGameArea.class);
  private static final int NUM_TREES = 7;
  private static final int NUM_GHOSTS = 2;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);
  private static final float WALL_WIDTH = 0.1f;
  private static final String[] gameTextures = {
          "images/virus_man.png",
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
          "images/basicenemysprite.png",
          "images/chasingenemy.png",
          "images/enemyspritehsee.png",
          "images/game_background.png",
          "map-textures/mapTextures_Platforms.png",
          "map-textures/mapTextures_Middle-Platform.png",
          "map-textures/mapTextures_Button-On.png",
          "map-textures/mapTextures_bridge.png",
          "map-textures/mapTextures_door.png",
          "images/animatedvoid.png",
  };
  private static final String[] gameTextureAtlases = {
          "images/terrain_iso_grass.atlas",
          "images/ghost.atlas",
          "images/ghostKing.atlas",
          "images/the_void.atlas",
          "images/testingenemy.atlas",
          "map-spritesheets/mapTextures.atlas",
          "images/void.atlas",
  };
  private static final String[] gameSounds = {"sounds/Impact4.ogg"};
  private static final String backgroundMusic = "sounds/BackingMusicWithDrums.mp3";
  private static final String[] gameMusic = {
          "sounds/BackingMusicWithDrums.mp3",
          "sounds/CLICK_Click.mp3",
          "sounds/End credits.mp3",
          "sounds/ENEMY_Collision.mp3",
          "sounds/Enemy_Little enemy wobble sound.mp3",
          "sounds/OBSTACLE_Button.mp3",
          "sounds/OBSTACLE_Player Jumping",
          "sounds/PLAYER_Player Getting Power.mp3",
          "sounds/PLAYER_Running Into.mp3",
          "sounds/VOID_LoseGame_VirusHit.mp3",
          "sounds/VOID_void sound.mp3",
          "sounds/MainMenuMusic.mp3"
  };


  private final TerrainFactory terrainFactory;

  private Entity player;

  public LevelGameArea(TerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;
  }

  /**
   * Initializes basic components such as loading assets, background and terrain
   */
  public void init() {
    loadAssets();

    displayBackground();
    spawnTerrain();
  }

  /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
  @Override
  public void create() {
    init();

    displayUI();

    //spawnTrees();
    spawnLevel();
    player = spawnPlayer();
    //spawnGhosts();
    //spawnGhostKing();
    spawnGroundEnemy();
    spawnTheVoid();

    playMusic();

    spawnPlatform(8, 21, 5);
    spawnDoor(9, 23, 5);
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

  private void spawnTerrain() {
    // Generate terrain
    terrain = terrainFactory.createTerrain();

    Entity terrainEntity = new Entity();
    terrainEntity.addComponent(terrain);

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

  public void clearTerrainCell(int x, int y) {
    TiledMapTileLayer layer = (TiledMapTileLayer)terrain.getMap().getLayers().get(0);
    layer.setCell(x, y, null);

    terrain.invalidateCache();
  }

  public void setTerrainCell(TerrainTile tile, int x, int y) {
    TiledMapTileLayer layer = (TiledMapTileLayer)terrain.getMap().getLayers().get(0);
    TiledMapTileLayer.Cell cell = tile.generateCell();
    layer.setCell(x, y, cell);

    terrain.invalidateCache();
  }

  public void spawnPlatform(int posX, int posY, int width) {
    this.spawnPlatform(posX, posY, width, true, true);
  }

  public void spawnPlatform(int posX, int posY, int width, boolean centerX, boolean centerY) {
    Entity platform = ObstacleFactory.createPlatform(width);
    GridPoint2 position = new GridPoint2(posX,posY);
    spawnEntityAt(platform, position, centerX, centerY);
  }

  public void spawnMiddlePlatform(int posX, int posY, int width) {
    this.spawnMiddlePlatform(posX, posY, width, true, true);
  }

  public void spawnMiddlePlatform(int posX, int posY, int width, boolean centerX, boolean centerY) {
    Entity platform = ObstacleFactory.createMiddlePlatform(width);
    GridPoint2 position = new GridPoint2(posX,posY);
    spawnEntityAt(platform, position, centerX, centerY);
  }
  public void spawnButton(int posX, int posY) {
    spawnButton(posX, posY, false, true);
  }

  public void spawnButton(int posX, int posY, boolean centerX, boolean centerY) {
    Entity button = ObstacleFactory.createButton();
    GridPoint2 position = new GridPoint2(posX,posY);
    spawnEntityAt(button, position, centerX, centerY);
  }

  public void spawnBridge(int posX, int posY, int width) {
    spawnBridge(posX, posY, width, false, true);
  }

  public void spawnBridge(int posX, int posY, int width, boolean centerX, boolean centerY) {
    Entity bridge = ObstacleFactory.createBridge(width);
    GridPoint2 position = new GridPoint2(posX,posY);
    spawnEntityAt(bridge, position, centerX, centerY);
  }

  public void spawnDoor(int posX, int posY, int height) {
    spawnDoor(posX, posY, height, false, true);
  }

  public void spawnDoor(int posX, int posY, int height, boolean centerX, boolean centerY) {
    Entity door = ObstacleFactory.createDoor(height);
    GridPoint2 position = new GridPoint2(posX,posY);
    spawnEntityAt(door, position, centerX, centerY);
  }

  private void spawnLevel() {
    int c;
    int i;
    for (c = 0; c < 4; c++) {
      for (i = 0; i < 8; i++) {
        spawnPlatform(c*8+i, c+5, 1);
      }
      spawnMiddlePlatform(c*8+8,c+5, 1);
    }
    int x = 32;
    int y = 8;
    spawnLadderPlatforms(x,y);
    for (i = 0; i < 8; i++) {
      spawnPlatform(32+i,22,1);
      spawnMiddlePlatform(39,22-i-1, 1);
      spawnPlatform(40+i,14, 1);
    }
    spawnButton(40,15);
    for (i = 0; i < 6; i++) {
      spawnBridge(48+i,14, 1);
      spawnPlatform(54+i,14, 1);
    }
    spawnDoor(56,15, 4);
  }

  private void spawnLadderPlatforms(int x, int y) {

    for (int i = 0; i < 13; i++) {
      spawnMiddlePlatform(x,y+1+i, 1);
      spawnMiddlePlatform(x-9,y+6+i, 1);
    }
    for (int i = 0; i < 3; i++) {
      spawnPlatform(x-3+i,y+3, 1);
      spawnPlatform(x-8+i,y+6, 1);
      spawnPlatform(x-3+i,y+9, 1);
      spawnPlatform(x-8+i,y+12, 1);
    }
  }

  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer();
    spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    return newPlayer;
  }

  /**
   * Spawn the ground enemy
   * It generate a random x cord and a fix y cord to ensure the enemy spawn on the ground
   * There is list that checks whether a x coordinate exist already to ensure the
   * enemy do not overlap
   */
  private void spawnGroundEnemy() {
    ArrayList<Integer>  check = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      int xCord = 20 + (int)(Math.random() * ((WALL_WIDTH - 5) + 1));

      while (check.contains(xCord)) {
        xCord = 20 + (int)(Math.random() * ((WALL_WIDTH - 5) + 1));
      }
      check.add(xCord);
      GridPoint2 randomPos = new GridPoint2(xCord,6);
      Entity ghost = NPCFactory.createGhost(player);
      spawnEntityAt(ghost, randomPos, true, true);
    }
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

  /**
   * Spawns the void on the map by calling the createTheVoid() method in NPCFactory
   * with player as its parameter. The void's vertical placement is determined by 1/2 of
   * the maps height and the horizontal placement is chosen to spawn the void to the far
   * left of the screen.
   *
   * @return void
   */
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
