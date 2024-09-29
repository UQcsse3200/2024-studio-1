package com.csse3200.game.components.npc.attack;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

public class AOEAttackComponent extends AttackComponent {
    private final float aoeRadius;
    private Vector2 origin;

    public AOEAttackComponent(Entity target, float attackRange, float attackRate,
                              NPCConfigs.NPCConfig.EffectConfig[] effectConfigs, float aoeRadius) {

        super(target, attackRange, attackRate, effectConfigs);
        this.target = target;
        this.aoeRadius = aoeRadius;
        this.origin = new Vector2();
    }

    @Override
    public void performAttack() {
        if (canAttack(entity, target)) {
            entity.getEvents().trigger("aoe_attack");
            logger.info("{} performs AOE attack at {}", entity, origin);
            Circle aoeCircle = new Circle(entity.getCenterPosition(), aoeRadius);
            List<Entity> entitiesInRange = ServiceLocator.getGameAreaService().getGameArea().getListOfEntities();

            for (Entity targetEntity : entitiesInRange) {
                if(entity != targetEntity) {
                    //entity.getEvents().trigger("updateCircle");
                    if (targetEntity.getComponent(CombatStatsComponent.class) != null
                            && aoeCircle.contains(targetEntity.getPosition())) {
                        logger.info("AOE attack hits {} for {} damage", targetEntity, combatStats.getBaseAttack());
                        targetEntity.getComponent(CombatStatsComponent.class).hit(combatStats);
                        applyEffects(targetEntity);
                    }
                }
            }
        }

    }

    public void setOrigin(Vector2 newOrigin) {
        this.origin.set(newOrigin);
    }

    @Override
    public boolean canAttack(Entity attacker, Entity target) {
        return target.getPosition().dst(entity.getCenterPosition()) <= aoeRadius;
    }
}