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
import com.csse3200.game.components.npc.SnakeAnimationController;
import com.csse3200.game.components.npc.MinotaurAnimationController;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.tasks.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BaseEntityConfig;
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
   * Creates a rat entity with predefined components and behaviour.
   *
   * @param target entity to chase
   * @return the created rat entity
   */
  public static Entity createRat(Entity target) {
    BaseEntityConfig config = configs.rat;
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new StraightWanderTask(2f))
                    .addTask(new ChaseTask(target, 9, 5f, 6f, 2f))
                    .addTask(new AttackTask(target, 10, 2f, 2.5f));
    
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
        .addComponent(new CombatStatsComponent(
                config.health,
                config.baseAttack))
        .addComponent(animator)
        .addComponent(new RatAnimationController())
        .addComponent(new NPCHealthBarComponent())
        .addComponent(new NPCDamageHandlerComponent())
        .addComponent(new NPCDeathHandler());

    rat.getComponent(AnimationRenderComponent.class).scaleEntity();
    return rat;
  }

  /**
   * Creates a Snake entity.
   *
   * @param target entity to chase
   * @return entity
   */
    public static Entity createSnake(Entity target) {
        BaseEntityConfig config = configs.snake;
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(3f, 3f), 4f, config.wanderSpeed))
                        .addTask(new ChaseTask(target, 10, config.viewDistance, config.chaseDistance,
                                config.chaseSpeed))
                        .addTask(new AttackTask(target, 10, 2f, 2.5f));


        Entity snake = createBaseNPC(aiComponent);

        AnimationRenderComponent animator =
            new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/snake.atlas", TextureAtlas.class));
        animator.addAnimation("idle", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("gesture", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("walk", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("attack", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("death", 0.1f, Animation.PlayMode.NORMAL);

        snake
            .addComponent(new CombatStatsComponent(
                    config.health,
                    config.baseAttack))
            .addComponent(animator)
            .addComponent(new SnakeAnimationController())
            .addComponent(new NPCHealthBarComponent())
            .addComponent(new NPCDamageHandlerComponent())
            .addComponent(new NPCDeathHandler());

        snake.getComponent(AnimationRenderComponent.class).scaleEntity();
        return snake;
    }


  /**
   * Creates a Minotaur entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public static Entity createMinotaur(Entity target) {
    BaseEntityConfig config = configs.minotaur;
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new StraightWanderTask(1.5f))
                    .addTask(new ChaseTask(target, 12, 4f, 8f, 3f))
                    .addTask(new AttackTask(target, 15, 3f, 3.5f));

    Entity minotaur = createBaseNPC(aiComponent);

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/minotaur.atlas", TextureAtlas.class));
    animator.addAnimation("idle", 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation("gesture", 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation("walk", 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation("attack", 0.15f, Animation.PlayMode.LOOP);
    animator.addAnimation("death", 0.2f, Animation.PlayMode.NORMAL);

    minotaur
            .addComponent(new CombatStatsComponent(
                    config.health,
                    config.baseAttack))
            .addComponent(animator)
            .addComponent(new MinotaurAnimationController())
            .addComponent(new NPCHealthBarComponent())
            .addComponent(new NPCDamageHandlerComponent())
            .addComponent(new NPCDeathHandler());

    minotaur.getComponent(AnimationRenderComponent.class).scaleEntity();
    return minotaur;
  }


  /**
   * Creates a dog entity with predefined components and behaviour.
   *
   * @param target entity to chase
   * @return the created dog entity
   */
  public static Entity createDog(Entity target) {
    BaseEntityConfig config = configs.dog;
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(4f, 4f), 2f, config.wanderSpeed))
                    .addTask(new ChargeTask(target, 10, config.viewDistance, config.chaseDistance,
                            config.chaseSpeed));

    Entity dog = createBaseNPC(aiComponent);

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService()
                .getAsset("images/ghostKing.atlas", TextureAtlas.class));
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
//    animator.addAnimation("death", 0.1f, Animation.PlayMode.NORMAL);

    dog
        .addComponent(new CombatStatsComponent(
                config.health,
                config.baseAttack))
        .addComponent(animator)
        .addComponent(new GhostAnimationController())
        .addComponent(new NPCHealthBarComponent())
        .addComponent(new NPCDamageHandlerComponent())
        .addComponent(new NPCDeathHandler());


    dog.getComponent(AnimationRenderComponent.class).scaleEntity();
    return dog;
  }

  /**
   * Creates a crocodile entity with predefined components and behaviour.
   *
   * @param target entity to chase
   * @return the created crocodile entity
   */
  public static Entity createCroc(Entity target) {
    BaseEntityConfig config = configs.croc;
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(1.5f, 1.5f), 5f, config.wanderSpeed))
                    .addTask(new ChaseTask(target, 10, config.viewDistance, config.chaseDistance,
                            config.chaseSpeed));

    Entity croc = createBaseNPC(aiComponent);

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/ghost.atlas", TextureAtlas.class));
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);

    croc
            .addComponent(new CombatStatsComponent(
                    config.health,
                    config.baseAttack))
            .addComponent(animator)
            .addComponent(new GhostAnimationController())
            .addComponent(new NPCHealthBarComponent())
            .addComponent(new NPCDamageHandlerComponent())
            .addComponent(new NPCDeathHandler());

    croc.getComponent(AnimationRenderComponent.class).scaleEntity();

    return croc;
  }

  /**
   * Creates a gorilla entity with predefined components and behaviour.
   *
   * @param target entity to chase
   * @return the created gorilla entity
   */
  public static Entity createGorilla(Entity target) {
    BaseEntityConfig config = configs.gorilla;
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(3f, 3f), 4f, config.wanderSpeed))
                    .addTask(new ChaseTask(target, 10, config.viewDistance, config.chaseDistance,
                            config.chaseSpeed));

    Entity gorilla = createBaseNPC(aiComponent);


    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService()
                            .getAsset("images/ghostKing.atlas", TextureAtlas.class));
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);

    gorilla
            .addComponent(new CombatStatsComponent(
                    config.health,
                    config.baseAttack))
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
   * @param aiComponent the AI component to be added to the NPC
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
