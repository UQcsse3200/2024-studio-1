package com.csse3200.game.areas;

import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.StairFactory;

import java.util.List;

public class MainRoom extends BaseRoom {
    @Override
    protected void initializeSpecifications() {
    private final String specification;
    List<List<String>> animalSpecifications = List.of(
            List.of("Rat", "Dog", "Minotaur", "Dino", "Bear", "Snake", "Bat","Werewolf"),
            List.of("Bear", "Snake", "Dino"),
            List.of("Bear", "Bear", "Minotaur"),
            List.of("Snake", "Bat", "Minotaur"),
            List.of("Bat", "Bat", "Bat"),
            List.of("Minotaur", "Minotaur", "Minotaur"),
            List.of("Rat", "Bat", "Bear"),
            List.of("Werewolf", "Werewolf", "Bear")
    );

    this.itemSpecifications = List.of(
            List.of("buff:energydrink:High", "item:medkit", "melee:knife", "ranged:shotgun", "item:shieldpotion"),
            List.of("item:bandage", "melee:knife", "ranged:shotgun", "buff:energydrink:Medium", "item:shieldpotion"),
            List.of("ranged:shotgun", "item:medkit", "melee:knife", "item:bandage", "buff:energydrink:High"),
            List.of("item:shieldpotion", "ranged:shotgun", "melee:knife", "item:medkit", "buff:energydrink:Medium"),
            List.of("melee:knife", "item:bandage", "ranged:shotgun", "item:shieldpotion", "item:medkit"),
            List.of("buff:energydrink:Medium", "item:shieldpotion", "ranged:shotgun", "melee:knife", "item:bandage"),
            List.of("item:medkit", "melee:knife", "buff:energydrink:High", "ranged:shotgun", "item:shieldpotion")
    );
    }

    

    public MainRoom(NPCFactory npcFactory,
                    CollectibleFactory collectibleFactory,
                    TerrainFactory terrainFactory,
                    StairFactory stairFactory,
                    List<String> roomConnections,
                    String specification) {
        super(npcFactory, collectibleFactory, terrainFactory, stairFactory, roomConnections, specification);
    }
}
