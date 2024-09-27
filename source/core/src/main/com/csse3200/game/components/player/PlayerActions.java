package com.csse3200.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.CombatStatsComponent;
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
    private boolean dead = false;
    private float maxSpeed = 5.0f;
    private float speedPercentage;

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        inventoryComponent = entity.getComponent(InventoryComponent.class);
        entity.getEvents().addListener("walk", this::walk);
        entity.getEvents().addListener("walkStop", this::stopWalking);
        entity.getEvents().addListener("attackMelee", this::attack);
        entity.getEvents().addListener("shoot", this::shoot);
        entity.getEvents().addListener("use1", () -> use(new MedKit()));
        entity.getEvents().addListener("use2", () -> use(new ShieldPotion()));
        entity.getEvents().addListener("use3", () -> use(new Bandage()));
        setSpeedPercentage(1.0f);

        setSpeedPercentage(0.0f); //Initialise the speed percentage on the UI to 0.0
    }

    @Override
    public void update() {
        if (moving) {
            updateSpeed();
        }
        if (entity.getComponent(CombatStatsComponent.class).isDead()) {
            entity.getEvents().trigger("stopAnimation");
            if (!dead) {
                entity.getEvents().trigger("death");
                dead = true;
            }
        }
    }

    /**
     * Gets the current speed of the player
     *
     * @return the current speed of the player
     */
    public Vector2 getCurrSpeed() {
        return this.speed;
    }

    /**
     * Gets the maximum speed limit of the player
     *
     * @return the maximum speed limit
     */
    public float getMaxSpeed() {
        return this.maxSpeed;
    }

    /**
     * Sets the current speed percentage stat to a new value
     *
     * @param speedPercentage the new speed percentage to set to
     */
    public void setSpeedPercentage(float speedPercentage) {
        this.speedPercentage = speedPercentage;
    }

    /**
     * Gets the current speed percentage, which is shown on the UI
     *
     * @return the current speed percentage
     */
    public float getCurrSpeedPercentage() {
        return this.speedPercentage;
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
        ServiceLocator.getResourceService().playSound("sounds/Impact4.ogg");
        entity.getComponent(WeaponComponent.class).attackMelee();
    }

    /**
     * Makes the player shoot in a direction.
     */
    private void shoot(Vector2 direction) {
        ServiceLocator.getResourceService().playSound("sounds/Impact4.ogg");
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

    /**
     * Gets the direction the player is walking in.
     * @return the direction the player is walking in
     */
    public Vector2 getWalkDirection() {
        return walkDirection;
    }

    private void use(UsableItem item) {
        Inventory inventory = inventoryComponent.getInventory();
        for (Collectible collectedItem : inventory.getItems()) {
            if (collectedItem.getClass() == item.getClass()) {
                item.apply(entity);
                entity.getEvents().trigger("itemUsed");
                inventoryComponent.drop(collectedItem);
                break;
            }
        }
    }

}

