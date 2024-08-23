package com.csse3200.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.extensions.GameExtension;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.*;

/**
 * As gdx files are not initialise properly in test environment, instead of
 * testing whether the file writes into a json file, code's ability to store
 * player's component into configuration is tested
 */
@ExtendWith(GameExtension.class)
public class PlayerConfigGeneratorTest {
    Entity player;
    PlayerConfigGenerator generator;
    InventoryComponent inventoryComponent;
    CombatStatsComponent statsComponent;
    PlayerConfig playerConfig;

    @Before
    public void setUp() {
        player = new Entity();
        inventoryComponent = new InventoryComponent();
        statsComponent = new CombatStatsComponent(100, 30);
        player.addComponent(inventoryComponent).addComponent(statsComponent);
        generator = new PlayerConfigGenerator();
        playerConfig = generator.savePlayerState(player);
    }

    @Test
    public void testInit() {

        // test with default inventory and stats
        assertEquals(30, playerConfig.baseAttack);
        assertEquals(100,playerConfig.health);
        assertEquals(0, playerConfig.items.length);
    }

    @Test
    public void testOneItemInventory() {
        inventoryComponent.getInventory().addItem(new Collectible() {
            @Override
            public Type getType() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public Texture getIcon() {
                return null;
            }

            @Override
            public String getSpecification() {
                return "Health Potion";
            }

            @Override
            public void pickup(Inventory inventory) {

            }

            @Override
            public void drop(Inventory inventory) {

            }
        });
        playerConfig = generator.savePlayerState(player);
        assertEquals("Health Potion", playerConfig.items[0]);
        assertEquals(1, playerConfig.items.length);
    }
}
