package io.github.dracosomething.trawakened.registry;

import com.github.manasmods.tensura.item.TensuraCreativeTab;
import io.github.dracosomething.trawakened.item.runeStone;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class itemRegistry {
    private static final DeferredRegister<Item> registry;
    public static final RegistryObject<Item> DEFAULT_OTHER_WORLDER;
    public static final RegistryObject<Item> STEALTH_STONE;
    public static final RegistryObject<Item> BLOODLUST_STONE;
    public static final RegistryObject<Item> MUTILATION_STONE;
    public static final RegistryObject<Item> QUICKSILVER_STONE;
    public static final RegistryObject<Item> RULERS_AUTHORITY_STONE;
    public static final RegistryObject<Item> DRAGONS_FEAR_STONE;

    public itemRegistry(){}

    public static void init(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.ITEMS, "trawakened");
        DEFAULT_OTHER_WORLDER = registry.register("other_worlder_spawn_egg", () -> {
            return new ForgeSpawnEggItem(entityRegistry.DEFAULT_OTHER_WORLDER, 7039851, 3026478, (new Item.Properties()).tab(TensuraCreativeTab.SPAWN_EGGS));
        });
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
