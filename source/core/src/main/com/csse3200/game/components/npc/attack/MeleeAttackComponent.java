package com.csse3200.game.components.npc.attack;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;


/**
 * Component that handles melee attacks for an NPC, applying damage and effects to a target within range.
 */
public class MeleeAttackComponent extends AttackComponent {

    public MeleeAttackComponent(Entity target, float attackRange, float attackRate, int damage,
                                NPCConfigs.NPCConfig.EffectConfig[] effectConfigs) {
        super(target, attackRange, attackRate, damage, effectConfigs);
    }

    @Override
    public void performAttack() {
        logger.info("{} attacks {} for {} damage", entity, target, damage);
        // Apply damage to the target
        target.getComponent(CombatStatsComponent.class).hit(combatStats);

        // Trigger attack animation
        entity.getEvents().trigger("attack");

        // Attack effects
        applyEffects(target);
    }
}
