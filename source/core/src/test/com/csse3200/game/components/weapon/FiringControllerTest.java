/*
package com.csse3200.game.components.weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.MeleeWeapon;
import com.csse3200.game.components.player.inventory.RangedWeapon;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    }

    @org.junit.jupiter.api.Test
    public void testCreateFiringControllerForMelee() {
        Collectible knifeCollectible = weaponFactory.create(Collectible.Type.MELEE_WEAPON, "knife");
        assert knifeCollectible instanceof MeleeWeapon;

        FiringController firingController = new FiringController((MeleeWeapon) knifeCollectible);
        assert firingController instanceof FiringController;
    }

    @org.junit.jupiter.api.Test
    public void testCreateFiringControllerForRanged() {
        Collectible gunCollectible = weaponFactory.create(Collectible.Type.RANGED_WEAPON, "shotgun");
        assert gunCollectible instanceof RangedWeapon;

        FiringController firingController = new FiringController((RangedWeapon) gunCollectible, new ProjectileConfig());
        assert firingController instanceof FiringController;

        firingController.create();
        assert firingController.getProjectileFactory() instanceof ProjectileFactory;

    }

    @org.junit.jupiter.api.Test
    public void testConnectAndDisconnectPlayer() {
        Collectible gunCollectible = weaponFactory.create(Collectible.Type.RANGED_WEAPON, "shotgun");
        assert gunCollectible instanceof RangedWeapon;

        FiringController firingController = new FiringController((RangedWeapon) gunCollectible, new ProjectileConfig());
        assert firingController instanceof FiringController;

        Entity player = new Entity();
        player.setPosition(new Vector2(0, 0));
        firingController.connectPlayer(player);
        assert firingController.getPlayer() == player;
        assert firingController.getTargetLayer() == PhysicsLayer.NPC;

        firingController.disconnectPlayer();
        assert firingController.getPlayer() == null;
    }

    @org.junit.jupiter.api.Test
    public void testActivateRanged() {
        Collectible gunCollectible = weaponFactory.create(Collectible.Type.RANGED_WEAPON, "shotgun");
        assert gunCollectible instanceof RangedWeapon;

        FiringController firingController = new FiringController((RangedWeapon) gunCollectible, new ProjectileConfig());
        assert firingController instanceof FiringController;

        Entity player = new Entity();
        player.setPosition(new Vector2(0, 0));
        firingController.connectPlayer(player);
        assert firingController.getPlayer() == player;
        assert firingController.getTargetLayer() == PhysicsLayer.NPC;

        String attackWith = firingController.activate(new Vector2(0, 0));
        assert attackWith.equals("Ranged weapon attack triggered");

        attackWith = firingController.activate(null);
        assert attackWith.equals("No direction specified for ranged weapon");
    }

    @org.junit.jupiter.api.Test
    public void testActivateMelee() {
        Collectible knifeCollectible = weaponFactory.create(Collectible.Type.MELEE_WEAPON, "knife");
        assert knifeCollectible instanceof MeleeWeapon;

        FiringController firingController = new FiringController((MeleeWeapon) knifeCollectible);
        assert firingController instanceof FiringController;

        firingController.create();
        assert firingController.getProjectileFactory() instanceof ProjectileFactory;

        String attackWith = firingController.activate(null);
        assert attackWith.equals("Melee weapon attack triggered");
        attackWith = firingController.activate(new Vector2(0, 0));
        assert attackWith.equals("Melee weapon attack triggered");
    }

    @org.junit.jupiter.api.Test
    public void testSetterAndGetterForMelee() {
        Collectible knifeCollectible = weaponFactory.create(Collectible.Type.MELEE_WEAPON, "knife");
        assert knifeCollectible instanceof MeleeWeapon;

        FiringController firingController = new FiringController((MeleeWeapon) knifeCollectible);
        assert firingController instanceof FiringController;

        assert firingController.getDamage() == ((MeleeWeapon) knifeCollectible).getDamage();
        firingController.setDamage(20);
        assert firingController.getDamage() == 20;

        assert firingController.getRange() == ((MeleeWeapon) knifeCollectible).getRange();
        firingController.setRange(20);
        assert firingController.getRange() == 20;

        assert firingController.getFireRate() == ((MeleeWeapon) knifeCollectible).getFireRate();
        firingController.setFireRate(20);
        assert firingController.getFireRate() == 20;

        assert firingController.getReloadTime() == 0; // default value
        firingController.setReloadTime(20);
        assert firingController.getReloadTime() == 0; // reload time is not set for melee weapon

        assert firingController.getMaxAmmo() == 0; // default value
        firingController.setMaxAmmo(20);
        assert firingController.getMaxAmmo() == 0; // ammo is not set for melee weapon

        assert firingController.getAmmo() == 0; // default value
        firingController.setAmmo(20);
        assert firingController.getAmmo() == 0; // ammo is not set for melee weapon

        assert firingController.getWeaponSprite() == null;
        firingController.setWeaponSprite(new Sprite(new Texture("images/Weapons/Knife.png")));
        assert firingController.getWeaponSprite() != null;
    }

    @org.junit.jupiter.api.Test
    public void testSetterAndGetterForRanged() {
        Collectible gunCollectible = weaponFactory.create(Collectible.Type.RANGED_WEAPON, "shotgun");
        assert gunCollectible instanceof RangedWeapon;

        FiringController firingController = new FiringController((RangedWeapon) gunCollectible, new ProjectileConfig());
        assert firingController instanceof FiringController;

        assert firingController.getDamage() == ((RangedWeapon) gunCollectible).getDamage();
        firingController.setDamage(20);
        assert firingController.getDamage() == 20;

        assert firingController.getRange() == ((RangedWeapon) gunCollectible).getRange();
        firingController.setRange(20);
        assert firingController.getRange() == 20;

        assert firingController.getFireRate() == ((RangedWeapon) gunCollectible).getFireRate();
        firingController.setFireRate(20);
        assert firingController.getFireRate() == 20;

        assert firingController.getReloadTime() == ((RangedWeapon) gunCollectible).getReloadTime();
        firingController.setReloadTime(20);
        assert firingController.getReloadTime() == 20;

        assert firingController.getMaxAmmo() == ((RangedWeapon) gunCollectible).getMaxAmmo();
        firingController.setMaxAmmo(20);
        assert firingController.getMaxAmmo() == 20;

        assert firingController.getAmmo() == ((RangedWeapon) gunCollectible).getAmmo();
        firingController.setAmmo(20);
        assert firingController.getAmmo() == 20;

        assert firingController.getWeaponSprite() == null;
        firingController.setWeaponSprite(new Sprite(new Texture("images/Weapons/Shotgun.png")));
        assert firingController.getWeaponSprite() != null;
    }
}

 */
