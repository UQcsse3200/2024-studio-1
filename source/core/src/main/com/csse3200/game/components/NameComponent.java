package com.csse3200.game.components;

import java.util.function.Supplier;

/**
 * A component to give an entity a name, this is useful for listing out all spawned entities.
 */
public class NameComponent extends Component {
    private final Supplier<String> nameGetter;

    /**
     * Construct a new name component.
     *
     * @param nameGetter the supplier of this entity's name.
     */
    public NameComponent(Supplier<String> nameGetter) {
        super();
        this.nameGetter = nameGetter;
    }

    /**
     * Construct a new name component.
     *
     * @param name the name this component endows on an entity.
     */
    public NameComponent(String name) {
        this(() -> name);
    }

    /**
     * Get this entity's name.
     *
     * @return the name.
     */
    public String getName() {
        return nameGetter.get();
    }
}
