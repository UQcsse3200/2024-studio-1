package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.*;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.InventoryComponent;
import com.csse3200.game.components.player.inventory.ItemPickupComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.WeaponAnimationRenderComponent;
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
        return createPlayer(options.get("default"));
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
        WeaponAnimationRenderComponent animatorWeapon =
                new WeaponAnimationRenderComponent(new TextureAtlas("images/Weapons/shotgun4.atlas"));
        animatorWeapon.addAnimation("idle", 0.2f, Animation.PlayMode.LOOP);
        animatorWeapon.addAnimation("left", 0.2f, Animation.PlayMode.LOOP);

        Entity player = new Entity()
                .addComponent(new PlayerConfigComponent(config))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(new PlayerActions())
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack, true))
                .addComponent(inventoryComponent)
                .addComponent(new ItemPickupComponent())
                .addComponent(ServiceLocator.getInputService().getInputFactory().createForPlayer())
                .addComponent(new PlayerStatsDisplay())
                .addComponent(createAnimationComponent(config.textureAtlasFilename))
                .addComponent(new PlayerAnimationController())
                .addComponent(animatorWeapon)
                .addComponent(new WeaponAnimationController())
                .addComponent(new PlayerInventoryDisplay(inventoryComponent))
                .addComponent(new PlayerHealthDisplay())
                .addComponent(new RangeDetectionComponent(PhysicsLayer.NPC))
                .addComponent(new WeaponComponent(
                        new Sprite(new Texture("images/Weapons/knife.png")),
                        Collectible.Type.RANGED_WEAPON,
                        10, 1, 1, 10, 10, 0));

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
