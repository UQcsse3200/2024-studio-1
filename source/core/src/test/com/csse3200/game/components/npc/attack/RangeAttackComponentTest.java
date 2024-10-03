package com.csse3200.game.components.npc.attack;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.areas.MainGameArea;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.configs.AttackConfig;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class RangeAttackComponentTest {
    private GameTime gameTime;

    @Mock
    private GameAreaService gameAreaService;

    @Mock
    private MainGameArea mainGameArea;

    @Mock
    private Entity player;

    @BeforeEach
    void beforeEach() {

        // Mock GameTime, GameAreaService and MainGameArea
        gameTime = mock(GameTime.class);
        gameAreaService = mock(GameAreaService.class);
        mainGameArea = mock(MainGameArea.class);

        player = new Entity().addComponent(new CombatStatsComponent(100, 10));

        // Register the mocked services with ServiceLocator
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerGameAreaService(gameAreaService);

        // Mock the behavior of gameAreaService and mainGameArea
        when(gameAreaService.getGameArea()).thenReturn(mainGameArea);
        when(mainGameArea.getPlayer()).thenReturn(player);

        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerEntityService(new EntityService());
    }

    /**
     * Test if a projectile is created and sent out from the attacker
     */
    @Test
    void shouldPerformAttack() {
        Entity target = createTarget();
        Entity attacker = createAttacker(target);
        attacker.setPosition(0, 0);
        target.setPosition(0, 3); // Within shoot range
        when(gameTime.getDeltaTime()).thenReturn(1f); // Simulate time passing
        RangeAttackComponent component = attacker.getComponent(RangeAttackComponent.class);
        attacker.update();
        Entity projectile = component.getLatestProjectile();
        projectile.create();
        Vector2 before = projectile.getPosition();
        for (int i = 0; i < 3; i++) {
            projectile.earlyUpdate();
            projectile.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }
        Vector2 after = projectile.getPosition();
        assertNotEquals(before, after);
    }

    /**
     * Should not create new projectile during cooldown
     */
    @Test
    void shouldNotCreateProjectileDuringCooldown() {
        Entity target = createTarget();
        Entity attacker = createAttacker(target);
        attacker.setPosition(0, 0);
        target.setPosition(0, 4); // Within attack range

        attacker.update();
        RangeAttackComponent component = attacker.getComponent(RangeAttackComponent.class);
        attacker.update();
        Entity projectile = component.getLatestProjectile();

        when(gameTime.getDeltaTime()).thenReturn(0.2f); // Simulate time passing

        attacker.update();
        assertEquals(projectile, component.getLatestProjectile());
    }

    /**
     * Should create new projectile after cooldown
     */
    @Test
    void shouldCreateProjectileAfterCooldown() {
        Entity target = createTarget();
        Entity attacker = createAttacker(target);
        attacker.setPosition(0, 0);
        target.setPosition(0, 4); // Within attack range

        attacker.update();
        RangeAttackComponent component = attacker.getComponent(RangeAttackComponent.class);
        attacker.update();
        Entity projectile = component.getLatestProjectile();

        when(gameTime.getDeltaTime()).thenReturn(5f); // Simulate time passing

        attacker.update();
        assertNotEquals(projectile, component.getLatestProjectile());
    }

    @Test
    void shouldNotAttackOutOfRange() {
        Entity target = createTarget();
        Entity attacker = createAttacker(target);
        attacker.setPosition(0, 0);
        target.setPosition(10, 10); // Out of attack range

        when(gameTime.getDeltaTime()).thenReturn(1f); // Simulate time passing
        RangeAttackComponent component = attacker.getComponent(RangeAttackComponent.class);
        attacker.update();
        Entity projectile = component.getLatestProjectile();
        assertNull(projectile);
    }

    private Entity createAttacker(Entity target) {
        // Setup attacker configs
        AttackConfig.EffectConfig[] effectConfigs = {}; // No effects for simplicity
        AttackConfig.RangeAttack rangeAttackConfig = new AttackConfig.RangeAttack();
        rangeAttackConfig.range = 5f;
        rangeAttackConfig.rate = 1f;
        rangeAttackConfig.type = 0;
        rangeAttackConfig.effects = effectConfigs;

        // Create attacker
        Entity attacker = new Entity()
                .addComponent(new NameComponent("attacker"))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new CombatStatsComponent(10, 10))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new RangeAttackComponent(target, rangeAttackConfig));
        attacker.create();
        attacker.getComponent(RangeAttackComponent.class).setEnabled(true);
        return attacker;
    }

    private Entity createTarget() {
        Entity target = new Entity()
                .addComponent(new NameComponent("target"))
                .addComponent(new CombatStatsComponent(20, 0))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));
        target.create();
        return target;
    }
}
