package com.csse3200.game.entities.factories;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.GhostAnimationController;
import com.csse3200.game.components.Direction;
import com.csse3200.game.components.npc.NPCAnimationController;
import com.csse3200.game.components.npc.NPCDamageHandlerComponent;
import com.csse3200.game.components.npc.NPCDeathHandler;
import com.csse3200.game.components.npc.NPCHealthBarComponent;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.services.ResourceService;

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
  private static final Logger logger = LoggerFactory.getLogger(NPCFactory.class);
  private static final NPCConfigs configs =
          FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");
  private static final String[] npcAtlas ={
    "images/ghost.atlas", 
    "images/ghostKing.atlas",
    "images/rat.atlas", 
    "images/snake.atlas", 
    "images/minotaur.atlas",
    "images/bear.atlas", 
    "images/dino.atlas",
    "images/bat.atlas", 
    "images/dog.atlas"
  };
  private static final String[] npcTextures ={
    "images/ghost_1.png",
    "images/ghost_king.png",
    "images/rat.png",
    "images/minotaur.png",
    "images/dog.png",
    "images/snake.png",
    "images/dino.png",
    "images/minotaur.png",
    "images/bear.png" 
  };

  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @param aiComponent the AI component to be added to the NPC
   * @return entity
   */
  private static Entity createBaseNPC(AITaskComponent aiComponent, BaseEntityConfig config,
                                      AnimationRenderComponent animator) {
    Entity npc = new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f))
            .addComponent(aiComponent)
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(animator)
            .addComponent(new NPCAnimationController())
            .addComponent(new NPCHealthBarComponent())
            .addComponent(new NPCDamageHandlerComponent())
            .addComponent(new NPCDeathHandler());
    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    npc.getComponent(AnimationRenderComponent.class).scaleEntity();
    return npc;
  }

  private static AnimationRenderComponent createAnimator(String atlasPath, BaseEntityConfig.AnimationData[] animations) {
    AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset(atlasPath, TextureAtlas.class));
    for (BaseEntityConfig.AnimationData animation : animations) {
      animator.addAnimation(animation.name, animation.frameDuration, animation.playMode);
    }
    return animator;
  }

  /**
   * Creates a rat entity with predefined components and behaviour.
   *
   * @param target entity to chase
   * @return the created rat entity
   */
  public Entity createRat(Entity target) {
    BaseEntityConfig config = configs.rat;
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new StraightWanderTask(2f))
                    .addTask(new ChaseTask(target, 9, 5f, 6f, 2f))
                    .addTask(new AttackTask(target, 10, 2f, 2.5f));
    AnimationRenderComponent animator = createAnimator("images/rat.atlas", config.animations);
    Entity rat = createBaseNPC(aiComponent, config, animator);

    return rat;
  }

  /**
   * Creates a bear entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public Entity createBear(Entity target) {
    BaseEntityConfig config = configs.bear;
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new StraightWanderTask(2f))
                    .addTask(new ChaseTask(target, 9, 5f, 6f, 2f))
                    .addTask(new AttackTask(target, 10, 2f, 2.5f));
    AnimationRenderComponent animator = createAnimator("images/bear.atlas", config.animations);
    Entity bear = createBaseNPC(aiComponent, config, animator);

    return bear;
  }

  /**
   * Creates a Snake entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public Entity createSnake(Entity target) {
    BaseEntityConfig config = configs.snake;
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new StraightWanderTask(1.5f))
                    .addTask(new ChaseTask(target, 12, 4f, 8f, 3f))
                    .addTask(new AttackTask(target, 15, 3f, 3.5f));
    AnimationRenderComponent animator = createAnimator("images/snake.atlas", config.animations);
    Entity snake = createBaseNPC(aiComponent, config, animator);

    return snake;
  }

  /**
   * Creates a Dino entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public Entity createDino(Entity target) {
    BaseEntityConfig config = configs.snake;
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new StraightWanderTask(1.5f))
                    .addTask(new ChaseTask(target, 12, 4f, 8f, 3f))
                    .addTask(new AttackTask(target, 15, 3f, 3.5f));
    AnimationRenderComponent animator = createAnimator("images/dino.atlas", config.animations);
    Entity dino = createBaseNPC(aiComponent, config, animator);

    return dino;
  }

  /**
   * Creates a bat entity with predefined components and behaviour.
   *
   * @param target entity to chase
   * @return the created rat entity
   */
  public Entity createBat(Entity target) {
    BaseEntityConfig config = configs.bat;
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new StraightWanderTask(2f))
                    .addTask(new ChaseTask(target, 9, 2f, 6f, 2f))
                    .addTask(new AttackTask(target, 10, 3f, 3f));
    AnimationRenderComponent animator = createAnimator("images/bat.atlas", config.animations);
    Entity bat = createBaseNPC(aiComponent, config, animator);

    return bat;
  }

  /**
   * Creates a Minotaur entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public Entity createMinotaur(Entity target) {
    BaseEntityConfig config = configs.minotaur;
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new StraightWanderTask(1.5f))
                    .addTask(new ChaseTask(target, 12, 4f, 8f, 3f))
                    .addTask(new AttackTask(target, 15, 3f, 3.5f));
    AnimationRenderComponent animator = createAnimator("images/minotaur.atlas", config.animations);
    Entity minotaur = createBaseNPC(aiComponent, config, animator);

    return minotaur;
  }

  /**
   * Creates a dog entity with predefined components and behaviour.
   *
   * @param target entity to chase
   * @return the created dog entity
   */
  public Entity createDog(Entity target) {
    BaseEntityConfig config = configs.dog;
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(4f, 4f), 2f, config.wanderSpeed))
                    .addTask(new ChargeTask(target, 10, config.viewDistance, config.chaseDistance,
                            config.chaseSpeed, 2f));
    AnimationRenderComponent animator = createAnimator("images/dog.atlas", config.animations);
    Entity dog = createBaseNPC(aiComponent, config, animator);

    return dog;
  }

  /**
   * Creates a crocodile entity with predefined components and behaviour.
   *
   * @param target entity to chase
   * @return the created crocodile entity
   */
  public Entity createCroc(Entity target) {
    BaseEntityConfig config = configs.croc;
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(1.5f, 1.5f), 5f, config.wanderSpeed))
                    .addTask(new ChaseTask(target, 10, config.viewDistance, config.chaseDistance,
                            config.chaseSpeed));

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/ghost.atlas", TextureAtlas.class));
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);

    Entity croc = createBaseNPC(aiComponent, config, animator);

    return croc;
  }

  /**
   * Creates a gorilla entity with predefined components and behaviour.
   *
   * @param target entity to chase
   * @return the created gorilla entity
   */
  public Entity createGorilla(Entity target) {
    BaseEntityConfig config = configs.gorilla;
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(3f, 3f), 4f, config.wanderSpeed))
                    .addTask(new ChaseTask(target, 10, config.viewDistance, config.chaseDistance,
                            config.chaseSpeed));

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService()
                            .getAsset("images/ghostKing.atlas", TextureAtlas.class));
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);

    Entity gorilla = createBaseNPC(aiComponent, config, animator);

    return gorilla;
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(npcTextures);
    resourceService.loadTextureAtlases(npcAtlas);

    while (!resourceService.loadForMillis(10)) {
      // This could be upgraded to a loading screen
      logger.info("Loading... {}%", resourceService.getProgress());
    }
  }
  public NPCFactory() {
    loadAssets();
  }
}
