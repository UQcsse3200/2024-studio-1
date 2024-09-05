package com.csse3200.game.components.npc;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component that handles attack logic for an NPC, applying damage and effects to a target within range.
 */
public class AttackComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(AttackComponent.class);
    private Entity target;
    private float attackRange;
    private float attackCooldown;
    private float timeSinceLastAttack;
    private int damage;
    private CombatStatsComponent combatStats;

    public AttackComponent(Entity target, float attackRange, float attackRate, int damage) {
        this.target = target;
        this.attackRange = attackRange;
        this.attackCooldown = 1/attackRate;
        this.damage = damage;
    }

    @Override
    public void create() {
        super.create();
        combatStats = entity.getComponent(CombatStatsComponent.class);
    }

    @Override
    public void update() {
        super.update();
        // Update attack logic
        if (target == null || entity == null) {
            return;
        }
        float distanceToTarget = entity.getPosition().dst(target.getPosition());

        if (distanceToTarget <= attackRange && timeSinceLastAttack >= attackCooldown) {
            performAttack();
            timeSinceLastAttack = 0; // Reset cooldown timer
        }

        timeSinceLastAttack += ServiceLocator.getTimeSource().getDeltaTime();
    }

    private void performAttack() {
        logger.info("{} attacks {} for {} damage", entity, target, damage);
        // Apply damage to the target
        target.getComponent(CombatStatsComponent.class).hit(combatStats);

        // Trigger attack animation
        entity.getEvents().trigger("attack");

        // Status effects
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public void setTarget(Entity target) {
        this.target = target;
    }
}
