package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.WeaponComponent;
import com.csse3200.game.components.weapon.FiringController;
import com.csse3200.game.components.weapon.PositionTracker;
import com.csse3200.game.components.weapon.WeaponAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.WeaponFactory;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

import java.util.logging.Logger;

/**
 * A ranged weapon that can be picked up by the player.
 */
public class ConcreteMeleeWeapon extends MeleeWeapon {
    /**
     * Contains the entity which has all the characteristic of this weapon
     */
    private Entity weaponEntity;

    /**
     * Constructor for ranged weapons.
     * @param name The name of the weapon.
     * @param iconPath The path to the icon of the weapon.
     * @param damage The damage of the weapon.
     * @param range The range of the weapon.
     * @param fireRate The fire rate of the weapon.
     */
    public ConcreteMeleeWeapon(String name, String iconPath, int damage, int range, int fireRate) {
        this.name = name;
        this.iconPath = iconPath;
        this.damage = damage;
        this.range = range;
        this.fireRate = fireRate;
    }

    /**
     * Set the weaponEntity, which will activate when the player pick up this weapon
     * Should only be called by the WeaponFactory
     * @param weaponEntity The entity that contains this weapon
     */
    public void setWeaponEntity(Entity weaponEntity) {
        this.weaponEntity = weaponEntity;
        ServiceLocator.getEntityService().register(this.weaponEntity);
        this.weaponEntity.setEnabled(false);
    }

    /**
     * Pick up the melee weapon and put it in the inventory.
     * @param inventory The inventory to be put in.
     */
    @Override
    public void pickup(Inventory inventory) {
        logger.info("Picking up melee weapon - no entity");
        Entity player = inventory.getEntity();
        inventory.setMelee(this);

        connectPlayer(player);
        this.weaponEntity.setEnabled(true);
    }

    /**
     * Set up the components to connect with the player
     * @param player the player who holds this weapon
     */
    private void connectPlayer(Entity player) {
        // Set weapon entity
        // Update weapon entity to link with player
        this.weaponEntity.getComponent(FiringController.class).connectPlayer(player);
        this.weaponEntity.getComponent(WeaponAnimationController.class).connectPlayer(player);
        this.weaponEntity.getComponent(PositionTracker.class).connectPlayer(player);
    }

    /**
     * Set up the components to disconnect with the player
     */
    private void disconnectPlayer() {
        // Reset weapon entity
        // Update weapon entity to disconnect from player
        this.weaponEntity.getComponent(FiringController.class).disconnectPlayer();
        this.weaponEntity.getComponent(PositionTracker.class).disconnectPlayer();
        this.weaponEntity.getComponent(WeaponAnimationController.class).disconnectPlayer();
    }
    /**
     * Pick up the ranged weapon and put it in the inventory.
     * @param inventory The inventory to be put in.
     * @param itemEntity The entity of the item to be picked up.
     */
    @Override
    public void pickup(Inventory inventory, Entity itemEntity) {
        logger.info("Picking up ranged weapon - with entity");
        inventory.setMelee(this);

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
        inventory.resetMelee();
        disconnectPlayer();
        this.weaponEntity.setEnabled(false);
    }

    /**
     * Activate this melee weapon in the walk direction of the player
     */
    @Override
    public void attack() {
        this.weaponEntity.getComponent(FiringController.class).activate(null);
    }
}
