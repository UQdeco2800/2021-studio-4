package com.deco2800.game.components.leveleditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.services.ServiceLocator;

public class BaseToolComponent extends InputComponent {
  public Vector2 getMousePos() {
    // Convert the current mouse position to the correct units
    Vector3 pos = ServiceLocator.getCamera().getCamera().unproject(
      new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

    return new Vector2(pos.x, pos.y);
  }

  public Vector2 getCellPos() {
    // Convert units to the cell the mouse is in
    Vector2 mousePos = getMousePos();
    return new Vector2((int)Math.floor(mousePos.x * 2) * 0.5f, (int)Math.floor(mousePos.y * 2) * 0.5f);
  }
}
