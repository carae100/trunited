package io.github.dracosomething.trawakened.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class potionRegistry {
    private static final DeferredRegister<Potion> registry;
    public static final RegistryObject<Potion> HEAL_POISON_POTION_1;
    public static final RegistryObject<Potion> SHP_POISON_POTION_1;
    public static final RegistryObject<Potion> MAD_POTION_1, MAD_POTION_SHORT;
    public static final RegistryObject<Potion> MELT_POTION_1, MELT_POTION_2, MELT_POTION_LONG;
    public static final RegistryObject<Potion> BRAIN_DAMAGE_POTION;
    public static final RegistryObject<Potion> EXTREME_WEAK_POTION_1, EXTREME_WEAK_POTION_2, EXTREME_WEAK_POTION_3, EXTREME_WEAK_POTION_LONG;
    public static final RegistryObject<Potion> SPIRITUAL_BLOCK_POTION_1,SPIRITUAL_BLOCK_POTION_2 ,SPIRITUAL_BLOCK_POTION_LONG;

    public potionRegistry(){}

    public static void init(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.POTIONS, "trawakened");
        HEAL_POISON_POTION_1 = registry.register("heal_poison_potion", () -> new Potion(new MobEffectInstance(effectRegistry.HEALPOISON.get(), 1800, 0)));
        SHP_POISON_POTION_1 = registry.register("shp_poison_potion", () -> new Potion(new MobEffectInstance(effectRegistry.SHPPOISON.get(), 1800, 0)));
        MAD_POTION_1 = registry.register("mad_potion", () -> new Potion(new MobEffectInstance(effectRegistry.OVERWHELMED.get(), 400, 0)));
        MAD_POTION_SHORT = registry.register("mad_potion_short", () -> new Potion(new MobEffectInstance(effectRegistry.OVERWHELMED.get(), 200, 0)));
        MELT_POTION_1 = registry.register("melt_potion", () -> new Potion(new MobEffectInstance(effectRegistry.MELT.get(), 1200, 0)));
        MELT_POTION_2 = registry.register("melt_potion_2", () -> new Potion(new MobEffectInstance(effectRegistry.MELT.get(), 600, 1)));
        MELT_POTION_LONG = registry.register("melt_potion_long", () -> new Potion(new MobEffectInstance(effectRegistry.MELT.get(), 2400, 0)));
        BRAIN_DAMAGE_POTION = registry.register("brain_damage_potion", () -> new Potion(new MobEffectInstance(effectRegistry.BRAINDAMAGE.get(), 1200, 0)));
        EXTREME_WEAK_POTION_1 = registry.register("extreme_weak_potion_1", () -> new Potion(new MobEffectInstance(effectRegistry.CREATIVE_MENU.get(), 3000, 0, false, false, false)));
        EXTREME_WEAK_POTION_2 = registry.register("extreme_weak_potion_2", () -> new Potion(new MobEffectInstance(effectRegistry.CREATIVE_MENU.get(), 2400, 1, false, false, false)));
        EXTREME_WEAK_POTION_3 = registry.register("extreme_weak_potion_3", () -> new Potion(new MobEffectInstance(effectRegistry.CREATIVE_MENU.get(), 1200, 2, false, false, false)));
        EXTREME_WEAK_POTION_LONG = registry.register("extreme_weak_potion_long", () -> new Potion(new MobEffectInstance(effectRegistry.CREATIVE_MENU.get(), 4800, 0, false, false, false)));
        SPIRITUAL_BLOCK_POTION_1 = registry.register("spiritual_block_potion_1", () -> new Potion(new MobEffectInstance(effectRegistry.SPIRITUAL_BLOCK.get(), 2400, 0, false, false, false)));
        SPIRITUAL_BLOCK_POTION_2 = registry.register("spiritual_block_potion_2", () -> new Potion(new MobEffectInstance(effectRegistry.SPIRITUAL_BLOCK.get(), 1200, 1, false, false, false)));
        SPIRITUAL_BLOCK_POTION_LONG = registry.register("spiritual_block_potion_long", () -> new Potion(new MobEffectInstance(effectRegistry.SPIRITUAL_BLOCK.get(), 3000, 0, false, false, false)));
    }
}
