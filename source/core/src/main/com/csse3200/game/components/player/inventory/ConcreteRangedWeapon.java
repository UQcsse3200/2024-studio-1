package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.weapon.FiringController;
import com.csse3200.game.components.weapon.PositionTracker;
import com.csse3200.game.components.weapon.WeaponAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

/**
 * A ranged weapon that can be picked up by the player.
 */
public class ConcreteRangedWeapon extends RangedWeapon {
    /**
     * Contains the entity which has all the characteristic of this weapon
     */
    private Entity weaponEntity;

    /**
     * Constructor for ranged weapons.
     *
     * @param name       The name of the weapon.
     * @param iconPath   The path to the icon of the weapon.
     * @param damage     The damage of the weapon.
     * @param range      The range of the weapon.
     * @param fireRate   The fire rate of the weapon.
     * @param ammo       The current ammo of the weapon.
     * @param maxAmmo    The maximum ammo of the weapon.
     * @param reloadTime The reload time of the weapon.
     */
    public ConcreteRangedWeapon(String name, String iconPath, int damage, int range, int fireRate, int ammo, int maxAmmo, int reloadTime) {
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
     * Set the weaponEntity, which will activate when the player pick up this weapon
     * Should only be called by the WeaponFactory
     *
     * @param weaponEntity The entity that contains this weapon
     */
    public void setWeaponEntity(Entity weaponEntity) {
        this.weaponEntity = weaponEntity;
        ServiceLocator.getEntityService().register(this.weaponEntity);
        this.weaponEntity.setEnabled(false);
    }

    /**
     * Get the weaponEntity
     *
     * @return The entity that contains this weapon
     */
    public Entity getWeaponEntity() {
        return this.weaponEntity;
    }

    /**
     * Pick up the ranged weapon and put it in the inventory.
     *
     * @param inventory The inventory to be put in.
     */
    @Override
    public void pickup(Inventory inventory) {
        logger.info("Picking up ranged weapon - no entity");
        Entity player = inventory.getEntity();
        inventory.setRanged(this);
        try {
            connectPlayer(player);
            this.weaponEntity.setEnabled(true);
            // Trigger weapon pick up event for UI
            player.getEvents().trigger("ranged_pickup", this.maxAmmo);
        } catch (NullPointerException e) {
            logger.info("Weapon entity is null or components not found");
        }
    }

    /**
     * Drop the ranged weapon from the inventory.
     *
     * @param inventory The inventory to drop from.
     */
    @Override
    public void drop(Inventory inventory) {
        logger.info("Dropping ranged weapon - " + this.name);
        inventory.resetRanged();
        try {
            disconnectPlayer();
            ServiceLocator.getEntityService().markEntityForRemoval(this.weaponEntity);
        } catch (NullPointerException e) {
            logger.info("Weapon entity is null or components not found");
        }
    }

    /**
     * Set up the weapon components to connect with the player
     *
     * @param player the player who holds this weapon
     */
    private void connectPlayer(Entity player) {
        // Set weapon entity
        // Update weapon entity to link with player
        try {
            this.weaponEntity.getComponent(FiringController.class).connectPlayer(player);
            this.weaponEntity.getComponent(WeaponAnimationController.class).connectPlayer(player);
            this.weaponEntity.getComponent(PositionTracker.class).connectPlayer(player);
        } catch (NullPointerException e) {
            logger.info("Weapon entity is null or components not found");
        }
    }

    /**
     * Set up the components to disconnect with the player
     */
    private void disconnectPlayer() {
        // Reset weapon entity
        // Update weapon entity to disconnect from player
        try {
            this.weaponEntity.getComponent(FiringController.class).disconnectPlayer();
            this.weaponEntity.getComponent(PositionTracker.class).disconnectPlayer();
            this.weaponEntity.getComponent(WeaponAnimationController.class).disconnectPlayer();
        } catch (NullPointerException e) {
            logger.info("Weapon entity is null or components not found");
        }
    }

    /**
     * Create a projectile and launch it in the direction specified
     *
     * @param direction direction to shoot in
     */
    @Override
    public void shoot(Vector2 direction) {
        this.weaponEntity.getComponent(FiringController.class).activate(direction);
    }


    /**
     * Get the clip size of this weapon
     * i.e how many shots it can fire before reloading
     *
     * @return integer represent the clip size
     */
    public int getClipSize() {
        return this.maxAmmo;
    }

    /**
     * Set the clip size of this weapon
     * i.e how many shots it can fire before reloading
     *
     * @param i the desired clip size
     */
    public void setClipSize(int i) {
        this.maxAmmo = i;
    }
}
