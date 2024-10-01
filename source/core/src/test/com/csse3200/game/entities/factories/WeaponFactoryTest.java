package com.csse3200.game.entities.factories;

import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.MeleeWeapon;
import com.csse3200.game.components.player.inventory.RangedWeapon;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.configs.ProjectileConfig;
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

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class WeaponFactoryTest {

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
        resourceService.loadTextureAtlases(new String []{new ProjectileConfig().projectileAtlasPath});
        while (!resourceService.loadForMillis(1000)) {
            // wait for assets to load.
        }
        weaponFactory = new WeaponFactory();
    }

    @org.junit.jupiter.api.Test
    public void testCreateCollectibleMelee() {

        Collectible knifeCollectible = weaponFactory.create(Collectible.Type.MELEE_WEAPON, "knife");

        assert knifeCollectible instanceof MeleeWeapon;
    }

    @org.junit.jupiter.api.Test
    public void testCreateCollectibleRanged() {

        Collectible shotgunCollectible = weaponFactory.create(Collectible.Type.RANGED_WEAPON, "shotgun");

        assert shotgunCollectible instanceof RangedWeapon;
    }

    @org.junit.jupiter.api.Test
    public void testCreateCollectibleInvalid() {
        // Test for melee weapon
        try {
            Collectible invalidCollectible = weaponFactory.create(Collectible.Type.MELEE_WEAPON, "invalid");
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid melee weapon specification: invalid");
        }

        // Test for ranged weapon
        try {
            Collectible invalidCollectible = weaponFactory.create(Collectible.Type.RANGED_WEAPON, "invalid");
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid ranged weapon specification: invalid");
        }
    }

    @org.junit.jupiter.api.Test
    public void testCreateEntity() {
        Entity entity = weaponFactory.createWeaponEntity(weaponFactory.create(Collectible.Type.MELEE_WEAPON, "knife"));
        assert entity != null;
        assert entity instanceof Entity;

        Entity entity2 = weaponFactory.createWeaponEntity(weaponFactory.create(Collectible.Type.RANGED_WEAPON, "shotgun"));
        assert entity2 != null;
        assert entity2 instanceof Entity;
    }

    @org.junit.jupiter.api.Test
    public void testCreateEntityInvalid() {
        try {
            Entity entity = weaponFactory.createWeaponEntity(weaponFactory.create(Collectible.Type.MELEE_WEAPON, "invalid"));
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid melee weapon specification: invalid");
        }

        try {
            Entity entity = weaponFactory.createWeaponEntity(weaponFactory.create(Collectible.Type.RANGED_WEAPON, "invalid"));
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid ranged weapon specification: invalid");
        }
    }

    @org.junit.jupiter.api.Test
    public void testCreateEntityInvalidCollectible() {
        try {
            Entity entity = weaponFactory.createWeaponEntity(null);
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid collectible");
        }
    }
}
