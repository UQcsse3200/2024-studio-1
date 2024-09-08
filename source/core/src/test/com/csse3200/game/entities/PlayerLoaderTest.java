/*
package com.csse3200.game.entities;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.*;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.InventoryComponent;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.entities.factories.AnimationFactory;
import com.csse3200.game.entities.factories.WeaponFactory;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class PlayerLoaderTest {

    private LoadPlayer loadPlayer;
    private PlayerConfig playerConfig;
    private WeaponFactory weaponFactoryMock;
    private AnimationFactory animationFactoryMock;
    private InventoryComponent inventoryComponentMock;
    private ResourceService resourceServiceMock;

    @BeforeEach
    void setUp() {
        resourceServiceMock = mock(ResourceService.class); // Mocking ResourceService
        weaponFactoryMock = mock(WeaponFactory.class);
        animationFactoryMock = mock(AnimationFactory.class);
        inventoryComponentMock = mock(InventoryComponent.class);

        // Mock the behavior of resource loading (you can specify which resources to mock load)
        doNothing().when(resourceServiceMock).loadTextures(any(String[].class));

        // Pass the mock ResourceService to LoadPlayer (if needed)
        loadPlayer = new LoadPlayer();  // Ensure your constructor accepts ResourceService
        playerConfig = new PlayerConfig();
        playerConfig.health = 100;
        playerConfig.baseAttack = 50;
        playerConfig.textureAtlasFilename = "textures/player.atlas";
        playerConfig.melee = "melee:knife";
        playerConfig.ranged = "ranged:shotgun";
    }


    @Test
    void testCreatePlayer() {
        Entity player = loadPlayer.createPlayer(playerConfig);

        assertNotNull(player.getComponent(PlayerConfigComponent.class));
        assertNotNull(player.getComponent(PhysicsComponent.class));
        assertNotNull(player.getComponent(ColliderComponent.class));
        assertNotNull(player.getComponent(HitboxComponent.class));
        assertNotNull(player.getComponent(PlayerActions.class));
        assertNotNull(player.getComponent(CombatStatsComponent.class));
        assertNotNull(player.getComponent(PlayerHealthDisplay.class));
        assertNotNull(player.getComponent(PlayerStatsDisplay.class));

        assertEquals(100, player.getComponent(CombatStatsComponent.class).getHealth());
        assertEquals(50, player.getComponent(CombatStatsComponent.class).getBaseAttack());
    }

    @Test
    void testAddComponents() {
        Entity player = loadPlayer.addComponents(playerConfig);

        assertNotNull(player.getComponent(InventoryComponent.class));
        assertNotNull(player.getComponent(PlayerActions.class));
    }


    @Test
    void testCreateMelee() {
        Entity player = new Entity();
        loadPlayer.createMelee(playerConfig, player);

        verify(weaponFactoryMock).create(Collectible.Type.MELEE_WEAPON, "knife");
        assertNotNull(player.getComponent(WeaponComponent.class));
    }

    @Test
    void testCreateRanged() {
        Entity player = new Entity();
        loadPlayer.createRanged(playerConfig, player);

        verify(weaponFactoryMock).create(Collectible.Type.RANGED_WEAPON, "shotgun");
        assertNotNull(player.getComponent(WeaponComponent.class));
    }

    @Test
    void testAddWeaponsAndItems() {
        Entity player = new Entity();
        loadPlayer.addWeaponsAndItems(player, playerConfig);

        verify(weaponFactoryMock, times(1)).create(Collectible.Type.MELEE_WEAPON, "knife");
        verify(weaponFactoryMock, times(1)).create(Collectible.Type.RANGED_WEAPON, "shotgun");

    }

}
*/
