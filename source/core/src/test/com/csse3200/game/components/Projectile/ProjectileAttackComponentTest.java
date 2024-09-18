package com.csse3200.game.components.Projectile;



import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.areas.MainGameArea;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.RangeDetectionComponent;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;


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
        ServiceLocator.registerGameAreaService(new GameAreaService(mock(MainGameArea.class)));


        //load in the current default texture.
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextureAtlases(new String []{new ProjectileConfig().projectileAtlasPath});

        // Load in sound effect
        resourceService.loadSounds(new String[]{"sounds/shotgun1_f.ogg"});
        resourceService.loadSounds(new String[]{"sounds/shotgun1_r.ogg"});
        while (!resourceService.loadForMillis(1000)) {
            // wait for assets to load
        }
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
    void shouldNotAttackOtherLayer() {

        // make a projectile on the player layer.
        ProjectileConfig config = new ProjectileConfig();
        config.Layer = PhysicsLayer.PLAYER;
        Entity projectile = new ProjectileFactory().createProjectile(config, Vector2Utils.LEFT, new Vector2(0,0));
        projectile.create();


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
        Entity projectile = new ProjectileFactory().createProjectile(new ProjectileConfig(), Vector2Utils.LEFT, new Vector2(0,0));
        projectile.create();
        return projectile;
    }

    Entity createTarget() {
        Entity target =
                new Entity()
                        .addComponent(new CombatStatsComponent(10, 0))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(new ProjectileConfig().Layer));
        target.create();
        return target;
    }
}
