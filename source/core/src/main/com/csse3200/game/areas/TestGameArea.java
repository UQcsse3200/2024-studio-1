package com.csse3200.game.areas;

import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;

/**
 * This is a testing ground for difficult to test
 */
public class TestGameArea extends MainGameArea {
    public TestGameArea(CameraComponent camera) {
        super(new TerrainFactory(camera), new NPCFactory(), new CollectibleFactory());
    }
}
