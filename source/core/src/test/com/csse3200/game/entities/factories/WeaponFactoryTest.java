package com.csse3200.game.entities.factories;

import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.weapons.MeleeWeapon;
import com.csse3200.game.components.player.inventory.weapons.RangedWeapon;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class WeaponFactoryTest {

    WeaponFactory weaponFactory;  // Use actual implementation

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

        // Load necessary resources
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextureAtlases(new String[]{new ProjectileConfig().projectileAtlasPath});
        while (!resourceService.loadForMillis(1000)) {
            // Wait for assets to load.
        }
        weaponFactory = new WeaponFactory();
    }

    @Test
    public void testCreateCollectibleMelee() {
        Collectible knifeCollectible = weaponFactory.create(Collectible.Type.OFF_HAND, "knife");
        assert knifeCollectible instanceof MeleeWeapon;
    }

    @Test
    public void testCreateCollectibleRanged() {
        Collectible shotgunCollectible = weaponFactory.create(Collectible.Type.MAIN_HAND, "shotgun");
        assert shotgunCollectible instanceof RangedWeapon;
    }

    @Test
    public void testCreateCollectibleInvalid() {
        try {
            weaponFactory.create(Collectible.Type.OFF_HAND, "invalid");
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid melee weapon specification: invalid");
        }

        try {
            weaponFactory.create(Collectible.Type.MAIN_HAND, "invalid");
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid ranged weapon specification: invalid");
        }
    }

    @Test
    public void testCreateEntity() {
        Entity meleeEntity = weaponFactory.createWeaponEntity(weaponFactory.create(Collectible.Type.OFF_HAND, "knife"));
        assert meleeEntity != null;

        Entity rangedEntity = weaponFactory.createWeaponEntity(weaponFactory.create(Collectible.Type.MAIN_HAND, "shotgun"));
        assert rangedEntity != null;
    }

    @Test
    public void testCreateEntityInvalid() {
        try {
            weaponFactory.createWeaponEntity(weaponFactory.create(Collectible.Type.OFF_HAND, "invalid"));
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid melee weapon specification: invalid");
        }

        try {
            weaponFactory.createWeaponEntity(weaponFactory.create(Collectible.Type.MAIN_HAND, "invalid"));
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid ranged weapon specification: invalid");
        }
    }

    @Test
    public void testCreateEntityInvalidCollectible() {
        try {
            weaponFactory.createWeaponEntity(null);
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid collectible");
        }
    }

    @Test
    public void testCreateEntityInvalidCollectibleType() {
        try {
            Collectible invalidCollectible = mock(Collectible.class);
            weaponFactory.createWeaponEntity(invalidCollectible);
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid collectible");
        }
    }
}
