package com.csse3200.game.areas;

import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;

import java.util.List;

public class MainRoom extends EnemyRoom {
    @Override
    protected List<List<String>> getAnimalSpecifications() {
        return List.of(
                List.of("Rat", "Bear", "Bat", "Dog", "Snake", "Dino", "Minotaur","Dragon"),
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
                List.of(
                        "buff:energydrink:High:mystery", "item:medkit:mystery",
                        "item:targetdummy", "pet:tombstone",
                        "melee:Knife", "ranged:Shotgun",
                        "item:beartrap", "buff:feather",
                        "melee:Axe", "ranged:Shotgun",
                        "ranged:Shotgun", "item:medkit",
                        "item:shieldpotion", "ranged:Shotgun",
                        "melee:Knife", "item:bandage",
                        "buff:energydrink:Medium", "item:shieldpotion",
                        "item:medkit", "melee:Knife",
                        "melee:Knife", "item:bandage",
                        "melee:Knife", "item:medkit",
                        "ranged:Shotgun", "item:shieldpotion",
                        "ranged:Shotgun", "melee:Knife",
                        "buff:energydrink:High", "ranged:Shotgun",
                        "buff:energydrink:High", "buff:energydrink:Medium",
                        "item:medkit", "item:bandage"
                ),
                List.of(
                        "buff:energydrink:High:mystery", "item:medkit:mystery",
                        "item:targetdummy", "pet:tombstone",
                        "melee:Knife", "ranged:Shotgun",
                        "item:beartrap", "buff:feather"
                )

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
