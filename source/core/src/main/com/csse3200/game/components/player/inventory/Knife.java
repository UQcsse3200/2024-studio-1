package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.player.WeaponComponent;
import com.csse3200.game.entities.Entity;

public class Knife extends MeleeWeapon {

    @Override
    public void pickup(Inventory inventory) {
        super.pickup(inventory);
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
