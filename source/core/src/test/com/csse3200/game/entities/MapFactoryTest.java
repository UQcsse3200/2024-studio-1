/**package com.csse3200.game.entities;

import com.csse3200.game.entities.configs.MapConfigs;
import com.csse3200.game.entities.factories.MapFactory;
import com.csse3200.game.extensions.GameExtension;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.*;

import static org.junit.Assert.assertEquals;

@ExtendWith(GameExtension.class)
public class MapFactoryTest {
   // Map<String, List<String>> roomConns = new HashMap<>();
    MapFactory mapFactory = new MapFactory();
   
    @Test
    public void getRoomConnectionsTest() {

        System.out.println(mapFactory.getRoomConnections("0_0"));
    }
}**/
