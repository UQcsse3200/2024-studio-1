package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;

public class Reroll extends UsableItem {
    @Override
    public String getName() {
        return "Reroll";
    }

    @Override
    public Texture getIcon() {
        return new Texture("images/items/mystery_box_red.png");
    }


    @Override
    public String getItemSpecification() {
        return "reroll";
    }

    @Override
    public void apply(Entity entity) {
        entity.getEvents().trigger("rerollUsed");
        //Do all the respawning stuff here
    }

}
