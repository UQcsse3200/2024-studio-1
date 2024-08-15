package com.example;


import com.csse3200.game.areas.RandomNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RandomNumberGeneratorTest {
    private RandomNumberGenerator rng;

    @BeforeEach
    void setUp() {
        rng = new RandomNumberGenerator("testSeed");
    }

    @Test
    void testGetRandomIntWithinRange() {
        int min = 10;
        int max = 20;
        int randomValue = rng.getRandomInt(min, max);

        assertTrue(randomValue >= min && randomValue < max,
            "Random integer should be within the specified range");
    }

    @Test
    void testGetRandomIntThrowsExceptionForInvalidRange() {
        assertThrows(IllegalArgumentException.class, () -> rng.getRandomInt(20, 10),
            "Should throw IllegalArgumentException when minVal is greater than maxVal");
    }

    @Test
    void testGetRandomDoubleWithinRange() {
        double min = 10.0;
        double max = 20.0;
        double randomValue = rng.getRandomDouble(min, max);

        assertTrue(randomValue >= min && randomValue < max,
            "Random double should be within the specified range");
    }

    @Test
    void testGetRandomDoubleThrowsExceptionForInvalidRange() {
        assertThrows(IllegalArgumentException.class, () -> rng.getRandomDouble(20.0, 10.0),
            "Should throw IllegalArgumentException when minVal is greater than maxVal");
    }

    @Test
    void testSetSeedChangesRandomSequence() {
        int min = 10;
        int max = 20;
        
        int firstValue = rng.getRandomInt(min, max);

        
        rng.setSeed("differentSeed");
        int secondValue = rng.getRandomInt(min, max);

        assertNotEquals(firstValue, secondValue,
            "Setting a different seed should change the random sequence");
    }

    @Test
    void testSameSeedProducesSameSequence() {
        int min = 10;
        int max = 20;

        
        int firstValue = rng.getRandomInt(min, max);

        
        rng.setSeed("testSeed");
        int secondValue = rng.getRandomInt(min, max);

        assertEquals(firstValue, secondValue,
            "Using the same seed should produce the same random sequence");
    }
}
