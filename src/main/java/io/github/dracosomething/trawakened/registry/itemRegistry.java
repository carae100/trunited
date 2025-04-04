package io.github.dracosomething.trawakened.registry;

import com.github.manasmods.tensura.item.TensuraCreativeTab;
import io.github.dracosomething.trawakened.item.runeStone;
import io.github.dracosomething.trawakened.registry.items.runeStones;
import io.github.dracosomething.trawakened.registry.items.spawnEggs;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class itemRegistry {
    public itemRegistry(){}

    public static void init(IEventBus modEventBus) {
        runeStones.init(modEventBus);
        spawnEggs.init(modEventBus);
    }
}
