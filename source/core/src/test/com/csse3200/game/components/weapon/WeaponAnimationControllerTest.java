package com.csse3200.game.components.weapon;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.configs.ProjectileConfig;
import com.csse3200.game.entities.factories.WeaponFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class WeaponAnimationControllerTest {

    @Mock
    WeaponFactory weaponFactory;

    @Mock
    Entity player;

    @BeforeEach
    public void beforeEach() {
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);

        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerTimeSource(gameTime);

        //load in the current default texture.
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextureAtlases(new String []{new ProjectileConfig().projectileAtlasPath});
        while (!resourceService.loadForMillis(1000)) {
            // wait for assets to load.
        }

        weaponFactory = new WeaponFactory();
        player = new Entity();
    }

    @org.junit.jupiter.api.Test
    public void testCreateWeaponAnimationController() {
        WeaponAnimationController weaponAnimationController = new WeaponAnimationController();
        assertEquals(WeaponAnimationController.class, weaponAnimationController.getClass());
    }

    @org.junit.jupiter.api.Test
    public void testConnectAndDisconnectPlayer() {
        WeaponAnimationController weaponAnimationController = new WeaponAnimationController();
        player.addComponent(weaponAnimationController)
                .addComponent(new NameComponent("Ranged"));

        weaponAnimationController.connectPlayer(player);
        assertEquals(true, weaponAnimationController.connected);

        weaponAnimationController.disconnectPlayer();
        assertEquals(false, weaponAnimationController.connected);


    }
}
