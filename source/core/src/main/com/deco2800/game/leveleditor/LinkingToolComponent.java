package com.deco2800.game.leveleditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.areas.LevelGameArea;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.physics.components.InteractableComponent;
import com.deco2800.game.physics.components.SubInteractableComponent;
import com.deco2800.game.screens.LevelEditorScreen;
import com.deco2800.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

/**
 * Component for linking buttons to other entities
 */
public class LinkingToolComponent extends BaseToolComponent {
  private Entity selectedParent;
  private List<Entity> markers = new ArrayList<>();
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
  private void createMarker(int x, int y, boolean isSubinteractable) {

    return;
  }
  
  /**
   * Links an entity to the selectedParent entity
   * @param entity Entity to link
   */
  private void link(Entity entity) {
    // check is subinteractable
    // add link if doesn't exist
    // add marker
  }

  /**
   * Unlinks an entity from the held entity
   * @param entity Entity to unlink
   */
  private void unlink(Entity entity) {
    // Check is subinteractable
    // remove link if exists
    // remove any makers
  }

  /**
   * Clears all linked entities from the parent
   */
  private void clearLinks() {

  }

  /**
   * Clears existing markers and sets the selectedParent.
   * Also adds markers from the loaded entity
   */
  private void setSelectedParent(Entity entity) {
    selectedParent = entity;
  }

  @Override
  public void create() {
    super.create();
  }

  @Override
  public void dispose() {
    for (Entity marker : markers) {
      ServiceLocator.getEntityService().unregister(marker);
      marker.dispose();
    }
    super.dispose();
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    Vector2 pos = getMousePos();
    ArrayList<Entity> entities = levelGameArea.getEntities(pos);

    if (button == Input.Buttons.LEFT) {
      // If there is an interactable component, set as the parent
      for (Entity entity : entities) {
        if (entity.getComponent(InteractableComponent.class) != null) {
          setSelectedParent(entity);
          return true;
        }
      }

      // Otherwise link entities
      for (Entity entity : entities) {
        if (entity.getComponent(SubInteractableComponent.class) != null) {
          link(entity);
        }
      }
      return true;
    } else if (button == Input.Buttons.RIGHT) {
      for (Entity entity : entities) {
        if (entity.getComponent(SubInteractableComponent.class) != null) {
          unlink(entity);
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
