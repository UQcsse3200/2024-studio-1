package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MeleeWeapon implements Collectible {
    /**
     * Logger for debugging purposes.
     */
    protected static final Logger logger = LoggerFactory.getLogger(RangedWeapon.class);
    protected String name;        // name of the weapon
    protected String iconPath;    // path to the icon of the weapon
    protected int damage;         // weapon damage
    protected int range;          // range of the weapon
    protected int fireRate;       // fire rate of the weapon

    /**
     * Get the Type of this item.
     * The type determines how it ought to be used by the player.
     *
     * @return mType.MELEE_WEAPON
     */
    @Override
    public Type getType() {
        return Type.MELEE_WEAPON;
    }

    /**
     * Get the name of the weapon.
     *
     * @return The name of the weapon.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Get the ranged weapon icon.
     *
     * @return The ranged weapon icon.
     */
    public Texture getIcon() {
        return new Texture(iconPath);
    }

    @Override
    public String getSpecification() {
        return "melee:" + getName();
    }

    /**
     * Pick up the melee weapon and put it in the inventory.
     *
     * @param inventory The inventory to be put in.
     */
    @Override
    public void pickup(Inventory inventory) {
        inventory.getContainer(MeleeWeapon.class).add(this);
    }

    @Override
    public void drop(Inventory inventory) {
        inventory.getContainer(MeleeWeapon.class).remove(this);
    }

    /**
     * Set the name of the weapon.
     *
     * @param name The name of the weapon.
     */
    public void setName(String name) {
        this.name = name;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getFireRate() {
        return fireRate;
    }

    public void setFireRate(int fireRate) {
        this.fireRate = fireRate;
    }

    /**
     * Activate this melee weapon in the walk direction of the player
     */
    public abstract void attack();
}