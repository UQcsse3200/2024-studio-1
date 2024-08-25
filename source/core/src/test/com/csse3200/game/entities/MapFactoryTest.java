package com.csse3200.game.entities;

import com.csse3200.game.entities.configs.MapConfigs;
import com.csse3200.game.entities.factories.MapFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.util.*;

import static org.junit.Assert.*;

@ExtendWith(GameExtension.class)
public class MapFactoryTest {

    MapConfigs config;
    HashMap<String, List<String>> roomCons;

    /**MapFactory mapFactory;
    @Before
    public void Setup()
    {
        config = new MapConfigs();
        roomCons = new HashMap<>();
        List<String> Conns = new ArrayList<>();
        Conns.add("0_1");
        Conns.add("1_0");
        Conns.add("-1_0");
        Conns.add("0_-1");
        List<String> Conns2 = new ArrayList<>();
        Conns2.add("2_0");
        Conns2.add("1_1");
        Conns2.add("0_0");
        Conns2.add("1_-1");
        roomCons.put("0_1", Conns2);
        config.room_connections = roomCons;
        mapFactory = new MapFactory(config);
    }

    @Test
    public void getRoomConnectionsTest() {
        List<String> connects = new ArrayList<>();
        connects.add("0_1");
        connects.add("1_0");
        connects.add("-1_0");
        connects.add("0_-1");
        
        assertEquals(connects, mapFactory.));
    }**/
}