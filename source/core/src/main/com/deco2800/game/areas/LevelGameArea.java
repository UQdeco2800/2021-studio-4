package com.deco2800.game.areas;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainTile;
import com.deco2800.game.areas.terrain.TerrainTileDefinition;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.ObstacleEntity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.physics.components.InteractableComponent;
import com.deco2800.game.physics.components.SubInteractableComponent;
import com.deco2800.game.rendering.BackgroundRenderComponent;
import com.deco2800.game.services.*;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.deco2800.game.leveleditor.ObstacleToolComponent;

import java.io.*;
import java.util.*;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class LevelGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(LevelGameArea.class);
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(15, 15);
  private static final GridPoint2 STATUSEFFECT_SPAWN1 = new GridPoint2(40, 25);
  private static final GridPoint2 STATUSEFFECT_SPAWN2 = new GridPoint2(30, 25);
  public List<ObstacleEntity> obstacleEntities = new ArrayList<>();
  public static ArrayList<TerrainTile> terrainTiles = new ArrayList<>();
  public static ArrayList<String> buffers = new ArrayList<>();
  public static ArrayList<String> deBuffers = new ArrayList<>();

  public Map<ObstacleEntity, ObstacleEntity> mapInteractables = new HashMap<>();

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
          "images/void_spritesheet2.png",
          "images/Pick_Ups.png",
          "images/portal-door.png",
          "images/jumppad.png",
          "images/button.png",
          "images/level1_background.jpg",


  };

  private static final String[] gameTextureAtlases = {

          "images/terrain_iso_grass.atlas",
          "images/ghost.atlas",
          "images/ghostKing.atlas",
          "images/testingenemy.atlas",
          "map-spritesheets/mapTextures.atlas",
          "images/void.atlas",
          "images/player.atlas",
          "images/Pick_Ups.atlas",
          "images/portal-door.atlas",
          "images/jumppad.atlas",
          "images/button.atlas"
  };
  private static final MusicServiceDirectory gameSong = new MusicServiceDirectory();
  private static final String[] gameMusic = {gameSong.click, gameSong.game_level_1,gameSong.end_credits,
    gameSong.enemy_collision,gameSong.enemy_death, gameSong.obstacle_boost, gameSong.obstacle_button,
    gameSong.player_collision, gameSong.player_power_up, gameSong.void_death, gameSong.void_noise, gameSong.game_level_1_option2,
  gameSong.ending_menu, gameSong.game_level_2, gameSong.main_menu, gameSong.death_noise_2};

  /*private static final String backgroundMusic = "sounds/BackingMusicWithDrums.mp3";
  private static final String[] gameMusic = {"sounds/BackingMusicWithDrums.mp3",
          "sounds/CLICK_Click.mp3", "sounds/End credits.mp3", "sounds/ENEMY_Collision.mp3",
          "sounds/Enemy_Little enemy wobble sound.mp3", "sounds/OBSTACLE_Button.mp3",
          "sounds/OBSTACLE_Player Jumping", "sounds/PLAYER_Player Getting Power.mp3",
          "sounds/PLAYER_Running Into.mp3", "sounds/VOID_LoseGame_VirusHit.mp3",
          "sounds/VOID_void sound.mp3", "sounds/MainMenuMusic.mp3"};*/


  private final TerrainFactory terrainFactory;

  private Entity player;

  public LevelGameArea(TerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;
    buffers.add(0, "Buff_Jump");
    buffers.add(1, "Buff_Time_Stop");
    buffers.add(2, "Buff_Speed");
    deBuffers.add(0, "Debuff_Bomb");
    deBuffers.add(1, "Debuff_Speed");
    deBuffers.add(2, "Debuff_Stuck");
  }

  /**
   * Initializes basic components such as loading assets, background and terrain
   */
  public void init() {
    loadAssets();
    mapInteractables();

    displayBackground();
    spawnTerrain();
    spawnLevelFromFile();
  }

  /**
   * Create the game area, including terrain, static entities (trees), dynamic entities (player)
   * */
  @Override
  public void create() {
    init();

    displayUI();

    //spawnTrees();
    //spawnLevel();
    player = spawnPlayer();
    //spawnGhosts();
    //spawnGhostKing();

    spawnLevelFromFile();
    //spawnGroundEnemy();

    //spawnGorgonGear(20,8);


    spawnTheVoid();

    spawnStatusEffectDeBuff("Buff_Time_Stop");
    spawnStatusEffectBuff("Buff_Jump");
//    spawnStatusEffectBuff(getBuff()); // To be selected randomly from a list of the effects
//    spawnStatusEffectDeBuff(getDeBuff()); // To be selected randomly from a list of the effects

    playTheMusic("game_level_1");
    //playMusic();

    spawnPlatform(8, 21, 5);
    spawnDoor(9, 23, 5);
  }

  private String getBuff() {
    Random random = new Random();
    int indexNum = random.nextInt(3);
    return buffers.get(indexNum);
  }
  
  private String getDeBuff() {
    Random random = new Random();
    int indexNum = random.nextInt(3);
    return deBuffers.get(indexNum);
  }

  private void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay("Box Forest"));
    spawnEntity(ui);
  }

  private void displayBackground() {
    Entity background = new Entity();
    background.addComponent(new BackgroundRenderComponent("images/level1_background.jpg"));
    spawnEntity(background);
  }

  private void spawnTerrain() {
    // Generate terrain
    terrain = terrainFactory.createTerrain();

    Entity terrainEntity = new Entity();
    terrainEntity.addComponent(terrain);

    spawnEntity(terrainEntity);
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
    ObstacleEntity platform = (ObstacleEntity) ObstacleFactory.createPlatform(width);
    GridPoint2 position = new GridPoint2(posX,posY);
    spawnEntityAt(platform, position, centerX, centerY);
    obstacleEntities.add(platform);
    platform.setTilePosition(position);
  }

  public void spawnMiddlePlatform(int posX, int posY, int width) {
    this.spawnMiddlePlatform(posX, posY, width, true, true);
  }

  public void spawnMiddlePlatform(int posX, int posY, int width, boolean centerX, boolean centerY) {
    ObstacleEntity platform = (ObstacleEntity) ObstacleFactory.createMiddlePlatform(width);
    GridPoint2 position = new GridPoint2(posX,posY);
    spawnEntityAt(platform, position, centerX, centerY);
    obstacleEntities.add(platform);
    platform.setTilePosition(position);
  }
  public void spawnButton(int posX, int posY) {
    spawnButton(posX, posY, false, true);
  }

  public void spawnButton(int posX, int posY, boolean centerX, boolean centerY) {
    ObstacleEntity button = (ObstacleEntity) ObstacleFactory.createButton();
    GridPoint2 position = new GridPoint2(posX,posY);
    spawnEntityAt(button, position, centerX, centerY);
    obstacleEntities.add(button);
    button.setTilePosition(position);
  }

  public void spawnBridge(int posX, int posY, int width) {
    spawnBridge(posX, posY, width, false, true);
  }

  public void spawnBridge(int posX, int posY, int width, boolean centerX, boolean centerY) {
    ObstacleEntity bridge = (ObstacleEntity) ObstacleFactory.createBridge(width);
    GridPoint2 position = new GridPoint2(posX,posY);
    spawnEntityAt(bridge, position, centerX, centerY);
    obstacleEntities.add(bridge);
    bridge.setTilePosition(position);
  }

  public void spawnDoor(int posX, int posY, int height) {
    spawnDoor(posX, posY, height, false, true);
  }

  public void spawnDoor(int posX, int posY, int height, boolean centerX, boolean centerY) {
    ObstacleEntity door = (ObstacleEntity) ObstacleFactory.createDoor(height);
    GridPoint2 position = new GridPoint2(posX,posY);
    spawnEntityAt(door, position, centerX, centerY);
    obstacleEntities.add(door);
    door.setTilePosition(position);
  }

  /**
   * Maps the sub-interactables (i.e. bridges and doors) to the closest
   * spawned interactable (i.e. button).
   */
  public void mapInteractables() {
      // list of all buttons in order of creation
      ArrayList<ObstacleEntity> buttons = new ArrayList<>();

      //list of all doors and bridges in order of creation
      ArrayList<ObstacleEntity> subInteractables = new ArrayList<>();

      for (int i = 0; i < obstacleEntities.size(); i++) {
        ObstacleEntity obstacle = obstacleEntities.get(i);
        InteractableComponent interactable = obstacle.getComponent(InteractableComponent.class); // button
        SubInteractableComponent subInteractable = obstacle.getComponent(SubInteractableComponent.class); //door or bridge

        if (subInteractable != null) { // if bridge or door
          subInteractables.add(obstacle);
        } else if (interactable != null) { //if button
          buttons.add(obstacle);
        }
      }

      // map earliest button with earliest door/bridge, continue for all buttons
      if (buttons.size() > 0 && subInteractables.size() > 0) {
        for (int j = 0; j < buttons.size(); j++) {
          mapInteractables.put(buttons.get(j), subInteractables.get(j));
        }
      }
  }

  public void saveAll(String name){
    FileWriter writer = null;
    try {
      writer = new FileWriter("levels/" + name + ".txt");
      saveTerrain(writer);
      saveObstacles(writer);
      writer.flush();
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    } finally {
      try {
        writer.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void saveTerrain(FileWriter writer) throws IOException {
    TiledMapTileLayer mapTileLayer = (TiledMapTileLayer)terrain.getMap().getLayers().get(0);
    for (int x = 0; x < mapTileLayer.getWidth(); x++) {
      for (int y = 0; y < mapTileLayer.getHeight(); y++) {
        TiledMapTileLayer.Cell cell = mapTileLayer.getCell(x, y);

        if (cell != null) {
          TerrainTile terrainTile = (TerrainTile)cell.getTile();
          String tileInfo = terrainTile.serialize(x, y);
          writer.write("T:"+tileInfo);
        }
      }
    }
  }

  private void saveObstacles(FileWriter writer) throws IOException {
    for (ObstacleEntity obstacle: obstacleEntities) {
      String obstacleInfo = obstacle.serialise();
      writer.write("O:"+obstacleInfo);
    }
  }

  @Override
  public void untrackEntity(Entity entity) {
    this.obstacleEntities.remove(entity);
    super.untrackEntity(entity);
  }

  private void spawnLevelFromFile() {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader("levels/level.txt"));

      String line;
      while ((line = reader.readLine()) != null) {
        String[] lineInfo = line.split(":");
        if (lineInfo[0].equals("O")){
          ObstacleToolComponent.Obstacle obstacle = ObstacleToolComponent.Obstacle.valueOf(lineInfo[1]);
          int size = Integer.parseInt(lineInfo[2]);
          int x = Integer.parseInt(lineInfo[3]);
          int y = Integer.parseInt(lineInfo[4]);
          spawnObstacle(obstacle,x,y,size);
        } else {
          TerrainTileDefinition definition = TerrainTileDefinition.valueOf(lineInfo[1]);
          int rotation = Integer.parseInt(lineInfo[2]);
          int x = Integer.parseInt(lineInfo[3]);
          int y = Integer.parseInt(lineInfo[4]);
          TiledMapTileLayer mapTileLayer = (TiledMapTileLayer)terrain.getMap().getLayers().get(0);
          TerrainFactory.loadTilesFromFile(mapTileLayer,definition,rotation,x,y);
        }
      }
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    } finally {
      try {
        reader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void spawnObstacle(ObstacleToolComponent.Obstacle selectedObstacle, int x, int y, int size) {
//    x = x*2;
//    y = y*2;
    switch (selectedObstacle){
      case PLATFORM:
        spawnPlatform(x, y, size, false, false);
        break;
      case MIDDLE_PLATFORM:
        spawnMiddlePlatform(x, y, size, false, false);
        break;
      case DOOR:
        spawnDoor(x, y, size, false, false);
        break;
      case BRIDGE:
        spawnBridge(x, y, size, false, false);
        break;
      case BUTTON:
        spawnButton(x, y, false, false);
        break;
    }
  }

  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer(mapInteractables);
    spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    return newPlayer;
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
    startPos.set(-20, startPosY/2 - 3);

    Entity theVoid = NPCFactory.createTheVoid(player);
    spawnEntityAt(theVoid, startPos, true, true);
  }

  /**
   * Spawns the Buff StatusEffect on the map by calling the createStatusEffect() method in NPCFactory
   * with player as its parameter.
   * @return void
   */
  private void spawnStatusEffectBuff(String statusEffectType) {
    Entity statusEffect = NPCFactory.createStatusEffect(player, statusEffectType);
    spawnEntityAt(statusEffect, STATUSEFFECT_SPAWN1, true, true);
  }

  /**
   * Spawns the DeBuff StatusEffect on the map by calling the createStatusEffect() method in NPCFactory
   * with player as its parameter.
   * @return void
   */
  private void spawnStatusEffectDeBuff(String statusEffectType) {
    Entity statusEffect = NPCFactory.createStatusEffect(player, statusEffectType);
    spawnEntityAt(statusEffect, STATUSEFFECT_SPAWN2, true, true);
  }





  /**
   * Music Dictionary for intialisation of various sound effects
   * @param musicPath - String (see Music Directory for more information)
   */
  private void playTheMusic(String musicPath) {
    MusicServiceDirectory dict = new  MusicServiceDirectory();
    MusicService gameMusic = null;
    switch (musicPath) {
      case "click":
        gameMusic = new MusicService(dict.click);
        break;
      case "end_credits":
        gameMusic = new MusicService(dict.end_credits);
        break;
      case "enemy_collision":
        gameMusic = new MusicService(dict.enemy_collision);
        break;
      case "enemy_death":
        gameMusic = new MusicService(dict.enemy_death);
        break;
      case "obstacle_boost":
        gameMusic = new MusicService(dict.obstacle_boost);
        break;
      case "obstacle_button":
        gameMusic = new MusicService(dict.obstacle_button);
        break;
      case "player_power_up":
        gameMusic = new MusicService(dict.player_power_up);
        break;
      case "player_collision":
        gameMusic = new MusicService(dict.player_collision);
        break;
      case "void_death":
        gameMusic = new MusicService(dict.void_death);
        break;
      case "void_noise":
        gameMusic = new MusicService(dict.void_noise);
        break;
      case "ending_menu":
        gameMusic = new MusicService(dict.ending_menu);
        break;
      case "level_1_2":
        gameMusic = new MusicService(dict.game_level_1_option2);
        break;
      case "level_2":
        gameMusic = new MusicService(dict.game_level_2);
        break;
      case "main_menu_new":
        gameMusic = new MusicService(dict.main_menu);
        break;
      case "death_noise_2":
        gameMusic = new MusicService(dict.death_noise_2);
        break;
      default:
        gameMusic = new MusicService(dict.game_level_1);//To make sure gameMusic is never null
    }
      gameMusic.playMusic();

  }

  /*private void playMusic() {
    //MusicServiceDirectory mainMenuSong = new MusicServiceDirectory();
    //MusicService musicScreen = new MusicService(mainMenuSong.main_menu);
    //musicScreen.playMusic();
    MusicSingleton s = MusicSingleton.getInstance();
    s.playMusicSingleton("sounds/BackingMusicWithDrums.mp3");
  }*/

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(gameTextures);
    resourceService.loadTextureAtlases(gameTextureAtlases);
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
    resourceService.unloadAssets(gameMusic);
  }

  @Override
  public void dispose() {
    super.dispose();
    //ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
    this.unloadAssets();
  }
}
