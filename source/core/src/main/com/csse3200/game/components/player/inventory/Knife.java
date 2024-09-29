package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;

/**
 * Example Weapon Collectible.
 */
public class Knife extends MeleeWeapon {

    @Override
    public void pickup(Inventory inventory) {
        super.pickup(inventory);
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


