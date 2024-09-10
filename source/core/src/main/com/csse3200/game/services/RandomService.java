package com.csse3200.game.services;

import com.csse3200.game.utils.RandomNumberGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * A service to access domain-specific random number generators.
 */
public class RandomService {
    private final RandomNumberGenerator masterGenerator;
    private final Map<Class, RandomNumberGenerator> generators = new HashMap<>();

    /**
     * Create a new master Randomizer
     * @param seed the master seed for all number generators.
     */
    public RandomService(String seed) {
        this.masterGenerator = new RandomNumberGenerator(seed);
    }

    /**
     * Get a domain specific random number generator based off of the master seed.
     * @param classType the class calling this function.
     * @return the new random number generator.
     */
    public RandomNumberGenerator getRandomNumberGenerator(Class classType) {
        // If we've never seen this class before add it dynamically to the generators.
        if (!generators.containsKey(classType)) {
            generators.put(classType, new RandomNumberGenerator(masterGenerator.getRandomString()));
        }

        return generators.get(classType);
    }
}
