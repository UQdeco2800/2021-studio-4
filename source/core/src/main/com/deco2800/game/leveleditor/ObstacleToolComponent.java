package com.deco2800.game.leveleditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.areas.LevelGameArea;
import com.deco2800.game.areas.terrain.TerrainTile;
import com.deco2800.game.areas.terrain.TerrainTileDefinition;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.rendering.SpriteRenderComponent;
import com.deco2800.game.screens.LevelEditorScreen;
import com.deco2800.game.services.ServiceLocator;

import java.util.ArrayList;

/**
 * Component for editing the obstacles within the map, such as platforms etc
 */
public class ObstacleToolComponent extends InputComponent {
  private Obstacle selectedObstacle = Obstacle.PLATFORM;
  private Entity heldEntity;
  private LevelGameArea levelGameArea;
  private int size = 1;
  private boolean removeLock = false;
  private boolean removing = false;
  private final LevelEditorScreen screen;

  public ObstacleToolComponent(LevelGameArea levelGameArea, LevelEditorScreen screen) {
    this.levelGameArea = levelGameArea;
    this.screen = screen;
  }

  /**
   * Creates an entity given the obstacle type.
   * @param obstacleType The obstacle to generate
   * @return The obstacle entity
   */
  private Entity createObstacle(Obstacle obstacleType) {
    switch (obstacleType){
      case PLATFORM:
        return ObstacleFactory.createPlatform(size);
      case MIDDLE_PLATFORM:
        return ObstacleFactory.createMiddlePlatform(size);
      case DOOR:
        return ObstacleFactory.createDoor(size);
      case BRIDGE:
        return ObstacleFactory.createBridge(size);
      case BUTTON:
        return ObstacleFactory.createButton();
    }
    return null;
  }

  /**
   * Refreshes the held entity to make sure it's up to date
   *
   * Disposes of old entity then creates a new one
   */
  private void refreshHeldEntity() {
    if (this.heldEntity != null) {
      ServiceLocator.getEntityService().unregister(this.heldEntity);
      this.heldEntity.dispose();
    }
    this.heldEntity = createObstacle(selectedObstacle);
    ServiceLocator.getEntityService().register(this.heldEntity);
    repositionEntity();
  }

  /**
   * Spawns the obstacle into the game
   * @return The obstacle entity
   */
  private void spawnObstacle() {
    Vector2 cellPos = getMousePos();
    int x = (int)(cellPos.x * 2), y = (int)(cellPos.y * 2);

    GridPoint2 pos = new GridPoint2((int)(cellPos.x * 2), (int)(cellPos.y * 2));

    switch (selectedObstacle){
      case PLATFORM:
        levelGameArea.spawnPlatform(x, y, size, false, true);
        break;
      case MIDDLE_PLATFORM:
        levelGameArea.spawnMiddlePlatform(x, y, size, false, true);
        break;
      case DOOR:
        levelGameArea.spawnDoor(x, y, size, false, true);
        break;
      case BRIDGE:
        levelGameArea.spawnBridge(x, y, size, false, true);
        break;
      case BUTTON:
        levelGameArea.spawnButton(x, y, false, true);
        break;
    }
  }

  /**
   * This function will adjust the selected obstacle size by the selected step
   * @param step either 1 for upward scrolling or -1 for downward scrolling
   */
  public void scrollSize(int step) {
    size += step;

    if (size < 1) size = 1;
  }

  private Vector2 getMousePos() {
    // Convert the current mouse position to the correct units
    Vector3 pos = ServiceLocator.getCamera().getCamera().unproject(
      new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

    return new Vector2(pos.x, pos.y);
  }

  private Vector2 getCellPos() {
    // Convert units to the cell the mouse is in
    Vector2 mousePos = getMousePos();
    return new Vector2((int)Math.floor(mousePos.x * 2) * 0.5f, (int)Math.floor(mousePos.y * 2) * 0.5f);
  }

  /**
   * Reposition the parent entity, ie when the mouse is moved.
   *
   * The tile should snap to a 0.5x0.5 coord grid
   */
  public void repositionEntity() {
    heldEntity.setPosition(getCellPos());
  }

  @Override
  public void create() {
    refreshHeldEntity();
    super.create();
  }

  @Override
  public void update() {
    if (removing) {
      removeLock = true;
      Vector2 pos = getMousePos();
      ArrayList<Entity> entities = levelGameArea.getEntities(pos);

      for (Entity entity : entities) {
        levelGameArea.untrackEntity(entity);
        ServiceLocator.getEntityService().unregister(entity);
        entity.dispose();
      }
      removeLock = false;
    }
  }

  @Override
  public boolean scrolled(float amountX, float amountY) {
    scrollSize(((int)amountY) * -1);
    refreshHeldEntity();
    return super.scrolled(amountX, amountY);
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    repositionEntity();
    return true;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    repositionEntity();
    return true;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    if (button == Input.Buttons.LEFT) {
      spawnObstacle();
      return true;
    } else if (button == Input.Buttons.RIGHT && !removeLock) {
      removing = true;
      return true;
    }
    return super.touchDown(screenX, screenY, pointer, button);
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    if (button == Input.Buttons.RIGHT) {
      removing = false;
      return true;
    }
    return super.touchUp(screenX, screenY, pointer, button);
  }

  @Override
  public boolean keyUp(int keycode) {
    switch (keycode) {
      case (Input.Keys.Z):
        selectedObstacle = Obstacle.PLATFORM;
        refreshHeldEntity();
        break;
      case (Input.Keys.X):
        selectedObstacle = Obstacle.MIDDLE_PLATFORM;
        refreshHeldEntity();
        break;
      case (Input.Keys.C):
        selectedObstacle = Obstacle.BUTTON;
        refreshHeldEntity();
        break;
      case (Input.Keys.V):
        selectedObstacle = Obstacle.DOOR;
        refreshHeldEntity();
        break;
      case (Input.Keys.B):
        selectedObstacle = Obstacle.BRIDGE;
        refreshHeldEntity();
        break;
    }

    if (keycode == Input.Keys.TAB) {
      this.screen.selectTileHand();
    }

    return super.keyUp(keycode);
  }

  private enum Obstacle {
    PLATFORM, MIDDLE_PLATFORM, BUTTON, DOOR, BRIDGE
  }
}
