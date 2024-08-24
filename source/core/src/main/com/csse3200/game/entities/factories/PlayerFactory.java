package com.csse3200.game.entities.factories;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.PlayerInventoryDisplay;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.player.PlayerStatsDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory to create a player entity.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stored in 'PlayerConfig'.
 */
public class PlayerFactory {
  private static final Logger logger = LoggerFactory.getLogger(PlayerFactory.class);
  private static final PlayerConfig stats =
          FileLoader.readClass(PlayerConfig.class, "configs/player.json");

  /**
   * Create a player entity.
   * @return entity
   */
  public static Entity createPlayer() {
    InputComponent inputComponent =
            ServiceLocator.getInputService().getInputFactory().createForPlayer();
    InventoryComponent inventoryComponent = new InventoryComponent();

    // Process items only if the array is not null
    if (stats.items != null) {
      for (String itemSpec : stats.items) {
        Collectible item = createItemFromSpecification(itemSpec);
        if (item != null) {
          inventoryComponent.pickup(item);
        }
      }
    }

    // Set melee weapon only if stats.melee is not null or empty
    if (stats.melee != null && !stats.melee.isEmpty()) {
      MeleeWeapon meleeWeapon = createMeleeWeaponFromSpecification(stats.melee);
      inventoryComponent.getInventory().setMelee(meleeWeapon);
    }

    // Set ranged weapon only if stats.ranged is not null or empty
    if (stats.ranged != null && !stats.ranged.isEmpty()) {
      RangedWeapon rangedWeapon = createRangedWeaponFromSpecification(stats.ranged);
      inventoryComponent.getInventory().setRanged(rangedWeapon);
    }

    Entity player = new Entity()
            .addComponent(new TextureRenderComponent("images/box_boy_leaf.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
            .addComponent(new PlayerActions())
            .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack))
            .addComponent(inventoryComponent)
            .addComponent(inputComponent)
            .addComponent(new PlayerStatsDisplay())
            .addComponent(new PlayerInventoryDisplay(inventoryComponent));

    PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
    player.getComponent(ColliderComponent.class).setDensity(1.5f);
    player.getComponent(TextureRenderComponent.class).scaleEntity();

    return player;
  }


  private static Collectible createItemFromSpecification(String itemSpec) {
    switch(itemSpec) {
      case "ShieldPotion":
        return new ShieldPotion(); // Return a new instance of ShieldPotion
      case "EnergyDrink":
        return new EnergyDrink(); // Assuming you have an EnergyDrink class
      case "Knife":
        return new Knife(); // Return a new instance of Knife (which might be a MeleeWeapon)
      // Add other cases as needed for different item types
      default:
        logger.warn("Unknown item specification: " + itemSpec);
        return null;
    }
  }

  private static MeleeWeapon createMeleeWeaponFromSpecification(String meleeSpec) {
    if (meleeSpec.equals("Knife")) {
      return new Knife(); // Assuming Knife is a subclass of MeleeWeapon
    }
    logger.warn("Unknown melee weapon specification: " + meleeSpec);
    return null;
  }

  private static RangedWeapon createRangedWeaponFromSpecification(String rangedSpec) {
    // Logic to create the appropriate RangedWeapon instance based on the rangedSpec string
    // Implement this based on your available RangedWeapon types
    logger.warn("Unknown ranged weapon specification: " + rangedSpec);
    return null;
  }

  private PlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
