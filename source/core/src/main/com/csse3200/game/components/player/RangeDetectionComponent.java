package com.csse3200.game.components.player;


import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;

public class RangeDetectionComponent extends Component {

    public static final Logger logger = LoggerFactory.getLogger(RangeDetectionComponent.class);
    /**
     * When this entity touches a valid enemy's hitbox, and attack key is pressed, deal damage to them.
     */
    private short targetLayer;
    private HitboxComponent hitboxComponent;

    private ArrayList<Entity> entities; // List of entities that within the range of the attack

    /**
     * Create a component which attacks entities on collision, without knockback.
     * @param targetLayer The physics layer of the target's collider.
     */
    public RangeDetectionComponent(short targetLayer) {
        this.targetLayer = targetLayer;
        this.hitboxComponent = null;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
        hitboxComponent = null;
        if (entity.getComponent(WeaponComponent.class).rangedItemEntity != null) {
            //  update to meleeItemEntity later
            hitboxComponent = entity.getComponent(WeaponComponent.class).rangedItemEntity.getComponent(HitboxComponent.class);
        } else {
            logger.warn("itemEntity is null at creation");
        }
        entities = new ArrayList<>();
    }

    /**
     * Update the hitbox component.
     * @param entity
     */
    public void update(Entity entity) {
        if (entity.getComponent(HitboxComponent.class) != null) {
            hitboxComponent = entity.getComponent(HitboxComponent.class);
        } else {
            logger.warn("itemEntity is null at update");
        }
    }

    private void onCollisionEnd(Fixture me, Fixture other) {
        if (hitboxComponent == null) {
            // Not triggered by hitbox, ignore
            return;
        }

        if (hitboxComponent.getFixture() == me) {
            // Not triggered by hitbox, ignore
            return;
        }

        if (!PhysicsLayer.contains(targetLayer, PhysicsLayer.PLAYER)) {
            // Doesn't match our target layer, ignore
            return;
        }

        // Try to attack target.
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;

        //remove the entity from the list of entities that are within the range of the attack
        entities.remove(target);
        logger.info("The list is now: " + entities.toString());
    }

    private void onCollisionStart(Fixture me, Fixture other) {
        logger.info("Collision detected");
        if (hitboxComponent == null) {
            logger.warn("hitboxComponent is null");
            return;
        }

        // if the hitboxComponent is equal to me (the entity that has the RangeDetectionComponent)
        if (hitboxComponent.getFixture() == me) {
            // Not triggered by hitbox, ignore
            return;
        }

        logger.info("hitboxComponent is not null and is equal to me");
        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }

        // Try to attack target.
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;

        //add the entity to the list of entities that are within the range of the attack
        entities.add(target);
        logger.info("The list is now: " + entities.toString());

    }

    /**
     * Get the list of entities that are within the range of the attack
     * @return the list of entities that are within the range of the attack
     */
    public ArrayList<Entity> getEntities() {
        if (entities == null) {
            return new ArrayList<>();
        }
        return entities;
    }
}
