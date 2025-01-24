package io.github.dracosomething.trawakened.registry;

import com.github.manasmods.manascore.api.skills.ManasSkill;
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
    }
}
