package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.areas.MainGameArea;
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

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class ConcreteRangedWeaponTest {

    @Mock
    Entity weaponEntity;

    @Mock
    RangedWeapon rangedWeapon;

    @Mock
    GameAreaService gameAreaService;

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
        ServiceLocator.registerGameAreaService(gameAreaService);

        //load in the current default texture.
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextureAtlases(new String []{new ProjectileConfig().projectileAtlasPath});
        while (!resourceService.loadForMillis(1000)) {
            // wait for assets to load.
        }

        //create gameArea


        rangedWeapon = new ConcreteRangedWeapon("gun", "gun.png", 10, 10, 10, 10, 10, 10);
        weaponEntity = new Entity().addComponent(new FiringController(rangedWeapon, new ProjectileConfig()));
    }

    @org.junit.jupiter.api.Test
    public void testCreateCollectibleRange() {
        ConcreteRangedWeapon rangeWeapon = new ConcreteRangedWeapon("gun", "gun.png", 10, 10, 10, 10, 10, 10);

        assert rangeWeapon instanceof RangedWeapon;
    }

    @org.junit.jupiter.api.Test
    public void testSetWeaponEntity() {
        ConcreteRangedWeapon rangeWeapon = new ConcreteRangedWeapon("gun", "gun.png", 10, 10, 10, 10, 10, 10);
        Entity weaponEntity = new Entity();
        rangeWeapon.setWeaponEntity(weaponEntity);

        assert rangeWeapon.getWeaponEntity() == weaponEntity;
    }

    @org.junit.jupiter.api.Test
    public void testGetWeaponEntity() {
        ConcreteRangedWeapon rangeWeapon = new ConcreteRangedWeapon("gun", "gun.png", 10, 10, 10, 10, 10, 10);
        Entity weaponEntity = new Entity();
        rangeWeapon.setWeaponEntity(weaponEntity);

        assert rangeWeapon.getWeaponEntity() == weaponEntity;
    }

    @org.junit.jupiter.api.Test
    public void testPickup() {
        InventoryComponent inventoryComponent = new InventoryComponent();
        ConcreteRangedWeapon rangeWeapon = new ConcreteRangedWeapon("gun", "gun.png", 10, 10, 10, 10, 10, 10);
        rangeWeapon.setWeaponEntity(weaponEntity);

        Inventory inventory = new Inventory(inventoryComponent);
        rangeWeapon.pickup(inventory);
        assert inventory.getRanged().isPresent();
    }

    @org.junit.jupiter.api.Test
    public void testDrop() {

        ConcreteRangedWeapon rangeWeapon = new ConcreteRangedWeapon("gun", "gun.png", 10, 10, 10, 10, 10, 10);
        rangeWeapon.setWeaponEntity(weaponEntity);
        InventoryComponent inventoryComponent = new InventoryComponent();

        new Entity().addComponent(inventoryComponent);
        Inventory inventory = new Inventory(inventoryComponent);
        rangeWeapon.pickup(inventory);
        rangeWeapon.drop(inventory);
        assert !inventory.getRanged().isPresent();
    }

    @org.junit.jupiter.api.Test
    public void testGetSpecification() {
        ConcreteRangedWeapon rangeWeapon = new ConcreteRangedWeapon("gun", "gun.png", 10, 10, 10, 10, 10, 10);
        assert rangeWeapon.getSpecification().equals("ranged:gun");
    }

    @org.junit.jupiter.api.Test
    public void testGetName() {
        ConcreteRangedWeapon rangeWeapon = new ConcreteRangedWeapon("gun", "gun.png", 10, 10, 10, 10, 10, 10);
        assert rangeWeapon.getName().equals("gun");
    }

    @org.junit.jupiter.api.Test
    public void testGettersAndSetters() {
        ConcreteRangedWeapon rangeWeapon = new ConcreteRangedWeapon("gun", "gun.png", 10, 10, 10, 10, 10, 10);
        assert rangeWeapon.getDamage() == 10;
        assert rangeWeapon.getRange() == 10;
        assert rangeWeapon.getFireRate() == 10;
        assert rangeWeapon.getReloadTime() == 10;
        assert rangeWeapon.getClipSize() == 10;
        assert rangeWeapon.getAmmo() == 10;

        rangeWeapon.setDamage(20);
        rangeWeapon.setRange(20);
        rangeWeapon.setFireRate(20);
        rangeWeapon.setReloadTime(20);
        rangeWeapon.setClipSize(20);
        rangeWeapon.setAmmo(20);

        assert rangeWeapon.getDamage() == 20;
        assert rangeWeapon.getRange() == 20;
        assert rangeWeapon.getFireRate() == 20;
        assert rangeWeapon.getReloadTime() == 20;
        assert rangeWeapon.getClipSize() == 20;
        assert rangeWeapon.getAmmo() == 20;

    }

    @org.junit.jupiter.api.Test
    public void testShoot() {
        ConcreteRangedWeapon rangeWeapon = new ConcreteRangedWeapon("gun", "gun.png", 10, 10, 10, 10, 10, 10);
        rangeWeapon.setWeaponEntity(weaponEntity);
        InventoryComponent inventoryComponent = new InventoryComponent();

        new Entity().addComponent(inventoryComponent);
        Inventory inventory = new Inventory(inventoryComponent);
        rangeWeapon.pickup(inventory);
        rangeWeapon.shoot(Vector2.Zero);
        assert inventory.getRanged().isPresent();
    }

}
