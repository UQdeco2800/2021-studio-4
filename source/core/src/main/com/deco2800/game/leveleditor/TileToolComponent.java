package com.deco2800.game.leveleditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.areas.LevelGameArea;
import com.deco2800.game.areas.terrain.TerrainTile;
import com.deco2800.game.areas.terrain.TerrainTileDefinition;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.rendering.SpriteRenderComponent;
import com.deco2800.game.screens.LevelEditorScreen;
import com.deco2800.game.services.ServiceLocator;
/**
 * Component for editing the terrain map
 */
public class TileToolComponent extends InputComponent {
  private final LevelEditorScreen screen;
  private EditorUIComponent ui;

  private TerrainTileDefinition tileDefinition = TerrainTileDefinition.TILE_FULL_MIDDLE;
  private Sprite currentSprite;
  private LevelGameArea levelGameArea;

  private boolean placing = false;
  private boolean removing = false;

  public TileToolComponent(LevelGameArea levelGameArea, LevelEditorScreen screen) {
    this.levelGameArea = levelGameArea;
    this.screen = screen;
    this.ui = new EditorUIComponent(levelGameArea);
  }

  /**
   * This function will scroll the selected tile in the direction of the input step.
   *
   * If step is 1 (forward scrolling), then the loop will be iterated forwards, and the next tile in line from the
   * one currently in use is selected.
   *
   * If step is -1 (reverse scrolling), then the loop is iterated backwards and the tile _before_ the selected one
   * is used.
   * @param step either 1 for upward scrolling or -1 for downward scrolling
   */
  public void scrollTile(int step) {
    TerrainTileDefinition[] tileDefinitions = TerrainTileDefinition.values();

    for (int i = step > 0 ? 0 : tileDefinitions.length-1; step > 0 ? i < tileDefinitions.length : i >= 0; i += step) {
      if (tileDefinitions[i].equals(tileDefinition)) {
        int newI = i + step;

        if (newI < 0) {
          tileDefinition = tileDefinitions[tileDefinitions.length-1];
        } else if (newI > tileDefinitions.length-1) {
          tileDefinition = tileDefinitions[0];
        } else {
          tileDefinition = tileDefinitions[newI];
        }

        break;
      }
    }

    currentSprite = tileDefinition.getSprite();
    this.getEntity().getComponent(SpriteRenderComponent.class).setSprite(currentSprite);
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
    getEntity().setPosition(getCellPos());
  }

  @Override
  public void create() {
    super.create();
  }

  @Override
  public void update() {
    if (placing) {
      SpriteRenderComponent spriteRenderComponent = this.getEntity().getComponent(SpriteRenderComponent.class);
      TerrainTile terrainTile = new TerrainTile(tileDefinition,
        (int)spriteRenderComponent.getRotation(),
        spriteRenderComponent.getFlipX(),
        spriteRenderComponent.getFlipY());

      Vector2 cellPos = getMousePos();
      levelGameArea.setTerrainCell(terrainTile, (int)(cellPos.x * 2), (int)(cellPos.y * 2));
    } else if (removing) {
      Vector2 cellPos = getMousePos();
      levelGameArea.clearTerrainCell((int)(cellPos.x * 2), (int)(cellPos.y * 2));
    }
    super.update();
  }

  @Override
  public boolean scrolled(float amountX, float amountY) {
    if ((int)amountY > 0) {
      scrollTile(1);
    } else if ((int)amountY < 0) {
      scrollTile(-1);
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
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    if (button == Input.Buttons.LEFT) {
      placing = true;
      return true;
    } else if (button == Input.Buttons.RIGHT) {
      removing = true;
      return true;
    }
    return super.touchDown(screenX, screenY, pointer, button);
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    if (button == Input.Buttons.LEFT) {
      placing = false;
      return true;
    } else if (button == Input.Buttons.RIGHT) {
      removing = false;
      return true;
    }
    return super.touchUp(screenX, screenY, pointer, button);
  }

  @Override
  public boolean keyUp(int keycode) {
    if (tileDefinition.isRotateable() && keycode == Input.Keys.R) {
      this.getEntity().getComponent(SpriteRenderComponent.class).rotate90();
    } else if (tileDefinition.isFlipable() && keycode == Input.Keys.UP) {
      this.getEntity().getComponent(SpriteRenderComponent.class).flipY();
    } else if (tileDefinition.isFlipable() && keycode == Input.Keys.RIGHT) {
      this.getEntity().getComponent(SpriteRenderComponent.class).flipX();
    } else if (keycode == Input.Keys.SHIFT_LEFT) {
      scrollTile(-1);
    }

    if (keycode == Input.Keys.TAB) {
      this.screen.selectObstacleHand();
    }
    if (keycode == Input.Keys.P) {
      ui.generatePopup();
    }

    return super.keyUp(keycode);
  }
}
