package com.csse3200.game.components.npc.attack;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;

/**
 * AOEAttackComponent manages Area of Effect attacks for NPCs, including a preparation phase.
 */
public class AOEAttackComponent extends AttackComponent {
    private final float aoeRadius;
    private Vector2 origin;

    public AOEAttackComponent(Entity target, float attackRange, float attackRate,
                              NPCConfigs.NPCConfig.EffectConfig[] effectConfigs) {

        super(target, attackRange, attackRate, effectConfigs);
        this.target = target;
        this.aoeRadius = attackRange;
        this.origin = new Vector2();
        this.setEnabled(false);
    }

    /**
     * Initiates the AOE attack.
     */
    @Override
    public void performAttack() {
        executeAOEAttack();
    }

    /**
     * Executes the actual AOE attack: triggers the attack event and applies damage.
     */
    void executeAOEAttack() {
        // Trigger the actual AOE attack event
        entity.getEvents().trigger("aoe_attack");
        logger.info("{} executes AOE attack at {}", entity, origin);
        Circle aoeCircle = new Circle(entity.getCenterPosition(), aoeRadius);

        if (target.getComponent(CombatStatsComponent.class) != null
                && aoeCircle.contains(target.getPosition())) {
            logger.info("AOE attack hits {} for {} damage", target, combatStats.getBaseAttack());
            target.getComponent(CombatStatsComponent.class).hit(combatStats);
            applyEffects(target);
        }
    }

    /**
     * Sets the origin point of the AOE attack.
     *
     * @param newOrigin The new origin position for the AOE attack.
     */
    public void setOrigin(Vector2 newOrigin) {
        this.origin.set(newOrigin);
    }

    @Override
    public boolean canAttack(Entity attacker, Entity target) {
        return true;
    }
}