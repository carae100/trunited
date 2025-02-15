package io.github.dracosomething.trawakened.event;

import io.github.dracosomething.trawakened.entity.otherwolder.*;
import io.github.dracosomething.trawakened.particles.FleshParticles;
import io.github.dracosomething.trawakened.registry.entityRegistry;
import io.github.dracosomething.trawakened.registry.particleRegistry;
import io.github.dracosomething.trawakened.trawakened;
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
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event)
    {
        event.register(particleRegistry.FLESHPARTICLE.get(), FleshParticles.Provider::new);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(entityRegistry.DEFAULT_OTHER_WORLDER.get(), defaultOtherWolder.setAttributes());
    }
}
