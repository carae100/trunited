package io.github.dracosomething.trawakened.registry.items;

import io.github.dracosomething.trawakened.item.runeStone;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class runeStones {
    private static final DeferredRegister<Item> registry;
    public static final RegistryObject<Item> STEALTH_STONE;
    public static final RegistryObject<Item> BLOODLUST_STONE;
    public static final RegistryObject<Item> MUTILATION_STONE;
    public static final RegistryObject<Item> QUICKSILVER_STONE;
    public static final RegistryObject<Item> RULERS_AUTHORITY_STONE;
    public static final RegistryObject<Item> DRAGONS_FEAR_STONE;

    public static void init(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.ITEMS, "trawakened");
        STEALTH_STONE = registry.register("stealth_stone", () -> {
            return new runeStone(skillRegistry.STEALTH, 30);
        });
        BLOODLUST_STONE = registry.register("bloodlust_stone", () -> {
            return new runeStone(skillRegistry.BLOOD_LUST, 35);
        });
        MUTILATION_STONE = registry.register("mutilation_stone", () -> {
            return new runeStone(skillRegistry.MUTILATION, 50);
        });
        QUICKSILVER_STONE = registry.register("quicksilver_stone", () -> {
            return new runeStone(skillRegistry.QUICKSILVER, 40);
        });
        RULERS_AUTHORITY_STONE = registry.register("rulers_authority_stone", () -> {
            return new runeStone(skillRegistry.RULERS_AUTHORITY, 70);
        });
        DRAGONS_FEAR_STONE = registry.register("dragons_fear_stone", () -> {
            return new runeStone(skillRegistry.DRAGONS_FEAR, 100);
        });
    }
}
