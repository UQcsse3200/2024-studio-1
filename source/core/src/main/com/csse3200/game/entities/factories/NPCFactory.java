package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.GhostAnimationController;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.tasks.ChaseTask;
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
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(6f, 6f), 1f))
                    .addTask(new ChaseTask(target, 10, 3f, 4f));

    Entity rat = createBaseNPC(aiComponent);
    BaseEntityConfig config = configs.ghost;

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/ghost.atlas", TextureAtlas.class));
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);

    rat
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
        .addComponent(animator)
        .addComponent(new GhostAnimationController());

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
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(4f, 4f), 2f))
                    .addTask(new ChaseTask(target, 10, 3f, 4f));
    Entity dog = createBaseNPC(aiComponent);
    GhostKingConfig config = configs.ghostKing;

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService()
                .getAsset("images/ghostKing.atlas", TextureAtlas.class));
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);

    dog
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
        .addComponent(animator)
        .addComponent(new GhostAnimationController());

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
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(1f, 1f), 10f))
                    .addTask(new ChaseTask(target, 10, 3f, 4f));

    Entity croc = createBaseNPC(aiComponent);
    BaseEntityConfig config = configs.ghost;

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/ghost.atlas", TextureAtlas.class));
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);

    croc
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(animator)
            .addComponent(new GhostAnimationController());

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
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(2f, 2f), 4f))
                    .addTask(new ChaseTask(target, 10, 3f, 4f));

    Entity gorilla = createBaseNPC(aiComponent);
    BaseEntityConfig config = configs.ghost;

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/ghost.atlas", TextureAtlas.class));
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);

    gorilla
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(animator)
            .addComponent(new GhostAnimationController());

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
            .addComponent(aiComponent);

    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    return npc;
  }

  private NPCFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
