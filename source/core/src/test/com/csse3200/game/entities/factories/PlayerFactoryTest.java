package com.csse3200.game.entities.factories;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputFactory;
import com.csse3200.game.input.InputService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.extensions.GameExtension;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class PlayerFactoryTest {

    private PlayerConfig playerConfig;
    private InputService mockInputService;
    private InputFactory mockInputFactory;
    private InputComponent mockInputComponent;

    @Before
    public void setUp() {
        // Mock the InputService, InputFactory, and InputComponent
        mockInputService = mock(InputService.class);
        mockInputFactory = mock(InputFactory.class);
        mockInputComponent = mock(InputComponent.class);

        // Mock the ServiceLocator to return the mock InputService
        try (MockedStatic<ServiceLocator> mockedServiceLocator = mockStatic(ServiceLocator.class)) {
            mockedServiceLocator.when(ServiceLocator::getInputService).thenReturn(mockInputService);
            when(mockInputService.getInputFactory()).thenReturn(mockInputFactory);
            when(mockInputFactory.createForPlayer()).thenReturn(mockInputComponent);

            // Mock FileLoader to return a PlayerConfig based on the test JSON
            try (MockedStatic<FileLoader> mockedFileLoader = mockStatic(FileLoader.class)) {
                mockedFileLoader.when(() -> FileLoader.readClass(PlayerConfig.class, "configs/player_save.json"))
                        .thenReturn(getTestPlayerConfig());

                // Now run the actual test
                Entity player = PlayerFactory.createPlayer();

                // Verify that the player has the correct components
                CombatStatsComponent combatStats = player.getComponent(CombatStatsComponent.class);
                assertNotNull(combatStats);
                assertEquals(150, combatStats.getHealth());
                assertEquals(20, combatStats.getBaseAttack());

                InventoryComponent inventory = player.getComponent(InventoryComponent.class);
                assertNotNull(inventory);

                // Verify that the inventory has the correct items
                assertEquals(2, inventory.getInventory().getItems().size);
                assertTrue(inventory.getInventory().getItems().get(0) instanceof EnergyDrink);
                assertTrue(inventory.getInventory().getItems().get(1) instanceof ShieldPotion);

                // Verify that the melee and ranged weapons are set correctly
                assertTrue(inventory.getInventory().getMelee().get() instanceof Knife);
                assertTrue(inventory.getInventory().getRanged().get() instanceof ConcreteRangedWeapon);
            }
        }
    }

    private PlayerConfig getTestPlayerConfig() {
        PlayerConfig config = new PlayerConfig();
        config.health = 150;
        config.baseAttack = 20;
        config.items = new String[]{"item:EnergyDrink", "item:ShieldPotion"};
        config.melee = "Knife";
        config.ranged = "ConcreteRangedWeapon";
        return config;
    }
}
