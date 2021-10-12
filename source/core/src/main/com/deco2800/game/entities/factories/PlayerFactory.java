package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.LevelGameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.PlayerMovementComponent;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.components.statuseffects.StatusEffectTargetComponent;
import com.deco2800.game.components.statuseffects.StatusEffectUIComponent;
import com.deco2800.game.components.tasks.PlayerChangeAnimationHelper;
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
  public static Entity createPlayer(Map<ObstacleEntity, List<ObstacleEntity>> mapInteractables, LevelGameArea levelGameArea) {
    InputComponent inputComponent =
        ServiceLocator.getInputService().getInputFactory().createForPlayer();

    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new PlayerChangeAnimationHelper());


    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("player/simple_player_sprite.atlas", TextureAtlas.class));

    animator.addAnimation("spawn_level1", 0.1f, Animation.PlayMode.NORMAL);
    animator.addAnimation("portal_flip", 0.1f, Animation.PlayMode.NORMAL);
    animator.addAnimation("spawn_portal", 0.1f, Animation.PlayMode.NORMAL);
    animator.addAnimation("death", 0.1f, Animation.PlayMode.NORMAL);



    String[] movement = {"Running", "Jump", "Sliding", "Falling", "Idle", "Walk", "Slow"};
    String[] direction = {"Left", "Right"};

    for(String mov : movement) {
      for (String dir : direction) {
        if (mov.equals("Jump") | mov.equals("Sliding") | mov.equals("Falling")) {
          animator.addAnimation(mov + dir, 0.03f, Animation.PlayMode.NORMAL);
        } else if (mov.equals("Slow")){
          animator.addAnimation(mov + dir, 0.16f, Animation.PlayMode.LOOP);
        } else {
          animator.addAnimation(mov + dir, 0.08f, Animation.PlayMode.LOOP);
        }
      }
    }






    Entity player =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
            .addComponent(animator)
            .addComponent(new PlayerActions(levelGameArea.getLevelDefinition()))
            .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack))
            .addComponent(new InventoryComponent(stats.gold))
            .addComponent(inputComponent)
            .addComponent(new PlayerStatsDisplay())
            .addComponent(aiComponent)
            .addComponent(new PlayerMovementComponent(PhysicsLayer.OBSTACLE, mapInteractables, levelGameArea))
            .addComponent(new StatusEffectTargetComponent())
            .addComponent(new StatusEffectUIComponent());



    PhysicsUtils.setScaledCollider(player, 0.8f, 0.7f);
    player.getComponent(ColliderComponent.class).setDensity(0.4f);
    //player.getComponent(AnimationRenderComponent.class).scaleEntity();
    player.setScale(1.5f,1f);
    return player;


  }

  private PlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
