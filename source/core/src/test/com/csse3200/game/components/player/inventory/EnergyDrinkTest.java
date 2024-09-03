package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnergyDrinkTest {
    @Test
    public void getNameTest() {
        EnergyDrink energyDrink = new EnergyDrink();
        String name = energyDrink.getName();
        assertEquals(name, "Energy drink");
        }

//    @Test
//    public void getSpeedTest() {
//        EnergyDrink energyDrink = new EnergyDrink();
//        Vector2 speed = energyDrink.getSpeed();
//        Vector2 compare = new Vector2(6f, 6f);
//        assertEquals(speed, compare);
//    }

    @Test
    public void getSpecificationTest() {
        EnergyDrink energyDrink = new EnergyDrink();
        String specification = energyDrink.getSpecification();
        assertEquals(specification, "buff:energydrink");
    }

    @Test
    public void getItemSpecificationTest() {
        EnergyDrink energyDrink = new EnergyDrink();
        String specification = energyDrink.getBuffSpecification();
        assertEquals(specification, "energydrink");
    }

    @Test
    public void getTypeTest() {
        EnergyDrink energyDrink = new EnergyDrink();
        Collectible.Type type = energyDrink.getType();
        assertEquals(type, Collectible.Type.BUFF_ITEM);
    }
}
