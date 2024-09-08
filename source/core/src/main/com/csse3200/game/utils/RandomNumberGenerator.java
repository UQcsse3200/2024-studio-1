package com.csse3200.game.utils;
import java.util.Random;

public class RandomNumberGenerator {
    private final Random random;
    private final String seed;

    public RandomNumberGenerator(String seed) {
        // Convert string seed to a long value
        this.seed = seed;
        long seedLong = stringToSeed(seed);
        this.random = new Random(seedLong);
    }

    private long stringToSeed(String seed) {
        // Converts string to a long value
        return seed.hashCode();
    }

    public String getSeed() {
        return this.seed;
    }

    public int getRandomInt(int minVal, int maxVal) {
        // Check minVal greater maxVal
        if (minVal > maxVal) {
            throw new IllegalArgumentException("minVal greater than maxVal");
        }
        // Generate random integer within a range [minVal, maxVal)
        return random.nextInt(maxVal - minVal) + minVal;
    }

    public double getRandomDouble(double minVal, double maxVal) {
        // Check minVal greater maxVal
        if (minVal > maxVal) {
            throw new IllegalArgumentException("minVal greater than maxVal");
        }
        // Generate random double within a range [minVal, maxVal)
        return minVal + (maxVal - minVal) * random.nextDouble();
    }

    public String getRandomString(){
         return Integer.toString(this.getRandomInt(0, Integer.MAX_VALUE));
    }
}