package com.deco2800.game.components.leveleditor;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.LevelGameArea;
import com.deco2800.game.effects.StatusEffect;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.screens.LevelEditorScreen;
import com.deco2800.game.services.ServiceLocator;

import java.util.List;

/**
 * Component for linking buttons to other entities
 */
public class StatusEffectToolComponent extends BaseToolComponent {
  private Entity selectedEffectEntity;
  private StatusEffect selectedEffect = StatusEffect.FAST;
  private LevelGameArea levelGameArea;
  private final LevelEditorScreen screen;
  private EditorUIComponent ui;
  private boolean removing = false;
  private boolean removeLock = false;

  public StatusEffectToolComponent(LevelGameArea levelGameArea, LevelEditorScreen screen, GdxGame game) {
    this.levelGameArea = levelGameArea;
    this.screen = screen;
    this.ui = new EditorUIComponent(levelGameArea, game);
  }

  /**
   * This function will scroll the selected status effect, similar to TileToolComponent.scrollTile
   * @param step either 1 for upward scrolling or -1 for downward scrolling
   */
  public void scrollEffect(int step) {
    StatusEffect[] statusEffects = StatusEffect.values();

    for (int i = step > 0 ? 0 : statusEffects.length-1; step > 0 ? i < statusEffects.length : i >= 0; i += step) {
      if (statusEffects[i].equals(selectedEffect)) {
        int newI = i + step;

        if (newI < 0) {
          selectedEffect = statusEffects[statusEffects.length-1];
        } else if (newI > statusEffects.length-1) {
          selectedEffect = statusEffects[0];
        } else {
          selectedEffect = statusEffects[newI];
        }

        break;
      }
    }

    if (selectedEffectEntity != null) {
      ServiceLocator.getEntityService().unregister(selectedEffectEntity);
      selectedEffectEntity.dispose();
    }

    selectedEffectEntity = NPCFactory.createMockStatusEffect(selectedEffect);
    ServiceLocator.getEntityService().register(selectedEffectEntity);
  }

  /**
   * Reposition the parent entity, ie when the mouse is moved.
   *
   * The tile should snap to a 0.5x0.5 coord grid
   */
  public void repositionEntity() {
    selectedEffectEntity.setPosition(getCellPos());
  }


  @Override
  public void create() {
    scrollEffect(1);
    repositionEntity();
    super.create();
  }

  @Override
  public void dispose() {
    ServiceLocator.getEntityService().unregister(selectedEffectEntity);
    selectedEffectEntity.dispose();
    super.dispose();
  }

  @Override
  public void update() {
    repositionEntity();

    if (removing) {
      removeLock = true;
      Vector2 pos = getMousePos();
      List<Entity> entities = levelGameArea.getEntities(pos);

      for (Entity entity : entities) {
        levelGameArea.untrackEntity(entity);
        ServiceLocator.getEntityService().unregister(entity);
        entity.dispose();
      }
      removeLock = false;
    }

    super.update();
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    Vector2 cellPos = getMousePos();
    int x = (int)(cellPos.x * 2), y = (int)(cellPos.y * 2);

    if (button == Input.Buttons.LEFT) {
      levelGameArea.spawnStatusEffect(selectedEffect, x, y);
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
  public boolean scrolled(float amountX, float amountY) {
    if ((int)amountY > 0) {
      scrollEffect(1);
    } else if ((int)amountY < 0) {
      scrollEffect(-1);
    }

    return super.scrolled(amountX, amountY);
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    repositionEntity();
    return true;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    repositionEntity();
    return true;
  }

  @Override
  public boolean keyUp(int keycode) {
    if (keycode == Input.Keys.TAB) {
      this.screen.selectTileHand();
    }

    if (keycode == Input.Keys.P) {
      ui.generateSavePopup();
    }

    return super.keyUp(keycode);
  }
}
