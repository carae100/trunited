package io.github.dracosomething.trawakened.registry.items;

import com.github.manasmods.tensura.item.TensuraToolTiers;
import com.github.manasmods.tensura.item.templates.SimpleSwordItem;
import io.github.dracosomething.trawakened.item.SimpleHammerItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class weapons {
    private static final DeferredRegister<Item> registry;
    public static final RegistryObject<Item> SHADOW_HAMMER;
    public static final RegistryObject<Item> SHADOW_SWORD;
    public static final RegistryObject<Item> SHADOW_KATANA;

    public static void init(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.ITEMS, "trawakened");
        SHADOW_HAMMER = registry.register("shadow_hammer", () -> {
            return new SimpleHammerItem(TensuraToolTiers.HIHIIROKANE, 1, 1, 1, 1, 1, SimpleSwordItem.SwordModifier.FIRE_RESISTED.getProperties());
        });
        SHADOW_SWORD = registry.register("shadow_hammer", () -> {
            return new SimpleHammerItem(TensuraToolTiers.HIHIIROKANE, 1, 1, 1, 1, 1, SimpleSwordItem.SwordModifier.FIRE_RESISTED.getProperties());
        });
        SHADOW_KATANA = registry.register("shadow_hammer", () -> {
            return new SimpleHammerItem(TensuraToolTiers.HIHIIROKANE, 1, 1, 1, 1, 1, SimpleSwordItem.SwordModifier.FIRE_RESISTED.getProperties());
        });
    }
}
