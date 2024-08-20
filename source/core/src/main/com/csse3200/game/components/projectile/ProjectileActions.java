package com.csse3200.game.components.projectile;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.Component;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;

public class ProjectileActions extends Component {

    private static final Vector2 MAX_SPEED = new Vector2(3f, 3f); // Metres per second
    private PhysicsComponent physicsComponent;
    private Vector2 shotDirection = Vector2.Zero.cpy();
    private boolean moving = false;

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        entity.getEvents().addListener("shootProjectile", this::startShoot);
        entity.getEvents().addListener("collisionStart", this::cleanUp);

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
        Vector2 desiredVelocity = shotDirection.cpy().scl(MAX_SPEED);
        Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }

    /**
     * Moves the projectile towards a given direction.
     */
    void startShoot(Vector2 direction) {
        this.shotDirection = direction;
        moving = true;
    }

    /**
     * Stops the player from walking.
     */
    void cleanUp() {
        this.shotDirection = Vector2.Zero.cpy();
        updateSpeed();
        moving = false;


    }


}
