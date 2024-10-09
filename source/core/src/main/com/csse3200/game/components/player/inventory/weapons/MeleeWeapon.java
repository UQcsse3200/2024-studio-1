package com.csse3200.game.components.player.inventory.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.player.inventory.OffHandItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MeleeWeapon extends OffHandItem {
    /**
     * Logger for debugging purposes.
     */
    protected static final Logger logger = LoggerFactory.getLogger(MeleeWeapon.class);
    protected String name;        // name of the weapon
    protected String iconPath;    // path to the icon of the weapon
    protected int damage;         // weapon damage
    protected int range;          // range of the weapon
    protected int fireRate;       // fire rate of the weapon

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
     * Get the filepath of the icon.
     *
     * @return the icons filepath.
     */
    public String getIconPath() {
        return iconPath;
    }

    /**
     * Get the ranged weapon icon.
     *
     * @return The ranged weapon icon.
     */
    public Texture getIcon() {
        return new Texture(iconPath);
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
}