package com.csse3200.game.components.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;

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

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        System.out.println(entity.getComponent(NameComponent.class));
    }

    @Override
    public void update() {
        if (moving) {
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
    void shoot(Vector2 direction, Vector2 speed, Vector2 parentPosition) {
        this.getEntity().getComponent(AnimationRenderComponent.class).startAnimation("GreenShoot");
        this.walkDirection = direction;
        this.speed = speed;
        this.parentPosition = parentPosition;

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

