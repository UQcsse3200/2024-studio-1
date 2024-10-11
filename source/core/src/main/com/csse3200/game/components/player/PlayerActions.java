package com.csse3200.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.physics.components.PhysicsComponent;

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
    private float maxSpeed = 1.5f;
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
        entity.getEvents().addListener("use7", ()-> use(new TeleporterItem()));
        entity.getEvents().addListener("useReroll", () -> handleReroll(new Reroll()));
//
//        setSpeedPercentage(0.0f); //Initialise the speed percentage on the UI to 0.0
        float diff = DEFAULT_SPEED.sub(speed).x;
        setSpeedPercentage(diff);
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
     * Gets the base speed limit of the player
     *
     * @return the maximum speed limit
     */
    public Vector2 getBaseSpeed() {
        return this.baseSpeed;
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
     * Gets the base speed of the player 
     *
     * @param speed the speed (in m/s)
     */
    public void setBaseSpeed(Vector2 speed) {
        this.baseSpeed= speed;
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
                .getInventory()
                .getMelee()
                .ifPresent(meleeWeapon -> meleeWeapon.attack());
    }

    /**
     * Makes the player shoot in a direction.
     */
    private void shoot(Vector2 direction) {
        entity.getComponent(InventoryComponent.class)
                .getInventory()
                .getRanged()
                .ifPresent(rangedWeapon -> rangedWeapon.shoot(direction));
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
        } else {
            //Otherwise the reroll item cannot be used
        }
    }

    private void use(UsableItem item) {
        if (!dead) {
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

}

