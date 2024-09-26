package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.WeaponComponent;
import com.csse3200.game.components.weapon.FiringController;
import com.csse3200.game.components.weapon.WeaponAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.WeaponFactory;
import com.csse3200.game.services.ServiceLocator;

import java.util.logging.Logger;

/**
 * A ranged weapon that can be picked up by the player.
 */
public class RangedWeapon implements Collectible {

    /**
     * Logger for debugging purposes.
     */
    private static final Logger logger = Logger.getLogger(RangedWeapon.class.getName());

    private String name;        // name of the weapon
    private String iconPath;    // path to the icon of the weapon
    private int damage;         // weapon damage
    private int range;          // range of the weapon
    private int fireRate;       // fire rate of the weapon
    private int ammo;           // current ammo left
    private int maxAmmo;        // maximum ammo
    private int reloadTime;     // reload time

    private Entity weaponEntity;
    private WeaponFactory weaponFactory;

    /**
     * Constructor for ranged weapons.
     * @param name The name of the weapon.
     * @param iconPath The path to the icon of the weapon.
     * @param damage The damage of the weapon.
     * @param range The range of the weapon.
     * @param fireRate The fire rate of the weapon.
     * @param ammo The current ammo of the weapon.
     * @param maxAmmo The maximum ammo of the weapon.
     * @param reloadTime The reload time of the weapon.
     */
    public RangedWeapon(String name, String iconPath, int damage, int range, int fireRate, int ammo, int maxAmmo, int reloadTime) {
        this.name = name;
        this.iconPath = iconPath;
        this.damage = damage;
        this.range = range;
        this.fireRate = fireRate;
        this.ammo = ammo;
        this.maxAmmo = maxAmmo;
        this.reloadTime = reloadTime;

    }

    /**
     * Constructor for ranged weapons.
     * @return The name of the weapon.
     */
    @Override
    public Type getType() {
        return Type.RANGED_WEAPON;
    }

    /**
     * Get the name of the weapon.
     * @return The name of the weapon.
     */
    @Override
    public String getName() {
        return null;
    }

    /**
     * Set the name of the weapon.
     * @param name The name of the weapon.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the icon path of the weapon.
     * @return The icon path of the weapon.
     */
    @Override
    public void pickup(Inventory inventory) {
        logger.info("Picking up ranged weapon - no entity");
        Entity player = inventory.getEntity();
        inventory.setRanged(this);

        // Add a Weapon Component
        if (player != null && player.getComponent(WeaponComponent.class) != null) {
            player.getComponent(WeaponComponent.class).updateWeapon(this);
        } else {
            logger.warning("Inventory entity or WeaponComponent is null");
        }

        this.weaponFactory = new WeaponFactory();
        // Create weapon entity
        this.weaponEntity = weaponFactory.createWeaponForPlayer(this);
        // Set weapon entity
        // Update weapon entity to link with player
        this.weaponEntity.getComponent(FiringController.class).connectPlayer(player);
        this.weaponEntity.getComponent(WeaponAnimationController.class).connectPlayer(player);
        // Register weapon entity
        ServiceLocator.getEntityService().register(this.weaponEntity);
    }

    /**
     * Pick up the ranged weapon and put it in the inventory.
     * @param inventory The inventory to be put in.
     * @param itemEntity The entity of the item to be picked up.
     */
    @Override
    public void pickup(Inventory inventory, Entity itemEntity) {
        logger.info("Picking up ranged weapon - with entity");
        inventory.setRanged(this);

        // Add a Weapon Component
        if (inventory.getEntity() != null && inventory.getEntity().getComponent(WeaponComponent.class) != null) {
            logger.info("Setting ranged weapon in inventory");
            inventory.getEntity().getComponent(WeaponComponent.class).updateWeapon(this, itemEntity);
        } else {
            logger.info("Inventory entity or WeaponComponent is null");
        }
    }

    /**
     * Drop the ranged weapon from the inventory.
     * @param inventory The inventory to drop from.
     */
    @Override
    public void drop(Inventory inventory) {
        Entity player = inventory.getEntity();
        inventory.resetRanged();

        // Switch to default weapon (bare hands)
        if (player != null
                && player.getComponent(WeaponComponent.class) != null) {
            player.getComponent(WeaponComponent.class).dropRangeWeapon();
        }

        // Reset weapon entity
        // Update weapon entity to disconnect from player
        this.weaponEntity.getComponent(FiringController.class).disconnectPlayer();
        // unregister weapon entity
        ServiceLocator.getEntityService().unregister(this.weaponEntity);
    }

    /**
     * Get the ranged weapon icon.
     * @return The ranged weapon icon.
     */
    @Override
    public Texture getIcon() {
        return new Texture(iconPath);
    }

    /**
     * Get the specification of the ranged weapon.
     * @return The specification of the ranged weapon.
     */
    @Override
    public String getSpecification() {
        return "ranged:" + getName();
    }

    /**
     * Get the ranged weapon damage.
     * @return The ranged weapon damage.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Set the ranged weapon damage.
     * @param damage The ranged weapon damage.
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Get the ranged weapon range.
     * @return The ranged weapon range.
     */
    public int getRange() {
        return range;
    }

    /**
     * Set the ranged weapon range.
     * @param range The ranged weapon range.
     */
    public void setRange(int range) {
        this.range = range;
    }

    /**
     * Get the ranged weapon fire rate.
     * @return The ranged weapon fire rate.
     */
    public int getFireRate() {
        return fireRate;
    }

    /**
     * Set the ranged weapon fire rate.
     * @param fireRate The ranged weapon fire rate.
     */
    public void setFireRate(int fireRate) {
        this.fireRate = fireRate;
    }

    /**
     * Get the ranged weapon ammo.
     * @return The ranged weapon ammo.
     */
    public int getAmmo() {
        return ammo;
    }

    /**
     * Set the ranged weapon ammo.
     * @param ammo The ranged weapon ammo.
     */
    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    /**
     * Get the ranged weapon maximum ammo.
     * @return The ranged weapon maximum ammo.
     */
    public int getMaxAmmo() {
        return maxAmmo;
    }

    /**
     * Set the ranged weapon maximum ammo.
     * @param maxAmmo The ranged weapon maximum ammo.
     */
    public void setMaxAmmo(int maxAmmo) {
        this.maxAmmo = maxAmmo;
    }

    /**
     * Get the ranged weapon reload time.
     * @return The ranged weapon reload time.
     */
    public int getReloadTime() {
        return reloadTime;
    }

    /**
     * Set the ranged weapon reload time.
     * @param reloadTime The ranged weapon reload time.
     */
    public void setReloadTime(int reloadTime) {
        this.reloadTime = reloadTime;
    }

    public void shoot(Vector2 direction) {
        this.weaponEntity.getComponent(FiringController.class).activate(direction);
    }

    public void attack() {
        this.weaponEntity.getComponent(FiringController.class).activate(null);
    }
}
