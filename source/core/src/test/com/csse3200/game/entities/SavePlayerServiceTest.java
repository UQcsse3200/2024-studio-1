package com.csse3200.game.entities;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.Inventory;
import com.csse3200.game.components.player.inventory.InventoryComponent;
import com.csse3200.game.components.player.inventory.UsableItem;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;


import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
public class SavePlayerServiceTest {

    /*
    @Before
    public void setUp() {
        InputService inputService = mock(InputService.class);
        ServiceLocator.registerInputService(inputService);
    }

     */

    @Test
    public void testSavePlayer() {
        Entity player = PlayerFactory.createPlayer();
/*
        player.getComponent(CombatStatsComponent.class).setHealth(50);
        player.getComponent(CombatStatsComponent.class).setBaseAttack(20);
        player.getComponent(InventoryComponent.class).pickup(new UsableItem() {
            @Override
            public void apply(Entity entity) {

            }

            @Override
            public String getName() {
                return "Potion";
            }

            @Override
            public Texture getIcon() {
                return null;
            }

            @Override
            public void drop(Inventory inventory) {

            }
        });

 */

        SavePlayerService savePlayerService = new SavePlayerService();
        savePlayerService.savePlayerState(player);

        // Load the player config from file
         PlayerConfig loadedConfig = FileLoader.readClass(PlayerConfig.class, "configs/player_save.json");

        // Check if the loaded config matches the player's current state
        assertEquals(100, loadedConfig.health);
        //assertEquals(20, loadedConfig.baseAttack);
        //assertTrue(Arrays.stream(loadedConfig.items).toList().contains("Potion"));

    }
}
