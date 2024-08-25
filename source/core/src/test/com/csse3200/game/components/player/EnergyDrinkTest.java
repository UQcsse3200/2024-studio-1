package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnergyDrinkTest {
    @Test
    public void getNameTest() {
        EnergyDrink energyDrink = new EnergyDrink();
        String name = energyDrink.getName();
        assertEquals(name, "Energy drink");
        };

    @Test
    public void iconTest() {
        EnergyDrink energyDrink = new EnergyDrink();
        Texture icon = energyDrink.getIcon();
        Texture compare = new Texture("images/items/energy_drink.png");
        assertEquals(icon, compare);
    }

    @Test
    public void getSpeedTest() {
        EnergyDrink energyDrink = new EnergyDrink();
        Vector2 speed = energyDrink.getSpeed();
        Vector2 compare = new Vector2(6f, 6f);
        assertEquals(speed, compare);
    }

    @Test
    public void getSpecificationTest() {
        EnergyDrink energyDrink = new EnergyDrink();
        String specification = energyDrink.getSpecification();
        assertEquals(specification, "energydrink");
    }

    @Test
    public void getTypeTest() {
        EnergyDrink energyDrink = new EnergyDrink();
        Collectible.Type type = energyDrink.getType();
        assertEquals(type, Collectible.Type.BUFF_ITEM);
    }
}
