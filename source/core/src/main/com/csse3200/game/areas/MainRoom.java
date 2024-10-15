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
                        "buff:energydrink:high:mystery", "item:medkit:mystery",
                        "item:targetdummy", "buff:heart",
                        "melee:knife", "ranged:supersoaker",
                        "melee:axe", "ranged:plasmablaster",
                        "ranged:shotgun", "item:medkit",
                        "item:shieldpotion", "ranged:fnscar", "buff:heart",
                        "melee:knife", "item:bandage",
                        "buff:energydrink:medium", "item:shieldpotion",
                        "item:medkit", "melee:knife",
                        "melee:axe", "item:bandage",
                        "melee:knife", "item:medkit", "buff:energydrink:low",
                        "ranged:shotgun", "item:shieldpotion",
                        "ranged:fnscar", "melee:knife",
                        "ranged:plasmablaster", "buff:energydrink:high",
                        "buff:energydrink:medium", "item:medkit", "item:bandage"
                ),
                List.of(
                        "item:beartrap", "buff:fang",
                        "buff:armor", "buff:feather",
                        "item:bigredbutton", "item:beartrap",
                        "buff:fang", "item:beartrap",
                        "item:beartrap", "buff:fang",
                        "item:bandage", "buff:feather"

                ),
                List.of(
                        "pet:tombstone", "buff:heart", "item:teleporter",
                        "item:bandage", "pet:tombstone",
                        "item:reroll", "item:reroll", "pet:tombstone",
                        "item:teleporter", "item:bigredbutton", "pet:tombstone"
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
