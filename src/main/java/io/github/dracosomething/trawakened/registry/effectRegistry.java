package io.github.dracosomething.trawakened.registry;

import com.github.manasmods.tensura.effect.BurdenEffect;
import io.github.dracosomething.trawakened.mobeffect.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.*;

import java.awt.*;

public class effectRegistry {
    private static final DeferredRegister<MobEffect> registry;
    public static final RegistryObject<MobEffect> HONKAIEFFECT;
    public static final RegistryObject<MobEffect> PLAGUEEFFECT;
    public static final RegistryObject<MobEffect> PLAGUE_MODE_EFFECT;
    public static final RegistryObject<MobEffect> SHPPOISON;
    public static final RegistryObject<MobEffect> HEALPOISON;
    public static final RegistryObject<MobEffect> OVERWHELMED;
    public static final RegistryObject<MobEffect> MELT;
    public static final RegistryObject<MobEffect> BRAINDAMAGE;

    public effectRegistry(){}

    public static void init(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "trawakened");
        HONKAIEFFECT = registry.register("honkai_beast_mode", () -> new HonkaiBeastEffect(MobEffectCategory.BENEFICIAL, 14160291));
        PLAGUEEFFECT = registry.register("plague", () -> new PlagueEffect(MobEffectCategory.HARMFUL, 0));
        PLAGUE_MODE_EFFECT = registry.register("plague_mode", () -> new PlagueModeEffect(MobEffectCategory.BENEFICIAL, 0));
        SHPPOISON = registry.register("shp_poison", () -> new SHPPoisonEffect(MobEffectCategory.HARMFUL, 11427072));
        HEALPOISON = registry.register("heal_poison", () -> new HealPoisonEffect(MobEffectCategory.HARMFUL, 10422622));
        OVERWHELMED = registry.register("mad", () -> new overwhelmedEffect(MobEffectCategory.HARMFUL, 0));
        MELT = registry.register("melt", () -> new MeltEffect(MobEffectCategory.NEUTRAL, 15622684));
        BRAINDAMAGE = registry.register("brain_damage", () -> new DestroyedBrainEffect(MobEffectCategory.NEUTRAL, 14163341));
    }
}
