package io.github.dracosomething.trawakened.registry;

import com.github.manasmods.tensura.item.TensuraCreativeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class itemRegistry {
    private static final DeferredRegister<Item> registry;
    public static final RegistryObject<Item> DEFAULT_OTHER_WORLDER;

    public itemRegistry(){}

    public static void init(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.ITEMS, "trawakened");
        DEFAULT_OTHER_WORLDER = registry.register("other_worlder_spawn_egg", () -> {
            return new ForgeSpawnEggItem(entityRegistry.DEFAULT_OTHER_WORLDER, 7039851, 3026478, (new Item.Properties()).tab(TensuraCreativeTab.SPAWN_EGGS));
        });
    }
}
