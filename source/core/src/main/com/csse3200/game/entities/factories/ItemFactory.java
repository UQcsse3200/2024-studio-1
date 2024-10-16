package com.csse3200.game.entities.factories;

import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.buffs.GoblinsGamble;
import com.csse3200.game.components.player.inventory.buffs.WerewolfFang;
import com.csse3200.game.components.player.inventory.buffs.*;
import com.csse3200.game.components.player.inventory.pets.RingFire;
import com.csse3200.game.components.player.inventory.pets.Tombstone;
import com.csse3200.game.components.player.inventory.usables.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A factory that creates usable items and buffs.
 */
public class ItemFactory {
    private static final Map<String, ItemCreator> creators = loadCreators();

    private static Map<String, ItemCreator> loadCreators() {
        Map<String, ItemCreator> creators = new HashMap<>();
        creators.put("medkit", (args) -> new MedKit());
        creators.put("bandage", (args) -> new Bandage());
        creators.put("energydrink", (args) -> new EnergyDrink(args[0]));
        creators.put("shieldpotion", (args) -> new ShieldPotion());
        creators.put("syringe", (args) -> new Syringe());
        creators.put("armor", (args) -> new Armor());
        creators.put("damagebuff", (args) -> new DamageBuff());
        creators.put("reroll", (args) -> new Reroll());
        creators.put("targetdummy", (args) -> new TargetDummy());
        creators.put("beartrap", (args) -> new BearTrap());
        creators.put("heart", (args) -> new Heart());
        creators.put("feather", (args) -> new Feather());
        creators.put("tombstone", (args) -> args.length < 1 ? new Tombstone()
                                                            : new Tombstone(args[0]));
        creators.put("ringfire", (args) -> new RingFire());
        creators.put("divinepotion", (args) -> new DivinePotion());
        creators.put("bigredbutton", (args) -> new BigRedButton());
        creators.put("teleporter", (args) -> new TeleporterItem());
        creators.put("goblinsgamble", (args) -> new GoblinsGamble());
        creators.put("fang", (args)-> new WerewolfFang());
        creators.put("gasoline", (args) -> new Gasoline());
        return creators;
    }

    /**
     * Creates a specified entity
     *
     * @param specification The specification of the item to create.
     * @return the specified entity
     */
    public Collectible create(String specification) {
        String[] arguments = specification.split(":");
        String type = arguments[0];


        arguments = Arrays.stream(Arrays.copyOfRange(arguments, 1, arguments.length))
                .filter(s -> !s.equals("mystery"))
                .toArray(String[]::new);

        ItemCreator creator = creators.getOrDefault(type, (args) -> {
            throw new IllegalArgumentException("Invalid item specification: " + specification);
        });

        return creator.create(arguments);
    }

    /**
     * Generate a list of all valid specifications this factory can generate.
     *
     * @return the specifications as a List.
     */
    public Collection<String> getAllSpecs() {
        return creators.keySet()
                .stream()
                .filter(s -> !s.equals("energydrink"))
                .map(s -> "item:" + s)
                .toList();
    }

    private interface ItemCreator {
        Collectible create(String[] arguments);
    }
}

