package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;

import java.util.logging.Logger;

public abstract class MeleeWeapon implements Collectible {
    /**
     * Logger for debugging purposes.
     */
    protected static final Logger logger = Logger.getLogger(RangedWeapon.class.getName());
    protected String name;        // name of the weapon
    protected String iconPath;    // path to the icon of the weapon
    protected int damage;         // weapon damage
    protected int range;          // range of the weapon
    protected int fireRate;       // fire rate of the weapon

    /**
     * Get the Type of this item.
     * The type determines how it ought to be used by the player.
     * @return mType.MELEE_WEAPON
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
    }

    @Override
    public void drop(Inventory inventory) {
        inventory.resetMelee();
    }

    @Override
    public String getSpecification() {
        return "melee:" + getName();
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public int getFireRate() {
        return fireRate;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setFireRate(int fireRate) {
        this.fireRate = fireRate;
    }

    /**
     * Get the ranged weapon icon.
     * @return The ranged weapon icon.
     */
    public Texture getIcon() {
        return new Texture(iconPath);
    }

    /**
     * Get the name of the weapon.
     * @return The name of the weapon.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Set the name of the weapon.
     * @param name The name of the weapon.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Activate this melee weapon in the walk direction of the player
     */
    public abstract void attack();
}