package io.github.dracosomething.trawakened.handler;

import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.AwakenedFearCapability;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.IFearCapability;
import io.github.dracosomething.trawakened.event.FearActivateEvent;
import io.github.dracosomething.trawakened.helper.FearHelper;
import io.github.dracosomething.trawakened.trawakened;
import io.github.dracosomething.trawakened.world.trawakenedGamerules;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = trawakened.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class alternateFearHandler {
    @SubscribeEvent
    static void onTickLiving(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        FearHelper.fearUpdates(entity);
        boolean check = entity.level.getGameRules().getBoolean(trawakenedGamerules.NORMAL_FEAR);
        if (check) {
            check = !AwakenedFearCapability.GetIsAlternate(entity);
            if (check) {
                FearHelper.fearPenalty(entity, Integer.valueOf(AwakenedFearCapability.getScared(entity)));
            }
        }
        FearHelper.resetData(entity);
    }

    @SubscribeEvent
    public static void indicator(FearActivateEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level.getGameRules().getBoolean(trawakenedGamerules.NORMAL_FEAR)) {
            TensuraParticleHelper.addServerParticlesAroundSelf(entity, ParticleTypes.ASH, 0.6D, 10);
            if (entity instanceof Player player) {
                player.level.playSound(player, player.blockPosition(), SoundEvents.WITHER_AMBIENT, SoundSource.VOICE, 1.0F, 1.0F);
            }
        }
    }

    @Nullable
    public static <T> T getCapability(Entity entity, Capability<T> capability) {
        return entity.getCapability(capability).isPresent() ? entity.getCapability(capability).orElseThrow(() -> {
            return new IllegalArgumentException("Lazy optional must not be empty");
        }) : null;
    }
}
