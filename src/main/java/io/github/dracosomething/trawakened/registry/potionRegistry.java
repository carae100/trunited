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
    public static final RegistryObject<Potion> MAD_POTION_1;

    public potionRegistry(){}

    public static void init(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.POTIONS, "trawakened");
        HEAL_POISON_POTION_1 = registry.register("heal_poison_potion", () -> new Potion(new MobEffectInstance(effectRegistry.HEALPOISON.get(), 200, 0)));
        SHP_POISON_POTION_1 = registry.register("shp_poison_potion", () -> new Potion(new MobEffectInstance(effectRegistry.SHPPOISON.get(), 200, 0)));
        MAD_POTION_1 = registry.register("mad_potion", () -> new Potion(new MobEffectInstance(effectRegistry.OVERWHELMED.get(), 10, 0)));
    }
}
