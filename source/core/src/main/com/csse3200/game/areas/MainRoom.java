package com.csse3200.game.areas;

import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;

import java.util.List;

public class MainRoom extends EnemyRoom {
    @Override protected List<List<String>> getAnimalSpecifications() {
        return List.of(
        //1 point = Bats + rats
        //2 points = Dino Dog Snake 
        //3 points Bear Dragon Minotaur 
        //Each room is a total of 6 points maximum
                //1+1+1+1+1+1
                List.of("Rat","Bat","Rat","Bat","Rat","Bat","Dog"),
                List.of("Rat","Rat","Rat","Rat","Rat","Rat"),
                List.of("Bat","Bat","Bat","Bat","Bat","Bat"),
                List.of("Bat","Bat","Bat","Bat","Bat","Bat"),

                //2+2+2
                List.of("Snake", "Dog", "Dino"),
                //2+2+1+1 
                List.of("Snake", "Dog", "Rat", "Bat"),
                List.of("Snake", "Dog", "Bat", "Bat"),
                List.of("Snake", "Dog", "Rat", "Rat"),

                List.of("Snake", "Dino", "Rat", "Bat"),
                List.of("Snake", "Dino", "Bat", "Bat"),
                List.of("Snake", "Dino", "Rat", "Rat"),

                List.of("Dog", "Dino", "Rat", "Bat"),
                List.of("Dog", "Dino", "Bat", "Bat"),
                List.of("Dog", "Dino", "Rat", "Rat"),

                //3 + 3  
                List.of("Bear", "Dragon"),
                List.of("Bear", "Minotaur"),
                List.of("Minotaur", "Dragon"),

                //3 + 2 + 1 
                List.of("Minotaur", "Dog", "Rat"),
                List.of("Minotaur", "Dog", "Bat"),
                List.of("Minotaur", "Snake", "Bat"),
                List.of("Minotaur", "Snake", "Rat"),
                List.of("Minotaur", "Dino", "Bat"),
                List.of("Minotaur", "Dino", "Rat"),

                List.of("Bear", "Dog", "Rat"),
                List.of("Bear", "Dog", "Bat"),
                List.of("Bear", "Snake", "Bat"),
                List.of("Bear", "Snake", "Rat"),
                List.of("Bear", "Dino", "Bat"),
                List.of("Bear", "Dino", "Rat"),

                List.of("Dragon", "Dog", "Rat"),
                List.of("Dragon", "Dog", "Bat"),
                List.of("Dragon", "Snake", "Bat"),
                List.of("Dragon", "Snake", "Rat"),
                List.of("Dragon", "Dino", "Bat"),
                List.of("Dragon", "Dino", "Rat"),

                //3 + 1 + 1 + 1
                List.of("Dragon", "Rat", "Rat", "Rat"),
                List.of("Dragon", "Bat", "Bat", "Bat"),
                List.of("Dragon", "Bat", "Rat", "Bat"),

                List.of("Bear", "Rat", "Rat", "Rat"),
                List.of("Bear", "Bat", "Bat", "Bat"),
                List.of("Bear", "Bat", "Rat", "Bat"),

                List.of("Minotaur", "Rat", "Rat", "Rat"),
                List.of("Minotaur", "Bat", "Bat", "Bat"),
                List.of("Minotaur", "Bat", "Rat", "Bat") 
        );
    }

    @Override
    protected List<List<String>> getItemSpecifications() {
        return List.of(
                List.of("pet:tombstone", "item:medkit:mystery"),
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
