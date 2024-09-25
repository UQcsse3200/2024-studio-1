package com.csse3200.game.areas;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.CombatStatsComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EnemyRoom extends BaseRoom {
    private static final Logger logger = LoggerFactory.getLogger(EnemyRoom.class);
    protected final NPCFactory npcFactory;
    protected final int animalGroup;
    protected List<List<String>> animalSpecifications;
    protected boolean isBossRoom = false;

    public EnemyRoom(
            NPCFactory npcFactory,
            CollectibleFactory collectibleFactory,
            TerrainFactory terrainFactory,
            List<String> roomConnections,
            String specification) {
        super(terrainFactory, collectibleFactory, roomConnections, specification);
        this.npcFactory = npcFactory;
        this.animalSpecifications = getAnimalSpecifications();

        List<String> split = List.of(specification.split(","));
        this.animalGroup = Integer.parseInt(split.get(4));
    }

    protected abstract List<List<String>> getAnimalSpecifications();

    protected void createEnemyEntities(List<String> animals, Entity player) {
        for (String animal : animals) {
            Entity enemy = npcFactory.create(animal, player);
            entities.add(enemy);
        }
    }

    protected void makeAllAnimalDead() {
        for (Entity entity : entities) {
            CombatStatsComponent combatStatsComponent = entity.getComponent(CombatStatsComponent.class);
            if (combatStatsComponent != null) {
                combatStatsComponent.setHealth(0);
                combatStatsComponent.hit(combatStatsComponent);
            }
        }
    }

    public boolean isAllAnimalDead() {
        boolean anyAlive = false;
        for (Entity entity : entities) {
            CombatStatsComponent combatStatsComponent = entity.getComponent(CombatStatsComponent.class);
            if (combatStatsComponent != null && !combatStatsComponent.isDead()) {
                anyAlive = true;
                break;
            }
        }
        if (!anyAlive) {
            this.isRoomCompleted = true;
        }
        return !anyAlive;
    }

    private void spawnItems() {
        MainGameArea area = ServiceLocator.getGameAreaService().getGameArea();
        List<String> itemGroup = this.itemSpecifications.get(this.itemGroup);
        if (itemGroup != null && itemGroup.size() >= 2) {
            spawnItem(area, itemGroup.get(0), new GridPoint2(8, 8));
            spawnItem(area, itemGroup.get(1), new GridPoint2(6, 8));
        }
    }

    protected void spawnAnimals(MainGameArea area, Entity player, GridPoint2 min, GridPoint2 max) {
        List<String> animalGroup = this.animalSpecifications.get(this.animalGroup);
        if (animalGroup != null) {
            createEnemyEntities(animalGroup, player);
            for (Entity enemy : entities) {
                if (enemy.getComponent(CombatStatsComponent.class) != null) {
                    GridPoint2 randomPos = new GridPoint2(
                        ServiceLocator.getRandomService().getRandomNumberGenerator(this.getClass()).getRandomInt(min.x, max.x + 1),
                        ServiceLocator.getRandomService().getRandomNumberGenerator(this.getClass()).getRandomInt(min.y, max.y + 1)
                    );
                    spawnAnimalEntity(area, enemy, randomPos);
                }
            }
        }
    }

    @Override
    public void spawn(Entity player, MainGameArea area) {
        super.spawn(player, area);

        if (!isRoomCompleted) {
            logger.info("spawning enemies");
            this.spawnAnimals(area, player, this.minGridPoint, this.maxGridPoint);

            logger.info("spawning items");
            this.spawnItems();
        }
    }

    @Override
    public void removeRoom() {
        super.removeRoom();
    }
}