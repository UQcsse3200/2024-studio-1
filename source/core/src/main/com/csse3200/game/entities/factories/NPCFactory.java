package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.DialogComponent;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.npc.*;
import com.csse3200.game.components.npc.attack.AOEAttackComponent;
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

import java.util.Map;

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
  private static NPCConfigs configs;

  /**
   * Construct a new NPC Factory.
   */
  public NPCFactory(){
    super(logger);
    Map<String, NPCConfigs.NPCConfig> npcConfigMap = FileLoader.readMap(NPCConfigs.NPCConfig.class, "configs/NPCs.json");
    if (npcConfigMap == null || npcConfigMap.isEmpty()) {
      logger.error("NPC Config map is empty or null");
    } else {
      logger.debug("Loaded NPC Config map with keys: {}", npcConfigMap.keySet());
    }
    configs = new NPCConfigs(npcConfigMap);
  }

  /**
   * Create a new NPC from specification
   *
   * @param npcType the type of npc to be created
   * @param target entity to chase
   * @return the created npc
   */
  public Entity create(String npcType, Entity target) {
    NPCConfigs.NPCConfig config = configs.getConfig(npcType.toLowerCase());
    if (config == null) {
      throw new IllegalArgumentException("Unknown NPC type: " + npcType);
    }

    AITaskComponent aiComponent = createAIComponent(target, config.tasks);
    String atlasPath = String.format("images/npc/%s/%s.atlas", npcType.toLowerCase(), npcType.toLowerCase());
    AnimationRenderComponent animator = createAnimator(atlasPath, config.animations);

    Entity npc = createBaseNPC(npcType, target, aiComponent, config, animator);

    // Additional components for bosses
    if (config.isBoss) {
      npc.addComponent(new BossHealthDialogueComponent());
      npc.addComponent(new DialogComponent());
    }

    return npc;
  }

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
            .addComponent(new NPCDeathHandler(target, config.getStrength()))
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
    if (config.attacks.aoe != null) {
      npc.addComponent(new AOEAttackComponent(target, config.attacks.aoe.range, config.attacks.aoe.rate,
              config.attacks.aoe.effects));
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
    // Add follow task
    if (tasks.follow != null) {
      aiComponent.addTask(new FollowTask(tasks.follow));
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
    // Add jump task
    if (tasks.jump != null) {
      aiComponent.addTask(new JumpTask(target, tasks.jump));
    }
    // Add run away task
    if (tasks.runAway != null) {
      aiComponent.addTask(new RunAwayTask(target, tasks.runAway));
    }
    // Add range attack tasks
    if (tasks.rangeAttack != null) {
        aiComponent.addTask(new RangeAttackTask(target, tasks.rangeAttack, "single"));
    }
    if (tasks.spreadRangeAttack != null) {
        aiComponent.addTask(new RangeAttackTask(target, tasks.spreadRangeAttack, "spread"));
    }
    if (tasks.aoeAttack != null) {
        aiComponent.addTask(new AOEAttackTask(target, tasks.aoeAttack));
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
