package com.csse3200.game.areas;

import java.util.List;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;

public class GambleRoom extends BaseRoom {
    
    public GambleRoom(NPCFactory npcFactory, CollectibleFactory collectibleFactory, TerrainFactory terrainFactory,
            List<String> roomConnections, String specification, String roomName) {
        super(terrainFactory, collectibleFactory, roomConnections, specification, roomName);
    }

    @Override
    protected List<List<String>> getItemSpecifications() {
        return List.of(
                List.of("item:targetdummy", "item:reroll")  
        );
    }

    private void spawnItems() {
        GameArea area = ServiceLocator.getGameAreaService().getGameArea();
        List<String> items = this.itemSpecifications.get(0);
        if (items != null) {
            spawnItem(area, items.get(0), new GridPoint2(7, 8));
        }
    }

    @Override
    public void spawn(Entity player, GameArea area) {
        super.spawn(player, area);
        this.spawnItems();
        this.checkIfRoomComplete();
    }

    public void checkIfRoomComplete() {
        setIsRoomComplete();
    }
}

