package com.deco2800.game.leveleditor;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.rendering.SpriteRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Component for editing the terrain map
 */
public class CameraMoveComponent extends InputComponent {
  private boolean moveUp = false;
  private boolean moveDown = false;
  private boolean moveLeft = false;
  private boolean moveRight = false;

  @Override
  public void update() {
    CameraComponent cameraComponent = ServiceLocator.getCamera();
    Vector2 camPos = cameraComponent.getEntity().getPosition();
    if (moveUp) {
      camPos.y += 0.25f;
    } else if (moveDown) {
      camPos.y -= 0.25f;
    } else if (moveLeft) {
      camPos.x -= 0.25f;
    } else if (moveRight) {
      camPos.x += 0.25f;
    }
    cameraComponent.getEntity().setPosition(camPos);
    super.update();
  }

  @Override
  public boolean keyDown(int keycode) {
    switch (keycode) {
      case (Input.Keys.W):
        moveUp = true;
        break;
      case (Input.Keys.S):
        moveDown = true;
        break;
      case (Input.Keys.A):
        moveLeft = true;
        break;
      case (Input.Keys.D):
        moveRight = true;
        break;
    }

    return super.keyUp(keycode);
  }

  @Override
  public boolean keyUp(int keycode) {
    switch (keycode) {
      case (Input.Keys.W):
        moveUp = false;
        break;
      case (Input.Keys.S):
        moveDown = false;
        break;
      case (Input.Keys.A):
        moveLeft = false;
        break;
      case (Input.Keys.D):
        moveRight = false;
        break;
    }

    return super.keyUp(keycode);
  }
}
