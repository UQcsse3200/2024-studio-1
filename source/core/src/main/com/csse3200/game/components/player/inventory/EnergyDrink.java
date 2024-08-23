package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;

public class EnergyDrink extends UsableItem {

    @Override
    public String getName() {
        return "EnergyDrink";
    }

    @Override
    public Texture getIcon() {
        return new Texture("images/items/energy_drink.png");
    }

    @Override
    public void drop(Inventory inventory) {

    }

    @Override
    public void pickup(Inventory inventory) {
        super.pickup(inventory);

        // do the real apply

    }

    @Override
    public void apply(Entity entity) {
        // do nothing

    }
}
