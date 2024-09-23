package com.csse3200.game.entities;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.csse3200.game.components.NameComponent;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashSet;
import java.util.Set;

@ExtendWith(GameExtension.class)
class EntityServiceTest {
  @Test
  void shouldCreateEntity() {
    EntityService entityService = new EntityService();
    Entity entity = spy(Entity.class);
    entityService.register(entity);
    verify(entity).create();
  }

  @Test
  void shouldUpdateEntities() {
    EntityService entityService = new EntityService();
    Entity entity = spy(Entity.class);
    entityService.register(entity);
    entityService.update();

    verify(entity).update();
    verify(entity).earlyUpdate();
  }

  @Test
  void shouldNotUpdateUnregisteredEntities() {
    EntityService entityService = new EntityService();
    Entity entity = spy(Entity.class);
    entityService.register(entity);
    entityService.unregister(entity);
    entityService.update();
    verify(entity, times(0)).update();
    verify(entity, times(0)).earlyUpdate();
  }

  @Test
  void shouldDisposeEntities() {
    EntityService entityService = new EntityService();
    Entity entity = mock(Entity.class);
    entityService.register(entity);
    entityService.dispose();
    verify(entity).dispose();
  }

  @Test
  void testSingleEntityNames(){
    EntityService entityService = new EntityService();
    entityService.register(new Entity().addComponent(new NameComponent("alice")));
    Assertions.assertEquals(Set.of("alice"), new HashSet<>(entityService.getEntityNames()));
  }

  @Test
  void testMultipleEntityNames(){
    EntityService entityService = new EntityService();
    entityService.register(new Entity().addComponent(new NameComponent("alice")));
    entityService.register(new Entity().addComponent(new NameComponent("bob")));
    Assertions.assertEquals(Set.of("alice", "bob"), new HashSet<>(entityService.getEntityNames()));
  }

  @Test
  void testSingleUnnamedEntities(){
    EntityService entityService = new EntityService();
    Entity entity = new Entity();
    entityService.register(entity);
    Assertions.assertEquals(Set.of("Unknown Entity: " + entity),
            new HashSet<>(entityService.getEntityNames()));
  }

  @Test
  void testMultipleUnnamedEntities(){
    EntityService entityService = new EntityService();
    Entity entity1 = new Entity();
    entityService.register(entity1);

    Entity entity2 = new Entity();
    entityService.register(entity2);

    Assertions.assertEquals(Set.of("Unknown Entity: " + entity1, "Unknown Entity: " + entity2),
            new HashSet<>(entityService.getEntityNames()));
  }

  @Test
  void testMixedNamedUnnamedEntities(){
    EntityService entityService = new EntityService();
    Entity entity1 = new Entity().addComponent(new NameComponent("alice"));
    entityService.register(entity1);

    Entity entity2 = new Entity();
    entityService.register(entity2);

    Assertions.assertEquals(Set.of("alice", "Unknown Entity: " + entity2),
            new HashSet<>(entityService.getEntityNames()));
  }
}