package io.github.dracosomething.trawakened.registry;

import com.github.manasmods.tensura.effect.BurdenEffect;
import io.github.dracosomething.trawakened.mobeffect.HonkaiBeastEffect;
import io.github.dracosomething.trawakened.mobeffect.PlagueEffect;
import io.github.dracosomething.trawakened.mobeffect.PlagueModeEffect;
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

    public  effectRegistry(){}

    public static void init(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "trawakened");
        HONKAIEFFECT = registry.register("honkai_beast_mode", () -> new HonkaiBeastEffect(MobEffectCategory.BENEFICIAL, 5000268));
        PLAGUEEFFECT = registry.register("plague", () -> new PlagueEffect(MobEffectCategory.HARMFUL, 5000235));
        PLAGUE_MODE_EFFECT = registry.register("plague_mode", () -> new PlagueModeEffect(MobEffectCategory.BENEFICIAL, 5000312));
    }
}
