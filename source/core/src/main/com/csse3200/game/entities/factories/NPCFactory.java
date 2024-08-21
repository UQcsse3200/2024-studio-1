package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.GhostAnimationController;
import com.csse3200.game.components.npc.NPCDamageHandlerComponent;
import com.csse3200.game.components.npc.NPCDeathHandler;
import com.csse3200.game.components.npc.NPCHealthBarComponent;
import com.csse3200.game.components.npc.RatAnimationController;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.tasks.ChargeTask;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.StraightWanderTask;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BaseEntityConfig;
import com.csse3200.game.entities.configs.GhostKingConfig;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

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
   * Creates a rat entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public static Entity createRat(Entity target) {
    BaseEntityConfig config = configs.rat;
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new StraightWanderTask(2f))
                    .addTask(new ChargeTask(target, 10, 3f, 3f, 1.5f));

    Entity rat = createBaseNPC(aiComponent);

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/rat.atlas", TextureAtlas.class));
    animator.addAnimation("idle", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("gesture", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("walk", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("attack", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("death", 0.1f, Animation.PlayMode.NORMAL);

    rat
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
        .addComponent(animator)
        .addComponent(new RatAnimationController())
        .addComponent(new NPCHealthBarComponent())
        .addComponent(new NPCDamageHandlerComponent())
        .addComponent(new NPCDeathHandler());

    rat.getComponent(AnimationRenderComponent.class).scaleEntity();
    return rat;
  }

  /**
   * Creates a dog entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public static Entity createDog(Entity target) {
    BaseEntityConfig config = configs.dog;
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(4f, 4f), 2f, 0.7f))
                    .addTask(new ChargeTask(target, 10, 5f, 6f, 4f));

    Entity dog = createBaseNPC(aiComponent);

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService()
                .getAsset("images/ghostKing.atlas", TextureAtlas.class));
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
//    animator.addAnimation("death", 0.1f, Animation.PlayMode.NORMAL);

    dog
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
        .addComponent(animator)
        .addComponent(new GhostAnimationController())
        .addComponent(new NPCHealthBarComponent())
        .addComponent(new NPCDamageHandlerComponent())
        .addComponent(new NPCDeathHandler());


    dog.getComponent(AnimationRenderComponent.class).scaleEntity();
    return dog;
  }

  /**
   * Creates a crocodile entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public static Entity createCroc(Entity target) {
    BaseEntityConfig config = configs.croc;
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(1.5f, 1.5f), 5f, 0.1f))
                    .addTask(new ChaseTask(target, 10, 2f, 2f, 0.2f));

    Entity croc = createBaseNPC(aiComponent);

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/ghost.atlas", TextureAtlas.class));
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);

    croc
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(animator)
            .addComponent(new GhostAnimationController())
            .addComponent(new NPCHealthBarComponent())
            .addComponent(new NPCDamageHandlerComponent())
            .addComponent(new NPCDeathHandler());

    croc.getComponent(AnimationRenderComponent.class).scaleEntity();

    return croc;
  }

  /**
   * Creates a gorilla entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public static Entity createGorilla(Entity target) {
    BaseEntityConfig config = configs.gorilla;
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(3f, 3f), 4f, 0.5f))
                    .addTask(new ChaseTask(target, 10, 3f, 7f, 1f));

    Entity gorilla = createBaseNPC(aiComponent);


    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService()
                            .getAsset("images/ghostKing.atlas", TextureAtlas.class));
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);

    gorilla
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(animator)
            .addComponent(new GhostAnimationController())
            .addComponent(new NPCHealthBarComponent())
            .addComponent(new NPCDamageHandlerComponent())
            .addComponent(new NPCDeathHandler());

    gorilla.getComponent(AnimationRenderComponent.class).scaleEntity();
    return gorilla;
  }


  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */
  private static Entity createBaseNPC(AITaskComponent aiComponent) {
    Entity npc = new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f))
            .addComponent(aiComponent)
            .addComponent(new NPCHealthBarComponent())
            .addComponent(new NPCDamageHandlerComponent())
            .addComponent(new NPCDeathHandler());
    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    return npc;
  }

  private NPCFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
