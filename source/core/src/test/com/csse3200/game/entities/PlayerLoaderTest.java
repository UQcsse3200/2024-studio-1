
package com.csse3200.game.entities;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PlayerLoaderTest {

    private PlayerSelection playerSelection;
    private ResourceService mockResourceService;
    private EntityService mockEntityService;
    private PhysicsService mockPhysicsService;
    private PhysicsEngine mockPhysicsEngine;

    @BeforeEach
    public void setUp() {
        // Mock the necessary services
        mockResourceService = mock(ResourceService.class);
        mockEntityService = mock(EntityService.class);
        mockPhysicsService = mock(PhysicsService.class);
        mockPhysicsEngine = mock(PhysicsEngine.class);

        // Register the mocked services in ServiceLocator
        ServiceLocator.registerResourceService(mockResourceService);
        ServiceLocator.registerEntityService(mockEntityService);
        ServiceLocator.registerPhysicsService(mockPhysicsService);

        // Mock the loadForMillis() and getProgress() methods to simulate successful loading
        when(mockResourceService.loadForMillis(anyInt())).thenReturn(true); // Simulates that resources are loaded immediately
        when(mockResourceService.getProgress()).thenReturn((int) 100f); // Simulates that loading is complete

        // Ensure that the mockPhysicsService returns the mockPhysicsEngine
        when(mockPhysicsService.getPhysics()).thenReturn(mockPhysicsEngine);

        // Mock Gdx.files to simulate file handling
        Gdx.files = mock(Files.class);
        FileHandle mockFileHandle = mock(FileHandle.class);
        when(Gdx.files.internal(anyString())).thenReturn(mockFileHandle);

        // Mock FileLoader.readClass to simulate reading PlayerConfig from files
        PlayerConfig mockPlayerConfig1 = new PlayerConfig();
        mockPlayerConfig1.name = "default";
        mockPlayerConfig1.melee = "melee:Pickaxe";


        PlayerConfig mockPlayerConfig2 = new PlayerConfig();
        mockPlayerConfig2.name = "default";
        mockPlayerConfig2.health = 70;
        mockPlayerConfig2.favouriteColour = "yellow";
        mockPlayerConfig2.textureAtlasFilename = "images/player/player.atlas";
        mockPlayerConfig2.textureFilename = "images/player/player.png";

        mockStatic(FileLoader.class);
        when(FileLoader.readClass(eq(PlayerConfig.class), anyString()))
                .thenReturn(mockPlayerConfig1)  // Return mock config for player1
                .thenReturn(mockPlayerConfig2);  // Return mock config for player2

        // Initialize the PlayerSelection object
        playerSelection = new PlayerSelection();
    }

    @Test
    public void testAddItems() {
        // Call the createTwoPlayers method
        a
    }
}
