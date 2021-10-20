package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.npc.StatusEffectController;
import com.deco2800.game.components.npc.TheVoidController;
import com.deco2800.game.components.tasks.StatusEffectTasks;
import com.deco2800.game.components.tasks.TheVoidTasks;
import com.deco2800.game.effects.StatusEffect;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.NPCConfigs;
import com.deco2800.game.entities.configs.TheVoidConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Factory to create non-playable character (NPC) entities with predefined components.
 *
 * <p>Each NPC entity type should have a creation method that returns a corresponding entity.
 * Predefined entity properties can be loaded from configs stored as json files which are defined in
 * "NPCConfigs".
 *
 * <p>If needed, this factory can be separated into more specific factories for entities with
 * similar characteristics.
 */
public class NPCFactory {
  private static final NPCConfigs configs =
      FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

  /**
   * Creates the void entity
   *
   * @param target entity whose distance from the void will be tracked (the player)
   * @return entity
   */
  public static Entity createTheVoid(Entity target) {
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new TheVoidTasks());

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService()
                            .getAsset("void/void.atlas", TextureAtlas.class));
    animator.addAnimation("void", 0.1f, Animation.PlayMode.LOOP);

    Entity theVoid = new Entity();
    TheVoidConfig config = configs.theVoid;

    theVoid
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new PhysicsComponent())
            .addComponent(new TheVoidController(target))
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f))
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(aiComponent)
            .addComponent(animator);

    theVoid.getComponent(AnimationRenderComponent.class).scaleEntity();
    theVoid.setScale(20f,22);
    return theVoid;
  }

  /**
   * Creates a statusEffect (PowerUp) entity.
   *
   * @return entity
   */
  public static Entity createStatusEffect(StatusEffect effect) {
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new StatusEffectTasks());

    // Will create switch statement on effect to determine which image to use based on effect variable/string.

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService()
                            .getAsset(effect.getGroundAnimationAtlas(), TextureAtlas.class));
    animator.addAnimation(effect.getGroundAnimationName(), 0.1f, Animation.PlayMode.LOOP);

    Entity statusEffect = new Entity();

    statusEffect
            .addComponent(new PhysicsComponent())
            .addComponent(new StatusEffectController(effect))
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new ColliderComponent())
            .addComponent(aiComponent)
            .addComponent(animator);

    statusEffect.setScale(0.5f,0.5f);
    statusEffect.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
    PhysicsUtils.setScaledCollider(statusEffect, 0.5f,0.5f);

    System.out.println(statusEffect);

    return statusEffect;
  }


/* Method that is supposed to spawn an entity that would block the player's view of game.
Shelved because this method cannot spawn entity mid-game.
 */

  private NPCFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
