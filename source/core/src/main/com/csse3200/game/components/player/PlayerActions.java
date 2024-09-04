package com.csse3200.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {
    private static final Vector2 DEFAULT_SPEED = new Vector2(3f, 3f); // Metres per second

    private PhysicsComponent physicsComponent;
    private InventoryComponent inventoryComponent;
    private Vector2 walkDirection = Vector2.Zero.cpy();
    private boolean moving = false;
    private Vector2 speed = DEFAULT_SPEED;

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        inventoryComponent = entity.getComponent(InventoryComponent.class);
        entity.getEvents().addListener("walk", this::walk);
        entity.getEvents().addListener("walkStop", this::stopWalking);
        entity.getEvents().addListener("attack", this::attack);
        entity.getEvents().addListener("shoot", this::shoot);
        entity.getEvents().addListener("useMedKit", this::applyMedKit);

    }

    @Override
    public void update() {
        if (moving) {
            updateSpeed();
        }
    }

    /**
     * Set the player speed
     *
     * @param speed the speed (in m/s)
     */
    public void setSpeed(Vector2 speed) {
        this.speed = speed;
        update();
    }


    /**
     * Stops the player from walking.
     */
    private void stopWalking() {
        this.walkDirection = Vector2.Zero.cpy();
        updateSpeed();
        moving = false;
    }

    /**
     * Makes the player attack.
     */
    private void attack() {
        ServiceLocator.getResourceService()
                .getAsset("sounds/Impact4.ogg", Sound.class)
                .play();
        entity.getComponent(WeaponComponent.class).attack();
    }

    /**
     * Makes the player shoot in a direction.
     */
    private void shoot(Vector2 direction) {
        ServiceLocator.getResourceService()
                .getAsset("sounds/Impact4.ogg", Sound.class)
                .play();
        entity.getComponent(WeaponComponent.class).shoot(direction);
    }

    private void updateSpeed() {
        Body body = physicsComponent.getBody();
        Vector2 velocity = body.getLinearVelocity();
        Vector2 desiredVelocity = walkDirection.cpy().scl(speed);
        // impulse = (desiredVel - currentVel) * mass
        Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }

    /**
     * Moves the player towards a given direction.
     *
     * @param direction direction to move in
     */
    private void walk(Vector2 direction) {
        this.walkDirection = direction;
        moving = true;
    }

    private void applyMedKit() {
        Inventory inventory = inventoryComponent.getInventory();
        for (Collectible item : inventory.getItems()) {
            if (item instanceof MedKit) {
                inventoryComponent.drop(item);
                ((MedKit) item).apply(entity);
                break;
            }
        }
    }
}


