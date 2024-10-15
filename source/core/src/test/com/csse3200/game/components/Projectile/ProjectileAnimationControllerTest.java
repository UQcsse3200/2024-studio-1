package com.csse3200.game.components.Projectile;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.components.npc.DirectionalNPCComponent;
import com.csse3200.game.components.projectile.ProjectileAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.configs.ProjectileConfigs;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class ProjectileAnimationControllerTest {

    @Mock
    private Entity projectile;

    @Mock
    private ResourceService resourceService;

    @Mock
    private ProjectileAnimationController animationController;

    @Mock
    private ProjectileFactory projectileFactory;

    @BeforeEach
    void beforeEach() {
        resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        projectileFactory = new ProjectileFactory();


        projectile = projectileFactory.create("kitsuneProjectile", new Vector2(0,0), new Vector2(0,0));
        animationController = projectile.getComponent(ProjectileAnimationController.class);
    }

    @Test
    void testCreate() {
        animationController.create();
        // Just verify that create() doesn't throw any exceptions
        assertTrue(true);
    }

}
