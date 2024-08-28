package com.csse3200.game.components.player;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.Pickaxe;
import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PickaxeTest {
    @Mock
    private Entity entity;

    @InjectMocks
    private Pickaxe pickaxe;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetName() {
        // Check if getName returns "Bandage"
        assertEquals("pickaxe", pickaxe.getName());
    }
}
