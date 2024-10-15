package com.csse3200.game.areas.test;

import com.csse3200.game.areas.*;
import com.csse3200.game.areas.generation.TestMapGenerator;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.RandomNumberGenerator;

import java.util.List;

/**
 * A Test Level, a single level of the test environment.
 */
public class TestLevel implements Level {
    private final Room armoury;
    private final RandomNumberGenerator rng;

    /**
     * Construct the test level.
     */
    public TestLevel() {
        this.armoury = new ArmouryRoom(List.of("", "0_1", "", ""));
        this.rng = ServiceLocator.getRandomService().getRandomNumberGenerator(TestLevel.class);
    }

    @Override
    public Room getRoom(String roomKey) {
        return switch (roomKey) {
            case "0_0" -> armoury;
            case "0_1" -> new MainRoom(
                    new NPCFactory(),
                    new CollectibleFactory(),
                    new TerrainFactory(0),
                    List.of("", "", "0_0", ""),
                    "0,0,14,10," + rng.getRandomInt(0, 5) + "," + rng.getRandomInt(0, 5),
                    "fight!");
            case "1_0" -> new BossRoom(new NPCFactory(),
                    new CollectibleFactory(),
                    new TerrainFactory(0),
                    List.of("", "", "", "0_0"),
                    "0,0,14,10," + rng.getRandomInt(0, 3) + "," + rng.getRandomInt(0, 3),
                    "boss!");
            case null, default -> throw new IllegalArgumentException(roomKey + " isn't a room");
        };
    }

    @Override
    public String getStartingRoomKey() {
        return "0_0";
    }

    @Override
    public int getLevelNumber() {
        return 0;
    }

    @Override
    public LevelMap getMap() {
        return new LevelMap(new TestMapGenerator());
    }
}
