package com.csse3200.game.components.Projectile;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.WeaponComponent;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.configs.ProjectileConfig;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.Vector2Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(GameExtension.class)
class ProjectileAttackComponentTest {

    /**
     * Services are made.
     * Texture is loaded dynamic to the current ProjectileConfig texture asset.
     * Acts to test asset and services load.
     */
    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerEntityService(new EntityService());

        //load in the current default texture.
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(new String []{new ProjectileConfig().projectileTexturePath});
        while (!resourceService.loadForMillis(1000)) {
            // wait for assets to load.
        }
        //should not cause exception
    }

    @Test
    void shouldHit() {
        Entity projectile = createProjectile();
        Entity target = createTarget();

        Fixture entityFixture = projectile.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        projectile.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        assertEquals(0, target.getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    public void testRangeWeaponFireRate () {


        int maxAmmo = 10;
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(),
                Collectible.Type.RANGED_WEAPON, 10, 5, 1, maxAmmo, maxAmmo, 2);
        // Create test entity to attach weaponcomponent
        Entity testEntity = new Entity();
        testEntity.addComponent(weaponComponent);
        // Shot in default direction with ammo at 0
        weaponComponent.shoot(new Vector2());
        try {
            Thread.sleep(800);
            weaponComponent.shoot(new Vector2());
            // Attempt to shoot weapon faster than fire rate, weapon should not shoot.
            assertEquals(maxAmmo - 1, weaponComponent.getAmmo());
            Thread.sleep(200);
            weaponComponent.shoot(new Vector2());
            // Attempt to shoot weapon equal to fire rate, weapon should shoot.
            assertEquals(maxAmmo - 2, weaponComponent.getAmmo());
        } catch (InterruptedException ex) {
            // sleep() failed
            Assertions.fail();
        }
    }

    @Test
    void shouldNotAttackOtherLayer() {

        // default config is on PhysicsLayer.PLAYER
        Entity projectile = createProjectile();

        //target on the NPC layer - not PLAYER
        Entity target =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new CombatStatsComponent(10, 0));
        target.create();

        Fixture entityFixture = projectile.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        projectile.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        //check the health is unchanged
        assertEquals(10, target.getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldNotAttackWithoutCombatComponent() {
        short targetLayer = (1 << 3);
        Entity projectile = createProjectile();
        // Target does not have a combat component
        Entity target =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(targetLayer));
        target.create();

        Fixture entityFixture = projectile.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();

        // This should not cause an exception
        projectile.getEvents().trigger("collisionStart", entityFixture, targetFixture);
    }

    Entity createProjectile() {
        Entity projectile = ProjectileFactory.createProjectile(new ProjectileConfig(), Vector2Utils.LEFT);
        projectile.create();
        return projectile;
    }

    Entity createTarget() {
        Entity target =
                new Entity()
                        .addComponent(new CombatStatsComponent(10, 0))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));
        target.create();
        return target;
    }
}
