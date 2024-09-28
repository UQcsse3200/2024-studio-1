package com.csse3200.game.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.csse3200.game.services.RandomService;
import com.csse3200.game.utils.RandomNumberGenerator;

class RandomServiceTest {

    private RandomService randomService;
    private final String testSeed = "testSeed";

    @BeforeEach
    void setUp() {
        randomService = new RandomService(testSeed);
    }

    @Test
    void testConstructor() {
        assertNotNull(randomService, "RandomService should be created successfully");
    }

    @Test
    void testGetRandomNumberGenerator() {
        RandomNumberGenerator gen1 = randomService.getRandomNumberGenerator(String.class);
        RandomNumberGenerator gen2 = randomService.getRandomNumberGenerator(Integer.class);

        assertNotNull(gen1, "Generator for String class should not be null");
        assertNotNull(gen2, "Generator for Integer class should not be null");
        assertNotEquals(gen1, gen2, "Generators for different classes should be different");
    }

    @Test
    void testGetRandomNumberGeneratorSameClass() {
        RandomNumberGenerator gen1 = randomService.getRandomNumberGenerator(String.class);
        RandomNumberGenerator gen2 = randomService.getRandomNumberGenerator(String.class);

        assertNotNull(gen1, "First generator should not be null");
        assertNotNull(gen2, "Second generator should not be null");
        assertEquals(gen1, gen2, "Generators for the same class should be the same instance");
    }

    @Test
    void testGetRandomNumberGeneratorDifferentSeeds() {
        RandomService randomService1 = new RandomService("seed1");
        RandomService randomService2 = new RandomService("seed2");

        RandomNumberGenerator gen1 = randomService1.getRandomNumberGenerator(String.class);
        RandomNumberGenerator gen2 = randomService2.getRandomNumberGenerator(String.class);

        assertNotNull(gen1, "Generator from first service should not be null");
        assertNotNull(gen2, "Generator from second service should not be null");
        assertNotEquals(gen1.getRandomInt(2, 20), gen2.getRandomInt(2, 20), "Generators with different seeds should produce different results");
    }
}