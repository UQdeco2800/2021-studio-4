package com.deco2800.game.leveleditor;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.LevelGameArea;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.ObstacleEntity;
import com.deco2800.game.physics.components.InteractableComponent;
import com.deco2800.game.physics.components.SubInteractableComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.screens.LevelEditorScreen;
import com.deco2800.game.services.ServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Component for linking buttons to other entities
 */
public class LinkingToolComponent extends BaseToolComponent {
  private ObstacleEntity selectedParent;
  private Map<Entity, Entity> markers = new HashMap<>();
  private LevelGameArea levelGameArea;
  private final LevelEditorScreen screen;
  private EditorUIComponent ui;

  public LinkingToolComponent(LevelGameArea levelGameArea, LevelEditorScreen screen) {
    this.levelGameArea = levelGameArea;
    this.screen = screen;
    this.ui = new EditorUIComponent(levelGameArea);
  }

  /**
   * Creates an entity marking an entity at position x, y as linked
   */
  private void createMarker(Entity target, boolean isSubinteractable) {
    String textureName = isSubinteractable ? "map-textures/marker_cross.png" : "map-textures/marker_o.png";
    Entity marker = new Entity();
    marker
      .addComponent(new TextureRenderComponent(textureName));
    marker.scaleHeight(0.5f);
    marker.setPosition(target.getPosition().cpy());
    markers.put(target, marker);
    ServiceLocator.getEntityService().register(marker);
  }
  
  /**
   * Links an entity to the selectedParent entity
   * @param entity Entity to link
   */
  private void link(Entity entity) {
    ObstacleEntity obstacleEntity = levelGameArea.getObstacle(entity);
    SubInteractableComponent subComponent = obstacleEntity.getComponent(SubInteractableComponent.class);
    InteractableComponent parentComponent = selectedParent.getComponent(InteractableComponent.class);

    if (subComponent == null) return;

    /*
    if (!levelGameArea.mapInteractables.containsKey(selectedParent)) {
      levelGameArea.mapInteractables.put(selectedParent, new ArrayList<>());
    } else if (!levelGameArea.mapInteractables.get(selectedParent).contains(obstacleEntity)){
      levelGameArea.mapInteractables.get(selectedParent).add(obstacleEntity);
    }
     */

    if (!levelGameArea.interactableEntities.contains(selectedParent)) {
      levelGameArea.interactableEntities.add(selectedParent);
    }

    if (parentComponent.getMapped().contains(entity)) return;

    parentComponent.addSubInteractable(obstacleEntity);

    createMarker(obstacleEntity,true);
  }

  /**
   * Unlinks an entity from the held entity
   * @param entity Entity to unlink
   */
  private void unlink(Entity entity) {
    ObstacleEntity obstacleEntity = levelGameArea.getObstacle(entity);
    SubInteractableComponent subComponent = obstacleEntity.getComponent(SubInteractableComponent.class);
    InteractableComponent parentComponent = selectedParent.getComponent(InteractableComponent.class);

    if (subComponent == null) return;

    /*
    if (levelGameArea.mapInteractables.containsKey(selectedParent)) {
      levelGameArea.mapInteractables.get(selectedParent).remove(obstacleEntity);
    }
     */

    if (levelGameArea.interactableEntities.contains(selectedParent)) {
      parentComponent.getMapped().remove(obstacleEntity);
    }

    ServiceLocator.getEntityService().unregister(markers.get(obstacleEntity));
    markers.get(obstacleEntity).dispose();
    markers.remove(obstacleEntity);
  }

  /**
   * Disposes all markers
   */
  private void clearMarkers() {
    for (Map.Entry<Entity, Entity> entityEntityEntry : markers.entrySet()) {
      ServiceLocator.getEntityService().unregister(entityEntityEntry.getValue());
      entityEntityEntry.getValue().dispose();
    }
  }

  /**
   * Clears all linked entities from the parent
   */
  private void clearLinks() {
    clearMarkers();
    levelGameArea.interactableEntities.remove(selectedParent);
    markers = new HashMap<>();
  }

  /**
   * Clears existing markers and sets the selectedParent.
   * Also adds markers from the loaded entity
   */
  private void setSelectedParent(Entity entity) {
    selectedParent = levelGameArea.getObstacle(entity);
    InteractableComponent parentComponent = selectedParent.getComponent(InteractableComponent.class);

    clearMarkers();

    createMarker(selectedParent, false);

    /*
    if (levelGameArea.mapInteractables.containsKey(selectedParent)) {
      List<ObstacleEntity> linked = levelGameArea.mapInteractables.get(selectedParent);

      for (ObstacleEntity obstacleEntity : linked) {
        createMarker(obstacleEntity, true);
      }
    }
     */

    if (levelGameArea.interactableEntities.contains(selectedParent)) {
      List<ObstacleEntity> linked = parentComponent.getMapped();

      for (ObstacleEntity obstacleEntity : linked) {
        createMarker(obstacleEntity, true);
      }
    }

  }

  @Override
  public void create() {
    super.create();
  }

  @Override
  public void dispose() {
    clearMarkers();
    super.dispose();
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    Vector2 pos = getMousePos();
    List<Entity> entities = levelGameArea.getEntities(pos);

    if (button == Input.Buttons.LEFT) {
      // If there is an interactable component, set as the parent
      for (Entity entity : entities) {
        if (entity.getComponent(InteractableComponent.class) != null) {
          setSelectedParent(entity);
          return true;
        }
      }

      // Otherwise link entities
      if (selectedParent != null) {
        for (Entity entity : entities) {
          if (entity.getComponent(SubInteractableComponent.class) != null) {
            link(entity);
          }
        }
      }
      return true;
    } else if (button == Input.Buttons.RIGHT) {
      if (selectedParent != null) {
        for (Entity entity : entities) {
          if (entity.getComponent(SubInteractableComponent.class) != null) {
            unlink(entity);
          }
        }
      }
      return true;
    }
    return super.touchDown(screenX, screenY, pointer, button);
  }

  @Override
  public boolean keyUp(int keycode) {
    if (keycode == Input.Keys.DEL) {
      clearLinks();
    }

    if (keycode == Input.Keys.TAB) {
      this.screen.selectTileHand();
    }

    if (keycode == Input.Keys.P) {
      ui.generateSavePopup();
    }

    return super.keyUp(keycode);
  }
}
