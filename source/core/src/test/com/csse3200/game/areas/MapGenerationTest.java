package com.csse3200.game.areas;

import com.csse3200.game.areas.Generation.MapGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;

class MapGeneratorTest {
    private MapGenerator mapGenerator;

    @BeforeEach
    void setUp() {
        // Initialize MapGenerator with a seed and a fixed size for testing
        mapGenerator = new MapGenerator(10, "testSeed");
        mapGenerator.createMap();
    }
}
