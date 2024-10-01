package com.csse3200.game.options;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.csse3200.game.options.GameOptions.Difficulty.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameOptionsTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void difficultyGetsHarder() {
        assertTrue(EASY.getMultiplier() > MEDIUM.getMultiplier());
        assertTrue(MEDIUM.getMultiplier() > HARD.getMultiplier());
    }

    @AfterEach
    void tearDown() {
    }
}