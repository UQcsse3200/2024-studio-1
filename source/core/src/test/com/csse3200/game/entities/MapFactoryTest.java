/**package com.csse3200.game.entities;

import com.csse3200.game.entities.factories.MapFactory;
import com.csse3200.game.extensions.GameExtension;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import static org.junit.Assert.*;

@ExtendWith(GameExtension.class)
public class MapFactoryTest {

    @Test
    public void getRoomConnectionsTest() {
      //  assertEquals(0,0);
       List<int[]> connections = MapFactory.getRoomConnections("0_0");
        assertArrayEquals(new int[]{1,0}, connections.get(1));
        assertArrayEquals(new int[]{0,1}, connections.get(2));
        assertArrayEquals(new int[]{-1,0}, connections.get(1));
        assertArrayEquals(new int[]{0,-1}, connections.get(3));
    }
}**/