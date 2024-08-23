package com.csse3200.game.entities;

import com.csse3200.game.components.player.WeaponComponent;
import com.csse3200.game.components.player.inventory.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.MeleeWeapon;
import com.csse3200.game.components.player.inventory.RangedWeapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayerTest {

    private Entity player;
    private InventoryComponent inventoryComponent;
    private WeaponComponent weaponComponent;

    @BeforeEach
    public void setUp() {
        // Create a player entity using the PlayerFactory
        player = PlayerFactory.createPlayer();
        inventoryComponent = player.getComponent(InventoryComponent.class);
        weaponComponent = player.getComponent(WeaponComponent.class);
    }
}
