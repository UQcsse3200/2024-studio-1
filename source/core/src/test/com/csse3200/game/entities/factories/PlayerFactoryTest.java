package com.csse3200.game.entities.factories;

import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class PlayerFactoryTest {

    private static final String DEFAULT_FILE_PATH = "configs/player.json";
    private PlayerConfig mockPlayerConfig;

    @Before
    public void setUp() {
        // Set up a mock PlayerConfig object to simulate the JSON file
        mockPlayerConfig = new PlayerConfig();
        mockPlayerConfig.health = 100;
        mockPlayerConfig.baseAttack = 20;
    }

    @Test
    public void testPlayerHealthFromJson() {
        try (MockedStatic<FileLoader> mockedFileLoader = mockStatic(FileLoader.class)) {
            // Mock the FileLoader.readClass method to return the mockPlayerConfig
            mockedFileLoader.when(() -> FileLoader.readClass(PlayerConfig.class, DEFAULT_FILE_PATH))
                    .thenReturn(mockPlayerConfig);

            // Read the config using the mocked method
            PlayerConfig config = FileLoader.readClass(PlayerConfig.class, DEFAULT_FILE_PATH);

            // Verify that the health value from the config matches 100
            assertEquals(100, config.health);
        }
    }
}