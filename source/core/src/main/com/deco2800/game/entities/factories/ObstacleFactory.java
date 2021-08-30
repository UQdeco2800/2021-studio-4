package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.tasks.ChaseTask;
import com.deco2800.game.components.tasks.WanderTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.JumpableComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import org.w3c.dom.Text;

/**
 * Factory to create obstacle entities.
 *
 * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.
 */
public class ObstacleFactory {

  /**
   * Creates a tree entity.
   * @return entity
   */
  public static Entity createTree() {
    Entity tree =
        new Entity()
            .addComponent(new TextureRenderComponent("images/tree.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    tree.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    tree.getComponent(TextureRenderComponent.class).scaleEntity();
    tree.scaleHeight(2.5f);
    PhysicsUtils.setScaledCollider(tree, 0.5f, 0.2f);
    return tree;
  }

  /**
   * Creates an invisible physics wall.
   * @param width Wall width in world units
   * @param height Wall height in world units
   * @return Wall entity of given width and height
   */
  public static Entity createWall(float width, float height) {
    Entity wall = new Entity()
        .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
        .addComponent(new JumpableComponent()); //Added this and hitbox for possible jump functionality
    wall.setScale(width, height);
    return wall;
  }

  public static Entity createPlatform() {
//    Texture platform1 = new Texture("map-textures/mapTextures_Platforms.png");
//    platform1.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
//    TextureRegion textureRegion = new TextureRegion(platform1);
//    textureRegion.setRegion(0,0, 2*platform1.getWidth(), 2*platform1.getHeight() );
//    Image image = new Image(textureRegion);
    Entity platform =
            new Entity()
                    .addComponent(new TextureRenderComponent("map-textures/mapTextures_Platforms.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    platform.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    platform.getComponent(TextureRenderComponent.class).scaleEntity();
    platform.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(platform, 1f, 1f);
    return platform;
  }

  public static Entity createMiddlePlatform() {
    Entity platformWall =
            new Entity()
                    .addComponent(new TextureRenderComponent("map-textures/mapTextures_Middle-Platform.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    platformWall.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    platformWall.getComponent(TextureRenderComponent.class).scaleEntity();
    platformWall.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(platformWall, 1f, 1f);
    return platformWall;
  }

  public static Entity createButton() {
    Entity button =
      new Entity()
        .addComponent(new TextureRenderComponent("map-textures/mapTextures_Button-On.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    button.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    button.getComponent(TextureRenderComponent.class).scaleEntity();
    button.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(button, 0.1f, 1f);
    return button;
  }

  public static Entity createBridge() {
    Entity bridge =
      new Entity()
        .addComponent(new TextureRenderComponent("map-textures/mapTextures_bridge.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    bridge.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    bridge.getComponent(TextureRenderComponent.class).scaleEntity();
    bridge.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(bridge, 1f, 1f);
    return bridge;
  }

  public static Entity createDoor() {
    Entity door =
      new Entity()
        .addComponent(new TextureRenderComponent("map-textures/mapTextures_door.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    door.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    door.getComponent(TextureRenderComponent.class).scaleEntity();
    door.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(door, 1f, 1f);
    return door;
  }

  private ObstacleFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
