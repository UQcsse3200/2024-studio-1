package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.npc.*;
import com.csse3200.game.components.npc.attack.MeleeAttackComponent;
import com.csse3200.game.components.tasks.FollowTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class PetFactory extends LoadedFactory {
  private static final Logger logger = LoggerFactory.getLogger(PetFactory.class);
  private static final NPCConfigs configs = loadConfigs();

  public static NPCConfigs loadConfigs() {
    NPCConfigs configs = FileLoader.readClass(NPCConfigs.class, "configs/pets.json");
    logger.debug("Loaded configs: {}", configs);
    if (configs.rat.attacks == null) {
      logger.warn("Rat attacks are null!");
    } else {
      logger.debug("Rat melee attack range: {}", configs.rat.attacks.melee.range);
    }
    return configs;
  }

  /**
   * Construct a new NPC Factory.
   */
  public PetFactory() {
    super(logger);
  }

  /**
   * Create a new pet from specification
   *
   * @param specification the specification of the npc
   * @return the created npc
   */
  public Entity create(String specification) {
    return switch (specification) {
      case "Rat" -> createRat();
      case "Bear" -> createBear();
      case "Snake" -> createSnake();
      case "Bat" -> createBat();
      case "Dog" -> createDog();
      case "Minotaur" -> createMinotaur();
      case "ringFire" -> createRingFire();
      default -> throw new IllegalArgumentException("Unknown animal: " + specification);
    };
  }

  /**
   * Creates a rat entity with predefined components and behaviour.
   *
   * @return the created rat entity
   */
  public Entity createRat() {
    NPCConfigs.NPCConfig config = configs.rat;
    AITaskComponent aiComponent = createAIComponent(config.tasks);
    AnimationRenderComponent animator = createAnimator("images/npc/rat/rat.atlas", config.animations);
    return createBaseNPC("Rat", aiComponent, config, animator);
  }

  /**
   * Creates a bear entity.
   *
   * @return entity
   */
  public Entity createBear() {
    NPCConfigs.NPCConfig config = configs.bear;
    AITaskComponent aiComponent = createAIComponent(config.tasks);
    AnimationRenderComponent animator = createAnimator("images/npc/bear/bear.atlas", config.animations);
    return createBaseNPC("Bear", aiComponent, config, animator);
  }

  /**
   * Creates a Snake entity.
   *
   * @return entity
   */
  public Entity createSnake() {
    NPCConfigs.NPCConfig config = configs.snake;
    AITaskComponent aiComponent = createAIComponent(config.tasks);
    AnimationRenderComponent animator = createAnimator("images/npc/snake/snake.atlas", config.animations);
    return createBaseNPC("Snake", aiComponent, config, animator);
  }

  /**
   * Creates a bat entity with predefined components and behaviour.
   *
   * @return the created bat entity
   */
  public Entity createBat() {
    NPCConfigs.NPCConfig config = configs.bat;
    AITaskComponent aiComponent = createAIComponent(config.tasks);
    AnimationRenderComponent animator = createAnimator("images/npc/bat/bat.atlas", config.animations);
    return createBaseNPC("Bat", aiComponent, config, animator);
  }

  /**
   * Creates a dog entity with predefined components and behaviour.
   *
   * @return the created dog entity
   */
  public Entity createDog() {
    NPCConfigs.NPCConfig config = configs.dog;
    AITaskComponent aiComponent = createAIComponent(config.tasks);
    AnimationRenderComponent animator = createAnimator("images/npc/dog/dog.atlas", config.animations);
    return createBaseNPC("Dog", aiComponent, config, animator);
  }

  /**
   * Creates a Minotaur entity.
   *
   * @return the created Minotaur entity
   */
  public Entity createMinotaur() {
    NPCConfigs.NPCConfig config = configs.minotaur;
    AITaskComponent aiComponent = createAIComponent(config.tasks);
    AnimationRenderComponent animator = createAnimator("images/npc/minotaur/minotaur.atlas", config.animations);
    return createBaseNPC("Minotaur", aiComponent, config, animator);
  }

  /**
   * Creates a Ring of Fire entity.
   *
   * @return the created Ring of Fire entity
   */
  public Entity createRingFire() {
    NPCConfigs.NPCConfig config = configs.ringFire;
    AITaskComponent aiComponent = createAIComponent(config.tasks);
    TextureRenderComponent animator = new TextureRenderComponent("images/items/Ring_Of_Fire.png");
    return createBaseNPCTexture("ringFire", aiComponent, config, animator);
  }

  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @param name The name of the NPC.
   * @param aiComponent The AI component to be added to the NPC.
   * @param config The configuration for the NPC.
   * @param animator The animator component for the NPC.
   * @return The created NPC entity.
   */
  private static Entity createBaseNPC(String name, Component aiComponent, NPCConfigs.NPCConfig config,
                                      AnimationRenderComponent animator) {
    Entity player = ServiceLocator.getGameAreaService().getGameArea().getPlayer();
    Entity npc = new Entity()
            .addComponent(new NameComponent(name))
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PET))
            .addComponent(aiComponent)
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack, true))
            .addComponent(animator)
            .addComponent(new NPCHealthBarComponent())
            .addComponent(new NPCDeathHandler())
            .addComponent(new DirectionalNPCComponent(config.isDirectional))
            .addComponent(new NPCAnimationController())
            .addComponent(new NPCConfigComponent(config));

    if (config.attacks.melee != null) {
      npc.addComponent(new MeleeAttackComponent(player, config.attacks.melee.range, config.attacks.melee.rate,
              config.attacks.melee.effects));
    }
    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    npc.getComponent(AnimationRenderComponent.class).scaleEntity();
    return npc;
  }

  private static Entity createBaseNPCTexture(String name, Component aiComponent, NPCConfigs.NPCConfig config,
                                             TextureRenderComponent animator) {
    Entity player = ServiceLocator.getGameAreaService().getGameArea().getPlayer();
    Entity npc = new Entity()
            .addComponent(new NameComponent(name))
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PET))
            .addComponent(aiComponent)
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack, true))
            .addComponent(animator)
            .addComponent(new NPCHealthBarComponent())
            .addComponent(new NPCDeathHandler())
            .addComponent(new DirectionalNPCComponent(config.isDirectional))
            .addComponent(new NPCAnimationController())
            .addComponent(new NPCConfigComponent(config));

    if (config.attacks.melee != null) {
      npc.addComponent(new MeleeAttackComponent(player, config.attacks.melee.range, config.attacks.melee.rate,
              config.attacks.melee.effects));
    }
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
   * @param tasks The task configuration for the NPC
   * @return The created AITaskComponent
   */
  private AITaskComponent createAIComponent(NPCConfigs.NPCConfig.TaskConfig tasks) {
    AITaskComponent aiComponent = new AITaskComponent();
    if (tasks.follow != null) {
      aiComponent.addTask(new FollowTask(tasks.follow));
    }
    return aiComponent;
  }

  @Override
  protected String[] getTextureAtlasFilepaths() {
    return new String[] {
            "images/npc/rat/rat.atlas",
            "images/npc/snake/snake.atlas",
            "images/npc/minotaur/minotaur.atlas",
            "images/npc/bat/bat.atlas",
            "images/npc/bear/bear.atlas",
            "images/npc/dog/dog.atlas"
    };
  }

  @Override
  protected String[] getTextureFilepaths() {
    return new String[]{
            "images/npc/rat/rat.png",
            "images/npc/minotaur/minotaur.png",
            "images/npc/snake/snake.png",
            "images/npc/bat/bat.png",
            "images/npc/bear/bear.png",
            "images/npc/dog/dog.png"
    };
  }
}