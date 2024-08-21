package com.csse3200.game.components.player.inventory;
import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.entities.Entity;

public class MedKit extends UsableItem{
    @Override
    public void pickup(Inventory inventory) {
        super.pickup(inventory);
    }

    @Override
    public void drop(Inventory inventory) {

    }

    @Override
    public String getName() {
        return "Medical Kit";
    }

    @Override
    public Texture getIcon() {
        return new Texture("images/items/med_kit.png");
    }

    @Override
    public void apply(Entity entity) {

    }
}