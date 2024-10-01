package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.ai.tasks.BossAITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.npc.*;
import com.csse3200.game.components.npc.attack.MeleeAttackComponent;
import com.csse3200.game.components.npc.attack.RangeAttackComponent;
import com.csse3200.game.components.tasks.*;
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
public class NPCFactory extends LoadedFactory {
  private static final Logger logger = LoggerFactory.getLogger(NPCFactory.class);
  private static final NPCConfigs configs =
          loadConfigs();

  public static NPCConfigs loadConfigs() {
    NPCConfigs configs = FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");
    System.out.println("Loaded configs: " + configs); // Add debug printout
    if (configs.rat.attacks == null) {
      System.out.println("Rat attacks are null!"); // Check if attacks are being loaded
    } else {
      System.out.println("Rat melee attack range: " + configs.rat.attacks.melee.range); // Log specific fields
    }
    return configs;
  }

  /**
   * Construct a new NPC Factory.
   */
  public NPCFactory(){
    super(logger);
  }

  /**
   * Create a new NPC from specification
   *
   * @param specification the specification of the npc
   * @param target entity to chase
   * @return the created npc
   */
  public Entity create(String specification, Entity target) {
    return switch (specification) {
      case "Rat" -> this.createRat(target);
      case "Bear" -> this.createBear(target);
      case "Snake" -> this.createSnake(target);
      case "Dino" -> this.createDino(target);
      case "Bat" -> this.createBat(target);
      case "Dog" -> this.createDog(target);
      case "Minotaur" -> this.createMinotaur(target);
      case "Werewolf" -> this.createWerewolf(target);
      case "Birdman" -> this.createBirdman(target);
      case "Kitsune" -> this.createKitsune(target);
      case "Dragon" -> this.createDragon(target);
      default -> throw new IllegalArgumentException("Unknown animal: " + specification);
    };
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
    AnimationRenderComponent animator = createAnimator("images/npc/rat/rat.atlas", config.animations);
    Entity rat = createBaseNPC("Rat", target, aiComponent, config, animator);

    return rat;
  }

  /**
   * Creates a kitsune boss entity with predefined components and behaviour.
   *
   * @param target entity to chase
   * @return the kitsune entity
   */
  public Entity createKitsune(Entity target) {
    NPCConfigs.NPCConfig config = configs.kitsune;
    AITaskComponent aiComponent = createAIComponent(target, config.tasks);
    AnimationRenderComponent animator = createAnimator("images/npc/kitsune/kitsune.atlas", config.animations);
    Entity kitsune = createBaseNPC("Kitsune", target, aiComponent, config, animator);

    return kitsune; 
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
    AnimationRenderComponent animator = createAnimator("images/npc/bear/bear.atlas", config.animations);
    Entity bear = createBaseNPC("Bear", target, aiComponent, config, animator);

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
    AnimationRenderComponent animator = createAnimator("images/npc/snake/snake.atlas", config.animations);
    Entity snake = createBaseNPC("Snake", target, aiComponent, config, animator);

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
    AnimationRenderComponent animator = createAnimator("images/npc/dino/dino.atlas", config.animations);
    Entity dino = createBaseNPC("Dino", target, aiComponent, config, animator);

    return dino;
  }

  /**
   * Creates a bat entity with predefined components and behaviour.
   *
   * @param target entity to chase
   * @return the created bat entity
   */
  public Entity createBat(Entity target) {
    NPCConfigs.NPCConfig config = configs.bat;
    AITaskComponent aiComponent = createAIComponent(target, config.tasks);
    AnimationRenderComponent animator = createAnimator("images/npc/bat/bat.atlas", config.animations);
    Entity bat = createBaseNPC("Bat", target, aiComponent, config, animator);

    return bat;
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
    AnimationRenderComponent animator = createAnimator("images/npc/dog/dog.atlas", config.animations);
    Entity dog = createBaseNPC("Dog", target, aiComponent, config, animator);

    return dog;
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
    AnimationRenderComponent animator = createAnimator("images/npc/minotaur/minotaur.atlas", config.animations);
    Entity minotaur = createBaseNPC("Minotaur", target, aiComponent, config, animator);

    return minotaur;
  }

  /**
   * Creates a Birdman entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public Entity createBirdman(Entity target) {
    NPCConfigs.NPCConfig config = configs.birdman;
    AITaskComponent aiComponent = createAIComponent(target, config.tasks);
    AnimationRenderComponent animator = createAnimator("images/npc/birdman/birdman.atlas", config.animations);
    Entity birdman = createBaseNPC("Birdman", target, aiComponent, config, animator);

    return birdman;
  }

  /**
   * Creates a Werewolf entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public Entity createWerewolf(Entity target) {
    NPCConfigs.NPCConfig config = configs.werewolf;
    BossAITaskComponent aiComponent = new BossAITaskComponent();
    aiComponent.addTask(new ChaseTask(target, config.tasks.chase));
    aiComponent.addTask(new ChargeTask(target, config.tasks.charge));
    aiComponent.addTask(new WanderTask(config.tasks.wander));
    AnimationRenderComponent animator = createAnimator("images/npc/werewolf/werewolf.atlas", config.animations);
    Entity werewolf = createBaseNPC("Werewolf", target, aiComponent, config, animator);

    return werewolf;
  }

  /**
   * Creates a dragon entity with predefined components and behaviour.
   *
   * @param target entity to chase
   * @return the created dragon entity
   */
  public Entity createDragon(Entity target) {
    NPCConfigs.NPCConfig config = configs.dragon;
    AITaskComponent aiComponent = createAIComponent(target, config.tasks);
    AnimationRenderComponent animator = createAnimator("images/npc/dragon/dragon.atlas", config.animations);
    Entity dragon = createBaseNPC("Dragon", target, aiComponent, config, animator);

    return dragon;
  }

  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @param name
   * @param target      The target entity for the NPC to chase.
   * @param aiComponent The AI component to be added to the NPC.
   * @param config      The configuration for the NPC.
   * @param animator    The animator component for the NPC.
   * @return The created NPC entity.
   */
  private static Entity createBaseNPC(String name, Entity target, Component aiComponent, NPCConfigs.NPCConfig config,
                                      AnimationRenderComponent animator) {
    Entity npc = new Entity()
            .addComponent(new NameComponent(name))
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(aiComponent)
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(animator)
            .addComponent(new NPCHealthBarComponent())
            .addComponent(new NPCDeathHandler()) 
            .addComponent(new DirectionalNPCComponent(config.isDirectional))
            .addComponent(new NPCAnimationController())
            .addComponent(new NPCConfigComponent(config));

    if (config.attacks.melee != null) {
      npc.addComponent(new MeleeAttackComponent(target, config.attacks.melee.range, config.attacks.melee.rate,
              config.attacks.melee.effects));
    }
    if (config.attacks.ranged != null) {
      npc.addComponent(new RangeAttackComponent(target, config.attacks.ranged.range, config.attacks.ranged.rate,
              config.attacks.ranged.type, config.attacks.ranged.effects));
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
   * @param target The target entity (e.g., the player)
   * @param tasks The task configuration for the NPC
   * @return The created AITaskComponent
   */
  private AITaskComponent createAIComponent(Entity target, NPCConfigs.NPCConfig.TaskConfig tasks) {
    AITaskComponent aiComponent = new AITaskComponent();

    // Add wander task
    if (tasks.wander != null) {
      aiComponent.addTask(new WanderTask(tasks.wander));
    }
    // Add straight wander task
    if (tasks.straightWander != null) {
      aiComponent.addTask(new StraightWanderTask(tasks.straightWander.wanderSpeed));
    }
    // Add chase task
    if (tasks.chase != null) {
      aiComponent.addTask(new ChaseTask(target, tasks.chase));
    }
    // Add charge task
    if (tasks.charge != null) {
      aiComponent.addTask(new ChargeTask(target, tasks.charge));
    }

    // Add boss attack task
    if (tasks.bossAttack != null) {
      aiComponent.addTask(new BossAttackTask(target, tasks.bossAttack));
    }

    // Add run away task
    if (tasks.runAway != null) {
      aiComponent.addTask(new RunAwayTask(target, tasks.runAway));
    }

    return aiComponent;
  }

// assets below are cited in core/assets/images/npc/citation.txt
  @Override
  protected String[] getTextureAtlasFilepaths() {
    return new String[] {
            "images/npc/rat/rat.atlas",
            "images/npc/dragon/dragon.atlas",
            "images/npc/snake/snake.atlas",
            "images/npc/minotaur/minotaur.atlas",
            "images/npc/dino/dino.atlas",
            "images/npc/bat/bat.atlas",
            "images/npc/bear/bear.atlas",
            "images/npc/dog/dog.atlas",
            "images/npc/werewolf/werewolf.atlas",
            "images/npc/kitsune/kitsune.atlas",
            "images/npc/birdman/birdman.atlas"
    };
  }

  @Override
  protected String[] getTextureFilepaths() {
    return new String[]{
            "images/npc/rat/rat.png",
            "images/npc/dragon/dragon.png",
            "images/npc/minotaur/minotaur.png",
            "images/npc/snake/snake.png",
            "images/npc/dino/dino.png",
            "images/npc/bat/bat.png",
            "images/npc/bear/bear.png",
            "images/npc/dog/dog.png",
            "images/npc/werewolf/werewolf.png",
            "images/npc/kitsune/kitsune.png",
            "images/npc/birdman/birdman.png"
    };
  }
}
