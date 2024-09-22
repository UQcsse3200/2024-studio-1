package com.csse3200.game.components.player;

import com.badlogic.gdx.Input;
import com.csse3200.game.components.player.inventory.MedKit;

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
        /**
         * The player action to walk up.
         */
        WALK_UP,
        /**
         * The player action to walk down.
         */
        WALK_DOWN,
        /**
         * The player action to walk left.
         */
        WALK_LEFT,
        /**
         * The player action to walk right.
         */
        WALK_RIGHT,
        /**
         * The player action to shoot left.
         */
        SHOOT_LEFT,
        /**
         * The player action to shoot up.
         */
        SHOOT_UP,
        /**
         * The player action to shoot right.
         */
        SHOOT_RIGHT,
        /**
         * The player action to shoot down.
         */
        SHOOT_DOWN,
        /**
         * The player action to make a melee attack.
         */
        MELEE,
        USE_1,
        USE_2,
        USE_3,
        USE_4,
        /**
         * The player action to jump to boss room.
         */
        ENTER_BOSS,
        PICK_UP,
        RE_ROLL
    }

    /**
     * Create a key map from a specification
     *
     * @param keyMap A mapping of keys to actions
     */
    public KeyMapping(Map<Integer, KeyBinding> keyMap) {
        this.keyMap = keyMap;
    }

    /**
     * Create a default key map.
     */
    public KeyMapping() {

            Map<Integer, KeyBinding> keyMap = new HashMap<>();
            keyMap.put(Input.Keys.W, WALK_UP);
            keyMap.put(Input.Keys.A, WALK_LEFT);
            keyMap.put(Input.Keys.D, WALK_RIGHT);
            keyMap.put(Input.Keys.S, WALK_DOWN);
            keyMap.put(Input.Keys.LEFT, SHOOT_LEFT);
            keyMap.put(Input.Keys.UP, SHOOT_UP);
            keyMap.put(Input.Keys.RIGHT, SHOOT_RIGHT);
            keyMap.put(Input.Keys.DOWN, SHOOT_DOWN);
            keyMap.put(Input.Keys.SPACE, MELEE);
            keyMap.put(Input.Keys.NUM_1, USE_1);
            keyMap.put(Input.Keys.NUM_2, USE_2);
            keyMap.put(Input.Keys.NUM_3, USE_3);
            keyMap.put(Input.Keys.B, ENTER_BOSS);
            keyMap.put(Input.Keys.E, PICK_UP);
            keyMap.put(Input.Keys.R, RE_ROLL);
            this.keyMap = keyMap;

    }

    /**
     * Set a key binding for this mapping.
     *
     * @param keycode    the key to bind
     * @param keyBinding the action to bind it to.
     */
    public void setKeyBinding(int keycode, KeyBinding keyBinding) {
        keyMap.put(keycode, keyBinding);
    }

    /**
     * Get the key mapping.
     *
     * @return the key map.
     */
    public Map<Integer, KeyBinding> getKeyMap() {
        return new HashMap<>(keyMap);
    }
}
