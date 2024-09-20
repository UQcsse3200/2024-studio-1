package com.csse3200.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.player.*;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;


/**
 * Handles the setup of various player components, including animations,
 * inventory, weapons, and physics.
 *
 */
public class LoadPlayer {
    private final WeaponFactory weaponFactory;
    private final ItemFactory itemFactory;
    private final InventoryComponent inventoryComponent;
    private static final float playerScale = 0.75f;


    /**
    * Constructs a new LoadPlayer instance, initializing factories and inventory component.
     */
    public LoadPlayer() {
        this.weaponFactory = new WeaponFactory();
        this.itemFactory = new ItemFactory();
        this.inventoryComponent = new InventoryComponent();
    }

    /**
     * Create a player entity based on provided config file
     *
     * @param config the config for the player.
     *
     * @return entity
     */
    public Entity createPlayer(PlayerConfig config) {
        Entity player = new Entity();
        addComponents(player, config);
        addWeaponsAndItems(player, config);
        addAtlas(player, config);
        PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
        player.getComponent(ColliderComponent.class).setDensity(1.5f);
        player.setScale(playerScale, playerScale);

        return player;
    }

    /**
     * Adds texture atlas and default texture settings to the player entity.
     *
     * @param player the player entity to which the atlas will be added.
     *
     * @param config the config file that contain the texture atlas filename.
     */
    public  void addAtlas(Entity player, PlayerConfig config) {
        TextureAtlas atlas = new TextureAtlas(config.textureAtlasFilename);
        TextureRegion defaultTexture = atlas.findRegion("idle");
        player.setScale(1f, (float) defaultTexture.getRegionHeight() / defaultTexture.getRegionWidth());

    }

    /**
     * Adds various components to the player entity based on the configuration.
     *
     * @param player the player entity to which components will be added.
     * @param config the configuration object containing player settings.
     */
    public void addComponents(Entity player, PlayerConfig config) {

        player.addComponent(new NameComponent("Main Player"))
                .addComponent(new PlayerConfigComponent(config))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(new PlayerActions())
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack, true, 0, 0))
                .addComponent(inventoryComponent)
                .addComponent(new ItemPickupComponent())
                .addComponent(new ShieldComponent())
                .addComponent(ServiceLocator.getInputService().getInputFactory().createForPlayer())
                .addComponent(new PlayerStatsDisplay())
                .addComponent(createAnimationComponent(config.textureAtlasFilename))
                .addComponent(new PlayerAnimationController())
                .addComponent(new DeathPlayerAnimation())
                .addComponent(new PlayerInventoryDisplay(inventoryComponent))
                .addComponent(new PlayerHealthDisplay())
                .addComponent(new CoinsComponent((inventoryComponent.getInventory())))
                .addComponent(new PlayerCoinDisplay(new CoinsComponent(inventoryComponent.getInventory())))
                .addComponent(new WeaponComponent(
                        new Sprite(new Texture("images/Weapons/knife.png")),
                        Collectible.Type.RANGED_WEAPON,
                        10, 1, 1, 10, 10, 0));


        /*
                CoinsComponent coinsComponent = new CoinsComponent(inventoryComponent.getInventory());
                player.addComponent(coinsComponent)
                        .addComponent(new PlayerCoinDisplay(coinsComponent));

         */

    }

    /**
     * Creates and adds a melee weapon to the player entity.
     *
     * @param config file containing melee weapon details.
     *
     * @param player the player entity to which the melee weapon will be added.
     */
    public void createMelee(PlayerConfig config, Entity player) {
        // calls create method in weapon factory to initialise a weapon
        Collectible melee = weaponFactory.create(Collectible.Type.MELEE_WEAPON, config.melee);
        if (melee instanceof MeleeWeapon meleeWeapon) {
            WeaponComponent meleeWeaponComponent = new WeaponComponent(
                    new Sprite(meleeWeapon.getIcon()),  // Use texture from the melee weapon class
                    Collectible.Type.MELEE_WEAPON,
                    meleeWeapon.getDamage(),
                    meleeWeapon.getRange(),
                    meleeWeapon.getFireRate(),
                    0, 0, 0
            );
            inventoryComponent.getInventory().setMelee(meleeWeapon); // Set melee weapon in the inventory
            player.addComponent(meleeWeaponComponent);
        }
    }

    /**
     * Creates and adds a ranged weapon to the player entity
     *
     * @param config file containing ranged weapon details.
     *
     * @param player the player entity to which the ranged weapon will be added.
     */
    public void createRanged(PlayerConfig config, Entity player) {

        Collectible ranged = weaponFactory.create(Collectible.Type.RANGED_WEAPON, config.ranged);
        if (ranged instanceof RangedWeapon rangedWeapon) {
            WeaponComponent rangedWeaponComponent = new WeaponComponent(
                    new Sprite(rangedWeapon.getIcon()),
                    Collectible.Type.RANGED_WEAPON,
                    rangedWeapon.getDamage(),
                    rangedWeapon.getRange(),
                    rangedWeapon.getFireRate(),
                    rangedWeapon.getAmmo(),
                    rangedWeapon.getMaxAmmo(),
                    rangedWeapon.getReloadTime()
            );
            player.addComponent(rangedWeaponComponent); // Add melee weapon component to the player
            inventoryComponent.getInventory().setRanged(rangedWeapon); // Set melee weapon in the inventory
        }
    }

    /**
     * Adds weapons and items to the player entity
     *
     * @param player the player entity to which weapons and items will be added.
     *
     * @param config the configuration object containing weapon and item details.
     */
    public void addWeaponsAndItems(Entity player, PlayerConfig config) {
        if (config.melee!=null) {
            createMelee(config, player);
        }

        if (config.ranged!=null) {
            createRanged(config, player);
        }

        if (config.items != null) {
            for (String itemName : config.items) {
                Collectible item = itemFactory.create(itemName);
                inventoryComponent.getInventory().addItem(item);
            }
        }
    }

    /**
     * Creates an AnimationRenderComponent for handling player animations.
     *
     * @param textureAtlasFilename the filename of the texture atlas containing animations.
     *
     * @return the created AnimationRenderComponent.
     */
    private AnimationRenderComponent createAnimationComponent(String textureAtlasFilename) {
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/player/player.atlas", TextureAtlas.class));
        animator.addAnimation("idle", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("walk-left", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("walk-up", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("walk-right", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("walk-down", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("death-down", 0.35f, Animation.PlayMode.NORMAL);
        animator.addAnimation("death-up", 0.35f, Animation.PlayMode.NORMAL);
        animator.addAnimation("death-left", 0.35f, Animation.PlayMode.NORMAL);
        animator.addAnimation("death-right", 0.35f, Animation.PlayMode.NORMAL);
        animator.addAnimation("damage-down", 0.35f, Animation.PlayMode.NORMAL);

        return animator;
    }
}

