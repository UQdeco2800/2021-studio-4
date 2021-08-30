package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
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
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
    wall.setScale(width, height);
    return wall;
  }

  public static Entity createPlatform(int blocks) {
    Texture platformTexture = new Texture("map-textures/mapTextures_Platforms.png");
    platformTexture.getTextureData().prepare();
    Pixmap platformPixmap = platformTexture.getTextureData().consumePixmap();
    Pixmap pixmap = new Pixmap(blocks*platformTexture.getWidth(),platformTexture.getHeight(), Pixmap.Format.RGBA8888);
    for (int i = 0; i < blocks; i++) {
      pixmap.drawPixmap(platformPixmap,i*platformTexture.getWidth(),0);
    }
    Texture finalTexture = new Texture(pixmap);
    Entity platform =
            new Entity()
                    .addComponent(new TextureRenderComponent(finalTexture))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    platform.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    platform.getComponent(TextureRenderComponent.class).scaleEntity();
    platform.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(platform, 1f, 1f);
    return platform;
  }

  public static Entity createMiddlePlatform(int blocks) {
    Texture platformTexture = new Texture("map-textures/mapTextures_Middle-Platform.png");
    platformTexture.getTextureData().prepare();
    Pixmap platformPixmap = platformTexture.getTextureData().consumePixmap();
    Pixmap pixmap = new Pixmap(platformTexture.getWidth(),blocks*platformTexture.getHeight(), Pixmap.Format.RGBA8888);
    for (int i = 0; i < blocks; i++) {
      pixmap.drawPixmap(platformPixmap,0,i*platformTexture.getHeight());
    }
    Texture finalTexture = new Texture(pixmap);
    Entity platformWall =
            new Entity()
                    .addComponent(new TextureRenderComponent(finalTexture))
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
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE));

    button.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    button.getComponent(TextureRenderComponent.class).scaleEntity();
    button.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(button, 0.1f, 1f);
    return button;
  }

  public static Entity createBridge(int blocks) {
    Texture bridgeTexture = new Texture("map-textures/mapTextures_bridge.png");
    bridgeTexture.getTextureData().prepare();
    Pixmap platformPixmap = bridgeTexture.getTextureData().consumePixmap();
    Pixmap pixmap = new Pixmap(blocks*bridgeTexture.getWidth(),bridgeTexture.getHeight(), Pixmap.Format.RGBA8888);
    for (int i = 0; i < blocks; i++) {
      pixmap.drawPixmap(platformPixmap,i*bridgeTexture.getWidth(),0);
    }
    Texture finalTexture = new Texture(pixmap);
    Entity bridge =
      new Entity()
        .addComponent(new TextureRenderComponent(finalTexture))
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
