package com.csse3200.game.components.weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.weapons.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.configs.ProjectileConfig;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.entities.factories.WeaponFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class FiringControllerTest {

    @Mock
    WeaponFactory weaponFactory;

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
        resourceService.loadTextureAtlases(new String[]{new ProjectileConfig().projectileAtlasPath});
        while (!resourceService.loadForMillis(1000)) {
            // wait for assets to load.
        }

        weaponFactory = new WeaponFactory();
    }

    @org.junit.jupiter.api.Test
    public void testCreateFiringControllerForMelee() {
        Collectible knifeCollectible = weaponFactory.create(Collectible.Type.OFF_HAND, "knife");
        assertEquals(true, knifeCollectible instanceof MeleeWeapon);

        FiringController firingController = new FiringController((MeleeWeapon) knifeCollectible);
        assertEquals(true, firingController instanceof FiringController);
    }

    @org.junit.jupiter.api.Test
    public void testCreateFiringControllerForRanged() {
        Collectible gunCollectible = weaponFactory.create(Collectible.Type.MAIN_HAND,
                "shotgun");
        assertEquals(true, gunCollectible instanceof RangedWeapon);

        FiringController firingController = new FiringController((RangedWeapon) gunCollectible, new ProjectileConfig());
        assertEquals(true, firingController instanceof FiringController);

        firingController.create();
        assertEquals(true, firingController.getProjectileFactory() instanceof ProjectileFactory);


    }

    @org.junit.jupiter.api.Test
    public void testConnectAndDisconnectPlayer() {
        Collectible gunCollectible = weaponFactory.create(Collectible.Type.MAIN_HAND,
                "shotgun");
        assertEquals(true, gunCollectible instanceof RangedWeapon);

        FiringController firingController = new FiringController((RangedWeapon) gunCollectible, new ProjectileConfig());
        assertEquals(true, firingController instanceof FiringController);

        Entity player = new Entity();
        player.setPosition(new Vector2(0, 0));
        firingController.connectPlayer(player);
        assertEquals(player, firingController.getPlayer());
        assertEquals(PhysicsLayer.NPC, firingController.getTargetLayer());

        firingController.disconnectPlayer();
        assertEquals(null, firingController.getPlayer());
    }

    @org.junit.jupiter.api.Test
    public void testActivateRanged() {
        Collectible gunCollectible = weaponFactory.create(Collectible.Type.MAIN_HAND,
                "shotgun");
        assertEquals(true, gunCollectible instanceof RangedWeapon);

        FiringController firingController = new FiringController((RangedWeapon) gunCollectible, new ProjectileConfig());
        assertEquals(true, firingController instanceof FiringController);

        firingController.create();
        assertEquals(true, firingController.getProjectileFactory() instanceof ProjectileFactory);

        Entity player = new Entity();
        player.setPosition(new Vector2(0, 0));
        firingController.connectPlayer(player);
        assertEquals(player, firingController.getPlayer());
        assertEquals(PhysicsLayer.NPC, firingController.getTargetLayer());

        String attackWith = firingController.activate(new Vector2(0, 0));
        assertEquals("Ranged weapon attack triggered", attackWith);

        attackWith = firingController.activate(null);
        assertEquals("No direction specified for ranged weapon", attackWith);
    }

    @org.junit.jupiter.api.Test
    public void testActivateMelee() {
        Collectible knifeCollectible = weaponFactory.create(Collectible.Type.OFF_HAND, "knife");
        assertEquals(true, knifeCollectible instanceof MeleeWeapon);

        FiringController firingController = new FiringController((MeleeWeapon) knifeCollectible);
        assertEquals(true, firingController instanceof FiringController);

        firingController.create();
        assertEquals(true, firingController.getProjectileFactory() instanceof ProjectileFactory);

        String attackWith = firingController.activate(null);
        assertEquals("Melee weapon attack triggered", attackWith);

        attackWith = firingController.activate(new Vector2(0, 0));
        assertEquals("Melee weapon attack triggered", attackWith);
    }

    @org.junit.jupiter.api.Test
    public void testSetterAndGetterForMelee() {
        Collectible knifeCollectible = weaponFactory.create(Collectible.Type.OFF_HAND, "knife");
        assertEquals(true, knifeCollectible instanceof MeleeWeapon);

        FiringController firingController = new FiringController((MeleeWeapon) knifeCollectible);
        assertEquals(true, firingController instanceof FiringController);

        assertEquals(firingController.getDamage(), ((MeleeWeapon) knifeCollectible).getDamage());
        firingController.setDamage(20);
        assertEquals(firingController.getDamage(), 20);

        Assertions.assertEquals(firingController.getRange(), ((MeleeWeapon) knifeCollectible).getRange());
        firingController.setRange(20);
        Assertions.assertEquals(firingController.getRange(), 20);

        Assertions.assertEquals(firingController.getFireRate(), ((MeleeWeapon) knifeCollectible).getFireRate());
        firingController.setFireRate(20);
        Assertions.assertEquals(firingController.getFireRate(), 20);

        Assertions.assertEquals(firingController.getReloadTime(), 0);
        firingController.setReloadTime(20);
        Assertions.assertEquals(firingController.getReloadTime(), 0);

        Assertions.assertEquals(firingController.getMaxAmmo(), 0);
        firingController.setMaxAmmo(20);
        Assertions.assertEquals(firingController.getMaxAmmo(), 0);

        Assertions.assertEquals(firingController.getAmmo(), 0);
        firingController.setAmmo(20);
        Assertions.assertEquals(firingController.getAmmo(), 0);

        Assertions.assertEquals(firingController.getWeaponSprite(), null);
        firingController.setWeaponSprite(new Sprite(new Texture("images/Weapons/knife.png")));
        Assertions.assertNotEquals(firingController.getWeaponSprite(), null);
    }

    @org.junit.jupiter.api.Test
    public void testSetterAndGetterForRanged() {
        Collectible gunCollectible = weaponFactory.create(Collectible.Type.MAIN_HAND,
                "shotgun");
        assertEquals(true, gunCollectible instanceof RangedWeapon);

        FiringController firingController = new FiringController((RangedWeapon) gunCollectible, new ProjectileConfig());
        assertEquals(true, firingController instanceof FiringController);

        assertEquals(firingController.getDamage(), ((RangedWeapon) gunCollectible).getDamage());
        firingController.setDamage(20);
        assertEquals(firingController.getDamage(), 20);

        Assertions.assertEquals(firingController.getRange(), ((RangedWeapon) gunCollectible).getRange());
        firingController.setRange(20);
        Assertions.assertEquals(firingController.getRange(), 20);

        Assertions.assertEquals(firingController.getFireRate(), ((RangedWeapon) gunCollectible).getFireRate());
        firingController.setFireRate(20);
        Assertions.assertEquals(firingController.getFireRate(), 20);

        Assertions.assertEquals(firingController.getReloadTime(), ((RangedWeapon) gunCollectible).getReloadTime());
        firingController.setReloadTime(20);
        Assertions.assertEquals(firingController.getReloadTime(), 20);

        Assertions.assertEquals(firingController.getMaxAmmo(), ((RangedWeapon) gunCollectible).getMaxAmmo());
        firingController.setMaxAmmo(20);
        Assertions.assertEquals(firingController.getMaxAmmo(), 20);

        Assertions.assertEquals(firingController.getAmmo(), ((RangedWeapon) gunCollectible).getAmmo());
        firingController.setAmmo(20);
        Assertions.assertEquals(firingController.getAmmo(), 20);

        Assertions.assertEquals(firingController.getWeaponSprite(), null);
        firingController.setWeaponSprite(new Sprite(new Texture("images/Weapons/shotgun.png")));
        Assertions.assertNotEquals(firingController.getWeaponSprite(), null);
    }
}
