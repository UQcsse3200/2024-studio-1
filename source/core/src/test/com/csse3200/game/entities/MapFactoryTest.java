package com.csse3200.game.entities;


import com.csse3200.game.entities.configs.MapConfigs;
import com.csse3200.game.entities.factories.MapFactory;
import com.csse3200.game.files.FileLoader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

class MapFactoryTest {

    private static MapConfigs mapData;
    //private static List<String> exampleConnections = new ArrayList<>();
    @Test
    void loadMapTestValid() {
        MapConfigs Data = MapFactory.loadMap("json_test/test.json");
        assertNotNull(Data);
        assertEquals(Data, mapData);
        //assertEquals(exampleConnections, Data.room_connections.get("0_0"));
    }


}
