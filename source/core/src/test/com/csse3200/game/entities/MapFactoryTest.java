package com.csse3200.game.entities;

import com.csse3200.game.entities.factories.MapFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

//@ExtendWith(GameExtension.class)
public class MapFactoryTest {
    Map<String, List<String>> roomConns = new HashMap<>();
    MapFactory mapFactory = new MapFactory();
    @Before
    public void Setup()
    {
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
        roomConns.put("0_1", Conns2);
        roomConns.put("0_2", Conns);
    }

    @Test
    public void getRoomConnectionsTest() {
        List<String> connects = new ArrayList<>();
        connects.add("0_1");
        connects.add("1_0");
        connects.add("-1_0");
        connects.add("0_-1");

        assertEquals(connects, MapFactory.getRoomConnections("0_0"));
    }
}