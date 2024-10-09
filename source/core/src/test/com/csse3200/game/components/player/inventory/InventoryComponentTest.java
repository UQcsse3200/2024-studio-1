package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.usables.MedKit;
import com.csse3200.game.components.player.inventory.weapons.MeleeWeapon;
import com.csse3200.game.components.player.inventory.weapons.RangedWeapon;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(GameExtension.class)
class InventoryComponentTest {

    @Test
    public void testWeaponsAreEmpty() {
        InventoryComponent inventory = new InventoryComponent();
        new Entity().addComponent(inventory);
        assertTrue(inventory.getOffhand().isEmpty());
        assertTrue(inventory.getMainWeapon().isEmpty());
    }

    @Test
    public void testItemsAreEmpty() {
        InventoryComponent inventory = new InventoryComponent();
        new Entity().addComponent(inventory);
        assertTrue(inventory.getItems().isEmpty());
    }

    @Test
    public void testPickupMelee() {
        InventoryComponent inventory = new InventoryComponent();
        new Entity().addComponent(inventory);

        //Knife knife = new Knife();
        MeleeWeapon knife = new MeleeWeapon(){
            @Override
            public void attack() {

            }
        };
        inventory.pickup(knife);

        assertTrue(inventory.getOffhand().isPresent());
        assertEquals(knife, inventory.getOffhand().get());
    }

    @Test
    public void testPickupRanged() {
        InventoryComponent inventory = new InventoryComponent();
        new Entity().addComponent(inventory);

        RangedWeapon shotgun = new RangedWeapon() {
            @Override
            public void drop(Inventory inventory) {
                super.drop(inventory);
                throw new TestException();
            }
            @Override
            public void shoot(Vector2 direction) {}
        };
        inventory.pickup(shotgun);

        assertTrue(inventory.getMainWeapon().isPresent());
        assertEquals(shotgun, inventory.getMainWeapon().get());
    }

    @Test
    public void testRangedDrop() {


        InventoryComponent inventory = new InventoryComponent();
        new Entity().addComponent(inventory);

        RangedWeapon shotgun = new RangedWeapon() {
            @Override
            public void drop(Inventory inventory) {
                super.drop(inventory);
                throw new TestException();
            }
            @Override
            public void shoot(Vector2 direction) {}
        };

        inventory.pickup(shotgun);

        Assert.assertThrows(TestException.class, () -> inventory.drop(shotgun));
        assertTrue(inventory.getMainWeapon().isEmpty());
    }

    @Test
    public void testMeleeDrop() {
        InventoryComponent inventory = new InventoryComponent();
        new Entity().addComponent(inventory);

        MeleeWeapon knife = new MeleeWeapon() {
            @Override
            public void drop(Inventory inventory) {
                super.drop(inventory);
                throw new TestException();
            }

            @Override
            public void attack() {}
        };

        inventory.pickup(knife);

        Assert.assertThrows(TestException.class, () -> inventory.drop(knife));
        assertTrue(inventory.getOffhand().isEmpty());
    }

    @Test
    public void testItemAdded() {
        InventoryComponent inventory = new InventoryComponent();
        new Entity()
                .addComponent(inventory)
                .addComponent(new CombatStatsComponent(10, 10));

        MedKit medkit = new MedKit();
        inventory.pickup(medkit);

        assertTrue(inventory.getItems().contains(medkit, true));
    }

    private static class TestException extends RuntimeException {
        // Simple Exception
    }

}
