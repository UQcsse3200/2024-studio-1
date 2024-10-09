

package com.csse3200.game.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.player.*;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.PetFactory;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Handles the setup of various player components, including animations,
 * inventory, weapons, and physics.
 *
 */
public class LoadPlayer {
    private final InventoryComponent inventoryComponent;
    private final PlayerActions playerActions;
    private static final float playerScale = 0.75f;
    private static final Logger logger = getLogger(LoadPlayer.class);
    private CollectibleFactory collectibleFactory;
    private PetFactory petFactory;


    /**
     * Constructs a new LoadPlayer instance, initializing factories and inventory component.
     */
    public LoadPlayer() {
        this.collectibleFactory = new CollectibleFactory();
        this.inventoryComponent = new InventoryComponent();
        this.playerActions = new PlayerActions();
        this.petFactory = new PetFactory();
    }

    /**
     * Create a player entity based on provided config file
     *
     * @param config the config for the player.
     *
     * @return entity
     */
    public Entity createPlayer(PlayerConfig config, boolean shouldLoad) {
        logger.info("Creating player with health {}", config.health);
        Entity player = new Entity();
        addComponents(player, config, shouldLoad);
        addWeaponsAndItems(player, config, shouldLoad);
        addAtlas(player, config);
        PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
        player.getComponent(ColliderComponent.class).setDensity(1.5f);

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
        String playerName = config.name;
        switch (playerName) {
            case ("bear") -> player.setScale(1f, 1f);
            case ("default") -> player.setScale(playerScale, playerScale);
            case ("player 3") -> player.setScale(0.4615f, 1.2f);
            default -> player.setScale(0.6f, 1.2f);
        }
    }

    /**
     * Adds various components to the player entity based on the configuration.
     *
     * @param player the player entity to which components will be added.
     * @param config the configuration object containing player settings.
     */
    public void addComponents(Entity player, PlayerConfig config, boolean shouldLoad) {
        player.addComponent(new NameComponent("Main Player"))
                .addComponent(new PlayerConfigComponent(config))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(inventoryComponent)
                .addComponent(playerActions)
                .addComponent(new PlayerAchievementComponent())
                .addComponent(new ItemPickupComponent())
                .addComponent(new FundsDisplayComponent())
                .addComponent(new ShieldComponent())
                .addComponent(ServiceLocator.getInputService().getInputFactory().createForPlayer())
                .addComponent(new PlayerStatsDisplay())
                .addComponent(createAnimationComponent(config.textureAtlasFilename))
                .addComponent(new PlayerAnimationController(config.textureAtlasFilename))
                .addComponent(new DeathPlayerAnimation())
                .addComponent(new PlayerInventoryDisplay(inventoryComponent))
                .addComponent(new PlayerHealthDisplay());

        if(!shouldLoad){
            player.addComponent(new CombatStatsComponent(config.health, config.baseAttack, true, 0, 0));

        }
        else{
            player.addComponent(new CombatStatsComponent(config.health, 100, // todo fix max
                config.baseAttack, true, config.armour, config.buff, config.canCrit,
                config.critChance));
        }
        CoinsComponent coinsComponent = new CoinsComponent(inventoryComponent.getInventory());

        player.addComponent(coinsComponent)
                .addComponent(new PlayerCoinDisplay(coinsComponent));
        player.getComponent(CoinsComponent.class).setCoins(config.coins);
        player.getComponent(PlayerActions.class).setSpeed(config.speed);


    }

    /**
     * Creates and adds a melee weapon to the player entity.
     *
     * @param config file containing melee weapon details.
     *
     * @param player the player entity to which the melee weapon will be added.
     */
    public void createMelee(PlayerConfig config, Entity player) {
        Collectible melee = collectibleFactory.create(config.melee);
        if (melee instanceof MeleeWeapon meleeWeapon) {
            inventoryComponent.getInventory().setMelee(meleeWeapon); // Set melee weapon in the inventory
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

        Collectible ranged = collectibleFactory.create(config.ranged);
        if (ranged instanceof RangedWeapon rangedWeapon) {
            inventoryComponent.pickup(rangedWeapon); // Set melee weapon in the inventory
        }
    }

    /**
     * Adds weapons and items to the player entity
     *
     * @param player the player entity to which weapons and items will be added.
     *
     * @param config the configuration object containing weapon and item details.
     */
    public void addWeaponsAndItems(Entity player, PlayerConfig config, boolean shouldLoad) {
        if (config.melee!=null && !config.melee.isEmpty()) {
            createMelee(config, player);
        }

        if (config.ranged!=null && !config.ranged.isEmpty()) {
            createRanged(config, player);
        }

        if (config.items != null) {
            for (String itemName : config.items) {
                Collectible item = collectibleFactory.create(itemName);
                inventoryComponent.getInventory().addItem(item);
            }
        }

        if (config.pets != null) {
            for (String petName : config.pets) {
                Entity pet = petFactory.create(petName);
                player.getComponent(InventoryComponent.class).getInventory().addPet(pet);
                ServiceLocator.getEntityService().register(pet);
                pet.setPosition(5,7);
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
                        ServiceLocator.getResourceService().getAsset(textureAtlasFilename, TextureAtlas.class));

        switch (textureAtlasFilename) {
            case ("images/player/player.atlas"):
                animator.addAnimation("idle", 0.2f, Animation.PlayMode.LOOP);
                animator.addAnimation("idle-left", 0.2f, Animation.PlayMode.LOOP);
                animator.addAnimation("idle-right", 0.2f, Animation.PlayMode.LOOP);
                animator.addAnimation("idle-up", 0.2f, Animation.PlayMode.LOOP);
                animator.addAnimation("walk-left", 0.2f, Animation.PlayMode.LOOP);
                animator.addAnimation("walk-up", 0.2f, Animation.PlayMode.LOOP);
                animator.addAnimation("walk-right", 0.2f, Animation.PlayMode.LOOP);
                animator.addAnimation("walk-down", 0.2f, Animation.PlayMode.LOOP);
                animator.addAnimation("death-down", 0.35f, Animation.PlayMode.NORMAL);
                animator.addAnimation("death-up", 0.35f, Animation.PlayMode.NORMAL);
                animator.addAnimation("death-left", 0.35f, Animation.PlayMode.NORMAL);
                animator.addAnimation("death-right", 0.35f, Animation.PlayMode.NORMAL);
                animator.addAnimation("damage-down", 0.35f, Animation.PlayMode.NORMAL);
                break;
            case ("images/player/homeless1.atlas"):
                addCommonAnimations(animator);
                animator.addAnimation("Special_left", 0.35f, Animation.PlayMode.LOOP);
                animator.addAnimation("Special_right", 0.35f, Animation.PlayMode.LOOP);
                break;
            case ("images/player/homeless2.atlas"):
                addCommonAnimations(animator);
                animator.addAnimation("Idle2_left", 0.35f, Animation.PlayMode.LOOP);
                animator.addAnimation("Idle2_right", 0.35f, Animation.PlayMode.LOOP);
                break;
            case ("images/player/homeless3.atlas"):
                addCommonAnimations(animator);
                animator.addAnimation("Idle2_left", 0.35f, Animation.PlayMode.LOOP);
                animator.addAnimation("Idle2_right", 0.35f, Animation.PlayMode.LOOP);
                animator.addAnimation("Special_left", 0.35f, Animation.PlayMode.LOOP);
                animator.addAnimation("Special_right", 0.35f, Animation.PlayMode.LOOP);
                break;
            case ("images/npc/bear/bear.atlas"):
                animator.addAnimation("idle_left", 0.1f, Animation.PlayMode.LOOP);
                animator.addAnimation("idle_right", 0.1f, Animation.PlayMode.LOOP);
                animator.addAnimation("idle_bottom", 0.1f, Animation.PlayMode.LOOP);
                animator.addAnimation("idle_top", 0.1f, Animation.PlayMode.LOOP);
                animator.addAnimation("walk_left", 0.1f, Animation.PlayMode.LOOP);
                animator.addAnimation("walk_right", 0.1f, Animation.PlayMode.LOOP);
                animator.addAnimation("walk_top", 0.1f, Animation.PlayMode.LOOP);
                animator.addAnimation("walk_bottom", 0.1f, Animation.PlayMode.LOOP);
                animator.addAnimation("run_left", 0.1f, Animation.PlayMode.LOOP);
                animator.addAnimation("run_right", 0.1f, Animation.PlayMode.LOOP);
                animator.addAnimation("attack_left", 0.1f, Animation.PlayMode.LOOP);
                animator.addAnimation("attack_right", 0.1f, Animation.PlayMode.LOOP);
                animator.addAnimation("death_left", 0.1f, Animation.PlayMode.NORMAL);
                animator.addAnimation("death_right", 0.1f, Animation.PlayMode.LOOP);
        }
        return animator;

    }

    /**
     * Add common animations for the characters with duplicate animation names.
     *
     * @param animator AnimationRenderComponent linked to the Player entity
     */
    private void addCommonAnimations(AnimationRenderComponent animator) {
        animator.addAnimation("idle", 0.35f, Animation.PlayMode.LOOP);
        animator.addAnimation("Idle_left", 0.35f, Animation.PlayMode.LOOP);
        animator.addAnimation("Idle_right", 0.35f, Animation.PlayMode.LOOP);
        animator.addAnimation("Walk_left", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Walk_right", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Run_left", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Run_right", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Dead_left", 0.15f, Animation.PlayMode.NORMAL);
        animator.addAnimation("Dead_right", 0.15f, Animation.PlayMode.NORMAL);
        animator.addAnimation("Attack1_left", 0.15f, Animation.PlayMode.NORMAL);
        animator.addAnimation("Attack1_right", 0.15f, Animation.PlayMode.NORMAL);
        animator.addAnimation("Attack2_left", 0.15f, Animation.PlayMode.NORMAL);
        animator.addAnimation("Attack2_right", 0.15f, Animation.PlayMode.NORMAL);
        animator.addAnimation("Hurt_left", 0.35f, Animation.PlayMode.NORMAL);
        animator.addAnimation("Hurt_right", 0.35f, Animation.PlayMode.NORMAL);
    }
}


