package com.csse3200.game.components.achievementsscreen;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.csse3200.game.files.FileLoader.Location.INTERNAL;
import static com.csse3200.game.services.ServiceLocator.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class AchievementsListComponentTest {

    private Entity entity;

    @BeforeEach
    void setUp() {
        entity = new Entity();
        registerResourceService(new ResourceService());
        registerEntityService(new EntityService());
    }

    @AfterEach
    void tearDown() {
        getResourceService().dispose();
        getEntityService().dispose();
        ServiceLocator.clear();
    }

    private AchievementsListComponent setupEntity(String path) {
        AchievementsListComponent component = new AchievementsListComponent(path, INTERNAL);
        entity.addComponent(component);
        getEntityService().register(entity);
        return component;
    }

    @Test
    void oneAchievement() {
        AchievementsListComponent component = setupEntity("test/files/test_achievements_1.json");
        assertEquals(1, component.getNumAchievements());
    }

    @Test
    void twoAchievements() {
        AchievementsListComponent component = setupEntity("test/files/test_achievements_2.json");
        assertEquals(2, component.getNumAchievements());
    }

    @Test
    void noAchievements() {
        AchievementsListComponent component = setupEntity("test/files/nonexistent.json");
        assertEquals(0, component.getNumAchievements());
    }
}