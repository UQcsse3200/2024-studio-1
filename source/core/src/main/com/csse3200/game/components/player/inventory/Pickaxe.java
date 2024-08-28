package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.PlayerActions;

/**
 * Example Weapon Collectible.
 */
public class Pickaxe extends MeleeWeapon {

    @Override
    public void pickup(Inventory inventory) {
        super.pickup(inventory);
    }

    @Override
    public void drop(Inventory inventory) {
        super.drop(inventory);
    }

    @Override
    public String getMeleeSpecification() {
        return "pickaxe";
    }

    @Override
    public String getName() {
        return "pickaxe";
    }

    @Override
    public Texture getIcon() {
        return new Texture("/images/Weapons/pickaxe.png");
    }
}


