package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.*;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.LoadPlayer;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Factory to create a player entity.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */
public class PlayerFactory extends LoadedFactory {
    private static final Logger logger = LoggerFactory.getLogger(PlayerFactory.class);
    Map<String, PlayerConfig> options;

    /**
     * Construct a new Player Factory (and load all of its assets)
     */
    public PlayerFactory(List<String> configFilenames) {
        super(logger);
        this.options = configFilenames.stream()
                .map(filename -> FileLoader.readClass(PlayerConfig.class, filename))
                .collect(Collectors.toMap(value -> value.name, value -> value));
        this.load(logger);
    }

    /**
     * Create a player entity
     *
     * @return entity
     */
    public Entity createPlayer(){
        LoadPlayer loader = new LoadPlayer();
        PlayerConfig config = options.get("default");
        return loader.createPlayer(config);
    }

    public Entity createPlayer(String player) {
        LoadPlayer loader = new LoadPlayer();
        PlayerConfig config = options.get(player);
        return loader.createPlayer(config);
    }

    /**
     * Create a player entity
     *
     * @param config the config for the player.
     * @return entity
     */

    public Entity createPlayer(PlayerConfig config) {
        TextureAtlas atlas = new TextureAtlas(config.textureAtlasFilename);
        TextureRegion defaultTexture = atlas.findRegion("idle");

        InventoryComponent inventoryComponent = new InventoryComponent();
        Entity player = new Entity()
                .addComponent(new PlayerConfigComponent(config))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(new PlayerActions())
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack, true))
                .addComponent(inventoryComponent)
                .addComponent(new ItemPickupComponent())
                // .addComponent(ServiceLocator.getInputService().getInputFactory().createForPlayer())
                .addComponent(new PlayerStatsDisplay())
                .addComponent(createAnimationComponent(config.textureAtlasFilename))
                .addComponent(new PlayerAnimationController())
                .addComponent(new PlayerInventoryDisplay(inventoryComponent))
                .addComponent(new PlayerHealthDisplay())
                .addComponent(new WeaponComponent(
                        new Sprite(new Texture("images/Weapons/knife.png")),
                        Collectible.Type.RANGED_WEAPON,
                        10, 1, 1, 10, 10, 0));

        if(config.items != null) {
            Array<Collectible> items = stringToItems(config.items);
            for (Collectible item : items) {
                inventoryComponent.getInventory().addItem(item); // Add each item to inventory
            }
        }

        if (config.melee != null) {
            MeleeWeapon meleeWeapon = createMeleeWeaponFromSpecification(config.melee); // Create MeleeWeapon
            WeaponComponent meleeWeaponComponent = new WeaponComponent(
                    new Sprite(meleeWeapon.getIcon()),  // Use texture from the melee weapon class
                    Collectible.Type.MELEE_WEAPON,
                    meleeWeapon.getDamage(),
                    meleeWeapon.getRange(),
                    meleeWeapon.getFireRate(),
                    0, 0,  0
            );
            player.addComponent(meleeWeaponComponent); // Add melee weapon component to the player
            inventoryComponent.getInventory().setMelee(meleeWeapon); // Set melee weapon in the inventory
        }

        if (config.ranged != null) {
            RangedWeapon rangedWeapon = createRangeFromSpec(config.ranged);
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

        PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
        player.getComponent(ColliderComponent.class).setDensity(1.5f);

        player.setScale(1f, (float) defaultTexture.getRegionHeight() / defaultTexture.getRegionWidth());

        return player;
    }

    private AnimationRenderComponent createAnimationComponent(String textureAtlasFilename) {
        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset(textureAtlasFilename,
                        TextureAtlas.class));
        animator.addAnimation("idle", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("walk-left", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("walk-up", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("walk-right", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("walk-down", 0.2f, Animation.PlayMode.LOOP);
        return animator;
    }

    private MeleeWeapon createMeleeWeaponFromSpecification(String specification) {
        switch (specification.toLowerCase()) {
            case "knife":
                return new Knife();
            case "pickaxe":
                return new Pickaxe();
            default:
                throw new IllegalArgumentException("Unknown melee weapon specification: " + specification);
        }
    }

    private RangedWeapon createRangeFromSpec(String specification) {
        switch (specification.toLowerCase()) {
            case "shotgun":
                return new Shotgun();
            default:
                throw new IllegalArgumentException("Unknown ranged weapon specification: " + specification);
        }
    }

    private static Array<Collectible> stringToItems(String[] itemSpecs) {
        Array<Collectible> items = new Array<>();

        for (String spec : itemSpecs) {
            Collectible item = createCollectibleFromSpecification(spec);
            if (item != null) {
                items.add(item);
            }
        }

        return items;
    }

    private static Collectible createCollectibleFromSpecification(String spec){
        switch (spec.toLowerCase()) {
            case "bandage":
                return new Bandage();
            case "med_kit":
                return new MedKit();
            case "shieldpotion":
                return new ShieldPotion();
            case "energydrink":
                return new EnergyDrink();
            //Add when team 3 implement
            //case "syringe":
                //return new Syringe();
            default:
                logger.warn("Unknown item specification: " + spec);
                return null;
        }
    }

    @Override
    protected String[] getTextureAtlasFilepaths() {
        if (this.options == null){
            return new String[]{};
        }
        return options.values().stream().map(config -> config.textureAtlasFilename).toArray(String[]::new);
    }

    @Override
    protected String[] getTextureFilepaths() {
        if (this.options == null){
            return new String[]{};
        }
        return options.values().stream().map(config -> config.textureFilename).toArray(String[]::new);
    }

}
