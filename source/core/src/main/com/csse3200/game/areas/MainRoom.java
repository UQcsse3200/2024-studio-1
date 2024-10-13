package com.csse3200.game.areas;

import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;

import java.util.List;

public class MainRoom extends EnemyRoom {
    @Override
    protected List<List<String>> getAnimalSpecifications() {
        return List.of(
                List.of("Dog","Snake"),
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
//                List.of("buff:energydrink:High:mystery", "item:medkit:mystery"),
                List.of("buff:fang", "buff:tombstone"),
                List.of("buff:tombstone", "item:beartrap"),
                List.of("melee:Knife", "ranged:Shotgun"),
                List.of("item:beartrap", "buff:feather"),
                List.of("melee:Axe", "ranged:Shotgun"),
                List.of("ranged:Shotgun", "item:medkit", "melee:Knife", "item:bandage", "buff:energydrink:High"),
                List.of("item:shieldpotion", "ranged:Shotgun", "melee:Knife", "item:medkit", "buff:energydrink:Medium"),
                List.of("melee:Knife", "item:bandage", "ranged:Shotgun", "item:shieldpotion", "item:medkit"),
                List.of("buff:energydrink:Medium", "item:shieldpotion", "ranged:Shotgun", "melee:Knife", "item:bandage"),
                List.of("item:medkit", "melee:Knife", "buff:energydrink:High", "ranged:Shotgun", "item:shieldpotion")
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
