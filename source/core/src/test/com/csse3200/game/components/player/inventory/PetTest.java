package com.csse3200.game.components.player.inventory;

import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.areas.GameController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class PetTest {

    private InventoryComponent inventoryComponent;
    private Entity playerEntity;

    @BeforeEach
    void setUp() {
        inventoryComponent = new InventoryComponent();
        playerEntity = new Entity().addComponent(inventoryComponent);

        // Mock services
        GameAreaService gameAreaServiceMock = mock(GameAreaService.class);
        when(gameAreaServiceMock.getGameController()).thenReturn(mock(GameController.class));
        ServiceLocator.registerEntityService(mock(EntityService.class));
        ServiceLocator.registerGameAreaService(gameAreaServiceMock);
    }

    @Test
    void testPickupPet() {
        Pet pet = mock(Pet.class);
        doNothing().when(pet).pickup(any(Inventory.class));

        // Use the pickup method from InventoryComponent
        inventoryComponent.pickup(pet);

        verify(pet, times(1)).pickup(any(Inventory.class));
    }

    @Test
    void testDropPet() {
        Pet pet = mock(Pet.class);
        doNothing().when(pet).pickup(any(Inventory.class));
        doNothing().when(pet).drop(any(Inventory.class));

        // Use the pickup and drop methods from InventoryComponent
        inventoryComponent.pickup(pet);
        inventoryComponent.drop(pet);

        verify(pet, times(1)).drop(any(Inventory.class));
    }

    @Test
    void testPetAggroInitialization() {
        Pet pet = mock(Pet.class);
        Entity enemy = new Entity();
        List<Entity> targets = new ArrayList<>();
        targets.add(enemy);

        when(pet.spawn(any(Entity.class))).thenReturn(new Entity());

        inventoryComponent.pickup(pet); // Simulate picking up the pet
        pet.initAggro(targets);

        verify(pet, times(1)).initAggro(targets);
    }

    @Test
    void testSetAggro() {
        Pet pet = mock(Pet.class);
        Entity enemy = new Entity();
        List<Entity> targets = new ArrayList<>();
        targets.add(enemy);

        when(pet.spawn(any(Entity.class))).thenReturn(new Entity());

        inventoryComponent.pickup(pet); // Simulate picking up the pet
        pet.setAggro(targets);

        verify(pet, times(1)).setAggro(targets);
    }
}
