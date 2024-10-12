package com.csse3200.game.components.effects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Knockback effect that can be applied to an entity.
 */
public class KnockbackEffect implements Effect {
    private final Entity sourceEntity;
    private final float force;
    private static final Logger logger = LoggerFactory.getLogger(KnockbackEffect.class);

    public KnockbackEffect(Entity sourceEntity, float force) {
        this.sourceEntity = sourceEntity;
        this.force = force;
    }

    @Override
    public void apply(Entity target) {
        // Apply the force to the entity's physics component
        PhysicsComponent physicsComponent = target.getComponent(PhysicsComponent.class);
        if (physicsComponent != null && force > 0f) {
            Body targetBody = physicsComponent.getBody();
            if (targetBody != null && sourceEntity != null) {
                Vector2 direction = target.getCenterPosition().sub(sourceEntity.getCenterPosition());
                Vector2 impulse = direction.setLength(force);
                targetBody.applyLinearImpulse(impulse, targetBody.getWorldCenter(), true);
                logger.info("Applied knockback force {} to entity {}", impulse, target);
            } else {
                logger.warn("Failed to apply knockback force to entity {} - null entities", target);
                logger.info("Target body: {}, Source entity: {}", targetBody, sourceEntity);
            }
        }
    }

    @Override
    public void update(Entity target, float deltaTime) {
        // Knockback doesn't need to be updated continuously
    }

    @Override
    public void refresh(Effect newEffect) {
        // Knockback is instantaneous, nothing to refresh
    }

    @Override
    public void remove(Entity target) {
        // Knockback is instantaneous, nothing to remove
    }

    @Override
    public boolean isExpired() {
        return true;
    }

    @Override
    public EffectType getType() {
        return EffectType.KNOCKBACK;
    }

    @Override
    public float getDuration() {
        return 0;
    }
}
