
package com.csse3200.game.areas;

import java.util.List;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.services.ServiceLocator;

public class ShopRoom extends BaseRoom {

    public ShopRoom(NPCFactory npcFactory, CollectibleFactory collectibleFactory, TerrainFactory terrainFactory,
            List<String> roomConnections, String specification, String roomName) {
                super(terrainFactory, collectibleFactory, roomConnections, specification, roomName);
    }

    @Override
    protected List<List<String>> getItemSpecifications() {
        return List.of(
                List.of("item:medkit:buyable", "buff:heart:buybale"),
                List.of("item:medkit:buyable", "item:medkit:buybale"),
                List.of("item:medkit:buyable", "item:medkit:buybale")
        );
    }

    @Override
    public void spawn(Entity player, MainGameArea area) {
        super.spawn(player, area);
        spawnItems(area);
        setIsRoomComplete();
    }

    private void spawnItems(MainGameArea area) {
        List<String> itemGroup = this.itemSpecifications.get(this.itemGroup);
        if (itemGroup != null && itemGroup.size() >= 2) {
            spawnItem(area, itemGroup.get(0), new GridPoint2(8, 8));
            spawnItem(area, itemGroup.get(1), new GridPoint2(6, 8));
        }
    }


    public void checkIfRoomComplete() {
        setIsRoomComplete();
    }

}
