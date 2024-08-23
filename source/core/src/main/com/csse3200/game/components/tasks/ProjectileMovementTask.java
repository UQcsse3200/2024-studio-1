package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.physics.components.PhysicsMovementComponent;

/**
 * Task that handles the movement of a projectile.
 * It moves the projectile in a given direction with a specific speed.
 */
public class ProjectileMovementTask extends Component {
    private final Vector2 direction;
    private final Vector2 speed;

    public ProjectileMovementTask(Vector2 direction, Vector2 speed) {
        this.direction = direction.nor();  // Normalize the direction vector
        this.speed = speed;
    }

    @Override
    public void create() {
        super.create();
        startMovement();
    }

    /**
     * Starts the movement of the projectile in the specified direction at the specified speed.
     */
    private void startMovement() {
        // Fetch the PhysicsMovementComponent to apply movement
        PhysicsMovementComponent movementComponent = entity.getComponent(PhysicsMovementComponent.class);
        if (movementComponent != null) {
            Vector2 velocity = new Vector2(direction).scl(speed);
            movementComponent.setLinearVelocity(velocity);
        }
    }

    @Override
    public void update() {
        // You could add additional logic here, such as checking for certain conditions to stop movement
    }
}

