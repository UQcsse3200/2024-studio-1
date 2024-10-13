package com.csse3200.game.components.weapon;


import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.inventory.Collectible;
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
public class PositionTrackerTest {

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
        //when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        ServiceLocator.registerTimeSource(gameTime);

        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerTimeSource(gameTime);

        //load in the current default texture.
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextureAtlases(new String[]{new ProjectileConfig().projectileAtlasPath});
        while (!resourceService.loadForMillis(1000)) {
            // wait for assets to load.
        }

        weaponFactory = new WeaponFactory();
        player = new Entity();
    }

    @org.junit.jupiter.api.Test
    public void testCreatePositionTracker() {
        PositionTracker positionTracker = new PositionTracker();

        assert positionTracker instanceof PositionTracker;
    }

    @org.junit.jupiter.api.Test
    public void testConnectAndDisconnectPlayer() {
        PositionTracker positionTracker = new PositionTracker();
        positionTracker.connectPlayer(player);
        assertEquals(positionTracker.getPlayer(), player);

        positionTracker.disconnectPlayer();
        assertEquals(positionTracker.getPlayer(), null);
    }

    @org.junit.jupiter.api.Test
    public void testUpdate() {
        PositionTracker positionTracker = new PositionTracker();

        Entity weaponEntity = new Entity().addComponent(positionTracker);
        player.setPosition(new Vector2(10, 10));
        positionTracker.connectPlayer(player);
        positionTracker.update();
        assertEquals(weaponEntity.getPosition(), player.getPosition());

        // change player position
        player.setPosition(new Vector2(20, 20));
        positionTracker.update();
        assertEquals(weaponEntity.getPosition(), new Vector2(20, 20));
    }

    @org.junit.jupiter.api.Test
    public void testGetOffset() {
        PositionTracker positionTracker = new PositionTracker();
        positionTracker.connectPlayer(player);
        assertEquals(positionTracker.getOffset(), new Vector2(0, 0));
    }
}
