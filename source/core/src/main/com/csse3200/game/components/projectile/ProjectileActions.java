package com.csse3200.game.components.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ObstacleFactory;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.Vector2Utils;

/**
 * Handles projectile actions currently including shoot(), and stopShoot().
 * These methods are called directly in ProjectileAttackComponent.
 * Further movement actions, including event driven actions on projectiles should be developed here.
 */
public class ProjectileActions extends Component {

    private PhysicsComponent physicsComponent;
    private Vector2 walkDirection = Vector2.Zero.cpy();
    private boolean moving = false;
    private Vector2 speed = new Vector2(3f, 3f);
    private Vector2 parentPosition;
    private float range;
    private double initSpeed = 3;



    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
    }

    @Override
    public void update() {
        if (moving) {
            double travel = Math.hypot(parentPosition.x - entity.getPosition().x, parentPosition.y - entity.getPosition().y );

            // juicy effect projectiles slow in last 10% of range.
            if((travel + range / 10) > range) {
                speed.setLength((float) (initSpeed * 0.5));
            }


            if (travel >= range) {
                // just trigger any collision to dispose when the projectile is out of range.
                ServiceLocator.getEntityService().markEntityForRemoval(entity);
            }

            updateSpeed();
        } else {
            this.getEntity().setPosition(parentPosition);
            moving = true;
        }

    }

    private void updateSpeed() {
        Body body = physicsComponent.getBody();
        Vector2 velocity = body.getLinearVelocity();
        Vector2 desiredVelocity = walkDirection.cpy().scl(speed);

        Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }

    /**
     * Moves the projectile towards a given direction.
     *
     * @param direction direction to move in.
     * @param speed speed to move at.
     *
     */
    void shoot(Vector2 direction, Vector2 speed, Vector2 parentPosition, float range) {
        this.walkDirection = direction.cpy();
        this.speed = speed.cpy();
        this.parentPosition = parentPosition.cpy();
        this.range = range;
        this.initSpeed = Math.hypot(speed.x, speed.y);

    }

    /**
     * Stops the projectile moving.
     */
    void stopShoot() {
        this.walkDirection = Vector2.Zero.cpy();
        updateSpeed();
        moving = false;
    }
}

