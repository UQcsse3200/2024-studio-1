package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.npc.*;
import com.csse3200.game.components.npc.attack.AOEAttackComponent;
import com.csse3200.game.components.npc.attack.MeleeAttackComponent;
import com.csse3200.game.components.npc.attack.RangeAttackComponent;
import com.csse3200.game.components.tasks.FollowTask;
import com.csse3200.game.components.tasks.TaskType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.AttackConfig;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.entities.configs.TaskConfig;
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

import java.util.Map;

/**
 * Factory to create pet entities with predefined components.
 *
 * <p>Uses configurations from NPCConfigs to dynamically create pets based on type.</p>
 */
public class PetFactory extends LoadedFactory {
  private static final Logger logger = LoggerFactory.getLogger(NPCFactory.class);
  private static final NPCConfigs configs = loadConfigs();
  private String[] textureAtlasFilepaths;
  private String[] textureFilepaths;

  /**
   * Construct a new pet Factory.
   */
  public PetFactory(){
    super(logger);
  }

  /**
   * Load the pet configs from file.
   *
   * @return the loaded pet configs
   */
  private static NPCConfigs loadConfigs() {
    Map<String, NPCConfigs.NPCConfig> npcConfigMap = FileLoader.readMap(NPCConfigs.NPCConfig.class, "configs/pets.json");
    if (npcConfigMap == null || npcConfigMap.isEmpty()) {
      logger.error("Pet Config map is empty or null");
    } else {
      logger.debug("Loaded Pet Config map with keys: {}", npcConfigMap.keySet());
    }
    return new NPCConfigs(npcConfigMap);
  }

  /**
   * Create a new pet from specification
   *
   * @param petType the type of the npc to be created
   * @return the created pet
   */
  public Entity create(String petType) {
    NPCConfigs.NPCConfig config = configs.getConfig(petType.toLowerCase());
    if (config == null) {
      logger.error("Pet type '{}' not found in configurations.", petType);
      throw new IllegalArgumentException("Unknown pet type: " + petType);
    }

    Entity pet = new Entity();
    Entity player = ServiceLocator.getGameAreaService().getGameArea().getPlayer();
    logger.debug("Creating pet of type '{}'", petType);

    // Add components to pet
    addBaseComponents(pet, player, config);
    addAIComponent(pet, config.tasks);
    addAttackComponents(pet, player, config.attacks);
    if (petType.equals("ringFire")) {
      addAnimator(pet, "images/items/Ring_Of_Fire.png");
    } else {
      addAnimator(pet, getAtlasFilepath(petType.toLowerCase()), config.animations);
    }

    // Scale entity
    PhysicsUtils.setScaledCollider(pet, 0.9f, 0.4f);
    pet.getComponent(AnimationRenderComponent.class).scaleEntity();

    return pet;
  }

  /**
   * Adds the base components to the pet entity.
   *
   * @param pet    The pet entity to add components to.
   * @param target The target entity for the NPC.
   * @param config The configuration for the NPC.
   */
  private static void addBaseComponents(Entity pet, Entity target, NPCConfigs.NPCConfig config) {
    pet.addComponent(new NameComponent(config.name))
        .addComponent(new PhysicsComponent())
        .addComponent(new PhysicsMovementComponent())
        .addComponent(new ColliderComponent())
        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PET))
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack,true))
        .addComponent(new NPCDeathHandler())
        .addComponent(new DirectionalNPCComponent(config.isDirectional))
        .addComponent(new NPCAnimationController())
        .addComponent(new NPCConfigComponent(config));
  }

  /**
   * Helper method to create an AnimationRenderComponent for a pet.
   *
   * @param pet        The pet entity to add the AnimationRenderComponent to.
   * @param atlasPath  The path to the texture atlas for the pet.
   * @param animations An array of animations for the pet.
   */
  private static void addAnimator(
          Entity pet, String atlasPath, NPCConfigs.NPCConfig.AnimationData[] animations) {
    AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset(atlasPath, TextureAtlas.class));
    for (NPCConfigs.NPCConfig.AnimationData animation : animations) {
      animator.addAnimation(animation.name, animation.frameDuration, animation.playMode);
    }
    pet.addComponent(animator);
  }

  /**
   * Helper method to create a texture render component for a pet.
   *
   * @param pet The pet entity to add the TextureRenderComponent to.
   */
  private void addAnimator(Entity pet, String texturePath) {
    TextureRenderComponent renderer = new TextureRenderComponent(texturePath);
    pet.addComponent(renderer);
  }

  /**
   * Helper method to create an AI component for the pet based on its tasks.
   *
   * @param pet     The pet entity to add the AI component to.
   * @param tasks   The task configuration for the pet.
   */
  private void addAIComponent(Entity pet, TaskConfig tasks) {
    AITaskComponent aiComponent = new AITaskComponent();
    Map<TaskType, Object> taskConfigs = tasks.getTaskConfigs();

    for (Map.Entry<TaskType, Object> entry : taskConfigs.entrySet()) {
      if (entry.getKey() == TaskType.FOLLOW) {
        aiComponent.addTask(new FollowTask((TaskConfig.FollowTaskConfig) entry.getValue()));
      }
    }
    pet.addComponent(aiComponent);
  }

  /**
   * Helper method to create and add attack components for a pet.
   *
   * @param pet     The pet entity.
   * @param target  The target entity.
   * @param attacks The attack configuration for the pet.
   */
  private void addAttackComponents(Entity pet, Entity target, AttackConfig attacks) {
    if (attacks.melee != null) {
      pet.addComponent(new MeleeAttackComponent(target, attacks.melee));
    }
    if (attacks.ranged != null) {
      pet.addComponent(new RangeAttackComponent(target, attacks.ranged));
    }
    if (attacks.aoe != null) {
      pet.addComponent(new AOEAttackComponent(target, attacks.aoe));
    }
  }

  /**
   * Get the filepath to the texture atlas for the pet.
   *
   * @param petType The type of pet
   * @return        The filepath to the texture atlas
   */
  private String getAtlasFilepath(String petType) {
    return String.format("images/npc/%s/%s.atlas", petType, petType);
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
