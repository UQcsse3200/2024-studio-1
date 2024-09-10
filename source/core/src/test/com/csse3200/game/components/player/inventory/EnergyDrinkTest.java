package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnergyDrinkTest {

    EnergyDrink energyDrink = new EnergyDrink("Low", true);
    Entity entity = new Entity().addComponent(new PlayerActions());

    @Test
    public void getNameTest() {
        assertEquals("Energy drink", energyDrink.getName());
    }

    @Test
    public void getSpeedTest() {
        Vector2 baseSpeed = new Vector2(3f, 3f);
        Vector2 speed = baseSpeed.scl(0.3f); //0.3% of the base speed
        assertEquals(speed, energyDrink.getSpeed());
    }

    @Test
    public void getSpecificationTest() {
        assertEquals("buff:energydrink:Low", energyDrink.getSpecification());
    }

    @Test
    public void getItemSpecificationTest() {
        assertEquals("energydrink:Low", energyDrink.getBuffSpecification());
    }

    @Test
    public void getTypeTest() {
        assertEquals(Collectible.Type.BUFF_ITEM, energyDrink.getType());
    }

    /*
    @Test
    public void getMysteryIconTest() {
        assertEquals("images/items/mystery_box_blue.png", energyDrink.getMysteryIcon());
    }
    */
    @Test
    public void effectTest() {
        Vector2 initialSpeed = entity.getComponent(PlayerActions.class).getCurrSpeed();
        Vector2 expectedInitialSpeed = new Vector2(3f, 3f);
        assertEquals(expectedInitialSpeed, initialSpeed);
        float initialSpeedPercentage = entity.getComponent(PlayerActions.class).getCurrSpeedPercentage();
        float expectedInitialPercentage = 0.0f;
        assertEquals(expectedInitialPercentage, initialSpeedPercentage);

        energyDrink.effect(entity);

        Vector2 newSpeed = entity.getComponent(PlayerActions.class).getCurrSpeed();
        Vector2 expectedNewSpeed = new Vector2(3.9f, 3.9f);
        assertEquals(expectedNewSpeed, newSpeed);
        float newSpeedPercentage = entity.getComponent(PlayerActions.class).getCurrSpeedPercentage();
        float expectednewPercentage = 0.3f;
        assertEquals(expectednewPercentage, newSpeedPercentage);
    }

    @Test
    public void maxSpeedTest() {
        float speedLimit = entity.getComponent(PlayerActions.class).getMaxSpeed();
        assertEquals(5.0f, speedLimit);
    }

    @Test
    public void effectTestMax() {
        float speedLimit = entity.getComponent(PlayerActions.class).getMaxSpeed();
        entity.getComponent(PlayerActions.class).setSpeedPercentage(speedLimit);
        energyDrink.effect(entity);
        Vector2 newSpeed = entity.getComponent(PlayerActions.class).getCurrSpeed();
        Vector2 expectedNewSpeed = new Vector2(8.0f, 8.0f);
        assertEquals(expectedNewSpeed, newSpeed);
        float newSpeedPercentage = entity.getComponent(PlayerActions.class).getCurrSpeedPercentage();
        float expectednewPercentage = 5.0f;
        assertEquals(expectednewPercentage, newSpeedPercentage);
    }

}
