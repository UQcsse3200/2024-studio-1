package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;

public class EnergyDrink implements Collectible {
    @Override
    public Type getType() {
        return Type.ITEM;
    }

    @Override
    public String getName() {
        return "Energy drink";
    }

    @Override
    public Texture getIcon() {
        return new Texture("images/items/energy_drink.png");
    }

    @Override
    public void pickup(Inventory inventory) {

    }

    @Override
    public void drop(Inventory inventory) {

    }
}
