package com.csse3200.game.entities.factories;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.PlayerInventoryDisplay;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.InventoryComponent;
import com.csse3200.game.components.player.inventory.MeleeWeapon;
import com.csse3200.game.components.player.inventory.RangedWeapon;
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

/**
 * Factory to create a player entity.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stored in 'PlayerConfig'.
 */
public class PlayerFactory {
  private static final String SAVE_FILE_PATH = "configs/player_save.json";
  private static final CollectibleFactory collectibleFactory = new CollectibleFactory(); // Instantiate the CollectibleFactory

  /**
   * Create a player entity by loading saved state if available.
   * @return entity
   */
  public static Entity createPlayer() {
    PlayerConfig playerConfig = FileLoader.readClass(PlayerConfig.class, SAVE_FILE_PATH);

    // If save file doesn't exist, load the default config
    if (playerConfig == null) {
      playerConfig = FileLoader.readClass(PlayerConfig.class, "configs/player.json");
    }

    InputComponent inputComponent =
            ServiceLocator.getInputService().getInputFactory().createForPlayer();
    InventoryComponent inventoryComponent = new InventoryComponent();

    // Initialize the player's components based on the loaded configuration
    Entity player = new Entity()
            .addComponent(new TextureRenderComponent("images/box_boy_leaf.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
            .addComponent(new PlayerActions())
            .addComponent(new CombatStatsComponent(playerConfig.health, playerConfig.baseAttack))
            .addComponent(inventoryComponent)
            .addComponent(inputComponent)
            .addComponent(new PlayerStatsDisplay())
            .addComponent(new PlayerInventoryDisplay(inventoryComponent));

    // Load the inventory items, melee weapon, and ranged weapon
    loadInventory(inventoryComponent, playerConfig);

    PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
    player.getComponent(ColliderComponent.class).setDensity(1.5f);
    player.getComponent(TextureRenderComponent.class).scaleEntity();
    return player;
  }

  /**
   * Load inventory items, melee, and ranged weapons from the configuration into the player's inventory.
   *
   * @param inventoryComponent the inventory component to load items into
   * @param config the player configuration containing item details
   */
  private static void loadInventory(InventoryComponent inventoryComponent, PlayerConfig config) {
    for (String itemSpec : config.items) {
      Collectible item = collectibleFactory.create(itemSpec); // Use CollectibleFactory to create items
      inventoryComponent.getInventory().addItem(item);
    }

    if (!config.melee.isEmpty()) {
      MeleeWeapon meleeWeapon = (MeleeWeapon) collectibleFactory.create("melee:" + config.melee); // Create melee weapon
      inventoryComponent.getInventory().setMelee(meleeWeapon);
    }

    if (!config.ranged.isEmpty()) {
      RangedWeapon rangedWeapon = (RangedWeapon) collectibleFactory.create("ranged:" + config.ranged); // Create ranged weapon
      inventoryComponent.getInventory().setRanged(rangedWeapon);
    }
  }

  private PlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
