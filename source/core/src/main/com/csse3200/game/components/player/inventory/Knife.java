package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.entities.Entity;

/**
 * Collectible Knife weapon
 */
public class Knife extends MeleeWeapon {

    public Knife() {
        this.setDamage(200);
        this.setRange(0);
        this.setFireRate(10);
    }

    @Override
    public void pickup(Inventory inventory) {
        super.pickup(inventory);
    }
    @Override
    public void pickup(Inventory inventory, Entity itemEntity) {
        super.pickup(inventory, itemEntity);
    }

    @Override
    public String getMeleeSpecification() {
        return "knife";
    }

    @Override
    public void drop(Inventory inventory) {
        super.drop(inventory);
    }

    @Override
    public String getName() {
        return "knife";
    }

    @Override
    public Texture getIcon() {
        return new Texture("images/Weapons/knife.png");
    }
}


