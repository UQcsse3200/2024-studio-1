package com.csse3200.game.components;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NameComponentTest {

    @Test
    void getName() {
        var name = new NameComponent("hello");
        assertEquals("hello", name.getName());
    }

    @Test
    void getBlank() {
        var name = new NameComponent("");
        assertTrue(name.getName().isEmpty());
    }

    @Test
    void getUnchangedName() {
        var name = new NameComponent(() -> "hello");
        assertEquals("hello", name.getName());
    }

    String s = "hello";

    @Test
    void getChangedName() {
        var name = new NameComponent(() -> s);
        assertEquals("hello", name.getName());
        s = "world";
        assertEquals("world", name.getName());
    }
}