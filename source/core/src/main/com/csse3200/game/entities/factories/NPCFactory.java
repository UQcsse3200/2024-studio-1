package com.csse3200.game.entities.factories;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.*;
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
  private static final NPCConfigs configs = FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

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

  public NPCFactory() {
    loadAssets();
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

  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @param target The target entity for the NPC to chase.
   * @param aiComponent The AI component to be added to the NPC.
   * @param config The configuration for the NPC.
   * @param animator The animator component for the NPC.
   *
   * @return entity
   */
  private static Entity createBaseNPC(Entity target, AITaskComponent aiComponent, NPCConfigs.NPCConfig config,
                                      AnimationRenderComponent animator) {
    Entity npc = new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new AttackComponent(target, config.attackRange, config.attackRate, config.baseAttack,
                    config.effects))
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

  /**
   * Helper method to create an AnimationRenderComponent for an NPC.
   *
   * @param atlasPath The path to the texture atlas for the NPC
   * @param animations An array of animations for the NPC
   * @return The created AnimationRenderComponent
   */
  private static AnimationRenderComponent createAnimator(String atlasPath,
                                                         NPCConfigs.NPCConfig.AnimationData[] animations) {
    AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset(atlasPath, TextureAtlas.class));
    for (NPCConfigs.NPCConfig.AnimationData animation : animations) {
      animator.addAnimation(animation.name, animation.frameDuration, animation.playMode);
    }
    return animator;
  }

  /**
   * Helper method to create an AI component for the NPC based on its tasks.
   *
   * @param target The target entity (e.g., the player)
   * @param tasks The task configuration for the NPC
   * @return The created AITaskComponent
   */
  private AITaskComponent createAIComponent(Entity target, NPCConfigs.NPCConfig.TaskConfig tasks) {
    AITaskComponent aiComponent = new AITaskComponent();

    // Add wander task
    if (tasks.wander != null) {
      aiComponent.addTask(new WanderTask(new Vector2(tasks.wander.wanderRadius, tasks.wander.wanderRadius),
              tasks.wander.waitTime, tasks.wander.wanderSpeed));
    }
    // Add straight wander task
    if (tasks.straightWander != null) {
      aiComponent.addTask(new StraightWanderTask(tasks.straightWander.wanderSpeed));
    }
    // Add chase task
    if (tasks.chase != null) {
      aiComponent.addTask(new ChaseTask(target, tasks.chase.priority, tasks.chase.viewDistance,
              tasks.chase.chaseDistance, tasks.chase.chaseSpeed));
    }
    // Add charge task
    if (tasks.charge != null) {
      aiComponent.addTask(new ChargeTask(target, tasks.charge.priority, tasks.charge.viewDistance,
              tasks.charge.chaseDistance, tasks.charge.chaseSpeed, tasks.charge.waitTime));
    }

    return aiComponent;
  }

  /**
   * Creates a rat entity with predefined components and behaviour.
   *
   * @param target entity to chase
   * @return the created rat entity
   */
  public Entity createRat(Entity target) {
    NPCConfigs.NPCConfig config = configs.rat;
    AITaskComponent aiComponent = createAIComponent(target, config.tasks);
    AnimationRenderComponent animator = createAnimator("images/rat.atlas", config.animations);
    Entity rat = createBaseNPC(target, aiComponent, config, animator);

    return rat;
  }

  /**
   * Creates a bear entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public Entity createBear(Entity target) {
    NPCConfigs.NPCConfig config = configs.bear;
    AITaskComponent aiComponent = createAIComponent(target, config.tasks);
    AnimationRenderComponent animator = createAnimator("images/bear.atlas", config.animations);
    Entity bear = createBaseNPC(target, aiComponent, config, animator);

    return bear;
  }

  /**
   * Creates a Snake entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public Entity createSnake(Entity target) {
    NPCConfigs.NPCConfig config = configs.snake;
    AITaskComponent aiComponent = createAIComponent(target, config.tasks);
    AnimationRenderComponent animator = createAnimator("images/snake.atlas", config.animations);
    Entity snake = createBaseNPC(target, aiComponent, config, animator);

    return snake;
  }

  /**
   * Creates a Dino entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public Entity createDino(Entity target) {
    NPCConfigs.NPCConfig config = configs.dino;
    AITaskComponent aiComponent = createAIComponent(target, config.tasks);
    AnimationRenderComponent animator = createAnimator("images/dino.atlas", config.animations);
    Entity dino = createBaseNPC(target, aiComponent, config, animator);

    return dino;
  }

  /**
   * Creates a bat entity with predefined components and behaviour.
   *
   * @param target entity to chase
   * @return the created rat entity
   */
  public Entity createBat(Entity target) {
    NPCConfigs.NPCConfig config = configs.bat;
    AITaskComponent aiComponent = createAIComponent(target, config.tasks);
    AnimationRenderComponent animator = createAnimator("images/bat.atlas", config.animations);
    Entity bat = createBaseNPC(target, aiComponent, config, animator);

    return bat;
  }

  /**
   * Creates a Minotaur entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public Entity createMinotaur(Entity target) {
    NPCConfigs.NPCConfig config = configs.minotaur;
    AITaskComponent aiComponent = createAIComponent(target, config.tasks);
    AnimationRenderComponent animator = createAnimator("images/minotaur.atlas", config.animations);
    Entity minotaur = createBaseNPC(target, aiComponent, config, animator);

    return minotaur;
  }

  /**
   * Creates a dog entity with predefined components and behaviour.
   *
   * @param target entity to chase
   * @return the created dog entity
   */
  public Entity createDog(Entity target) {
    NPCConfigs.NPCConfig config = configs.dog;
    AITaskComponent aiComponent = createAIComponent(target, config.tasks);
    AnimationRenderComponent animator = createAnimator("images/dog.atlas", config.animations);
    Entity dog = createBaseNPC(target, aiComponent, config, animator);

    return dog;
  }

  /**
   * Creates a crocodile entity with predefined components and behaviour.
   *
   * @param target entity to chase
   * @return the created crocodile entity
   */
  public Entity createCroc(Entity target) {
    NPCConfigs.NPCConfig config = configs.croc;
    AITaskComponent aiComponent = createAIComponent(target, config.tasks);
    AnimationRenderComponent animator = createAnimator("images/rat.atlas", config.animations);
    Entity croc = createBaseNPC(target, aiComponent, config, animator);

    return croc;
  }


  /**
   * Creates a gorilla entity with predefined components and behaviour.
   *
   * @param target entity to chase
   * @return the created gorilla entity
   */
  public Entity createGorilla(Entity target) {
    NPCConfigs.NPCConfig config = configs.gorilla;
    AITaskComponent aiComponent = createAIComponent(target, config.tasks);
    AnimationRenderComponent animator = createAnimator("images/rat.atlas", config.animations);
    Entity gorilla = createBaseNPC(target, aiComponent, config, animator);

    return gorilla;
  }
}
