package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;


public class EnergyDrink extends BuffItem {

    private static final Vector2 speed = new Vector2(6f, 6f);

    @Override
    public String getName() {
        return "Energy drink";
    }

    @Override
    public Texture getIcon() {
        return new Texture("images/items/energy_drink.png");
    }

    @Override
    public void drop(Inventory inventory) {

    }

    @Override
    public void effect(Entity entity) {
        entity.getComponent(PlayerActions.class).setSpeed(this.getSpeed());
    }

    public Vector2 getSpeed() {
        return speed;
    }

    @Override
    public String getSpecification() { return "energydrink";}
}
