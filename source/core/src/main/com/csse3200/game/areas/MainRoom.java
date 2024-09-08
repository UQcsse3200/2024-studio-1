package com.csse3200.game.areas;

import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;

import java.util.List;

public class MainRoom extends BaseRoom {

    @Override
    protected void initializeSpecifications() {
    this.animalSpecifications = List.of(
            List.of("Rat", "Dog", "Minotaur", "Dino", "Bear", "Snake", "Bat"),
            List.of("Bear", "Snake", "Dino"),
            List.of("Bear", "Bear", "Minotaur"),
            List.of("Snake", "Bat", "Minotaur"),
            List.of("Bat", "Bat", "Bat"),
            List.of("Minotaur", "Minotaur", "Minotaur"),
            List.of("Rat", "Bat", "Bear")
    );
    this.itemSpecifications = List.of(
            List.of("buff:energydrink", "item:medkit", "melee:knife", "ranged:shotgun", "item:shieldpotion"),
            List.of("item:bandage", "melee:knife", "ranged:shotgun", "buff:energydrink", "item:shieldpotion"),
            List.of("ranged:shotgun", "item:medkit", "melee:knife", "item:bandage", "buff:energydrink"),
            List.of("item:shieldpotion", "ranged:shotgun", "melee:knife", "item:medkit", "buff:energydrink"),
            List.of("melee:knife", "item:bandage", "ranged:shotgun", "item:shieldpotion", "item:medkit"),
            List.of("buff:energydrink", "item:shieldpotion", "ranged:shotgun", "melee:knife", "item:bandage"),
            List.of("item:medkit", "melee:knife", "buff:energydrink", "ranged:shotgun", "item:shieldpotion")
    );
    }

    

    public MainRoom(NPCFactory npcFactory,
                    CollectibleFactory collectibleFactory,
                    TerrainFactory terrainFactory,
                    List<String> roomConnections,
                    String specification) {
        super(npcFactory, collectibleFactory, terrainFactory, roomConnections, specification);
    }
}
