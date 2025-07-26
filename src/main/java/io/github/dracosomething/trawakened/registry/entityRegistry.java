package io.github.dracosomething.trawakened.registry;

import io.github.dracosomething.trawakened.entity.barrier.IntruderBarrier;
import io.github.dracosomething.trawakened.entity.otherwolder.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class entityRegistry {
    private static final DeferredRegister<EntityType<?>> registry;
    public static final RegistryObject<EntityType<defaultOtherWolder>> DEFAULT_OTHER_WORLDER;
    public static final RegistryObject<EntityType<IntruderBarrier>> INTRUDER_BARRIER;

    public entityRegistry(){}

    public static void init(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "trawakened");
        DEFAULT_OTHER_WORLDER = registry.register("default_other_worlder", () -> {
            return EntityType.Builder.of(defaultOtherWolder::new, MobCategory.MONSTER).canSpawnFarFromPlayer().updateInterval(2).clientTrackingRange(32).sized(0.6F, 1.8F).build((new ResourceLocation("trawakened", "default_other_worlder")).toString());
        });
        INTRUDER_BARRIER = registry.register("intruder_barrier", () -> {
            return EntityType.Builder.<IntruderBarrier>of(IntruderBarrier::new, MobCategory.MISC).sized(0.1F, 0.1F).clientTrackingRange(64).updateInterval(Integer.MAX_VALUE).fireImmune().build((new ResourceLocation("trawakened", "intruder)barrier")).toString());
        });
    }
}
