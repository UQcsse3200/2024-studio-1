package com.csse3200.game.components.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.physics.components.PhysicsComponent;

/**
 * Task that handles the movement of a projectile.
 * It moves the projectile in a given direction with a specific speed.
 */
public class ProjectileActions extends Component {

    private PhysicsComponent physicsComponent;
    private Vector2 walkDirection = Vector2.Zero.cpy();
    private boolean moving = false;
    private Vector2 speed = new Vector2(3f, 3f);

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);

    }

    @Override
    public void update() {
        if (moving) {
            updateSpeed();
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
     * Moves the player towards a given direction.
     *
     * @param direction direction to move in
     */
    void shoot(Vector2 direction, Vector2 speed) {
        this.walkDirection = direction;
        this.speed = speed;
        moving = true;
    }

    /**
     * Stops the player from walking.
     */
    void stopShoot() {
        this.walkDirection = Vector2.Zero.cpy();
        updateSpeed();
        moving = false;
    }
}

