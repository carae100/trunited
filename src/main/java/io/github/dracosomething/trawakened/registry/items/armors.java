package io.github.dracosomething.trawakened.registry.items;

import com.github.manasmods.tensura.item.TensuraToolTiers;
import com.github.manasmods.tensura.item.templates.SimpleSwordItem;
import com.github.manasmods.tensura.item.templates.custom.SimpleGreatSwordItem;
import com.github.manasmods.tensura.item.templates.custom.SimpleKatanaItem;
import io.github.dracosomething.trawakened.item.SimpleHammerItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class armors {
    private static final DeferredRegister<Item> registry;
    public static final RegistryObject<Item> SHADOW_ARMOR;

    public static void init(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.ITEMS, "trawakened");
    }
}
