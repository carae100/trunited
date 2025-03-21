package io.github.dracosomething.trawakened.handler;

import io.github.dracosomething.trawakened.capability.ShadowCapability.AwakenedShadowCapability;
import io.github.dracosomething.trawakened.capability.ShadowCapability.AwakenedShadowCapabilityProvider;
import io.github.dracosomething.trawakened.capability.ShadowCapability.IShadowCapability;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.AwakenedFearCapability;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.AwakenedFearCapabilityProvider;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.IFearCapability;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = trawakened.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityHandler {
    public CapabilityHandler(){}

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IFearCapability.class);
        event.register(IShadowCapability.class);
    }

    @SubscribeEvent
    static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        AwakenedFearCapability.sync(event.getEntity());
        AwakenedShadowCapability.sync(event.getEntity());
    }

    @SubscribeEvent
    public static void attach(AttachCapabilitiesEvent<Entity> e) {
        if (e.getObject() instanceof LivingEntity) {
            e.addCapability(new ResourceLocation("trawakened", "fear"), new AwakenedFearCapabilityProvider());
            e.addCapability(new ResourceLocation("trawakened", "shadow"), new AwakenedShadowCapabilityProvider());
        }
    }

    @SubscribeEvent
    static void onPlayerTrack(PlayerEvent.StartTracking e) {
        Entity var2 = e.getTarget();
        if (var2 instanceof LivingEntity living) {
            AwakenedFearCapability.sync(living);
            AwakenedFearCapability.sync(e.getEntity());
            AwakenedShadowCapability.sync(living);
            AwakenedShadowCapability.sync(e.getEntity());
        }
    }

    @SubscribeEvent
    static void onPlayerClone(PlayerEvent.Clone e) {
        e.getOriginal().reviveCaps();
        AwakenedFearCapability.getFrom(e.getOriginal()).ifPresent((oldData) -> {
            AwakenedFearCapability.getFrom(e.getEntity()).ifPresent((data) -> {
                data.deserializeNBT((CompoundTag)oldData.serializeNBT());
            });
        });
        AwakenedShadowCapability.getFrom(e.getOriginal()).ifPresent((oldData) -> {
            AwakenedShadowCapability.getFrom(e.getOriginal()).ifPresent((data) -> {
                data.deserializeNBT(oldData.serializeNBT());
            });
        });
        e.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent e) {
        AwakenedFearCapability.sync(e.getEntity());
        AwakenedShadowCapability.sync(e.getEntity());
    }

    @SubscribeEvent
    static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent e) {
        AwakenedFearCapability.sync(e.getEntity());
        AwakenedShadowCapability.sync(e.getEntity());
    }

    @Nullable
    public static <T> T getCapability(Entity entity, Capability<T> capability) {
        return entity.getCapability(capability).isPresent() ? entity.getCapability(capability).orElseThrow(() -> {
            return new IllegalArgumentException("Lazy optional must not be empty");
        }) : null;
    }
}
