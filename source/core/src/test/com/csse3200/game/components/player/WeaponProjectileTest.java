package com.csse3200.game.components.player;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.areas.MainGameArea;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.configs.ProjectileConfig;
import com.csse3200.game.entities.factories.WeaponFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;


@ExtendWith(GameExtension.class)
class WeaponProjectileTest {

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

        ServiceLocator.getGameAreaService().getGameArea()
                .setTerrain(new TerrainComponent(
                        mock(OrthographicCamera.class),
                        mock(TiledMap.class),
                        mock(TiledMapRenderer.class),
                        3f));

        //load in the current default texture.
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextureAtlases(new String []{new ProjectileConfig().projectileAtlasPath});
        // load in sound asset
        WeaponFactory weaponFactory = new WeaponFactory();
        weaponFactory.load();
        while (!resourceService.loadForMillis(1000)) {
            // wait for assets to load.
        }
        //should not cause exception
    }


    @Test
    public void testRangeWeaponFireRate () {

        int maxAmmo = 10;
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(),
                Collectible.Type.RANGED_WEAPON, 10, 5, 1, maxAmmo, maxAmmo, 2);
        // Create test entity to attach weaponComponent
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
    public void testRangeWeaponReload() {

        int maxAmmo = 10;
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(),
                Collectible.Type.RANGED_WEAPON, 10, 5, 1, 0, maxAmmo, 1);
        // Create test entity to attach weaponComponent
        Entity testEntity = new Entity();
        testEntity.addComponent(weaponComponent);
        // Ammo is 0
        assertEquals(0, weaponComponent.getAmmo());
        // Shot in default direction with ammo at 0
        weaponComponent.shoot(new Vector2());
        try {
            Thread.sleep(800);
            weaponComponent.shoot(new Vector2());
            // Weapon is still reloading, weapon should not shoot.
            assertEquals(maxAmmo, weaponComponent.getAmmo());
            Thread.sleep(200);
            weaponComponent.shoot(new Vector2());
            // Attempt to shoot weapon after reload, weapon should shoot.
            assertEquals(maxAmmo - 1, weaponComponent.getAmmo());
        } catch (InterruptedException ex) {
            // sleep() failed
            Assertions.fail();
        }

    }


}
