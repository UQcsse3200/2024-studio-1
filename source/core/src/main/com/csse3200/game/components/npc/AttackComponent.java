package com.csse3200.game.components.npc;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.npc.attackeffects.Effect;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.entities.factories.EffectFactory;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Component that handles attack logic for an NPC, applying damage and effects to a target within range.
 */
public class AttackComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(AttackComponent.class);
    private Entity target;
    private final float attackRange;
    private final float attackCooldown;
    private float timeSinceLastAttack;
    private final int damage;
    private CombatStatsComponent combatStats;
    private List<Effect> effects;
    private final NPCConfigs.NPCConfig.EffectConfig[] effectConfigs;

    public AttackComponent(Entity target, float attackRange, float attackRate, int damage,
                           NPCConfigs.NPCConfig.EffectConfig[] effectConfigs) {
        this.target = target;
        this.attackRange = attackRange;
        this.attackCooldown = 1/attackRate;
        this.damage = damage;
        this.effectConfigs = effectConfigs;
    }

    @Override
    public void create() {
        super.create();
        combatStats = entity.getComponent(CombatStatsComponent.class);
        effects = createEffects(effectConfigs);
        logger.info("AttackComponent created for entity {}", entity);
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

        // Attack effects
        for (Effect effect : effects) {
            effect.apply(target);
        }
    }

    private List<Effect> createEffects(NPCConfigs.NPCConfig.EffectConfig[] effectConfigs) {
        List<Effect> effects = new ArrayList<>();
        if (effectConfigs != null) {
            for (NPCConfigs.NPCConfig.EffectConfig config : effectConfigs) {
                logger.info("Creating effect of type {} for entity {}", config.type, entity);
                effects.add(EffectFactory.createEffect(config, entity));
            }
        }
        return effects;
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public void setTarget(Entity target) {
        this.target = target;
    }
}
