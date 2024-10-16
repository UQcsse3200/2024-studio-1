package com.csse3200.game.components.player.inventory;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.player.inventory.buffs.DivinePotion;
import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DivinePotionTest {

    @Mock
    Entity entity;

    @Mock
    private CombatStatsComponent combatStatsComponent;

    @Mock
    private PlayerActions playerActions;

    @InjectMocks
    private DivinePotion divinePotion;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        entity = mock(Entity.class);
        playerActions = mock(PlayerActions.class);

        // Mock the entity to return the necessary components
        when(entity.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);
        when(entity.getComponent(PlayerActions.class)).thenReturn(playerActions);

        // Mock getting the PlayerActions component
        when(entity.getComponent(PlayerActions.class)).thenReturn(playerActions);
    }

    @Test
    public void testGetName() {
        // Check if getName returns "Divine Potion"
        assertEquals("Divine Potion", divinePotion.getName());
    }

    @Test
    public void testGetBuffSpecification() {
        // Check if getBuffSpecification returns "Divine potion"
        assertEquals("divinepotion", divinePotion.getBuffSpecification());
    }
}




