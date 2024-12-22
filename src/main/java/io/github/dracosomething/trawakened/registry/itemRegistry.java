package io.github.dracosomething.trawakened.registry;

import io.github.dracosomething.trawakened.item.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class itemRegistry {
    private static final DeferredRegister<Item> registry;
    public static final RegistryObject<Item> DataTabletItem;
    public static final RegistryObject<Item> staffitem;
    public static final RegistryObject<Item> staff1item;
    public static final RegistryObject<Item> staff2item;
    public static final RegistryObject<Item> staff3item;
    public static final RegistryObject<Item> staff4item;
    public static final RegistryObject<Item> staff5item;


    private itemRegistry(){}

    public static void init(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.ITEMS, "trawakened");
        DataTabletItem = registry.register("data_tablet", () -> new DataTabletItem(new Item.Properties().stacksTo(1)));
        staffitem = registry.register("staff_model", () -> new staffItem(Tiers.DIAMOND, 1, 1.0F, new Item.Properties().stacksTo(1)));
        staff1item = registry.register("staff_1_model", () -> new staff1Item(Tiers.DIAMOND, 1, 1.0F, new Item.Properties().stacksTo(1)));
        staff2item = registry.register("staff_2_model", () -> new staff2Item(Tiers.DIAMOND, 1, 1.0F, new Item.Properties().stacksTo(1)));
        staff3item = registry.register("staff_3_model", () -> new staff3Item(Tiers.DIAMOND, 1, 1.0F, new Item.Properties().stacksTo(1)));
        staff4item = registry.register("staff_4_model", () -> new staff4Item(Tiers.DIAMOND, 1, 1.0F, new Item.Properties().stacksTo(1)));
        staff5item = registry.register("staff_5_model", () -> new staff5Item(Tiers.DIAMOND, 1, 1.0F, new Item.Properties().stacksTo(1)));
    }
}
