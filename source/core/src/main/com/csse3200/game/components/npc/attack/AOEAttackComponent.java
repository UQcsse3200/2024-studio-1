package com.csse3200.game.components.npc.attack;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.attack.attackeffects.Effect;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.entities.factories.EffectFactory;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Component for handling Area of Effect (AOE) attacks.
 */
public class AOEAttackComponent extends Component implements AttackBehaviour {
    private static final Logger logger = LoggerFactory.getLogger(AOEAttackComponent.class);

    private float radius;
    private float damage;
    private float attackCooldown;
    private float timeSinceLastAttack;
    private Vector2 origin;
    private List<Effect> effects;
    private Circle aoeCircle;

    /**
     * Creates an AOE attack component.
     *
     * @param radius The radius of the AOE attack
     * @param damage The damage dealt by the AOE attack
     * @param attackRate The rate of attacks per second
     * @param effectConfigs The configurations for additional effects
     */
    public AOEAttackComponent(float radius, float damage, float attackRate,
                              NPCConfigs.NPCConfig.EffectConfig[] effectConfigs) {
        this.radius = radius;
        this.damage = damage;
        this.attackCooldown = 1 / attackRate;
        this.timeSinceLastAttack = attackCooldown;
        this.origin = new Vector2();
        this.aoeCircle = new Circle(origin, radius);
        this.effects = createEffects(effectConfigs);
    }

    @Override
    public void update() {
        timeSinceLastAttack += ServiceLocator.getTimeSource().getDeltaTime();
        if (timeSinceLastAttack >= attackCooldown) {
            performAttack();
            timeSinceLastAttack = 0;
        }
    }

    @Override
    public void performAttack() {
        // For updating AOE circle position
        aoeCircle.setPosition(origin);

        // Getting all entities within the AOE
        List<Entity> entitiesInRange = getEntitiesInRange();

        for (Entity target : entitiesInRange) {
            if (canAttack(entity, target)) {
                // For applying damange
                CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
                if (targetStats != null) {
                    targetStats.addHealth((int) -damage);
                }

                // for applying effects
                applyEffects(target);
            }
        }
    }

    @Override
    public boolean canAttack(Entity attacker, Entity target) {
        // Checking if target is within the range or not
        PhysicsComponent targetPhysics = target.getComponent(PhysicsComponent.class);
        return targetPhysics != null && aoeCircle.contains(targetPhysics.getBody().getPosition());
    }

    @Override
    public void applyEffects(Entity target) {
        for (Effect effect : effects) {
            effect.apply(target);
        }
    }

    /**
     * Sets the origin point of the AOE attack.
     *
     * @param x The x-coordinate of the origin
     * @param y The y-coordinate of the origin
     */
    public void setOrigin(float x, float y) {
        this.origin.set(x, y);
    }

    /**
     * Sets the origin point of the AOE attack.
     *
     * @param origin The origin point as a Vector2
     */
    public void setOrigin(Vector2 origin) {
        this.origin.set(origin);
    }

    private List<Entity> getEntitiesInRange() {

        return new ArrayList<>(); // Placeholder
    }

    private List<Effect> createEffects(NPCConfigs.NPCConfig.EffectConfig[] effectConfigs) {
        List<Effect> effects = new ArrayList<>();
        if (effectConfigs != null) {
            for (NPCConfigs.NPCConfig.EffectConfig config : effectConfigs) {
                logger.info("Creating effect of type {} for AOE attack", config.type);
                effects.add(EffectFactory.createEffect(config, entity));
            }
        }
        return effects;
    }
}