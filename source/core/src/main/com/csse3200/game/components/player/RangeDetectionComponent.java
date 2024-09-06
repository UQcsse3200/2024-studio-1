package com.csse3200.game.components.player;


import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

public class RangeDetectionComponent extends Component {

    public static final Logger logger = LoggerFactory.getLogger(RangeDetectionComponent.class);
    /**
     * When this entity touches a valid enemy's hitbox, and attack key is pressed, deal damage to them.
     */
    private short targetLayer;
    private float range = 0f;
    private CombatStatsComponent combatStats;
    private HitboxComponent hitboxComponent;

    private List<Entity> entities; // List of entities that within the range of the attack

    /**
     * Create a component which attacks entities on collision, without knockback.
     * @param targetLayer The physics layer of the target's collider.
     */
    public RangeDetectionComponent(short targetLayer) {
        this.targetLayer = targetLayer;
        this.range = 0f;
        this.hitboxComponent = null;
    }

    /**
     * Create a component which attacks entities on collision, with knockback.
     * @param targetLayer The physics layer of the target's collider.
     * @param range The range of the attack.
     */
    public RangeDetectionComponent(short targetLayer, float range) {
        this.targetLayer = targetLayer;
        this.range = range;
        this.hitboxComponent = null;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        combatStats = entity.getComponent(CombatStatsComponent.class);
        hitboxComponent = null;
        if (entity.getComponent(WeaponComponent.class).rangedItemEntity != null) {
            hitboxComponent = entity.getComponent(WeaponComponent.class).rangedItemEntity.getComponent(HitboxComponent.class);
        }
    }

    private void onCollisionStart(Fixture me, Fixture other) {
        if (hitboxComponent == null) {
            // Not triggered by hitbox, ignore
            if (entity != null) {
                if (getEntity().getComponent(WeaponComponent.class).rangedItemEntity != null) {
                hitboxComponent = getEntity().getComponent(WeaponComponent.class).rangedItemEntity.getComponent(HitboxComponent.class);
                } else {
                    logger.warn("itemEntity is null");
                    return;
                }
            } else {
                logger.warn("entity is null");
                return;
            }
        }
        logger.debug("hitboxComponent: {} is not null", hitboxComponent);
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }

        if (!PhysicsLayer.contains(targetLayer, PhysicsLayer.PLAYER)) {
            // Doesn't match our target layer, ignore
            return;
        }

        // Try to attack target.
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
        if (targetStats != null) {
            targetStats.hit(combatStats);
        }

        // Apply knockback
//        PhysicsComponent physicsComponent = target.getComponent(PhysicsComponent.class);
//        if (physicsComponent != null && knockbackForce > 0f) {
//            Body targetBody = physicsComponent.getBody();
//            Vector2 direction = target.getCenterPosition().sub(entity.getCenterPosition());
//            Vector2 impulse = direction.setLength(knockbackForce);
//            targetBody.applyLinearImpulse(impulse, targetBody.getWorldCenter(), true);
//        }
    }
}
