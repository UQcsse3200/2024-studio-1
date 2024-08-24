package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;

/**
 * Example Weapon Collectible.
 */
public class Knife extends MeleeWeapon {

    @Override
    public void pickup(Inventory inventory) {
        super.pickup(inventory);
        inventory.setMelee(this); // Equipped the knife as the current melee weapon
        System.out.println("Knife picked up and equipped!");
//        inventory.getEntity().addComponent(new );
    }

    @Override
    public void attack() {
        System.out.println("stab!");
    }

    @Override
    public String getName() {
        return "knife";
    }

    @Override
    public Texture getIcon() {
        return new Texture("knife.png");
    }
}
