package com.csse3200.game.components.npc;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.configs.NPCConfigs;

/**
 * A component used to store the NPC's behaviour and task configurations.
 */
public class NPCConfigComponent extends Component {

    public final NPCConfigs.NPCConfig config;

    /**
     * Constructs an NPCConfigComponent with the given NPC configuration
     * @param config the configuration for the specific NPC
     */
    public NPCConfigComponent (NPCConfigs.NPCConfig config) {
        this.config = config;
    }

}
