package com.csse3200.game.components.player;

import com.badlogic.gdx.Input;
import com.csse3200.game.files.UserSettings;

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
        USE_5,
        USE_6,
        USE_7,
        USE_8,
        USE_9,

        /**
         * The player action to jump to boss room.
         */
        ENTER_BOSS,

        /**
         * The player action to jump to shop room.
         */
        ENTER_SHOP,

        /**
         * The player action to pick up an item
         */
        PICK_UP,

        /**
         * The player action to use a reroll item
         */
        RE_ROLL,

        /**
         * The player action to attempt to purchase a buyable item
         */
        PURCHASE_ITEM,
        /**
         * Necromancer Target Switching 
         */
        NECROMANCER_BINDING
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

        if (UserSettings.get().walkWithWASD) {
            keyMap.put(Input.Keys.W, KeyBinding.WALK_UP);
            keyMap.put(Input.Keys.A, KeyBinding.WALK_LEFT);
            keyMap.put(Input.Keys.S, KeyBinding.WALK_DOWN);
            keyMap.put(Input.Keys.D, KeyBinding.WALK_RIGHT);
        } else {
            keyMap.put(Input.Keys.UP, KeyBinding.WALK_UP);
            keyMap.put(Input.Keys.LEFT, KeyBinding.WALK_LEFT);
            keyMap.put(Input.Keys.DOWN, KeyBinding.WALK_DOWN);
            keyMap.put(Input.Keys.RIGHT, KeyBinding.WALK_RIGHT);
        }
        if (UserSettings.get().shootWithWASD) {
            keyMap.put(Input.Keys.W, KeyBinding.SHOOT_UP);
            keyMap.put(Input.Keys.A, KeyBinding.SHOOT_LEFT);
            keyMap.put(Input.Keys.S, KeyBinding.SHOOT_DOWN);
            keyMap.put(Input.Keys.D, KeyBinding.SHOOT_RIGHT);
        } else {
            keyMap.put(Input.Keys.UP, KeyBinding.SHOOT_UP);
            keyMap.put(Input.Keys.LEFT, KeyBinding.SHOOT_LEFT);
            keyMap.put(Input.Keys.DOWN, KeyBinding.SHOOT_DOWN);
            keyMap.put(Input.Keys.RIGHT, KeyBinding.SHOOT_RIGHT);
        }
            keyMap.put(Input.Keys.SPACE, MELEE);
            keyMap.put(Input.Keys.NUM_1, USE_1);
            keyMap.put(Input.Keys.NUM_2, USE_2);
            keyMap.put(Input.Keys.NUM_3, USE_3);
            keyMap.put(Input.Keys.NUM_4, USE_4);
            keyMap.put(Input.Keys.NUM_5, USE_5);
            keyMap.put(Input.Keys.NUM_6, USE_6);
            keyMap.put(Input.Keys.NUM_7, USE_7);
            keyMap.put(Input.Keys.NUM_8, USE_8);
            keyMap.put(Input.Keys.NUM_9, USE_9);
            keyMap.put(Input.Keys.B, ENTER_BOSS);
            keyMap.put(Input.Keys.N, ENTER_SHOP);
            keyMap.put(Input.Keys.E, PICK_UP);
            keyMap.put(Input.Keys.R, RE_ROLL);
            keyMap.put(Input.Keys.P, PURCHASE_ITEM);
            keyMap.put(Input.Keys.Q, NECROMANCER_BINDING);
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
