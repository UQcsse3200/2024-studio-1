package com.csse3200.game.entities.configs;

import static org.junit.jupiter.api.Assertions.*;
import com.csse3200.game.entities.configs.NPCConfigs.NPCConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.badlogic.gdx.math.Vector2;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NPCConfigsTest {

    private NPCConfigs npcConfigs;
    private Map<String, NPCConfig> npcConfigMap;

    @BeforeEach
    public void setUp() {
        npcConfigMap = new HashMap<>();

        // Create a sample NPCConfig and add it to the map
        NPCConfig npcConfig = new NPCConfig();
        npcConfig.strength = 10;
        npcConfig.isBoss = true;
        npcConfig.isDirectional = true;
        npcConfig.scale = new Vector2(1, 1);
        npcConfigMap.put("rat", npcConfig);

        npcConfigs = new NPCConfigs(npcConfigMap);
    }

    @Test
    public void testGetConfig() {
        // Get the config for "Warrior" and check if it matches the expected values
        NPCConfig config = npcConfigs.getConfig("rat");

        assertNotNull(config);
        assertEquals("rat", config.name);
        assertEquals(10, config.strength);
        assertTrue(config.isBoss);
        assertTrue(config.isDirectional);
        assertEquals(new Vector2(1, 1), config.scale);
    }

    @Test
    public void testGetNpcTypes() {
        // Check if the NPC types include "Warrior"
        Set<String> npcTypes = npcConfigs.getNpcTypes();

        assertNotNull(npcTypes);
        assertTrue(npcTypes.contains("rat"));
    }
}
