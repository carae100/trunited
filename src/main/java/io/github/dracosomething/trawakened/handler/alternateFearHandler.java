package io.github.dracosomething.trawakened.handler;

import io.github.dracosomething.trawakened.capability.alternateFearCapability.AwakenedFearCapability;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.IFearCapability;
import io.github.dracosomething.trawakened.helper.FearHelper;
import io.github.dracosomething.trawakened.trawakened;
import io.github.dracosomething.trawakened.world.trawakenedGamerules;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = trawakened.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class alternateFearHandler {
    public alternateFearHandler() {
    }

    @SubscribeEvent
    static void onTickLiving(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        FearHelper.fearUpdates(entity);
        if (entity.level.getGameRules().getBoolean(trawakenedGamerules.NORMAL_FEAR) || !AwakenedFearCapability.GetIsAlternate(entity)) {
            FearHelper.fearPenalty(entity, AwakenedFearCapability.getScared(entity));
        }
        FearHelper.resetData(entity);
    }

    @Nullable
    public static <T> T getCapability(Entity entity, Capability<T> capability) {
        return entity.getCapability(capability).isPresent() ? entity.getCapability(capability).orElseThrow(() -> {
            return new IllegalArgumentException("Lazy optional must not be empty");
        }) : null;
    }
}
