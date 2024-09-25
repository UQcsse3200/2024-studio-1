package com.csse3200.game.entities.configs;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.options.GameOptions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(GameExtension.class)
class PlayerConfigTest {

    private static final String TEST_PLAYER_PATH = "test/files/test_player.json";
    private static final float INACCURACY = 0.01f;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    private static boolean floatCompare(float val1, float val2) {
        return Math.abs(val1 - val2) < INACCURACY;
    }

    @Test
    void difficultyGetsAdjusted() {
        PlayerConfig testPlayer = FileLoader.readClass(PlayerConfig.class, TEST_PLAYER_PATH);
        int initialHealth = testPlayer.health;
        float initialSpeed = testPlayer.speed.len();
        GameOptions.Difficulty difficulty = GameOptions.Difficulty.MEDIUM;
        testPlayer.adjustForDifficulty(difficulty);
        assertTrue(
                floatCompare(initialHealth * difficulty.getMultiplier(), testPlayer.health),
                "player has wrong health after difficulty adjustment");
        assertTrue(
                floatCompare(initialSpeed * difficulty.getMultiplier(), testPlayer.speed.len()),
                "player has wrong speed after difficulty adjustment");
    }
}