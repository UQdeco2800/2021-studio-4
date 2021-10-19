package com.deco2800.game.areas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainTile;
import com.deco2800.game.effects.StatusEffect;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.ObstacleDefinition;
import com.deco2800.game.entities.ObstacleEntity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.files.LevelFile;
import com.deco2800.game.levels.LevelInfo;
import com.deco2800.game.physics.components.InteractableComponent;
import com.deco2800.game.physics.components.SubInteractableComponent;
import com.deco2800.game.rendering.BackgroundRenderComponent;
import com.deco2800.game.services.*;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.deco2800.game.components.leveleditor.ObstacleToolComponent;
import java.util.*;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class LevelGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(LevelGameArea.class);
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 15);
  private static final GridPoint2 STATUSEFFECT_SPAWN = new GridPoint2(15, 15);
  public List<ObstacleEntity> obstacleEntities = new ArrayList<>();
  public static ArrayList<String> buffers = new ArrayList<>();
  public static ArrayList<String> deBuffers = new ArrayList<>();
  private LevelFile levelFile;
  private Random random = new Random();

  public Map<ObstacleEntity, List<ObstacleEntity>> mapInteractables = new HashMap<>();
  public List<ObstacleEntity> interactableEntities = new ArrayList<>();

  private static final String[] gameTextures = {
    "backgrounds/game_background.png",
    "map-textures/marker_cross.png",
    "map-textures/marker_o.png",
    "map-textures/end_portal.png",
    "void/animatedvoid.png",
    "void/void_spritesheet2.png",
    "powerups/Pick_Ups.png",
    "map-textures/portal-door.png",
    "backgrounds/level1_background.jpg",
    "player/simple_player_animation.png",
    "player/testingrunningsprite.png",
    "spawn-animations/spawn_portal.png",
    "spawn-animations/levelOneSpawn.png",
    "spawn-animations/spawnAnimationOne.png",
    "backgrounds/background_level1.jpg",
    "backgrounds/background_level2.jpg",
    "backgrounds/background_level3.png",
    "backgrounds/background_level4.png"
  };

  private static final String[] gameTextureAtlases = {
    "void/void.atlas",
    "powerups/Pick_Ups.atlas",
    "map-textures/portal-door.atlas",
    "player/testingrunning.atlas",
    "player/simple_player_sprite.atlas",
    "powerup-ui-spritesheets/bomb_item.atlas",
    "powerup-ui-spritesheets/jump_boost.atlas",
    "powerup-ui-spritesheets/lightning.atlas",
    "powerup-ui-spritesheets/speed_decrease.atlas",
    "powerup-ui-spritesheets/stuck_lock.atlas",
    "powerup-ui-spritesheets/time_stop.atlas",
  };

  private static final MusicServiceDirectory gameSong = new MusicServiceDirectory();
  private static final String[] gameMusic = {gameSong.click, gameSong.game_level_1,gameSong.end_credits,
    gameSong.enemy_collision,gameSong.enemy_death, gameSong.obstacle_boost, gameSong.obstacle_button,
    gameSong.player_collision, gameSong.player_power_up, gameSong.void_death, gameSong.void_noise, gameSong.game_level_1_option2,
  gameSong.ending_menu, gameSong.game_level_2, gameSong.main_menu, gameSong.death_noise_2,
          gameSong.game_level_3};



  private final TerrainFactory terrainFactory;
  private final LevelInfo levelInfo;
  private static boolean loading = true;
  private Entity player;

  public LevelGameArea(TerrainFactory terrainFactory, LevelInfo levelInfo) {
    super();
    this.levelInfo = levelInfo;

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
    loadLevelFile();

    loadAssets();

    String levels = levelInfo.getLevelFileName();
    if (levels != null) {
      if (levels.equals("levels/level1.json")) {
        displayBackground("backgrounds/background_level1.jpg");
      } else if (levels.equals("levels/level2.json")) {
        displayBackground("backgrounds/background_level2.jpg");
      } else if (levels.equals("levels/level3.json")) {
        displayBackground("backgrounds/background_level3.png");
      } else if (levels.equals("levels/level4.json")) {
        displayBackground("backgrounds/background_level4.png");
      } else {
        displayBackground("backgrounds/background_level1.jpg");
      }
    } else {
      displayBackground("backgrounds/background_level1.jpg");
    }

    spawnTerrain();
    spawnLevelFromFile();
    mapInteractables();
  }

  /**
   * Create the game area, including terrain, static entities (trees), dynamic entities (player)
   * */
  @Override
  public void create() {
    init();
    displayUI();
    player = spawnPlayer();
    spawnTheVoid();

    int statusPosX = STATUSEFFECT_SPAWN.x;
    int statusPosY = STATUSEFFECT_SPAWN.y;
    spawnStatusEffect(StatusEffect.FAST, statusPosX, statusPosY);
    spawnStatusEffect(StatusEffect.JUMP, statusPosX+10, statusPosY);
    spawnStatusEffect(StatusEffect.TIME_STOP, statusPosX+20, statusPosY);
    spawnStatusEffect(StatusEffect.SLOW, statusPosX+30, statusPosY);
    spawnStatusEffect(StatusEffect.STUCK, statusPosX+40, statusPosY);

    playTheMusic(levelInfo.getMusicPath());
  }

  public Entity getPlayer() {
    return this.player;
  }

  private void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay(levelInfo.getName()));
    spawnEntity(ui);
    loading = false;
  }

  private void displayBackground(String imagePath) {
    Entity background = new Entity();
    background.addComponent(new BackgroundRenderComponent(imagePath));
    spawnEntity(background);
  }


  private void displayLoadingScreen() {
    ResourceService resourceService = ServiceLocator.getResourceService();
    String[] loadingTexture = { "images/button_exit.png"}; //Update this to be correct path once implemented
    resourceService.loadTextures(loadingTexture );
    while (!resourceService.loadForMillis(10)) {
      // This could be upgraded to a loading screen
      logger.info("Loading LoadingScreen texture... {}%", resourceService.getProgress());
    }
    Entity loadingScreen = new Entity();
    loadingScreen.addComponent(new BackgroundRenderComponent("images/button_exit.png")); //update to be correct path
    spawnEntity(loadingScreen);
  }

  private void spawnTerrain() {
    // Generate terrain
    TextureAtlas levelAtlas = ServiceLocator.getResourceService()
      .getAsset(levelFile.levelTexture.getAtlasName(), TextureAtlas.class);
    terrain = terrainFactory.createTerrain(levelFile.terrain.mapLayer, levelAtlas);

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
   * Spawn a new jump pad.
   * @param posX X-position
   * @param posY Y-position
   */
  public void spawnJumppad(int posX, int posY) {
    spawnButton(posX, posY, false, true);
  }

  /**
   * Spawn a new jump pad.
   * @param posX X-position
   * @param posY Y-position
   * @param centerX boolean center X value
   * @param centerY boolean center Y value
   * @return jump pad
   */
  public ObstacleEntity spawnJumppad(int posX, int posY, boolean centerX, boolean centerY) {
    ObstacleEntity jumppad = (ObstacleEntity) ObstacleFactory.createJumpPad();
    GridPoint2 position = new GridPoint2(posX,posY);
    spawnEntityAt(jumppad, position, centerX, centerY);
    obstacleEntities.add(jumppad);
    jumppad.setTilePosition(position);
    return jumppad;
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

      if (buttons.size() > 0 && subInteractables.size() > 0) {
        for (int j = 0; j < buttons.size(); j++) {
          InteractableComponent interactable = buttons.get(j).getComponent(InteractableComponent.class);
          interactable.addSubInteractable(subInteractables.get(j));
          interactableEntities.add(buttons.get(j));
        }
      }

    System.out.println(buttons.toString());
    System.out.println(subInteractables.toString());
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
  /*
  private void generateInteractableIDs() {
    int id = 0;
    for (ObstacleEntity obstacleEntity : obstacleEntities) {
      if (obstacleEntity.getComponent(SubInteractableComponent.class) != null
        || obstacleEntity.getComponent(InteractableComponent.class) != null) {
        obstacleEntity.interactableID = id++;
      }
    }
  }

   */

  /**
   * Generates a map of interactableIDs to be saved/loaded from file
   */
  /*
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

   */

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
    // Theoretically it is more efficient to simply change the current level file and save that again, however this
    // could result in unintended consequences and thus is not a priority
    Json json = new Json();
    json.setOutputType(JsonWriter.OutputType.json);

    FileHandle file = Gdx.files.local(levelInfo.getLevelFileName());
    assert file != null;

    // Create a new LevelFile object
    LevelFile levelFile = new LevelFile();

    // save the TiledMapTileLayer
    TiledMapTileLayer layer = (TiledMapTileLayer)terrain.getMap().getLayers().get(0);
    LevelFile.TileLayerData layerData = new LevelFile.TileLayerData();
    layerData.height = this.levelFile.terrain.mapLayer.height;
    layerData.width = this.levelFile.terrain.mapLayer.width;

    // Add the tiles to the layer data
    for (int x = 0; x < layer.getWidth(); x++) {
      for (int y = 0; y < layer.getHeight(); y++) {
        TiledMapTileLayer.Cell cell = layer.getCell(x, y);

        if (cell != null) {
          LevelFile.PositionedTerrainTile positionedTile =
            new LevelFile.PositionedTerrainTile((TerrainTile) cell.getTile(), x, y);

          layerData.tiles.add(positionedTile);
        }
      }
    }

    levelFile.terrain = new LevelFile.Terrain();
    levelFile.terrain.mapLayer = layerData;

    // Save the obstacles
    //generateInteractableIDs(); // Assign interactable IDs to obstacles here
    levelFile.obstacles = new LevelFile.Obstacles();
    levelFile.obstacles.obstacleEntities = this.obstacleEntities;
    //levelFile.obstacles.interactablesMap = generateInteractablesMap();

    // Save current texture from old level file
    levelFile.levelTexture = this.levelFile.levelTexture;

    // save the file
    file.writeString(json.prettyPrint(levelFile), false);
  }

  private void loadLevelFile() {
    levelFile = levelInfo.readLevelFile();
    ServiceLocator.registerCurrentTexture(levelFile.levelTexture);
  }

  private void generateAll() {
    try {
      for (ObstacleEntity obstacleEntity : levelFile.obstacles.obstacleEntities) {
        ObstacleEntity newObstacle = spawnObstacle(obstacleEntity.getDefinition(), (int) obstacleEntity.getPosition().x,
                (int) obstacleEntity.getPosition().y, obstacleEntity.size);

        //newObstacle.interactableID = obstacleEntity.interactableID;
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
    }

    // Add entities to subInteractables list
    for (ObstacleEntity obstacleEntity : obstacleEntities) {
      if (obstacleEntity.interactableID != null) {
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

  @Override
  public void untrackEntity(Entity entity) {
    this.obstacleEntities.remove(entity);
    super.untrackEntity(entity);
  }

  private void spawnLevelFromFile() {
    // Load actual elements
    generateAll();

    // Generate tile bodies
    TerrainFactory.generateBodies(terrain.getMap());
  }


  private void spawnObstacle(ObstacleToolComponent.Obstacle selectedObstacle, int x, int y, int size) {
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
        return spawnJumppad(x, y, false, false);
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
    int startPosY = PLAYER_SPAWN.y;
    GridPoint2 startPos = new GridPoint2();
    startPos.set(-25, startPosY);

    Entity theVoid = NPCFactory.createTheVoid(player);
    spawnEntityAt(theVoid, startPos, true, true);
  }

  /**
   * Spawns a status effect as the given location
   */
  private void spawnStatusEffect(StatusEffect statusEffect, int posX, int posY) {
    System.out.println(statusEffect);
    System.out.println(posX);
    System.out.println(posY);
    Entity statusEffectEntity = NPCFactory.createStatusEffect(statusEffect);
    GridPoint2 position = new GridPoint2(posX, posY);
    spawnEntityAt(statusEffectEntity, position, true, true);
  }

  /**
   * Music Dictionary for intialisation of various sound effects
   * @param musicPath - String (see Music Directory for more information)
   */
  private void playTheMusic(String musicPath) {
    logger.debug("Playing game area music");
    MusicServiceDirectory dict = new  MusicServiceDirectory();
    MusicService gameMusic = null;
    switch (musicPath) {
      case "click":
        gameMusic = new MusicService(dict.click);
        logger.debug("Play jump song");
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
      case "level_3":
        gameMusic = new MusicService(dict.game_level_3);
        break;
      default:
        gameMusic = new MusicService(dict.game_level_1);//To make sure gameMusic is never null
    }
      //gameMusic.playMusic();
    gameMusic.playSong(true, 0.2f);

  }


  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(gameTextures);
    resourceService.loadTextureAtlases(gameTextureAtlases);
    resourceService.loadTextureAtlases(new String[] {levelFile.levelTexture.getAtlasName()});
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

  public LevelInfo getLevelInfo() {
    return this.levelInfo;
  }
}


