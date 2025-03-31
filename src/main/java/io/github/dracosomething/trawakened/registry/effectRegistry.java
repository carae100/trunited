 package io.github.dracosomething.trawakened.registry;

import io.github.dracosomething.trawakened.mobeffect.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

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
    public static final RegistryObject<MobEffect> TIMESTOP;
    public static final RegistryObject<MobEffect> TIMESTOP_CORE;
    public static final RegistryObject<MobEffect> WHEAKENING;
    public static final RegistryObject<MobEffect> SPIRITUAL_BLOCK;
    public static final RegistryObject<MobEffect> ALTERNATE_MODE;
    public static final RegistryObject<MobEffect> INTRUDER_MODE;
    public static final RegistryObject<MobEffect> FEAR_AMPLIFICATION;
    public static final RegistryObject<MobEffect> BLOODLUST_DEBUFF;
    public static final RegistryObject<MobEffect> MONARCHS_DOMAIN;

    public effectRegistry(){}

    public static void init(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "trawakened");
        HONKAIEFFECT = registry.register("honkai_beast_mode", () -> new HonkaiBeastEffect(MobEffectCategory.BENEFICIAL, 14160291));
        PLAGUEEFFECT = registry.register("plague", () -> new PlagueEffect(MobEffectCategory.HARMFUL, 0));
        PLAGUE_MODE_EFFECT = registry.register("plague_mode", () -> new PlagueModeEffect(MobEffectCategory.BENEFICIAL, 0));
        SHPPOISON = registry.register("shp_poison", () -> new emptyEffect(MobEffectCategory.HARMFUL, 11427072));
        HEALPOISON = registry.register("heal_poison", () -> new emptyEffect(MobEffectCategory.HARMFUL, 10422622));
        OVERWHELMED = registry.register("mad", () -> new overwhelmedEffect(MobEffectCategory.HARMFUL, 0));
        MELT = registry.register("melt", () -> new MeltEffect(MobEffectCategory.NEUTRAL, 15622684));
        BRAINDAMAGE = registry.register("brain_damage", () -> new DestroyedBrainEffect(MobEffectCategory.NEUTRAL, 14163341));
        TIMESTOP = registry.register("time_stop", () -> new emptyEffect(MobEffectCategory.NEUTRAL, 14914605));
        TIMESTOP_CORE = registry.register("time_stop_core", () -> new TimeStopCoreEffect(MobEffectCategory.BENEFICIAL, 14914605));
        WHEAKENING = registry.register("extreme_weakening", () -> new emptyEffect(MobEffectCategory.NEUTRAL, 258276));
        SPIRITUAL_BLOCK = registry.register("spiritual_blocking", () -> new emptyEffect(MobEffectCategory.BENEFICIAL, 13407025));
        ALTERNATE_MODE = registry.register("alternate_mode", () -> new AlternateModeEffect(MobEffectCategory.BENEFICIAL, 0));
        INTRUDER_MODE = registry.register("intruder_mode", () -> new IntruderModeEffect(MobEffectCategory.BENEFICIAL, 0));
        FEAR_AMPLIFICATION = registry.register("fear_amplification", () -> new emptyEffect(MobEffectCategory.HARMFUL, 4343629));
        BLOODLUST_DEBUFF = registry.register("bloodlust_debuff", () -> new BloodlustDebuffEffect(MobEffectCategory.HARMFUL, 0));
        MONARCHS_DOMAIN = registry.register("monarchs_domain", () -> new MonarchsDomainEffect(MobEffectCategory.BENEFICIAL, 11557101));
    }
}
