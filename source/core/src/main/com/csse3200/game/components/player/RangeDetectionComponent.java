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
     * When this entity touches a valid enemy's hit box, and attack key is pressed, deal damage to them.
     */
    private short targetLayer;

    /**
     * The hit box component 2 of the entity that has the RangeDetectionComponent
     */
    private HitboxComponent hitboxComponent;

    private ArrayList<Entity> entities; // List of entities that within the range of the attack

    /**
     * Create a component which attacks entities on collision, without knock-back.
     * @param targetLayer The physics layer of the target's collider.
     */
    public RangeDetectionComponent(short targetLayer) {
        this.targetLayer = targetLayer;
        this.hitboxComponent = null;
    }

    /**
     * Create the component.
     */
    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        //entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
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
     * Update the hit box component.
     * IMPORTANT: CALL THIS BEFORE USING THE LIST OF ENTITIES
     * @param entity The entity the that hit box component attached to.
     */
    public void updateWeaponEntity(Entity entity) {
        if (entity.getComponent(HitboxComponent.class) != null) {
            hitboxComponent = entity.getComponent(HitboxComponent.class);
        } else {
            logger.warn("itemEntity is null at update");
        }
    }

    @Override
    public void update() {

        // For each entity in the list of entities, if the entity is not within the range of the attack (< 3f), remove it from the list
//        for (Entity e : entities) {
//            if (entity.getCenterPosition().dst(e.getCenterPosition()) > 3f) {
//                entities.remove(e);
//            }
//        }
    }

//    /**
//     * When the entity stops colliding with another entity.
//     * @param me The entity that is colliding.
//     * @param other The entity that is being collided with.
//     */
//    private void onCollisionEnd(Fixture me, Fixture other) {
//        logger.info("Collision end detected");
//        if (hitboxComponent == null) {
//            // Not triggered by hit box, ignore
//            return;
//        }
//
//        if (hitboxComponent.getFixture() == me) {
//            // Not triggered by hit box, ignore
//            return;
//        }
//
//        if (!PhysicsLayer.contains(targetLayer, PhysicsLayer.PLAYER)) {
//            // Doesn't match our target layer, ignore
//            return;
//        }
//
//        // Try to attack target.
//        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
//
//        //remove the entity from the list of entities that are within the range of the attack
//        logger.info("Collision end detected");
//        entities.remove(target);
//        logger.info("The list is now: " + entities);
//    }

    /**
     * When the entity starts colliding with another entity.
     * @param me The entity that is colliding.
     * @param other The entity that is being collided with.
     */
    private void onCollisionStart(Fixture me, Fixture other) {
        logger.info("Collision start detected");
        if (hitboxComponent == null) {
            logger.warn("hitboxComponent is null");
            return;
        }

        // if the hitboxComponent is equal to me (the entity that has the RangeDetectionComponent)
        if (hitboxComponent.getFixture() == me) {
            // Not triggered by hitbox, ignore
            return;
        }

        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }
        // Try to attack target.
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;

        //add the entity to the list of entities that are within the range of the attack
        entities.add(target);
        logger.info("The list is now: " + entities);

    }

    /**
     * Get the list of entities that are within the range of the attack
     * @return the list of entities that are within the range of the attack
     */
    public ArrayList<Entity> getEntities() {
        if (entities == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(entities);  // Return a copy of the list
    }

    /**
     * Get the hit box component of the entity that has the RangeDetectionComponent
     * @return the hit box component of the entity that has the RangeDetectionComponent
     */
    public HitboxComponent getHitboxComponent() {
        return hitboxComponent;
    }
}
