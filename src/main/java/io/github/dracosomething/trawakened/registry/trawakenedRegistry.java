package io.github.dracosomething.trawakened.registry;

import net.minecraftforge.eventbus.api.IEventBus;

public class trawakenedRegistry {
    public trawakenedRegistry() {
    }

    public static void register(IEventBus modEventBus) {
        effectRegistry.init(modEventBus);
        particleRegistry.init(modEventBus);
        skillRegistry.init(modEventBus);
        potionRegistry.init(modEventBus);
        enchantRegistry.init(modEventBus);
        entityRegistry.init(modEventBus);
        itemRegistry.init(modEventBus);
        biomeRegistry.init(modEventBus);
        blockRegistry.init(modEventBus);
        dimensionRegistry.init();
        argumentRegistry.init(modEventBus);
    }
}
