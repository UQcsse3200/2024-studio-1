package com.csse3200.game.entities.factories;

import com.csse3200.game.components.player.inventory.usables.Bandage;
import com.csse3200.game.components.player.inventory.usables.MedKit;
import com.csse3200.game.components.player.inventory.usables.ShieldPotion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
    public void testCreateShieldPotion() {
        assertInstanceOf(ShieldPotion.class, itemFactory.create("shieldpotion"));
    }

    @Test
    public void testCreateInvalidItem() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> itemFactory.create("invalid"));
        assertEquals(exception.getMessage(), "Invalid item specification: invalid");
    }

}
