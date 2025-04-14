package io.github.dracosomething.trawakened.registry.items;

import io.github.dracosomething.trawakened.item.shadowItems.shadowArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class armors {
    private static final DeferredRegister<Item> registry;
    public static final RegistryObject<Item> SHADOW_ARMOR_HEAD;
    public static final RegistryObject<Item> SHADOW_ARMOR_CHEST;
    public static final RegistryObject<Item> SHADOW_ARMOR_LEGS;
    public static final RegistryObject<Item> SHADOW_ARMOR_FEET;

    public static void init(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.ITEMS, "trawakened");
        SHADOW_ARMOR_HEAD = registry.register("shadow_helmet", () -> {
           return new shadowArmorItem(EquipmentSlot.HEAD);
        });
        SHADOW_ARMOR_CHEST = registry.register("shadow_chestplate", () -> {
            return new shadowArmorItem(EquipmentSlot.CHEST);
        });
        SHADOW_ARMOR_LEGS = registry.register("shadow_leggings", () -> {
            return new shadowArmorItem(EquipmentSlot.LEGS);
        });
        SHADOW_ARMOR_FEET = registry.register("shadow_boots", () -> {
            return new shadowArmorItem(EquipmentSlot.FEET);
        });
    }
}
