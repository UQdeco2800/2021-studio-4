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
import com.deco2800.game.levels.LevelTexture;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.*;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Factory to create obstacle entities.
 *
 * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.
 */
public class ObstacleFactory {
  public static LevelTexture currentTextures = LevelTexture.LEVEL_ONE;


  private static Pixmap textureRegionToPixmap(TextureRegion textureRegion) {
    // Get texture data for entire atlas
    TextureData textureData = textureRegion.getTexture().getTextureData();

    // Prepare texture data
    if (!textureData.isPrepared()) {
      textureData.prepare();
    }

    // Create a new appropriately sized pixmap
    Pixmap newPixmap = new Pixmap(
      textureRegion.getRegionWidth(),
      textureRegion.getRegionHeight(),
      textureData.getFormat()
    );

    // Draw the texture atlas over the new pixmap, in such a location that the unwanted pixels are cropped off
    newPixmap.drawPixmap(
      textureData.consumePixmap(),
      0, 0, // top left corner
      textureRegion.getRegionX(), textureRegion.getRegionY(), // source x/y
      textureRegion.getRegionWidth(), textureRegion.getRegionHeight() // width/height to copy over
    );

    return newPixmap;
  }

  /**
   * Uses pixmap to make a wider version of the same texture by repeating the texture widthways
   * @param texture Base texture
   * @param width Number of textures wide to make the new texture
   * @return The widened texture
   */
  private static Texture expandTexture(Pixmap tilePixmap, TextureRegion texture, int width, int height) {
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

  public static Entity createPlatform(int width) {
    TextureAtlas atlas = ServiceLocator.getResourceService()
      .getAsset(currentTextures.getAtlasName(), TextureAtlas.class);

    TextureRegion textureRegion = atlas.findRegion("mapTextures_Platforms");
    Pixmap tilePixmap = textureRegionToPixmap(textureRegion);

    Texture platformTexture = expandTexture(tilePixmap, textureRegion, width, 1);

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
      .getAsset(currentTextures.getAtlasName(), TextureAtlas.class);

    TextureRegion textureRegion = atlas.findRegion("mapTextures_Middle-Platform");
    Pixmap tilePixmap = textureRegionToPixmap(textureRegion);

    Texture platformTexture = expandTexture(tilePixmap, textureRegion, width, 1);

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
    TextureAtlas atlas = ServiceLocator.getResourceService()
      .getAsset(currentTextures.getAtlasName(), TextureAtlas.class);

    TextureRegion textureRegion = atlas.findRegion("mapTextures_Button-On");
    Pixmap tilePixmap = textureRegionToPixmap(textureRegion);

    Texture buttonTexture = expandTexture(tilePixmap, textureRegion, 1, 1);

    ObstacleEntity button =
      new ObstacleEntity(ObstacleDefinition.BUTTON,1)
        .addComponent(new TextureRenderComponent(buttonTexture))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
        .addComponent(new InteractableComponent());


    button.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    button.getComponent(TextureRenderComponent.class).scaleEntity();
    button.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(button, 0.1f, 1f);
    return button;
  }

  /**
   * Creates a jump pad entity.
   * @return jump pad
   */
  public static Entity createJumpPad() {
    TextureAtlas atlas = ServiceLocator.getResourceService()
      .getAsset(currentTextures.getAtlasName(), TextureAtlas.class);

    TextureRegion textureRegion = atlas.findRegion("mapTextures_Jumppad-idle");
    Pixmap tilePixmap = textureRegionToPixmap(textureRegion);

    Texture jumpPadTexture = expandTexture(tilePixmap, textureRegion, 1, 1);

    ObstacleEntity jumpPad =
            new ObstacleEntity(ObstacleDefinition.JUMPPAD,1)
                    .addComponent(new TextureRenderComponent(jumpPadTexture))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                    .addComponent(new InteractableComponent())
                    .addComponent(new JumpPadComponent());

    jumpPad.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    jumpPad.getComponent(TextureRenderComponent.class).scaleEntity();
    jumpPad.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(jumpPad, 0.1f, 1f);
    return jumpPad;
  }

  public static Entity createBridge(int width) {
    TextureAtlas atlas = ServiceLocator.getResourceService()
      .getAsset(currentTextures.getAtlasName(), TextureAtlas.class);

    TextureRegion textureRegion = atlas.findRegion("mapTextures_bridge");
    Pixmap tilePixmap = textureRegionToPixmap(textureRegion);

    Texture platformTexture = expandTexture(tilePixmap, textureRegion, width, 1);

    ObstacleEntity bridge =
      new ObstacleEntity(ObstacleDefinition.BRIDGE,width)
        .addComponent(new TextureRenderComponent(platformTexture))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
        .addComponent(new JumpableComponent()) //Added for jump functionality
        .addComponent(new SubInteractableComponent());

    bridge.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    bridge.getComponent(TextureRenderComponent.class).scaleEntity();

    bridge.getComponent(ColliderComponent.class).setSensor(true);
    bridge.getComponent(HitboxComponent.class).setSensor(true); // starts with collisions

    bridge.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(bridge, 1f, 1f);
    return bridge;
  }

  public static Entity createDoor(int height) {
    TextureAtlas atlas = ServiceLocator.getResourceService()
      .getAsset(currentTextures.getAtlasName(), TextureAtlas.class);

    TextureRegion textureRegion = atlas.findRegion("mapTextures_door");
    Pixmap tilePixmap = textureRegionToPixmap(textureRegion);

    Texture doorTexture = expandTexture(tilePixmap, textureRegion, 1, height);

    ObstacleEntity door =
      new ObstacleEntity(ObstacleDefinition.DOOR, height)
        .addComponent(new TextureRenderComponent(doorTexture))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
        .addComponent(new SubInteractableComponent());

    door.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    door.getComponent(TextureRenderComponent.class).scaleEntity();
    door.scaleHeight(0.5f);
    PhysicsUtils.setScaledCollider(door, 1f, 1f);
    return door;
  }

  public static Entity createLevelEndPortal(int width) {
    ObstacleEntity levelEndPortal =
      new ObstacleEntity(ObstacleDefinition.LEVEL_END_PORTAL,width)
        .addComponent(new TextureRenderComponent("map-textures/end_portal.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
        .addComponent(new JumpableComponent())
        .addComponent(new LevelEndComponent()); // indicates end of level reached


    //.addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
    levelEndPortal.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    levelEndPortal.getComponent(TextureRenderComponent.class).scaleEntity();
    levelEndPortal.scaleHeight(4f);
    PhysicsUtils.setScaledColliderForEndPortal(levelEndPortal, 1f, 0.25f);
    PhysicsUtils.setScaledColliderForEndPortal(levelEndPortal,0.25f,1f);
    return levelEndPortal;
  }

  private ObstacleFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
