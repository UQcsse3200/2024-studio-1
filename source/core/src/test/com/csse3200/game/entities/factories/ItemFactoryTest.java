package com.csse3200.game.entities.factories;

import com.csse3200.game.components.player.inventory.Bandage;
import com.csse3200.game.components.player.inventory.EnergyDrink;
import com.csse3200.game.components.player.inventory.MedKit;
import static org.junit.jupiter.api.Assertions.*;
import com.csse3200.game.components.player.inventory.ShieldPotion;
import org.junit.jupiter.api.Test;

public class ItemFactoryTest {

    ItemFactory itemFactory = new ItemFactory();

    @Test
    public void testCreateMedKit() {
        assertInstanceOf(MedKit.class, itemFactory.create("medkit"));
    }

    @Test
    public void testCreateBandage() {
        assertInstanceOf(Bandage.class, itemFactory.create("bandage"));
    }

    @Test
    public void testCreateEnergyDrink() {
        assertInstanceOf(EnergyDrink.class, itemFactory.create("energydrink,Low"));
    }

    @Test
    public void testCreateShieldPotion() {
        assertInstanceOf(ShieldPotion.class, itemFactory.create("shieldpotion"));
    }

    @Test
    public void testCreateInvalidItem() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> itemFactory.create("invalid"));
        assertEquals(exception.getMessage(), "Invalid item specification: invalid");
    }

}
