package com.csse3200.game.areas;

import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;

import java.util.List;

public class MainRoom extends EnemyRoom {
    @Override
    protected List<List<String>> getAnimalSpecifications() {
        return List.of(
                List.of("Snake", "Bear"),
                List.of("Bear", "Snake", "Dino", "Dragon"),
                List.of("Bear", "Bear", "Minotaur"),
                List.of("Snake", "Bat", "Minotaur"),
                List.of("Bat", "Bat", "Bat"),
                List.of("Minotaur", "Minotaur", "Minotaur"),
                List.of("Rat", "Bat", "Bear"),
                List.of("Werewolf", "Werewolf", "Bear")
        );
    }

    @Override
    protected List<List<String>> getItemSpecifications() {
        return List.of(
                List.of("buff:energydrink:High:mystery", "item:medkit:mystery"),
                List.of("item:targetdummy", "pet:tombstone"),
                List.of("melee:knife", "ranged:supersoaker"),
                List.of("item:beartrap", "buff:feather"),
                List.of("melee:axe", "ranged:plasmablaster"),
                List.of("ranged:shotgun", "item:medkit"),
                List.of("item:shieldpotion", "ranged:fnscar"),
                List.of("melee:knife", "item:bandage"),
                List.of("buff:energydrink:Medium", "item:shieldpotion"),
                List.of("item:medkit", "melee:knife"),
                List.of("melee:axe", "item:bandage"),
                List.of("melee:knife", "item:medkit"),
                List.of("ranged:shotgun", "item:shieldpotion"),
                List.of("ranged:fnscar", "melee:knife"),
                List.of("buff:energydrink:High", "ranged:plasmablaster"),
                List.of("buff:energydrink:High", "buff:energydrink:Medium"),
                List.of("item:medkit", "item:bandage")
        );
    }

    public MainRoom(NPCFactory npcFactory,
                    CollectibleFactory collectibleFactory,
                    TerrainFactory terrainFactory,
                    List<String> roomConnections,
                    String specification,
                    String roomName) {
        super(npcFactory, collectibleFactory, terrainFactory, roomConnections, specification, roomName);
    }
}
