package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.player.inventory.buffs.EnergyDrink;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
public class EnergyDrinkTest {

    EnergyDrink energyDrink = new EnergyDrink("Low");
    Entity entity = new Entity()
            .addComponent(new PlayerActions())
            .addComponent(new ItemPickupComponent())
            .addComponent(new CombatStatsComponent(1, 1, true, 1, 0));

    @Test
    public void getNameTest() {
        assertEquals("Energy drink", energyDrink.getName());
    }

//    @Test
//    public void getSpeedTest() {
//        Vector2 baseSpeed = new Vector2(3f, 3f);
//        Vector2 speed = baseSpeed.scl(0.03f); //0.03% of the base speed
//        assertEquals(speed, energyDrink.getSpeed());
//    }

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
//    @Test
//    public void effectTest() {
//        Vector2 initialSpeed = entity.getComponent(PlayerActions.class).getCurrPlayerSpeed();
//        Vector2 expectedInitialSpeed = new Vector2(3f, 3f);
//        assertEquals(expectedInitialSpeed, initialSpeed);
//        float initialSpeedPercentage = entity.getComponent(PlayerActions.class).getTotalSpeedBoost();
//        float expectedInitialPercentage = 0.0f;
//        assertEquals(expectedInitialPercentage, initialSpeedPercentage);
//
//        energyDrink.effect(entity);
//
//        Vector2 newSpeed = entity.getComponent(PlayerActions.class).getCurrPlayerSpeed();
//        Vector2 expectedNewSpeed = new Vector2(3.09f, 3.09f); //0.03% of the base speed
//        assertEquals(expectedNewSpeed, newSpeed);
//        float newSpeedPercentage = entity.getComponent(PlayerActions.class).getTotalSpeedBoost();
//        float expectednewPercentage = 0.03f; //Speed is 0.03 times faster
//        assertEquals(expectednewPercentage, newSpeedPercentage);
//    }
//
//    @Test
//    public void maxSpeedTest() {
//        float speedLimit = entity.getComponent(PlayerActions.class).getMaxTotalSpeedBoost();
//        assertEquals(1.5f, speedLimit);
//    }
//
//    @Test
//    public void effectTestMax() {
//        float speedLimit = entity.getComponent(PlayerActions.class).getMaxTotalSpeedBoost();
//        entity.getComponent(PlayerActions.class).setTotalSpeedBoost(speedLimit);
//        energyDrink.effect(entity);
//        Vector2 newSpeed = entity.getComponent(PlayerActions.class).getCurrPlayerSpeed();
//        Vector2 expectedNewSpeed = new Vector2(4.5f, 4.5f);
//        assertEquals(expectedNewSpeed, newSpeed);
//        float newSpeedPercentage = entity.getComponent(PlayerActions.class).getTotalSpeedBoost();
//        float expectednewPercentage = 1.5f;
//        assertEquals(expectednewPercentage, newSpeedPercentage);
//    }

}
