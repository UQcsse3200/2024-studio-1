package com.csse3200.game.entities.configs;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.options.GameOptions.Difficulty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.EnumMap;

import static com.csse3200.game.options.GameOptions.Difficulty.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(GameExtension.class)
class PlayerConfigTest {

    private static final String TEST_PLAYER_PATH = "test/files/test_player.json";
    private static final float INACCURACY = 0.01f;

    private PlayerConfig testPlayer;

    @BeforeEach
    void setUp() {
        testPlayer = FileLoader.readClass(PlayerConfig.class, TEST_PLAYER_PATH);
    }

    @AfterEach
    void tearDown() {
    }

    private static boolean floatCompare(float val1, float val2) {
        return Math.abs(val1 - val2) < INACCURACY;
    }

    private static boolean leq(float val1, float val2) {
        return val1 < val2 || floatCompare(val1, val2);
    }

    @Test
    void difficultyGetsAdjusted() {
        int initialHealth = testPlayer.health;
        float initialSpeed = testPlayer.speed.len();

        for (Difficulty diff : Difficulty.values()) {
            PlayerConfig newPlayer = testPlayer.copy();
            newPlayer.adjustForDifficulty(diff);
            assertTrue(
                    floatCompare(initialHealth * diff.getMultiplier(), newPlayer.health),
                    "player has wrong health after difficulty adjustment");
            assertTrue(
                    floatCompare(initialSpeed * diff.getMultiplier(), newPlayer.speed.len()),
                    "player has wrong speed after difficulty adjustment");
        }
    }

    private static void assertStatsDecreased(PlayerConfig before, PlayerConfig after) {
        assertTrue(after.health < before.health);
        assertTrue(after.speed.len() < before.speed.len());
    }

    @Test
    void playerStatsGoDown() {
        EnumMap<Difficulty, PlayerConfig> players = new EnumMap<>(Difficulty.class);
        for (Difficulty diff : Difficulty.values()) {
            players.put(diff, testPlayer.copy().adjustForDifficulty(diff));
        }
        assertStatsDecreased(players.get(EASY), players.get(MEDIUM));
        assertStatsDecreased(players.get(MEDIUM), players.get(HARD));
    }
}