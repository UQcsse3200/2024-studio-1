package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.entities.Entity;

public class SpeedBoost extends UsableItem {

    @Override
    public String getName() {
        return "SpeedBoost";
    }

    @Override
    public Texture getIcon() {
        return new Texture("images/items/energy_drink.png");
    }

    @Override
    public void drop(Inventory inventory) {

    }

    @Override
    public void apply(Entity entity) {

    }
}
