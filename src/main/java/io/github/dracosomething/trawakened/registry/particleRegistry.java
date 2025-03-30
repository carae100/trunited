package io.github.dracosomething.trawakened.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class particleRegistry {
    private static final DeferredRegister<ParticleType<?>> registry;
    public static final RegistryObject<SimpleParticleType> FLESHPARTICLE;
    public static final RegistryObject<SimpleParticleType> RED_FIRE;
    public static final RegistryObject<SimpleParticleType> PURPLE_FIRE;


    private particleRegistry(){}

    public static void init(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, "trawakened");
        FLESHPARTICLE = registry.register("flesh_particle", () -> new SimpleParticleType(true));
        RED_FIRE = registry.register("red_fire", () -> new SimpleParticleType(false));
        PURPLE_FIRE = registry.register("purple_fire", () -> new SimpleParticleType(false));
    }
}
