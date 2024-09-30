
package com.csse3200.game.areas;

import java.util.List;

import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;

public class ShopRoom extends BaseRoom {

    public ShopRoom(NPCFactory npcFactory, CollectibleFactory collectibleFactory, TerrainFactory terrainFactory,
            List<String> roomConnections, String specification, String roomName) {
                super(terrainFactory, collectibleFactory, roomConnections, specification, roomName);
    }

    @Override
    protected List<List<String>> getItemSpecifications() {
        return List.of(
                List.of("item:beartrap:buyable", "buff:heart:buyable"),
                List.of("item:medkit:buyable", "melee:knife:buyable", "buff:energydrink:High:buyable"),
                List.of("item:medkit:buyable", "buff:energydrink:buyable", "ranged:shotgun:buyable", "item:shieldpotion:buyable")
        );
    }

    @Override
    public void spawn(Entity player, MainGameArea area) {
        super.spawn(player, area);
    }


    public void checkIfRoomComplete() {
        setIsRoomComplete();
    }

}
