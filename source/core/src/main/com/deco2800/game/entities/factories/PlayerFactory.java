package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.PlayerMovementComponent;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.components.tasks.PlayerChangeAnimationHelper;
import com.deco2800.game.components.npc.StatusEffectsController;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.ObstacleEntity;
import com.deco2800.game.entities.configs.PlayerConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;

import java.util.List;
import java.util.Map;

/**
 * Factory to create a player entity.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */
public class PlayerFactory {
  private static final PlayerConfig stats =
      FileLoader.readClass(PlayerConfig.class, "configs/player.json");

  /**
   * Create a player entity.
   * @return entity
   */
  public static Entity createPlayer(Map<ObstacleEntity, List<ObstacleEntity>> mapInteractables) {
    InputComponent inputComponent =
        ServiceLocator.getInputService().getInputFactory().createForPlayer();

    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new PlayerChangeAnimationHelper());

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService()
                            .getAsset("images/player.atlas", TextureAtlas.class));
    animator.addAnimation("RunningRightDefault", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("RunningLeftDefault", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("IdleLeftDefault", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("IdleRightDefault", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("FallingRightDefault", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("FallingLeftDefault", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("SlidingRightDefault", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("SlidingLeftDefault", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("JumpingLeftDefault", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("JumpingRightDefault", 0.1f, Animation.PlayMode.LOOP);


    Entity player =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
            .addComponent(animator)
            .addComponent(new PlayerActions())
            .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack))
            .addComponent(new InventoryComponent(stats.gold))
            //.addComponent(new StatusEffectsController()) /** Added a new StatusEffects Component */
            .addComponent(inputComponent)
            .addComponent(new PlayerStatsDisplay())
            .addComponent(aiComponent)
                                                                                                  // Added in to allow                                          // for collision controlled jumping
            .addComponent(new PlayerMovementComponent(PhysicsLayer.OBSTACLE, mapInteractables)); // Added in to allow
                                                                                  // for collision controlled jumping
                                                    // Recently added mapInteractables for interactable functionality


    PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
    player.getComponent(ColliderComponent.class).setDensity(1.5f);
    player.getComponent(AnimationRenderComponent.class).scaleEntity();
    return player;
  }

  private PlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
