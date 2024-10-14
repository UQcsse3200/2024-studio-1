package com.csse3200.game.components.Projectile;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.areas.GameController;
import com.csse3200.game.components.CombatStatsComponent;
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
import org.mockito.Mock;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(GameExtension.class)
class ProjectileAttackComponentTest {

    @Mock
    private GameAreaService gameAreaService;

    @Mock
    private GameController gameController;

    @Mock
    private Entity player;

    /**
     * Services are made.
     * Texture is loaded dynamic to the current ProjectileConfig texture asset.
     * Acts to test asset and services load.
     */
    @BeforeEach
    void beforeEach() {

        // Mock GameAreaService and MainGameArea
        gameAreaService = mock(GameAreaService.class);
        gameController = mock(GameController.class);
        player = new Entity().addComponent(new CombatStatsComponent(100, 10));

        // Register the mocked services with ServiceLocator
        ServiceLocator.registerGameAreaService(gameAreaService);

        // Mock the behavior of gameAreaService and mainGameArea
        when(gameAreaService.getGameController()).thenReturn(gameController);
        when(gameController.getPlayer()).thenReturn(player);

        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerEntityService(new EntityService());

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
    void shouldDispose() {

        // make a projectile on the player layer.
        ProjectileConfig config = new ProjectileConfig();
        config.Layer = PhysicsLayer.NPC;
        Entity projectile = new ProjectileFactory().createProjectile(config, Vector2Utils.LEFT, new Vector2(0,0));
        projectile.create();

        ServiceLocator.getEntityService().register(projectile);

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

        //this should process the disposal of the entity marked on collision.
        ServiceLocator.getEntityService().update();
        Entity projectile2 = new Entity();

        //let the disposal cycle in entity service run for a while.
        for (int i = 0; i < 5; i++) {
            ServiceLocator.getEntityService().update();
        }

        //should have unregistered after - disposal is much more than unregistering.
        //However, unregistering is the last step
        assertEquals(0, ServiceLocator.getEntityService().getEntities().length);

    }


    @Test
    void shouldNotAttackAnotherProjectileOnSameLayer() {

        // make two projectile on the same layer.
        Entity projectile1 = new ProjectileFactory().createProjectile(new ProjectileConfig(), Vector2Utils.LEFT, new Vector2(0,0));
        Entity projectile2 =  new ProjectileFactory().createProjectile(new ProjectileConfig(), Vector2Utils.LEFT, new Vector2(0,0));
        projectile1.create();
        projectile2.create();

        //Add these puppies to the entity service.
        ServiceLocator.getEntityService().register(projectile1);
        ServiceLocator.getEntityService().register(projectile2);

        Fixture entityFixture = projectile1.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = projectile2.getComponent(HitboxComponent.class).getFixture();
        projectile1.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        //check that the projectile has not been disposed.
        projectile2.earlyUpdate();
        assertNotNull(projectile2.getComponent(CombatStatsComponent.class));

        //run that service to flush down a potential disposal.
        for (int i = 0; i < 5; i++) {
            ServiceLocator.getEntityService().update();
        }

        // they should not have 'hit' each other
        assertEquals(2, ServiceLocator.getEntityService().getEntities().length);

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
