package com.csse3200.game.components.npc.attack;

import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.areas.GameController;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
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
class MeleeAttackComponentTest {
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
    void shouldPerformAttack() {
        Entity target = createTarget();
        Entity attacker = createAttacker(target);
        attacker.setPosition(0, 0);
        target.setPosition(0, 1); // Within attack range

        when(gameTime.getDeltaTime()).thenReturn(1f); // Simulate time passing

        attacker.update();
        assertEquals(10, target.getComponent(CombatStatsComponent.class).getHealth(),
                "Target should have taken damage.");
    }

    @Test
    void shouldNotAttackDuringCooldown() {
        Entity target = createTarget();
        Entity attacker = createAttacker(target);
        attacker.setPosition(0, 0);
        target.setPosition(0, 1); // Within attack range

        attacker.update();
        assertEquals(10, target.getComponent(CombatStatsComponent.class).getHealth(),
                "Target should not have taken damage.");

        when(gameTime.getDeltaTime()).thenReturn(0.5f); // Simulate half of cooldown time passing

        attacker.update();
        assertEquals(10, target.getComponent(CombatStatsComponent.class).getHealth(),
                "Target should not take damage during attack cooldown.");
    }

    @Test
    void shouldNotAttackOutOfRange() {
        Entity target = createTarget();
        Entity attacker = createAttacker(target);
        attacker.setPosition(0, 0);
        target.setPosition(0, 5); // Out of attack range

        when(gameTime.getDeltaTime()).thenReturn(1f); // Simulate time passing

        attacker.update();
        assertEquals(20, target.getComponent(CombatStatsComponent.class).getHealth(),
                "Target should not take damage when out of attack range.");
    }

    private Entity createAttacker(Entity target) {
        NPCConfigs.NPCConfig.EffectConfig[] effectConfigs = {}; // No effects
        Entity attacker = new Entity()
                .addComponent(new CombatStatsComponent(10, 10))
                .addComponent(new MeleeAttackComponent(target, 2f, 1f, effectConfigs));
        attacker.create();
        attacker.getComponent(MeleeAttackComponent.class).setEnabled(true);
        return attacker;
    }

    private Entity createTarget() {
        Entity target = new Entity()
                .addComponent(new CombatStatsComponent(20, 0));
        target.create();
        return target;
    }
}
