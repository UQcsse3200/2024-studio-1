package com.csse3200.game.components.player;

import com.badlogic.gdx.Input;

import java.util.HashMap;
import java.util.Map;

import static com.csse3200.game.components.player.KeyMapping.KeyBinding.*;

/**
 * A mapping of keyboard keys to Player Actions
 */
public class KeyMapping {
    private final Map<Integer, KeyBinding> keyMap;

    /**
     * Each of the kinds of action the player could do.
     */
    public enum KeyBinding {
        WALK_UP,
        WALK_DOWN,
        WALK_LEFT,
        WALK_RIGHT,
        SHOOT_LEFT,
        SHOOT_UP,
        SHOOT_RIGHT,
        SHOOT_DOWN,
        MELEE
    }

    /**
     * Create a key map from a specification
     * @param keyMap A mapping of keys to actions
     */
    public KeyMapping(Map<Integer, KeyBinding> keyMap) {
        this.keyMap = keyMap;
    }

    /**
     * Create a default key map.
     */
    public KeyMapping() {
        this(Map.of(
                Input.Keys.W, WALK_UP,
                Input.Keys.A, WALK_LEFT,
                Input.Keys.D, WALK_RIGHT,
                Input.Keys.S, WALK_DOWN,

                Input.Keys.LEFT, SHOOT_LEFT,
                Input.Keys.UP, SHOOT_UP,
                Input.Keys.RIGHT, SHOOT_RIGHT,
                Input.Keys.DOWN, SHOOT_DOWN,

                Input.Keys.SPACE, MELEE
                )
        );
    }

    /**
     * Set a key binding for this mapping.
     * @param keycode the key to bind
     * @param keyBinding the action to bind it to.
     */
    public void setKeyBinding(int keycode, KeyBinding keyBinding) {
        keyMap.put(keycode, keyBinding);
    }

    /**
     * Get the key mapping.
     * @return the key map.
     */
    public Map<Integer, KeyBinding> getKeyMap() {
        return new HashMap<>(keyMap);
    }
}
