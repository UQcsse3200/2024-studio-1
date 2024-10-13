package com.csse3200.game.components.player;

import com.badlogic.gdx.Input;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class KeyMappingTest {
    private KeyMapping keyMapping;

    @BeforeEach
    public void setUp() {
        // Initialize with default key mappings
        keyMapping = new KeyMapping();
    }

    /**
     * Verify default Walk key mappings
     */
    @Test
    public void testDefaultWalkKeyMapping() {
        Map<Integer, KeyMapping.KeyBinding> defaultKeyMap = keyMapping.getKeyMap();
        assertEquals(KeyMapping.KeyBinding.WALK_UP, defaultKeyMap.get(Input.Keys.W));
        assertEquals(KeyMapping.KeyBinding.WALK_LEFT, defaultKeyMap.get(Input.Keys.A));
        assertEquals(KeyMapping.KeyBinding.WALK_RIGHT, defaultKeyMap.get(Input.Keys.D));
        assertEquals(KeyMapping.KeyBinding.WALK_DOWN, defaultKeyMap.get(Input.Keys.S));


    }

    /**
     * Verify default shoot Key Mappings
     */
    @Test
    public void testDefaultShootKeyMapping() {
        Map<Integer, KeyMapping.KeyBinding> defaultKeyMap = keyMapping.getKeyMap();
        assertEquals(KeyMapping.KeyBinding.SHOOT_LEFT, defaultKeyMap.get(Input.Keys.LEFT));
        assertEquals(KeyMapping.KeyBinding.SHOOT_UP, defaultKeyMap.get(Input.Keys.UP));
        assertEquals(KeyMapping.KeyBinding.SHOOT_RIGHT, defaultKeyMap.get(Input.Keys.RIGHT));
        assertEquals(KeyMapping.KeyBinding.SHOOT_DOWN, defaultKeyMap.get(Input.Keys.DOWN));
    }

    /**
     * Verify default Items Key binding
     */
    @Test
    public void testDefaultItemKeyBiding() {
        Map<Integer, KeyMapping.KeyBinding> defaultKeyMap = keyMapping.getKeyMap();
        assertEquals(KeyMapping.KeyBinding.MELEE, defaultKeyMap.get(Input.Keys.SPACE));
        assertEquals(KeyMapping.KeyBinding.USE_1, defaultKeyMap.get(Input.Keys.NUM_1));
        assertEquals(KeyMapping.KeyBinding.USE_2, defaultKeyMap.get(Input.Keys.NUM_2));
        assertEquals(KeyMapping.KeyBinding.USE_3, defaultKeyMap.get(Input.Keys.NUM_3));
    }


    /**
     * Verify that a new binding is set correctly
     */
    @Test
    public void testSetKeyBinding() {
        // Create a custom key mapping
        KeyMapping.KeyBinding newBinding = KeyMapping.KeyBinding.USE_4;
        keyMapping.setKeyBinding(Input.Keys.NUM_4, newBinding);

        Map<Integer, KeyMapping.KeyBinding> keyMap = keyMapping.getKeyMap();
        assertEquals(newBinding, keyMap.get(Input.Keys.NUM_4));
    }

    /**
     * Verify custom binding
     */
    @Test
    public void testGetKeyMap() {
        // Create a custom key mapping
        KeyMapping.KeyBinding customBinding = KeyMapping.KeyBinding.SHOOT_UP;
        keyMapping.setKeyBinding(Input.Keys.F1, customBinding);

        Map<Integer, KeyMapping.KeyBinding> keyMap = keyMapping.getKeyMap();
        assertEquals(customBinding, keyMap.get(Input.Keys.F1));
    }

    /**
     * Verify that after creating custom key mapping, it is updated in Key Map
     */
    @Test
    public void testModifyKeyMap() {
        // Create a custom key mapping
        KeyMapping.KeyBinding initialBinding = KeyMapping.KeyBinding.USE_4;
        keyMapping.setKeyBinding(Input.Keys.NUM_4, initialBinding);

        // Modify the key mapping
        KeyMapping.KeyBinding modifiedBinding = KeyMapping.KeyBinding.WALK_UP;
        keyMapping.setKeyBinding(Input.Keys.NUM_4, modifiedBinding);

        // Verify that the key mapping has been updated
        Map<Integer, KeyMapping.KeyBinding> keyMap = keyMapping.getKeyMap();
        assertEquals(modifiedBinding, keyMap.get(Input.Keys.NUM_4));
        assertNotEquals(initialBinding, keyMap.get(Input.Keys.NUM_4));
    }
}

