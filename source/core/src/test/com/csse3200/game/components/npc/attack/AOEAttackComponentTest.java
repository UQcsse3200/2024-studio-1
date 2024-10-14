package com.csse3200.game.components.npc.attack;

import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.areas.GameController;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.AttackConfig;
import com.csse3200.game.entities.configs.EffectConfig;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class AOEAttackComponentTest {
    private GameTime gameTime;

    @Mock
    private GameAreaService gameAreaService;

    @Mock
    private GameController gameController;

    @Mock
    private Entity player;

    @BeforeEach
    void beforeEach() {
        // Mock GameTime, GameAreaService and MainGameArea
        gameTime = mock(GameTime.class);
        gameAreaService = mock(GameAreaService.class);
        gameController = mock(GameController.class);

        player = new Entity().addComponent(new CombatStatsComponent(100, 10));

        // Register the mocked services with ServiceLocator
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerGameAreaService(gameAreaService);

        // Mock the behavior of gameAreaService and mainGameArea
        when(gameAreaService.getGameController()).thenReturn(gameController);
        when(gameController.getPlayer()).thenReturn(player);
    }

    @Test
    void shouldPerformAOEAttack() {
        Entity target = createTarget();
        Entity attacker = createAttacker(target, 5f);
        attacker.setPosition(0, 0);
        target.setPosition(0, 0); // Within AOE range

        when(gameTime.getDeltaTime()).thenReturn(1f); // Simulate time passing

        attacker.update();
        // Wait for the preparation time to pass
        when(gameTime.getDeltaTime()).thenReturn(2f); // Simulate preparation time passing

        attacker.update();
        assertEquals(20, target.getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldNotAttackOutOfRange() {
        Entity target = createTarget();
        Entity attacker = createAttacker(target, 5f);
        attacker.setPosition(0, 0);
        target.setPosition(10, 0); // Out of AOE range

        when(gameTime.getDeltaTime()).thenReturn(1f); // Simulate time passing

        attacker.update();
        // Wait for the preparation time to pass
        when(gameTime.getDeltaTime()).thenReturn(2f); // Simulate preparation time passing

        attacker.update();
        assertEquals(20, target.getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldNotAttackDuringCooldown() {
        Entity target = createTarget();
        Entity attacker = createAttacker(target, 5f);
        attacker.setPosition(0, 0);
        target.setPosition(0, 0); // Within AOE range

        attacker.update();
        // Wait for the preparation time to pass
        when(gameTime.getDeltaTime()).thenReturn(2f); // Simulate preparation time passing

        attacker.update();
        assertEquals(20, target.getComponent(CombatStatsComponent.class).getHealth());

        when(gameTime.getDeltaTime()).thenReturn(0.5f); // Simulate half of cooldown time passing

        attacker.update();
        assertEquals(20, target.getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldPerformAOEAttackDirectly() {
        Entity target = createTarget();
        Entity attacker = createAttacker(target, 5f);
        attacker.setPosition(0, 0);
        target.setPosition(0, 0); // Within AOE range

        AOEAttackComponent aoeAttackComponent = (AOEAttackComponent) attacker.getComponent(AOEAttackComponent.class);
        aoeAttackComponent.executeAOEAttack();
        assertEquals(10, target.getComponent(CombatStatsComponent.class).getHealth());
    }

    private Entity createAttacker(Entity target, float aoeRadius) {
        // Setup attacker configs
        EffectConfig[] effectConfigs = {}; // No effects
        AttackConfig.AOEAttack aoeAttackConfig
                = new AttackConfig.AOEAttack();
        aoeAttackConfig.range = aoeRadius;
        aoeAttackConfig.rate = 1f;
        aoeAttackConfig.effects = effectConfigs;

        // Create attacker
        Entity attacker = new Entity()
                .addComponent(new CombatStatsComponent(10, 10))
                .addComponent(new AOEAttackComponent(target, aoeAttackConfig));
        attacker.create();
        return attacker;
    }

    private Entity createTarget() {
        Entity target = new Entity()
                .addComponent(new CombatStatsComponent(20, 0));
        target.create();
        return target;
    }
}