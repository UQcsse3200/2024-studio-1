package com.csse3200.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.components.player.inventory.usables.*;
import com.csse3200.game.physics.components.PhysicsComponent;
import java.lang.Math;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {
    private static final Vector2 DEFAULT_SPEED = new Vector2(3f, 3f); // Metres per second

    private PhysicsComponent physicsComponent;
    private InventoryComponent inventoryComponent;
    private ItemPickupComponent itemPickupComponent;
    private Vector2 walkDirection = Vector2.Zero.cpy();
    private boolean moving = false;
    private Vector2 speed = DEFAULT_SPEED;
    private Vector2 baseSpeed = new Vector2(3f, 3f); // Metres per second;
    private boolean dead = false;
    private float maxSpeed = 0.5f;
    private float speedPercentage;

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        inventoryComponent = entity.getComponent(InventoryComponent.class);
        itemPickupComponent = entity.getComponent(ItemPickupComponent.class);
        entity.getEvents().addListener("walk", this::walk);
        entity.getEvents().addListener("walkStop", this::stopWalking);
        entity.getEvents().addListener("attackMelee", this::attack);
        entity.getEvents().addListener("shoot", this::shoot);
        entity.getEvents().addListener("use1", () -> use(new MedKit()));
        entity.getEvents().addListener("use2", () -> use(new ShieldPotion()));
        entity.getEvents().addListener("use3", () -> use(new Bandage()));
        entity.getEvents().addListener("use4", () -> use(new TargetDummy()));
        entity.getEvents().addListener("use5", () -> use(new BearTrap()));
        entity.getEvents().addListener("use6", () -> use(new BigRedButton()));
        entity.getEvents().addListener("use7", () -> use(new TeleporterItem()));
        entity.getEvents().addListener("useReroll", () -> handleReroll(new Reroll()));

        setTotalSpeedBoost(0.0f); //Initialise the speed percentage on the UI to 0.0
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
                stopWalking();
                dead = true;
            }
        }
    }

    /**
     * Gets the current speed of the player
     *
     * @return the current speed of the player
     */
    public Vector2 getCurrPlayerSpeed() {
        return this.speed;
    }

    /**
     * Gets the maximum speed limit of the player
     *
     * @return the maximum speed limit
     */
    public float getMaxTotalSpeedBoost() {
        return this.maxSpeed;
    }

    public Vector2 getMaxPlayerSpeed() {
        float maxSpeedBoost = getMaxTotalSpeedBoost();
        Vector2 scalar = getBaseSpeed().scl(maxSpeedBoost);
        Vector2 maximumPlayerSpeed = new Vector2(getBaseSpeed().x + scalar.x, getBaseSpeed().y + scalar.y);
        return maximumPlayerSpeed;
    }

    /**
     * Gets the base speed of the player
     *
     * @return the base speed 
     */
    public Vector2 getBaseSpeed() {
        return this.entity.getComponent(PlayerConfigComponent.class).getPlayerConfig().speed.cpy();
    }

    /**
     * Sets the current speed percentage stat to a new value
     *
     * @param speedPercentage the new speed percentage to set to
     */
    public void setTotalSpeedBoost(float speedPercentage) {
        this.speedPercentage = speedPercentage;
    }

    /**
     * Gets the percentage of speed compared to its original speed. This function is mainly used for updating
     * the UI of the speed progress bar
     * @param newSpeed the new speed that is being compared as a percentage of the original
     * @param originalSpeed the original speed
     * @return a value representing the percentage of speed compared to its original speed
     */
    public float getSpeedProgressBarProportion(Vector2 newSpeed, Vector2 originalSpeed) {
        //Get a new vector representing the difference between the two vectors
        Vector2 diff = new Vector2(newSpeed.x - originalSpeed.x, newSpeed.y - originalSpeed.y);
        //Get the length of the vector representing the difference
        float diffSpeedLength = (float) Math.sqrt(Math.pow(diff.x, 2) + Math.pow(diff.y, 2));
        //Get the length of the vector representing the original speed
        float originalSpeedLength = (float) Math.sqrt(Math.pow(originalSpeed.x, 2) + Math.pow(originalSpeed.y, 2));
        //Find the percentage of the original length that the difference is
        return diffSpeedLength/originalSpeedLength;
    }

    /**
     * Gets the current speed percentage, which is shown on the UI
     *
     * @return the current speed percentage
     */
    public float getTotalSpeedBoost() {
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
        if (!dead) {
            this.walkDirection = Vector2.Zero.cpy();
            updateSpeed();
            moving = false;
        }
    }

    /**
     * Makes the player attack.
     */
    private void attack() {
        entity.getComponent(InventoryComponent.class)
                .getOffhand()
                .ifPresent(OffHandItem::attack);
    }

    /**
     * Makes the player shoot in a direction.
     */
    private void shoot(Vector2 direction) {
        if (!dead) {
            entity.getComponent(InventoryComponent.class)
                    .getMainWeapon()
                    .ifPresent(weapon -> weapon.shoot(direction));
        }
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
        if (!dead) {
            this.walkDirection = direction;
            moving = true;
        }
    }

    /**
     * Gets the direction the player is walking in.
     * @return the direction the player is walking in
     */
    public Vector2 getWalkDirection() {
        return walkDirection;
    }


    /**
     * Detects whether a reroll item is allowed to be used or not. The only valid scenario is when
     * the player is in contact with another entity, and that entity is valid (not null)
     * @param reroll the reroll item instance
     */
    public void handleReroll(UsableItem reroll) {
        if (entity.getComponent(ItemPickupComponent.class).isInContact() && entity.getComponent(ItemPickupComponent.class).getItem() != null) {
            use(reroll); //Ensures that the reroll item can only be used when it is in collision with another item
        }
    }

    private void use(UsableItem item) {
        if (!dead) {
            for (Collectible collectedItem : inventoryComponent.getItems()) {
                if (collectedItem.getClass() == item.getClass()) {
                    item.apply(entity);
                    entity.getEvents().trigger("itemUsed");
                    inventoryComponent.drop(collectedItem);
                    break;
                }
            }
        }
    }

}

