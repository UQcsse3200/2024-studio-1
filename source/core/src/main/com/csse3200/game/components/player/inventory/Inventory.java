package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.entities.Entity;

import java.util.Optional;

/**
 * A component intended to be used by the player to track their inventory.
 * <p>
 * Currently only stores the gold amount but can be extended for more advanced functionality such as storing items.
 * Can also be used as a more generic component for other entities.
 */
public class Inventory {
    private final Entity entity;
    private final Array<Collectible> items = new Array<>();
    private Optional<MeleeWeapon> meleeWeapon = Optional.empty();
    private Optional<RangedWeapon> rangedWeapon = Optional.empty();

    public Inventory(Entity entity) {
        super();
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    /**
     * Set the player's currently held melee weapon.
     *
     * @param melee the melee weapon to pickup.
     */
    public void setMelee(MeleeWeapon melee) {
        resetMelee();
        this.meleeWeapon = Optional.of(melee);
    }

    /**
     * Reset the player's currently held melee weapon to the default.
     */
    public void resetMelee() {
        this.meleeWeapon.ifPresent(w -> w.drop(this.entity));
        this.meleeWeapon = Optional.empty();
    }

    /**
     * Set the player's currently held ranged weapon.
     *
     * @param ranged the ranged weapon to pickup.
     */
    public void setRanged(RangedWeapon ranged) {
        resetRanged();
        this.rangedWeapon = Optional.of(ranged);
    }

    /**
     * Reset the player's currently held ranged weapon to the default.
     */
    public void resetRanged() {
        this.rangedWeapon.ifPresent(w -> w.drop(this.entity));
        this.rangedWeapon = Optional.empty();
    }

    /**
     * Get the player's currently held melee weapon.
     *
     * @return the melee weapon currently held.
     */
    public MeleeWeapon getMelee() {
        return meleeWeapon.orElse(null); // FIXME reset to default
    }

    /**
     * Get the player's currently held ranged weapon.
     *
     * @return the ranged weapon currently held.
     */
    public RangedWeapon getRanged() {
        return rangedWeapon.orElse(null); // FIXME reset to default
    }

    /**
     * Get the current list of items.
     *
     * @return the current list of items
     */
    public Array<Collectible> getItems() {
        return new Array<>(items);
    }
}
