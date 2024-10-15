package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.player.PlayerConfigComponent;
import com.csse3200.game.components.player.inventory.buffs.EnergyDrink;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
public class EnergyDrinkTest {

    EnergyDrink energyDrink = new EnergyDrink("low");
    Entity entity = new Entity()
            .addComponent(new PlayerActions())
            .addComponent(new ItemPickupComponent())
            .addComponent(new CombatStatsComponent(1, 1, true, 1, 0));

    @Mock
    private PlayerConfig playerConfig;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        playerConfig = mock(PlayerConfig.class);
        entity.addComponent(new PlayerConfigComponent(playerConfig));
        playerConfig.speed = new Vector2(3f, 3f);

    }

    @Test
    public void getNameTest() {
        assertEquals("Energy drink", energyDrink.getName());
    }

    @Test
    public void getSpeedTest() {
        energyDrink.effect(entity);
        Vector2 speed = energyDrink.getSpeed();
        Vector2 baseSpeed = entity.getComponent(PlayerActions.class).getBaseSpeed();
        assertEquals(baseSpeed.scl(0.03f), speed);
    }

    @Test
    public void getSpecificationTest() {
        assertEquals("buff:energydrink:low", energyDrink.getSpecification());
    }

    @Test
    public void getItemSpecificationTest() {
        assertEquals("energydrink:low", energyDrink.getBuffSpecification());
    }

    @Test
    public void getTypeTest() {
        assertEquals(Collectible.Type.BUFF_ITEM, energyDrink.getType());
    }


//    @Test
//    public void getMysteryIconTest() {
//        assertEquals(new Texture("images/items/mystery_box_blue.png"), energyDrink.getMysteryIcon());
//    }

    @Test
    public void effectTest() {
        Vector2 initialSpeed = entity.getComponent(PlayerActions.class).getCurrPlayerSpeed();
        Vector2 expectedInitialSpeed = new Vector2(3f, 3f);
        assertEquals(expectedInitialSpeed, initialSpeed);
        Vector2 baseSpeed = entity.getComponent(PlayerActions.class).getBaseSpeed();
        float initialSpeedPercentage = entity.getComponent(PlayerActions.class).getSpeedProgressBarProportion(initialSpeed, baseSpeed);
        float expectedInitialPercentage = 0.0f;
        assertEquals(expectedInitialPercentage, initialSpeedPercentage);

        energyDrink.effect(entity);

        Vector2 newSpeed = entity.getComponent(PlayerActions.class).getCurrPlayerSpeed();
        Vector2 expectedNewSpeed = new Vector2(3.09f, 3.09f); //0.03% of the base speed
        assertEquals(expectedNewSpeed, newSpeed);

        float newSpeedPercentage = entity.getComponent(PlayerActions.class).getSpeedProgressBarProportion(newSpeed, baseSpeed);
        float expectednewPercentage = 0.03f; //Speed is 0.03 times faster

        //To account for rounding issues - i.e the new speedPercentage is 0.0299
        assertTrue(Math.abs(expectednewPercentage - newSpeedPercentage) < 0.001);
    }

    @Test
    public void maxSpeedTest() {
        float speedLimit = entity.getComponent(PlayerActions.class).getMaxTotalSpeedBoost();
        assertEquals(0.5f, speedLimit);
    }

    @Test
    public void effectTestMax() {
        //Set player speed to max limit
        float speedLimit = entity.getComponent(PlayerActions.class).getMaxTotalSpeedBoost();
        Vector2 baseSpeed = entity.getComponent(PlayerActions.class).getBaseSpeed();
        Vector2 maxPlayerSpeed = entity.getComponent(PlayerActions.class).getMaxPlayerSpeed();
        entity.getComponent(PlayerActions.class).setSpeed(maxPlayerSpeed);

        //Energy drink effect goes over max speed
        energyDrink.effect(entity);

        //Max speed should be capped to a boost of 0.5% of the base speed
        Vector2 newSpeed = entity.getComponent(PlayerActions.class).getCurrPlayerSpeed();
        Vector2 expectedNewSpeed = new Vector2(4.5f, 4.5f);
        assertEquals(expectedNewSpeed, newSpeed);

        float newSpeedPercentage = entity.getComponent(PlayerActions.class).getSpeedProgressBarProportion(newSpeed, baseSpeed);
        float expectednewPercentage = 0.5f; //Speed is 0.5 times faster

        //To account for rounding issues - i.e the new speedPercentage is 0.0299
        assertTrue(Math.abs(expectednewPercentage - newSpeedPercentage) < 0.001);
    }

}
