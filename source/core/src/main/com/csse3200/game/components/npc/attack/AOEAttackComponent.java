package com.csse3200.game.components.npc.attack;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.attack.attackeffects.Effect;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.entities.factories.EffectFactory;
import com.csse3200.game.physics.PhysicsEngine;
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
    private PhysicsEngine physicsEngine;

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
        this.physicsEngine = ServiceLocator.getPhysicsService().getPhysics();
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
        aoeCircle.setPosition(origin);
        List<Entity> entitiesInRange = getEntitiesInRange();

        for (Entity target : entitiesInRange) {
            if (canAttack(entity, target)) {
                CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
                if (targetStats != null) {
                    targetStats.addHealth((int) -damage);
                }
                applyEffects(target);
            }
        }
    }

    @Override
    public boolean canAttack(Entity attacker, Entity target) {
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
        List<Entity> entitiesInRange = new ArrayList<>();
        World physicsWorld = physicsEngine.getWorld();

        physicsWorld.QueryAABB(
                new QueryCallback() {
                    @Override
                    public boolean reportFixture(Fixture fixture) {
                        Body body = fixture.getBody();
                        Object userData = body.getUserData();
                        if (userData instanceof Entity) {
                            Entity entity = (Entity) userData;
                            if (entity != AOEAttackComponent.this.entity) {
                                Vector2 entityPosition = body.getPosition();
                                if (aoeCircle.contains(entityPosition)) {
                                    entitiesInRange.add(entity);
                                }
                            }
                        }
                        return true;
                    }
                },
                origin.x - radius, origin.y - radius,
                origin.x + radius, origin.y + radius
        );

        return entitiesInRange;
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

    public float getRadius() {
        return radius;
    }
}