package com.csse3200.game.components.player.inventory;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class InventoryComponentTest {

    private static class TestException extends RuntimeException {
    }

    @Test
    public void testEntityIsSame() {
        InventoryComponent inventoryComponent = new InventoryComponent();
        Entity entity = new Entity().addComponent(inventoryComponent);
        Inventory inventory = inventoryComponent.getInventory();
        assertEquals(entity, inventory.getEntity());
    }

    @Test
    public void testWeaponsAreEmpty() {
        InventoryComponent inventoryComponent = new InventoryComponent();
        new Entity().addComponent(inventoryComponent);
        Inventory inventory = inventoryComponent.getInventory();
        assertTrue(inventory.getMelee().isEmpty());
        assertTrue(inventory.getRanged().isEmpty());
    }

    @Test
    public void testItemsAreEmpty() {
        InventoryComponent inventoryComponent = new InventoryComponent();
        new Entity().addComponent(inventoryComponent);
        Inventory inventory = inventoryComponent.getInventory();
        assertTrue(inventory.getItems().isEmpty());
    }

    @Test
    public void testPickupMelee() {
        InventoryComponent inventoryComponent = new InventoryComponent();
        new Entity().addComponent(inventoryComponent);
        Inventory inventory = inventoryComponent.getInventory();

        //Knife knife = new Knife();
        MeleeWeapon knife = new MeleeWeapon("Knife", "images/Weapons/knife.png", 10, 10, 10);
        inventory.setMelee(knife);

        assertTrue(inventory.getMelee().isPresent());
        assertEquals(knife, inventory.getMelee().get());
    }

    @Test
    public void testPickupRanged() {
        // Register the necessary service for creating the weapon entity when picking up weapon
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        InventoryComponent inventoryComponent = new InventoryComponent();
        new Entity().addComponent(inventoryComponent);
        Inventory inventory = inventoryComponent.getInventory();

        RangedWeapon shotgun = new RangedWeapon( "Shotgun", "images/Weapons/shotgun.png", 10, 10, 10, 10, 10, 10);
        inventoryComponent.pickup(shotgun);

        assertTrue(inventory.getRanged().isPresent());
        assertEquals(shotgun, inventory.getRanged().get());
    }

    @Test
    public void testRangedDrop() {
        // Register the necessary service for creating the weapon entity when picking up weapon
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        InventoryComponent inventoryComponent = new InventoryComponent();
        new Entity().addComponent(inventoryComponent);
        Inventory inventory = inventoryComponent.getInventory();

        RangedWeapon shotgun = new RangedWeapon( "Shotgun", "images/Weapons/shotgun.png", 10, 10, 10, 10, 10, 10) {
            @Override
            public void drop(Inventory inventory) {
                super.drop(inventory);
                throw new TestException();
            }
        };

        inventoryComponent.pickup(shotgun);

        Assert.assertThrows(TestException.class, () -> inventoryComponent.drop(shotgun));
        assertTrue(inventory.getRanged().isEmpty());
    }

    @Test
    public void testMeleeDrop() {
        InventoryComponent inventoryComponent = new InventoryComponent();
        new Entity().addComponent(inventoryComponent);
        Inventory inventory = inventoryComponent.getInventory();

        MeleeWeapon knife = new MeleeWeapon("Knife", "images/Weapons/knife.png", 10, 10, 10) {
            @Override
            public void drop(Inventory inventory) {
                super.drop(inventory);
                throw new TestException();
            }
        };

        inventoryComponent.pickup(knife);

        Assert.assertThrows(TestException.class, () -> inventoryComponent.drop(knife));
        assertTrue(inventory.getMelee().isEmpty());
    }

    @Test
    public void testItemAdded() {
        InventoryComponent inventoryComponent = new InventoryComponent();
        new Entity()
                .addComponent(inventoryComponent)
                .addComponent(new CombatStatsComponent(10, 10));
        Inventory inventory = inventoryComponent.getInventory();

        MedKit medkit = new MedKit();
        inventoryComponent.pickup(medkit);

        assertTrue(inventory.getItems().contains(medkit, true));
    }

}
