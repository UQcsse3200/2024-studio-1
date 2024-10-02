package com.csse3200.game.components.npc.attack;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;

/**
 * AOEAttackComponent manages Area of Effect attacks for NPCs.
 * This component extends the basic AttackComponent to provide AOE functionality.
 */
public class AOEAttackComponent extends AttackComponent {
    private final float aoeRadius;
    private Vector2 origin;

    /**
     * Constructs a new AOEAttackComponent.
     *
     * @param target The target entity for the attack.
     * @param attackRange The radius of the area of effect for the attack.
     * @param attackRate The frequency of attacks (attacks per second).
     * @param effectConfigs An array of effect configurations to be applied with the attack.
     */
    public AOEAttackComponent(Entity target, float attackRange, float attackRate,
                              NPCConfigs.NPCConfig.EffectConfig[] effectConfigs) {
        super(target, attackRange, attackRate, effectConfigs);
        this.target = target;
        this.aoeRadius = attackRange; // Using attackRange as the AOE radius
        this.origin = new Vector2();
        this.setEnabled(false); // Component starts disabled
    }

    /**
     * Initiates the AOE attack.
     * This method is called when the attack conditions are met.
     */
    @Override
    public void performAttack() {
        executeAOEAttack();
    }

    /**
     * Executes the actual AOE attack.
     * This method applies damage and effects to the target if within the AOE.
     */
    void executeAOEAttack() {
        // Trigger the AOE attack event
        entity.getEvents().trigger("aoe_attack");
        logger.info("{} executes AOE attack at {}", entity, origin);

        // Create an AOE circle centered on the entity
        Circle aoeCircle = new Circle(entity.getCenterPosition(), aoeRadius);

        // Check if the target is within the AOE and has combat stats
        if (target.getComponent(CombatStatsComponent.class) != null
                && aoeCircle.contains(target.getPosition())) {
            // Apply damage and effects to the target
            logger.info("AOE attack hits {} for {} damage", target, combatStats.getBaseAttack());
            target.getComponent(CombatStatsComponent.class).hit(combatStats);
            applyEffects(target);
        }
    }

    /**
     * Sets the origin point of the AOE attack.
     * This can be used to adjust the center of the AOE if needed.
     *
     * @param newOrigin The new origin position for the AOE attack.
     */
    public void setOrigin(Vector2 newOrigin) {
        this.origin.set(newOrigin);
    }

    /**
     * Determines if the attacker can perform an attack on the target.
     * In this implementation, it always returns true, allowing attacks at any time.
     *
     * @param attacker The entity performing the attack.
     * @param target The entity being targeted.
     * @return Always returns true, indicating the attack can always be performed.
     */
    @Override
    public boolean canAttack(Entity attacker, Entity target) {
        return true;
    }
}