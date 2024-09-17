package com.csse3200.game.components;

/**
 * A component to give an entity a name, this is useful for listing out all spawned entities.
 */
public class NameComponent extends Component {
    private final String name;

    /**
     * Construct a new name component.
     *
     * @param name the name this component endows on an entity.
     */
    public NameComponent(String name) {
        super();
        this.name = name;
    }

    /**
     * Get this entity's name.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }
}
