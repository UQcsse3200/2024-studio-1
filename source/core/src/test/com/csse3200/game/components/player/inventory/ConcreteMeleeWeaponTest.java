package com.csse3200.game.components.player.inventory;

import com.csse3200.game.components.player.inventory.weapons.ConcreteMeleeWeapon;
import com.csse3200.game.components.weapon.FiringController;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class ConcreteMeleeWeaponTest {

    @Mock
    Entity weaponEntity;

    @Mock
    MeleeWeapon meleeWeapon;

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

        meleeWeapon = new ConcreteMeleeWeapon("knife", "knife.png", 10, 10, 10);
        weaponEntity = new Entity().addComponent(new FiringController(meleeWeapon));

    }

    @org.junit.jupiter.api.Test
    public void testCreateCollectibleMelee() {
        ConcreteMeleeWeapon meleeWeapon = new ConcreteMeleeWeapon("knife", "knife.png", 10, 10, 10);

        assert meleeWeapon instanceof MeleeWeapon;
    }

    @org.junit.jupiter.api.Test
    public void testSetWeaponEntity() {
        ConcreteMeleeWeapon meleeWeapon = new ConcreteMeleeWeapon("knife", "knife.png", 10, 10, 10);
        Entity weaponEntity = new Entity();
        meleeWeapon.setWeaponEntity(weaponEntity);

        assert meleeWeapon.getWeaponEntity().equals(weaponEntity);
    }

    @org.junit.jupiter.api.Test
    public void testPickup() {
        ConcreteMeleeWeapon meleeWeapon = new ConcreteMeleeWeapon("knife", "knife.png", 10, 10, 10);
        meleeWeapon.setWeaponEntity(weaponEntity);
        InventoryComponent inventory = new InventoryComponent();

        Entity player = new Entity().addComponent(inventory);
        inventory.pickup(meleeWeapon);
        assert inventory.getMelee().isPresent();
    }

    @org.junit.jupiter.api.Test
    public void testDrop() {
        ConcreteMeleeWeapon meleeWeapon = new ConcreteMeleeWeapon("knife", "knife.png", 10, 10, 10);
        meleeWeapon.setWeaponEntity(weaponEntity);
        InventoryComponent inventory = new InventoryComponent();

        new Entity().addComponent(inventory);
        inventory.pickup(meleeWeapon);
        inventory.drop(meleeWeapon);
        assert inventory.getMelee().isEmpty();
    }

    @org.junit.jupiter.api.Test
    public void testGetSpecification() {
        ConcreteMeleeWeapon meleeWeapon = new ConcreteMeleeWeapon("knife", "knife.png", 10, 10, 10);
        assert meleeWeapon.getSpecification().equals("melee:knife");
    }

    @org.junit.jupiter.api.Test
    public void testGetName() {
        ConcreteMeleeWeapon meleeWeapon = new ConcreteMeleeWeapon("knife", "knife.png", 10, 10, 10);
        assert meleeWeapon.getName().equals("knife");
    }

    @org.junit.jupiter.api.Test
    public void testGetIconPath() {
        ConcreteMeleeWeapon meleeWeapon = new ConcreteMeleeWeapon("knife", "knife.png", 10, 10, 10);
        assert meleeWeapon.iconPath.equals("knife.png");
    }

    @org.junit.jupiter.api.Test
    public void testGetters() {
        ConcreteMeleeWeapon meleeWeapon = new ConcreteMeleeWeapon("knife", "knife.png", 10, 10, 10);
        assertEquals(meleeWeapon.getDamage(), 10);
        assertEquals(meleeWeapon.getRange(), 10);
        assertEquals(meleeWeapon.getFireRate(), 10);
    }

    @org.junit.jupiter.api.Test
    public void testSetters() {
        ConcreteMeleeWeapon meleeWeapon = new ConcreteMeleeWeapon("knife", "knife.png", 10, 10, 10);
        meleeWeapon.setDamage(20);
        meleeWeapon.setRange(20);
        meleeWeapon.setFireRate(20);
        assertEquals(meleeWeapon.getDamage(), 20);
        assertEquals(meleeWeapon.getRange(), 20);
        assertEquals(meleeWeapon.getFireRate(), 20);
    }

    @org.junit.jupiter.api.Test
    public void testAttack() {
        ConcreteMeleeWeapon meleeWeapon = new ConcreteMeleeWeapon("knife", "knife.png", 10, 10, 10);
        meleeWeapon.setWeaponEntity(weaponEntity);
        InventoryComponent inventory = new InventoryComponent();

        new Entity().addComponent(inventory);
        inventory.pickup(meleeWeapon);
        meleeWeapon.attack();
        assert inventory.getMelee().isPresent();
    }
}
