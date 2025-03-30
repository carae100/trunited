package io.github.dracosomething.trawakened.event;

import io.github.dracosomething.trawakened.entity.otherwolder.*;
import io.github.dracosomething.trawakened.particles.FleshParticles;
import io.github.dracosomething.trawakened.registry.entityRegistry;
import io.github.dracosomething.trawakened.registry.particleRegistry;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.client.particle.FireworkParticles;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = trawakened.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        event.register(particleRegistry.FLESHPARTICLE.get(), FleshParticles.Provider::new);
        event.register(particleRegistry.PURPLE_FIRE.get(), FlameParticle.Provider::new);
        event.register(particleRegistry.RED_FIRE.get(), FlameParticle.Provider::new);
    }
}
