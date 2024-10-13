package com.csse3200.game.areas.test;

import com.csse3200.game.areas.BaseRoom;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.factories.CollectibleFactory;

import java.util.List;

/**
 * A simple room with everything needed for manual testing.
 */
public class TestRoom extends BaseRoom {
    /**
     * Constructs a new BaseRoom with the given parameters.
     */
    public TestRoom() {
        super(new TerrainFactory(0),
                new CollectibleFactory(),
                List.of("", "", "", ""),
                "0,0,14,10,0,0",
                "test"
        );
    }

    @Override
    protected List<List<String>> getItemSpecifications() {
        return List.of();
    }

    @Override
    public void checkComplete() {
        // do nothing.
    }
}
