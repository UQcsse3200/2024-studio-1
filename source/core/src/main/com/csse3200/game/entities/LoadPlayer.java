package com.csse3200.game.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.effects.EffectComponent;
import com.csse3200.game.components.player.*;
import com.csse3200.game.components.player.inventory.CoinsComponent;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.InventoryComponent;
import com.csse3200.game.components.player.inventory.ItemPickupComponent;
import com.csse3200.game.components.player.inventory.weapons.MeleeWeapon;
import com.csse3200.game.components.player.inventory.weapons.RangedWeapon;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * Handles the setup of various player components, including animations,
 * inventory, weapons, and physics.
 */
public class LoadPlayer {
    private final InventoryComponent inventoryComponent;
    private final PlayerActions playerActions;
    private static final float playerScale = 0.75f;
    private static final Logger LOGGER = getLogger(LoadPlayer.class);
    private final CollectibleFactory collectibleFactory;


    /**
     * Constructs a new LoadPlayer instance, initializing factories and inventory component.
     */
    public LoadPlayer() {
        this.collectibleFactory = new CollectibleFactory();
        this.inventoryComponent = new InventoryComponent();
        this.playerActions = new PlayerActions();
    }

    /**
     * Create a player entity based on provided config file
     *
     * @param config the config for the player.
     * @return entity
     */
    public Entity createPlayer(PlayerConfig config) {
        LOGGER.info("Creating player with config: {}", config);

        Entity player = new Entity();

        addComponents(player, config);
        addWeaponsAndItems(config);
        addAtlas(player, config);
        PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
        player.getComponent(ColliderComponent.class).setDensity(1.5f);

        return player;
    }

    /**
     * Adds texture atlas and default texture settings to the player entity.
     *
     * @param player the player entity to which the atlas will be added.
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
    private void addComponents(Entity player, PlayerConfig config) {
        player.addComponent(new NameComponent("Main Player"))
                .addComponent(new PlayerConfigComponent(config))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(new CombatStatsComponent(
                config.health, config.maxHealth,
                config.baseAttack, true, config.armour, config.buff, config.canCrit,
                config.critChance, config.timeInvincible))
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
                .addComponent(new LevelChangeListener())
                .addComponent(new PlayerInventoryDisplay(inventoryComponent))
                .addComponent(new PlayerHealthDisplay())
                .addComponent(new EffectComponent());

        CoinsComponent coinsComponent = new CoinsComponent();

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
     */
    private void createMelee(PlayerConfig config) {
        Collectible melee = collectibleFactory.create(config.melee);
        if (melee instanceof MeleeWeapon meleeWeapon) {
            inventoryComponent.pickup(meleeWeapon);
        }
    }

    /**
     * Creates and adds a ranged weapon to the player entity
     *
     * @param config file containing ranged weapon details.
     *
     */
    private void createRanged(PlayerConfig config) {
        Collectible ranged = collectibleFactory.create(config.ranged);
        if (ranged instanceof RangedWeapon rangedWeapon) {
            inventoryComponent.pickup(rangedWeapon);
        }
    }

    /**
     * Adds weapons and items to the player entity
     *
     * @param config the configuration object containing weapon and item details.
     */
    private void addWeaponsAndItems(PlayerConfig config) {
        if (config.melee != null && !config.melee.isEmpty()) {
            createMelee(config);
        }

        if (config.ranged != null && !config.ranged.isEmpty()) {
            createRanged(config);
        }

        List<String> itemSpecs = new ArrayList<>();
        if (config.items != null) {
            itemSpecs.addAll(Arrays.stream(config.items).toList());
        }

        if (config.buffs != null) {
            itemSpecs.addAll(Arrays.stream(config.buffs).toList());
        }

        // Do not load pets here, the game area isn't initialised so pets can't be spawned.

        for (String itemSpec : itemSpecs) {
            inventoryComponent.pickup(collectibleFactory.create(itemSpec));
        }
    }

    /**
     * Creates an AnimationRenderComponent for handling player animations.
     *
     * @param textureAtlasFilename the filename of the texture atlas containing animations.
     * @return the created AnimationRenderComponent.
     */
    private AnimationRenderComponent createAnimationComponent(String textureAtlasFilename) {
        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset(
                        textureAtlasFilename, TextureAtlas.class
                )
        );

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


