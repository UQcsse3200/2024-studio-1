package com.csse3200.game.areas;

import com.csse3200.game.entities.Entity;

import java.util.List;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;

public class BossRoomSpawner extends BaseRoomSpawner{
    List<List<String>> animalSpecifications = List.of(
            List.of("Rat"),
            List.of("Bear"),
            List.of("Bear")
    );

    List<List<String>> itemSpecifications = List.of(
            List.of("buff:energydrink", "item:medkit", "melee:knife", "ranged:shotgun", "item:shieldpotion"),
            List.of("item:bandage", "melee:knife", "ranged:shotgun", "buff:energydrink", "item:shieldpotion"),
            List.of("ranged:shotgun", "item:medkit", "melee:knife", "item:bandage", "buff:energydrink")
    );

    private static final float WALL_THICKNESS = 0.15f;
    public BossRoomSpawner (GameArea gameArea, NPCFactory npcFactory, CollectibleFactory collectibleFactory,  TerrainFactory terrainFactory){
        super(gameArea, npcFactory, collectibleFactory, terrainFactory);
    }

    @Override
    public void spawnRoom(Entity player, String specification) {

    }
}
