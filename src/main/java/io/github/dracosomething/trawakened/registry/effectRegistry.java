package io.github.dracosomething.trawakened.registry;

import com.github.manasmods.tensura.effect.BurdenEffect;
import io.github.dracosomething.trawakened.mobeffect.HonkaiBeastEffect;
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

    public  effectRegistry(){}

    public static void init(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "trawakened");
        HONKAIEFFECT = registry.register("honkai_beast_mode", () -> new HonkaiBeastEffect(MobEffectCategory.BENEFICIAL, 5000268));
    }
}
