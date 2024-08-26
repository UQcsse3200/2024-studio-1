package com.csse3200.game.components.player;

import com.badlogic.gdx.Input;

import java.util.Map;

import static com.csse3200.game.components.player.KeyMapping.KeyBinding.*;

public class KeyMapping {
    private final Map<Integer, KeyBinding> keyMap;

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

    public KeyMapping(Map<Integer, KeyBinding> keyMap) {
        this.keyMap = keyMap;
    }

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

    public void setKeyBinding(int keycode, KeyBinding keyBinding) {
        keyMap.put(keycode, keyBinding);
    }

    public Map<Integer, KeyBinding> getKeyMap() {
        return keyMap;
    }
}
