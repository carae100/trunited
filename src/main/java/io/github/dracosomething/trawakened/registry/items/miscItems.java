package io.github.dracosomething.trawakened.registry.items;

import io.github.dracosomething.trawakened.item.amplificationOrb;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class miscItems {
    private static final DeferredRegister<Item> registry;
    public static final RegistryObject<Item> AMPLIFICATION_ORB;

    public static void init(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.ITEMS, "trawakened");
        AMPLIFICATION_ORB = registry.register("amplification_orb", () -> {
            return new amplificationOrb(new Item.Properties().tab(CreativeModeTab.TAB_MISC).fireResistant());
        });
    }
}
