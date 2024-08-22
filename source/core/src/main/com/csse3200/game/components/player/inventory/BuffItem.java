package com.csse3200.game.components.player.inventory;
import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.entities.Entity;

public abstract class BuffItem implements Collectible {
    @Override
    public Type getType() { return Type.BUFF_ITEM; }

    @Override
    public void pickup(Inventory inventory) {
        inventory.getEntity().getEvents().addListener("buffitem", () -> this.effect(inventory.getEntity()));

    }
    @Override
    public String getSpecification() { return "buffitem";}

    public abstract void effect(Entity entity);
}
