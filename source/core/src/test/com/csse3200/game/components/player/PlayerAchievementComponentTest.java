package com.csse3200.game.components.player;


public class PlayerAchievementComponentTest {

}

/*
import com.csse3200.game.files.FileLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

public class PlayerAchievementComponentTest {


    private PlayerAchievementComponent achievementComponent;
    @Mock
    private Stage mockStage;
    @Mock
    private Skin mockSkin;

    @BeforeEach
    public void setUp() {
        // Mocking necessary components
        mockStage = mock(Stage.class);
        mockSkin = mock(Skin.class);

        // Initialize the component
        achievementComponent = new PlayerAchievementComponent();
        achievementComponent.stage = mockStage;
        achievementComponent.skin = mockSkin;

        // Set up the achievements map
        achievementComponent.achievements = new HashMap<>();
    }

    @Test
    public void testHandleDefeatedEnemyAchievement_FirstEnemy() {
        achievementComponent.handleDefeatedEnemyAchievement(1);

        assertTrue(achievementComponent.getAchievements().containsKey("First defeated enemy"));
        assertEquals("images/items/energy_drink_blue.png", achievementComponent.getAchievements().get("First defeated enemy"));
    }

    @Test
    public void testHandleDefeatedEnemyAchievement_TenEnemies() {
        achievementComponent.handleDefeatedEnemyAchievement(10);

        assertTrue(achievementComponent.getAchievements().containsKey("10 enemies defeated"));
        assertEquals("images/items/energy_drink_blue.png", achievementComponent.getAchievements().get("10 enemies defeated"));
    }

}

 */
