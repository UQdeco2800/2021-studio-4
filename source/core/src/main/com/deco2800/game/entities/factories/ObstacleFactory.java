package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.components.endgame.LevelEndComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.ObstacleDefinition;
import com.deco2800.game.entities.ObstacleEntity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.JumpableComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Factory to create obstacle entities.
 *
 * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.
 */
public class ObstacleFactory {
  /**
   * Uses pixmap to make a wider version of the same texture by repeating the texture widthways
   * @param texture Base texture
   * @param width Number of textures wide to make the new texture
   * @return The widened texture
   */
  private static Texture expandTexture(TextureRegion texture, int width, int height) {
    TextureData textureData = texture.getTexture().getTextureData();
    textureData.prepare();
    Pixmap tilePixmap = textureData.consumePixmap();
    Pixmap widePixmap = new Pixmap(
      texture.getRegionWidth()*width, texture.getRegionHeight()*height, Pixmap.Format.RGBA8888);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        widePixmap.drawPixmap(tilePixmap,
          x*texture.getRegionWidth(), y*texture.getRegionHeight(),
          texture.getRegionX(), texture.getRegionY(),
          texture.getRegionWidth(), texture.getRegionHeight());
      }
    }

    Texture wideTexture = new Texture(widePixmap);

    tilePixmap.dispose();
    widePixmap.dispose();

    return wideTexture;
  }

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
        .addComponent(new JumpableComponent()); //Added for jump functionality
    wall.setScale(width, height);
    return wall;
  }

  public static Entity createPlatform(int width) {
    TextureAtlas atlas = ServiceLocator.getResourceService()
      .getAsset("map-spritesheets/mapTextures.atlas", TextureAtlas.class);

    Texture platformTexture = expandTexture(atlas.findRegion("mapTextures_Platforms"), width, 1);

    ObstacleEntity platform =
            new ObstacleEntity(ObstacleDefinition.PLATFORM,width)
                    .addComponent(new TextureRenderComponent(platformTexture))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
                    .addComponent(new JumpableComponent()); //Added for jump functionality

    platform.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    platform.getComponent(TextureRenderComponent.class).scaleEntity();
    platform.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(platform, 1f, 1f);
    return platform;
  }

  public static Entity createMiddlePlatform(int width) {
    TextureAtlas atlas = ServiceLocator.getResourceService()
      .getAsset("map-spritesheets/mapTextures.atlas", TextureAtlas.class);

    Texture platformTexture = expandTexture(atlas.findRegion("mapTextures_Middle-Platform"), width, 1);

    ObstacleEntity platformWall =
            new ObstacleEntity(ObstacleDefinition.MIDDLE_PLATFORM,width)
                    .addComponent(new TextureRenderComponent(platformTexture))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
                    .addComponent(new JumpableComponent()); //Added for jump functionality

    platformWall.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    platformWall.getComponent(TextureRenderComponent.class).scaleEntity();
    platformWall.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(platformWall, 1f, 1f);
    return platformWall;
  }

  public static Entity createButton() {
    ObstacleEntity button =
      new ObstacleEntity(ObstacleDefinition.BUTTON,1)
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

  public static Entity createBridge(int width) {
    TextureAtlas atlas = ServiceLocator.getResourceService()
      .getAsset("map-spritesheets/mapTextures.atlas", TextureAtlas.class);

    Texture platformTexture = expandTexture(atlas.findRegion("mapTextures_bridge"), width, 1);

    ObstacleEntity bridge =
      new ObstacleEntity(ObstacleDefinition.BRIDGE,width)
        .addComponent(new TextureRenderComponent(platformTexture))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
        .addComponent(new JumpableComponent()); //Added for jump functionality

    bridge.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    bridge.getComponent(TextureRenderComponent.class).scaleEntity();
    bridge.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(bridge, 1f, 1f);
    return bridge;
  }

  public static Entity createDoor(int height) {
    TextureAtlas atlas = ServiceLocator.getResourceService()
      .getAsset("map-spritesheets/mapTextures.atlas", TextureAtlas.class);

    Texture platformTexture = expandTexture(atlas.findRegion("mapTextures_door"), 1, height);

    ObstacleEntity door =
      new ObstacleEntity(ObstacleDefinition.DOOR,height)
        .addComponent(new TextureRenderComponent(platformTexture))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    door.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    door.getComponent(TextureRenderComponent.class).scaleEntity();
    door.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(door, 1f, 1f);
    return door;
  }

  public static Entity createLevelEndPortal(int width) {
    TextureAtlas atlas = ServiceLocator.getResourceService()
      .getAsset("map-spritesheets/mapTextures.atlas", TextureAtlas.class);

    Texture levelEndPortalTexture = expandTexture(atlas.findRegion("mapTextures_Platforms"), width, 1);

    ObstacleEntity levelEndPortal =
      new ObstacleEntity(ObstacleDefinition.LEVEL_END_PORTAL,width)
        .addComponent(new TextureRenderComponent(levelEndPortalTexture))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
        .addComponent(new JumpableComponent())
        .addComponent(new LevelEndComponent()); // indicates end of level reached

    levelEndPortal.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    levelEndPortal.getComponent(TextureRenderComponent.class).scaleEntity();
    levelEndPortal.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(levelEndPortal, 1f, 1f);
    return levelEndPortal;
  }

  private ObstacleFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
