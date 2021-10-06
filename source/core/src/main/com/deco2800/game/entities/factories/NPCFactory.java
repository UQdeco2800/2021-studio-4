package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.npc.GhostAnimationController;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.npc.StatusEffectController;
import com.deco2800.game.components.npc.TheVoidController;
import com.deco2800.game.components.tasks.ChaseTask;
import com.deco2800.game.components.tasks.StatusEffectTasks;
import com.deco2800.game.components.tasks.TheVoidTasks;
import com.deco2800.game.components.tasks.WanderTask;
import com.deco2800.game.effects.StatusEffect;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.*;
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

//  private static final List<Entity> entitiesForRemoval = new ArrayList<>();
//
//
//  /**
//   * Adds the entities schedualed for removal to a list
//   */
//  public static void entitiesForRemovalAdd(Entity entity) {
//    entitiesForRemoval.add(entity);
//  }
//
//  /**
//   * Adds the entities schedualed for removal to a list
//   */
//  public static void RemoveEntities() {
//    for (Entity entity : entitiesForRemoval) {
//      entity.dispose();
//    }
//  }

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
                            .getAsset("images/void.atlas", TextureAtlas.class));
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
/**
  public static Entity createInterference(Entity target) {
    Entity interference = new Entity()
            .addComponent(new TextureRenderComponent("images/lizzy.png"));
    return interference;
  }
 */

  /**
   * Creates a ghost entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public static Entity createGhost(Entity target) {
    Entity ghost = createGroundNPC(target);
    BaseEntityConfig config = configs.ghost;

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/testingenemy.atlas", TextureAtlas.class));
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);

    ghost
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
        .addComponent(animator)
        .addComponent(new GhostAnimationController());

    ghost.getComponent(AnimationRenderComponent.class).scaleEntity();

    return ghost;
  }






  /**
   * Creates a gorgon gear entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public static Entity createGorgonGear(Entity target) {
    Entity gorgonGear = createBaseNPC(target);
    GorgonGearConfig config = configs.gorgonGear;

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService()
                .getAsset("images/gorgonGear.atlas", TextureAtlas.class));
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);

    gorgonGear
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
        .addComponent(animator)
        .addComponent(new GhostAnimationController());

    gorgonGear.getComponent(AnimationRenderComponent.class).scaleEntity();
    return gorgonGear;
  }



  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */
  private static Entity createGroundNPC(Entity target) {
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(7, 0), 0.3f));
//                    .addTask(new ChaseTask(target, 10, 3f, 4f));
    Entity npc =
            new Entity()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new ColliderComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                    .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f))
                    .addComponent(aiComponent);

    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    return npc;
  }



  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */
  private static Entity createBaseNPC(Entity target) {
    AITaskComponent aiComponent =
        new AITaskComponent()
            .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
            .addTask(new ChaseTask(target, 10, 3f, 4f));
    Entity npc =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f))
            .addComponent(aiComponent);

    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    return npc;
  }

  private NPCFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
