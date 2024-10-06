package com.csse3200.game.components.npc.attack;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;


/**
 * Component that handles melee attacks for an NPC, applying damage and effects to a target within range.
 */
public class MeleeAttackComponent extends AttackComponent {

    public MeleeAttackComponent(Entity target, float attackRange, float attackRate,
                                NPCConfigs.NPCConfig.EffectConfig[] effectConfigs) {
        super(target, attackRange, attackRate, effectConfigs);
        this.setEnabled(false);
    }

    @Override
    public void performAttack() {
        if(target != null){
            Component combatComponent = target.getComponent(CombatStatsComponent.class);
            if(combatComponent != null){
                // Apply damage to the target
                target.getComponent(CombatStatsComponent.class).hit(combatStats);
                logger.info("{} attacks {} for {} damage", entity, target, combatStats.getBaseAttack());

                // Trigger attack animation
                entity.getEvents().trigger("attack");

                // Attack effects
                applyEffects(target);
            }
        }
    }

    /**
     * Updates the attack target
     * @param newTarget the new target
     */
    public void updateTarget(Entity newTarget) {
        this.target = newTarget;
    }

}
