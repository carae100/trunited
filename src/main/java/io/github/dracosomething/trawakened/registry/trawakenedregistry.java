package io.github.dracosomething.trawakened.registry;

import net.minecraftforge.eventbus.api.IEventBus;

public class trawakenedregistry {
    public trawakenedregistry() {
    }

    public static void register(IEventBus modEventBus) {
        effectRegistry.init(modEventBus);
        itemRegistry.init(modEventBus);
    }
}
