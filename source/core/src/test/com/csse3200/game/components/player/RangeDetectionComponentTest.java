package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.ItemPickupComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class RangeDetectionComponentTest {
    @Mock
    private Entity player;

    @Mock
    private Entity enemy;

    @Mock
    private CombatStatsComponent combatStatsComponent;

    @Mock
    private WeaponComponent weaponComponent;

    @Mock
    private HitboxComponent hitboxComponent;

    // mock RangeDetectionComponent with PhysicsLayer.NPC
    @Mock
    private RangeDetectionComponent rangeDetectionComponent;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // mock RangeDetectionComponent with PhysicsLayer.NPC
        rangeDetectionComponent = new RangeDetectionComponent(PhysicsLayer.NPC);
        player = mock(Entity.class);
        enemy = mock(Entity.class);


        // Mock the player entity to return the WeaponComponent
        when(player.getComponent(WeaponComponent.class)).thenReturn(weaponComponent);
        when(player.getComponent(HitboxComponent.class)).thenReturn(hitboxComponent);
        when(player.getComponent(RangeDetectionComponent.class)).thenReturn(rangeDetectionComponent);
        // Mock the player to return the CombatStatsComponent
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);
    }

    @Test
    public void testCreate() {
        // create new range detection component
        RangeDetectionComponent rangeDetectionComponent = new RangeDetectionComponent(PhysicsLayer.NPC);
        // check if range detection component is not null
        assert rangeDetectionComponent != null;
    }

    @Test
    public void testUpdateWeaponEntity() {
        setUp();

        // NULL before updateWeaponEntity
        assert player.getComponent(RangeDetectionComponent.class).getHitboxComponent() == null;
        // create new range detection component
        player.getComponent(RangeDetectionComponent.class).updateWeaponEntity(player);
        // check if hitbox component is not null
        assert player.getComponent(RangeDetectionComponent.class).getHitboxComponent() != null;
        assert player.getComponent(RangeDetectionComponent.class).getHitboxComponent() instanceof HitboxComponent;
    }

    @Test
    public void testCreateEntities() {
        //setUp();
        // create new range detection component
        RangeDetectionComponent rangeDetectionComponent = new RangeDetectionComponent(PhysicsLayer.NPC);
        // check if entities is not null
        assert rangeDetectionComponent.getEntities() != null;
        assert rangeDetectionComponent.getEntities() instanceof java.util.ArrayList;
        assert rangeDetectionComponent.getEntities().size() == 0;

    }

    @Test
    public void testGetEntities() {
        setUp();
        // create new range detection component
        RangeDetectionComponent rangeDetectionComponent = new RangeDetectionComponent(PhysicsLayer.NPC);
        // check if entities is not null
        assert rangeDetectionComponent.getEntities() != null;
        assert rangeDetectionComponent.getEntities() instanceof java.util.ArrayList;
        assert rangeDetectionComponent.getEntities().size() == 0;
    }


}
