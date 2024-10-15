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
                List.of(
                        "buff:energydrink:High:mystery", "item:medkit:mystery",
                        "item:targetdummy", "pet:tombstone",
                        "melee:knife", "ranged:supersoaker",
                        "item:beartrap", "buff:feather",
                        "melee:axe", "ranged:plasmablaster",
                        "ranged:shotgun", "item:medkit",
                        "item:shieldpotion", "ranged:fnscar",
                        "melee:knife", "item:bandage",
                        "buff:energydrink:Medium", "item:shieldpotion",
                        "item:medkit", "melee:knife",
                        "melee:axe", "item:bandage",
                        "melee:knife", "item:medkit",
                        "ranged:shotgun", "item:shieldpotion",
                        "ranged:fnscar", "melee:knife",
                        "buff:energydrink:High", "ranged:plasmablaster",
                        "buff:energydrink:High", "buff:energydrink:Medium",
                        "item:medkit", "item:bandage"
                ),
                List.of(
                        "buff:energydrink:High:mystery", "item:medkit:mystery",
                        "item:targetdummy", "pet:tombstone",
                        "melee:knife", "ranged:shotgun",
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
