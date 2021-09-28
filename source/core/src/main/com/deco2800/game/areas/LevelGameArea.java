package com.deco2800.game.areas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.GridPoint3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainTile;
import com.deco2800.game.areas.terrain.TerrainTileDefinition;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.ObstacleDefinition;
import com.deco2800.game.entities.ObstacleEntity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.files.LevelFile;
import com.deco2800.game.levels.LevelDefinition;
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
import java.util.concurrent.ConcurrentHashMap;

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

  public Map<ObstacleEntity, List<ObstacleEntity>> mapInteractables = new ConcurrentHashMap<>();

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
    "map-textures/marker_cross.png",
    "map-textures/marker_o.png",
    "map-textures/end_portal.png",
    "images/animatedvoid.png",
    "images/void_spritesheet2.png",
    "images/Pick_Ups.png",
    "images/portal-door.png",
    "images/jumppad.png",
    "images/button.png",
    "images/level1_background.jpg",
    "images/simple_player_animation.png",
    "images/walkingsprite.png",
    "images/playerStill.png",
    "images/testingrunningsprite.png",
    "images/background_level1.jpg"
  };

  private static final String[] gameTextureAtlases = {


    "images/terrain_iso_grass.atlas",
    "images/ghost.atlas",
    "images/ghostKing.atlas",
    "images/testingenemy.atlas",
    "map-spritesheets/mapTextures.atlas",
    "images/void.atlas",
    "images/Pick_Ups.atlas",
    "images/portal-door.atlas",
    "images/jumppad.atlas",
    "images/button.atlas",
    "images/walking_sprite.atlas",
    "images/testingrunning.atlas",
    "images/simple_player_sprite.atlas"

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
  private final LevelDefinition levelDefinition;

  private Entity player;

  public LevelGameArea(TerrainFactory terrainFactory, LevelDefinition levelDefinition) {
    super();
    this.levelDefinition = levelDefinition;

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




//    spawnStatusEffectDeBuff("Debuff_Speed");
//    spawnStatusEffectBuff("Buff_Jump");
    spawnStatusEffectBuff(getBuff()); // To be selected randomly from a list of the effects
    spawnStatusEffectDeBuff(getDeBuff()); // To be selected randomly from a list of the effects

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
    background.addComponent(new BackgroundRenderComponent("images/background_level1.jpg"));
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

  public ObstacleEntity spawnPlatform(int posX, int posY, int width, boolean centerX, boolean centerY) {
    ObstacleEntity platform = (ObstacleEntity) ObstacleFactory.createPlatform(width);
    GridPoint2 position = new GridPoint2(posX,posY);
    spawnEntityAt(platform, position, centerX, centerY);
    obstacleEntities.add(platform);
    platform.setTilePosition(position);
    return platform;
  }

  public void spawnMiddlePlatform(int posX, int posY, int width) {
    this.spawnMiddlePlatform(posX, posY, width, true, true);
  }

  public ObstacleEntity spawnMiddlePlatform(int posX, int posY, int width, boolean centerX, boolean centerY) {
    ObstacleEntity platform = (ObstacleEntity) ObstacleFactory.createMiddlePlatform(width);
    GridPoint2 position = new GridPoint2(posX,posY);
    spawnEntityAt(platform, position, centerX, centerY);
    obstacleEntities.add(platform);
    platform.setTilePosition(position);
    return platform;
  }
  public void spawnButton(int posX, int posY) {
    spawnButton(posX, posY, false, true);
  }

  public ObstacleEntity spawnButton(int posX, int posY, boolean centerX, boolean centerY) {
    ObstacleEntity button = (ObstacleEntity) ObstacleFactory.createButton();
    GridPoint2 position = new GridPoint2(posX,posY);
    spawnEntityAt(button, position, centerX, centerY);
    obstacleEntities.add(button);
    button.setTilePosition(position);
    return button;
  }

  public void spawnBridge(int posX, int posY, int width) {
    spawnBridge(posX, posY, width, false, true);
  }

  public ObstacleEntity spawnBridge(int posX, int posY, int width, boolean centerX, boolean centerY) {
    ObstacleEntity bridge = (ObstacleEntity) ObstacleFactory.createBridge(width);
    GridPoint2 position = new GridPoint2(posX,posY);
    spawnEntityAt(bridge, position, centerX, centerY);
    obstacleEntities.add(bridge);
    bridge.setTilePosition(position);
    return bridge;
  }

  public void spawnDoor(int posX, int posY, int height) {
    spawnDoor(posX, posY, height, false, true);
  }

  public ObstacleEntity spawnDoor(int posX, int posY, int height, boolean centerX, boolean centerY) {
    ObstacleEntity door = (ObstacleEntity) ObstacleFactory.createDoor(height);
    GridPoint2 position = new GridPoint2(posX,posY);
    spawnEntityAt(door, position, centerX, centerY);
    obstacleEntities.add(door);
    door.setTilePosition(position);
    return door;
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
          //mapInteractables.put(buttons.get(j), subInteractables.get(j));
        }
      }
  }

  public ObstacleEntity getObstacle(Entity entity) {
    for (ObstacleEntity obstacleEntity : obstacleEntities) {
      if (obstacleEntity.equals(entity)) return obstacleEntity;
    }
    return null;
  }

  /**
   * Method assigning the interactableID property to all interactable ObstacleEntities.
   * This is not a very clean solution (this data needs to be moved to be stored on the actual InteractableComponent)
   * but it's the simplest way to do it with the existing implementation.
   */
  private void generateInteractableIDs() {
    int id = 0;
    for (ObstacleEntity obstacleEntity : obstacleEntities) {
      if (obstacleEntity.getComponent(SubInteractableComponent.class) != null
        || obstacleEntity.getComponent(InteractableComponent.class) != null) {
        obstacleEntity.interactableID = id++;
      }
    }
  }

  /**
   * Generates a map of interactableIDs to be saved/loaded from file
   */
  private Map<Integer, List<Integer>> generateInteractablesMap() {
    Map<Integer, List<Integer>> interactableIds = new HashMap<>();
    for (Map.Entry<ObstacleEntity, List<ObstacleEntity>> obstacleEntityListEntry : mapInteractables.entrySet()) {
      List<Integer> subInteractableIds = new ArrayList<>();
      for (ObstacleEntity obstacleEntity : obstacleEntityListEntry.getValue()) {
        subInteractableIds.add(obstacleEntity.interactableID);
      }

      interactableIds.put(obstacleEntityListEntry.getKey().interactableID, subInteractableIds);
    }

    return interactableIds;
  }

  public void spawnLevelEndPortal(int posX, int posY, int width) {
    spawnLevelEndPortal(posX, posY, width, false, true);
  }

  public ObstacleEntity spawnLevelEndPortal(int posX, int posY, int width, boolean centerX, boolean centerY) {
    ObstacleEntity levelEndPortal = (ObstacleEntity) ObstacleFactory.createLevelEndPortal(width);
    GridPoint2 position = new GridPoint2(posX,posY);
    spawnEntityAt(levelEndPortal, position, centerX, centerY);
    obstacleEntities.add(levelEndPortal);
    levelEndPortal.setTilePosition(position);
    return levelEndPortal;
  }

  public void writeAll() {
    Json json = new Json();
    json.setOutputType(JsonWriter.OutputType.json);

    FileHandle file = Gdx.files.local(levelDefinition.getLevelFileName());
    assert file != null;

    // Create a new LevelFile object
    LevelFile levelFile = new LevelFile();

    // save the TiledMapTileLayer
    applyTerrainSerializers(json);
    levelFile.terrain = new LevelFile.Terrain();
    levelFile.terrain.mapLayer = (TiledMapTileLayer)terrain.getMap().getLayers().get(0);

    // Save the obstacles
    generateInteractableIDs(); // Assign interactable IDs to obstacles here
    levelFile.obstacles = new LevelFile.Obstacles();
    levelFile.obstacles.obstacleEntities = this.obstacleEntities;
    levelFile.obstacles.interactablesMap = generateInteractablesMap();

    // save the file
    file.writeString(json.prettyPrint(levelFile), false);
  }

  private void readAll() {
    Json json = new Json();
    applyTerrainSerializers(json);

    FileHandle file = Gdx.files.local(levelDefinition.getLevelFileName());
    assert file != null;

    LevelFile levelFile = json.fromJson(LevelFile.class, file);

    for (ObstacleEntity obstacleEntity : levelFile.obstacles.obstacleEntities) {
      ObstacleEntity newObstacle = spawnObstacle(obstacleEntity.getDefinition(), (int)obstacleEntity.getPosition().x,
        (int)obstacleEntity.getPosition().y, obstacleEntity.size);

      newObstacle.interactableID = obstacleEntity.interactableID;
    }

    // Add entities to subInteractables list
    for (ObstacleEntity obstacleEntity : obstacleEntities) {
      if (obstacleEntity.interactableID != null) {
//        System.out.println(obstacleEntity.interactableID);
//        System.out.println(levelFile.obstacles.interactablesMap);
//        System.out.println(levelFile.obstacles.interactablesMap.keySet());
//        System.out.println(levelFile.obstacles.interactablesMap.get(obstacleEntity.interactableID);
//        System.out.println(levelFile.obstacles.interactablesMap.containsKey(obstacleEntity.interactableID));
        if (levelFile.obstacles.interactablesMap.containsKey(obstacleEntity.interactableID)) {
          List<Integer> subInteractableIds = levelFile.obstacles.interactablesMap.get(obstacleEntity.interactableID);
          List<ObstacleEntity> subInteractables = new ArrayList<>();
          for (ObstacleEntity entity : obstacleEntities) {
            if (subInteractableIds.contains(entity.interactableID)) {
              subInteractables.add(entity);
              break;
            }
          }

          mapInteractables.put(obstacleEntity, subInteractables);
        }
      }
    }
    System.out.println(levelFile.obstacles.interactablesMap);
  }

  private void applyTerrainSerializers(Json json) {
    // Serializer for loading/saving cells to json using PositionedTerrainTile
    json.setSerializer(TiledMapTileLayer.class, new Json.Serializer<>() {
      @Override
      public void write(Json json, TiledMapTileLayer layer, Class knownType) {
        List<LevelFile.PositionedTerrainTile> tiles = new ArrayList<>();
        // Create a list of PositionedTerrainTile to save
        for (int x = 0; x < layer.getWidth(); x++) {
          for (int y = 0; y < layer.getHeight(); y++) {
            TiledMapTileLayer.Cell cell = layer.getCell(x, y);

            if (cell != null) {
              LevelFile.PositionedTerrainTile positionedTile =
                new LevelFile.PositionedTerrainTile((TerrainTile) cell.getTile(), x, y);

              tiles.add(positionedTile);
            }
          }
        }

        json.writeValue(tiles);
      }

      @Override
      public TiledMapTileLayer read(Json json, JsonValue jsonData, Class type) {
        TiledMapTileLayer mapTileLayer = (TiledMapTileLayer)terrain.getMap().getLayers().get(0);

        for (JsonValue tileData : jsonData) {
          LevelFile.PositionedTerrainTile posTile = json.readValue(LevelFile.PositionedTerrainTile.class, tileData);

          TiledMapTileLayer.Cell cell = posTile.tile.generateCell();
          mapTileLayer.setCell(posTile.x, posTile.y, cell);
        }

        return mapTileLayer;
      }
    });
  }

//  @Deprecated
//  private void _spawnLevelFromFile() {
//    BufferedReader reader = null;
//    try {
//      reader = new BufferedReader(new FileReader("levels/level.txt"));
//
//      String line;
//      while ((line = reader.readLine()) != null) {
//        String[] lineInfo = line.split(":");
//        if (lineInfo[0].equals("O")) {
//          ObstacleDefinition obstacle = ObstacleDefinition.valueOf(lineInfo[1]);
//          int size = Integer.parseInt(lineInfo[2]);
//          int x = Integer.parseInt(lineInfo[3]);
//          int y = Integer.parseInt(lineInfo[4]);
//          spawnObstacle(obstacle, x, y, size);
//        } else {
//          TerrainTileDefinition definition = TerrainTileDefinition.valueOf(lineInfo[1]);
//          int rotation = Integer.parseInt(lineInfo[2]);
//          int x = Integer.parseInt(lineInfo[3]);
//          int y = Integer.parseInt(lineInfo[4]);
//          TiledMapTileLayer mapTileLayer = (TiledMapTileLayer) terrain.getMap().getLayers().get(0);
//          TerrainFactory.loadTilesFromFile(mapTileLayer, definition, rotation, x, y);
//        }
//      }
//    } catch (IOException | NullPointerException e) {
//      e.printStackTrace();
//    } finally {
//      try {
//        reader.close();
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
//    }
//  }

  @Override
  public void untrackEntity(Entity entity) {
    this.obstacleEntities.remove(entity);
    super.untrackEntity(entity);
  }

  private void spawnLevelFromFile() {
  //  _spawnLevelFromFile(); // Used to load an old level.txt file to the new json format.
    // Load actual elements
    readAll();

    // Generate tile bodies
    TerrainFactory.generateBodies(terrain.getMap());
  }

  private ObstacleEntity spawnObstacle(ObstacleDefinition selectedObstacle, int x, int y, int size) {
//    x = x*2;
//    y = y*2;
    switch (selectedObstacle){
      case PLATFORM:
        return spawnPlatform(x, y, size, false, false);
      case MIDDLE_PLATFORM:
        return spawnMiddlePlatform(x, y, size, false, false);
      case DOOR:
        return spawnDoor(x, y, size, false, false);
      case BRIDGE:
        return spawnBridge(x, y, size, false, false);
      case BUTTON:
        return spawnButton(x, y, false, false);
      case LEVEL_END_PORTAL:
        return spawnLevelEndPortal(x,y,size,false,false);
      case JUMPPAD:
//        return spawnJumpPad ??? why this missing
    }

    return null;
  }

  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer(mapInteractables, this);
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

  public String getLevelDefinition() {
    return this.levelDefinition.name();
  }


}


