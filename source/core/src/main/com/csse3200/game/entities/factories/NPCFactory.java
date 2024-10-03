package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
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
 * <p>Uses configurations from NPCConfigs to dynamically create NPCs based on type.</p>
 */
public class NPCFactory extends LoadedFactory {
  private static final Logger logger = LoggerFactory.getLogger(NPCFactory.class);
  private static final NPCConfigs configs = loadConfigs();
  private String[] textureAtlasFilepaths;
  private String[] textureFilepaths;

  /**
   * Construct a new NPC Factory.
   */
  public NPCFactory(){
    super(logger);
  }

  /**
   * Load the NPC configs from file.
   *
   * @return the loaded NPC configs
   */
  private static NPCConfigs loadConfigs() {
    Map<String, NPCConfigs.NPCConfig> npcConfigMap = FileLoader.readMap(NPCConfigs.NPCConfig.class, "configs/NPCs.json");
    if (npcConfigMap == null || npcConfigMap.isEmpty()) {
      logger.error("NPC Config map is empty or null");
    } else {
      logger.debug("Loaded NPC Config map with keys: {}", npcConfigMap.keySet());
    }
    return new NPCConfigs(npcConfigMap);
  }

  /**
   * Creates an NPC based on the provided type and target.
   *
   * @param npcType The type of NPC to create.
   * @param target  The target entity for the NPC.
   * @return The created NPC entity.
   */
  public Entity create(String npcType, Entity target) {
    NPCConfigs.NPCConfig config = configs.getConfig(npcType.toLowerCase());
    if (config == null) {
      throw new IllegalArgumentException("Unknown NPC type: " + npcType);
    }

    Entity npc = new Entity();

    // Add components to the NPC entity
    addBaseComponents(npc, target, config);
    addAIComponent(npc, target, config.tasks);
    addAnimator(npc, getAtlasFilepath(npcType.toLowerCase()), config.animations);
    addAttackComponents(npc, target, config.attacks);

    // Additional components for bosses
    if (config.isBoss) {
      npc.addComponent(new BossHealthDialogueComponent());
      npc.addComponent(new DialogComponent());
    }

    // Scale entity
    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    npc.getComponent(AnimationRenderComponent.class).scaleEntity();

    return npc;
  }

  private static void addBaseComponents(Entity npc, Entity target, NPCConfigs.NPCConfig config) {
    npc.addComponent(new NameComponent(config.name))
        .addComponent(new PhysicsComponent())
        .addComponent(new PhysicsMovementComponent())
        .addComponent(new ColliderComponent())
        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
        .addComponent(new NPCHealthBarComponent())
        .addComponent(new NPCDeathHandler(target, config.getStrength()))
        .addComponent(new DirectionalNPCComponent(config.isDirectional))
        .addComponent(new NPCAnimationController())
        .addComponent(new NPCConfigComponent(config));
  }

  /**
   * Helper method to create an AnimationRenderComponent for an NPC.
   *
   * @param atlasPath  The path to the texture atlas for the NPC
   * @param animations An array of animations for the NPC
   */
  private static void addAnimator(
          Entity npc, String atlasPath, NPCConfigs.NPCConfig.AnimationData[] animations) {
    AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset(atlasPath, TextureAtlas.class));
    for (NPCConfigs.NPCConfig.AnimationData animation : animations) {
      animator.addAnimation(animation.name, animation.frameDuration, animation.playMode);
    }
    npc.addComponent(animator);
  }

  /**
   * Helper method to create an AI component for the NPC based on its tasks.
   *
   * @param target The target entity (e.g., the player)
   * @param tasks  The task configuration for the NPC
   */
  private void addAIComponent(Entity npc, Entity target, NPCConfigs.NPCConfig.TaskConfig tasks) {
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

    npc.addComponent(aiComponent);
  }

  /**
   * Helper method to create attack components for the NPC.
   *
   * @param target  The target entity
   * @param attacks The attack configuration for the NPC
   */
  private void addAttackComponents(Entity npc, Entity target, NPCConfigs.NPCConfig.AttackConfig attacks) {
    if (attacks.melee != null) {
      npc.addComponent(new MeleeAttackComponent(target, attacks.melee));
    }
    if (attacks.ranged != null) {
      npc.addComponent(new RangeAttackComponent(target, attacks.ranged));
    }
    if (attacks.aoe != null) {
      npc.addComponent(new AOEAttackComponent(target, attacks.aoe));
    }
  }

  /**
   * Get the filepath to the texture atlas for the NPC.
   *
   * @param npcType The type of NPC
   * @return The filepath to the texture atlas
   */
  private String getAtlasFilepath(String npcType) {
    return String.format("images/npc/%s/%s.atlas", npcType, npcType);
  }

// assets below are cited in core/assets/images/npc/citation.txt
  @Override
  protected String[] getTextureAtlasFilepaths() {
    if (textureAtlasFilepaths == null) {
      textureAtlasFilepaths = configs.getNpcTypes().stream()
              .map(npcType -> String.format("images/npc/%s/%s.atlas", npcType, npcType))
              .toArray(String[]::new);
    }
    return textureAtlasFilepaths;
  }

    @Override
    protected String[] getTextureFilepaths() {
      if (textureFilepaths == null) {
        textureFilepaths = configs.getNpcTypes().stream()
                .map(npcType -> String.format("images/npc/%s/%s.png", npcType, npcType))
                .toArray(String[]::new);
      }
      return textureFilepaths;
    }
}
