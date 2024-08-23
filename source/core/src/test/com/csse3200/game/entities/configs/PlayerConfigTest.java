package com.csse3200.game.entities.configs;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
public class PlayerConfigTest {
    PlayerConfig player;
    @Before
    public void setUp() {
        player = new PlayerConfig();
    }
    @Test
    public void testInitInventory() {
        assertNull(player.items);
    }
    @Test
    public void testInitStats() {
        assertEquals(100, player.health);
        assertEquals(10, player.baseAttack);
    }
}