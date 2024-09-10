package com.csse3200.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.PlayerConfigComponent;
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
    PlayerConfigGenerator generator = new PlayerConfigGenerator();
    InventoryComponent inventoryComponent;
    CombatStatsComponent statsComponent;

    @Before
    public void setUp() {
        player = new Entity();
        inventoryComponent = new InventoryComponent();
        statsComponent = new CombatStatsComponent(100, 30, true, 0, 0);
        player.addComponent(inventoryComponent).addComponent(statsComponent);
        player.addComponent(new PlayerConfigComponent(new PlayerConfig()));
    }

    /**
     * Test with default player attributes
     */
    @Test
    public void testInit() {
        PlayerConfig playerConfig = generator.savePlayerState(player);
        assertEquals(30, playerConfig.baseAttack);
        assertEquals(100,playerConfig.health);
        assertEquals(0, playerConfig.items.length);
    }

    /**
     * Test with customised Combat stats component
     */
    @Test
    public void testCustomCombatStat() {
        statsComponent.setHealth(50);
        statsComponent.setBaseAttack(20);
        PlayerConfig playerConfig = generator.savePlayerState(player);
        assertEquals(50, playerConfig.health);
        assertEquals(20, playerConfig.baseAttack);
    }

    /**
     * Test saved player's melee weapon
     */
    @Test
    public void testMeleeWeapon() {
        inventoryComponent.getInventory().setMelee(new MeleeWeapon() {

            @Override
            public String getMeleeSpecification() {
                return "test";
            }

            @Override
            public String getName() {
                return "Knife";
            }

            @Override
            public Texture getIcon() {
                return null;
            }
        });

        PlayerConfig playerConfig = generator.savePlayerState(player);
        assertEquals("melee:test", playerConfig.melee);
    }


    /** Test player's saved Ranged weapon */
    @Test
    public void testRangedWeapon() {
        inventoryComponent.getInventory().setRanged(new RangedWeapon() {
            @Override
            public String getRangedSpecification() {
                return "test";
            }

            @Override
            public void shoot(Vector2 direction) {

            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public Texture getIcon() {
                return null;
            }
        });
        PlayerConfig playerConfig = generator.savePlayerState(player);
        assertEquals("ranged:test", playerConfig.ranged);
    }

    /** Test player with no weapon */
    @Test
    public void testNoWeapon() {
        PlayerConfig playerConfig = generator.savePlayerState(player);
        assertEquals("", playerConfig.ranged);
        assertEquals("", playerConfig.melee);
    }

    @Test
    public void testNoInventory() {
        PlayerConfig playerConfig = generator.savePlayerState(player);
        assertEquals(0, playerConfig.items.length);
    }
    /**
     * Test player with only one item collected
     */
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
        PlayerConfig playerConfig = generator.savePlayerState(player);
        assertEquals("Health Potion", playerConfig.items[0]);
        assertEquals(1, playerConfig.items.length);
    }

    /**
     * Test player with multiple items collected
     */
    @Test
    public void testMultipleItemsInventory() {
        // add a new item to player's inventory
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
                return "Energy bar";
            }

            @Override
            public void pickup(Inventory inventory) {

            }

            @Override
            public void drop(Inventory inventory) {

            }
        });
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
                return "Food";
            }

            @Override
            public void pickup(Inventory inventory) {

            }

            @Override
            public void drop(Inventory inventory) {

            }
        });

        PlayerConfig playerConfig = generator.savePlayerState(player);
        String[] expected = {"Energy bar", "Food"};
        assertArrayEquals(expected, playerConfig.items);
    }


}
