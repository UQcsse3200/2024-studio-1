package com.csse3200.game.components.npc.attack;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.npc.attack.attackeffects.Effect;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.entities.factories.EffectFactory;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class that handles attack logic for an NPC, applying damage and effects to a target within range.
 */
public abstract class AttackComponent extends Component implements AttackBehaviour{
    protected Entity target;
    protected float attackRange;
    protected float attackCooldown;
    protected float timeSinceLastAttack = 0;
    protected int damage;
    protected CombatStatsComponent combatStats;
    protected List<Effect> effects;
    protected NPCConfigs.NPCConfig.EffectConfig[] effectConfigs;
    protected static final Logger logger = LoggerFactory.getLogger(MeleeAttackComponent.class);


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
    }

    @Override
    public void update() {
        super.update();
        if (target == null || entity == null || !canAttack(entity, target)) {
            return;
        }

        timeSinceLastAttack += ServiceLocator.getTimeSource().getDeltaTime();
        if (timeSinceLastAttack >= attackCooldown) {
            performAttack();
            timeSinceLastAttack = 0; // Reset cooldown timer
        }
    }

    @Override
    public void applyEffects(Entity target) {
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
    public boolean canAttack(Entity attacker, Entity target) {
        float distanceToTarget = attacker.getPosition().dst(target.getPosition());
        return distanceToTarget <= attackRange;
    }
}
