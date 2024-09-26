package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.player.WeaponComponent;
import com.csse3200.game.entities.Entity;

/**
 * A melee weapon that can be picked up by the player.
 */
public class MeleeWeapon implements Collectible {

    private String name;    // name of the weapon
    private String iconPath;// path to the icon of the weapon
    private int damage;     // weapon damage
    private int range;      // range of the weapon
    private int fireRate;   // fire rate of the weapon

    /**
     * Create a melee weapon.
     * @param name the name of the weapon
     * @param iconPath the path to the icon of the weapon
     * @param damage the damage of the weapon
     * @param range the range of the weapon
     * @param fireRate the fire rate of the weapon
     */
    public MeleeWeapon(String name, String iconPath, int damage, int range, int fireRate) {
        this.name = name;
        this.iconPath = iconPath;
        this.damage = damage;
        this.range = range;
        this.fireRate = fireRate;
    }

    /**
     * Get the type of the collectible.
     * @return the type of the collectible
     */
    @Override
    public Type getType() {
        return Type.MELEE_WEAPON;
    }

    /**
     * Pick up the melee weapon and put it in the inventory.
     * @param inventory The inventory to be put in.
     */
    @Override
    public void pickup(Inventory inventory) {
        inventory.setMelee(this);

        // Add a Weapon Component
        if (inventory.getEntity() != null && inventory.getEntity().getComponent(WeaponComponent.class) != null) {
            inventory.getEntity().getComponent(WeaponComponent.class).updateWeapon(this); // update existing weapon
        }
    }

    /**
     * Pick up the melee weapon and put it in the inventory.
     * @param inventory The inventory to be put in.
     * @param itemEntity The entity of the item to be picked up.
     */
    @Override
    public void pickup(Inventory inventory, Entity itemEntity) {
        inventory.setMelee(this);

        // Add a Weapon Component
        if (inventory.getEntity() != null && inventory.getEntity().getComponent(WeaponComponent.class) != null) {
            inventory.getEntity().getComponent(WeaponComponent.class).updateWeapon(this,
                    itemEntity);
            // update existing weapon
        }
    }

    /**
     * Drop the melee weapon from the inventory.
     * @param inventory The inventory to be dropped from.
     */
    @Override
    public void drop(Inventory inventory) {
        inventory.resetMelee();

        if (inventory.getEntity() != null && inventory.getEntity().getComponent(WeaponComponent.class) != null) {
            inventory.getEntity().getComponent(WeaponComponent.class).dropMeleeWeapon(); // remove weapon
        }
    }

    /**
     * Get the specification of the melee weapon.
     */
    @Override
    public String getSpecification() {
        return "melee:" + name;
    }

    /**
     * Get the damage of the melee weapon.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Get the range of the melee weapon.
     */
    public int getRange() {
        return range;
    }

    /**
     * Get the fire rate of the melee weapon.
     */
    public int getFireRate() {
        return fireRate;
    }

    /**
     * Get the name of the melee weapon.
     * @return the name of the melee weapon
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Get the icon of the melee weapon.
     * @return the icon of the melee weapon
     */
    @Override
    public Texture getIcon() {
        return new Texture(iconPath);
    }

    /**
     * Set the damage of the melee weapon.
     * @param damage the new damage of the melee weapon
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Set the range of the melee weapon.
     * @param range the new range of the melee weapon
     */
    public void setRange(int range) {
        this.range = range;
    }

    /**
     * Set the fire rate of the melee weapon.
     * @param fireRate the new fire rate of the melee weapon
     */
    public void setFireRate(int fireRate) {
        this.fireRate = fireRate;
    }


}
