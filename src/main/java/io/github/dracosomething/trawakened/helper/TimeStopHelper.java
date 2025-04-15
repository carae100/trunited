package io.github.dracosomething.trawakened.helper;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TimeStopHelper {
    public static List<MobEffect> timeStops;
    public static List<MobEffect> timeStopCores;

    public static void onServerSetup(FMLDedicatedServerSetupEvent event) {
        event.enqueueWork(() -> {
            ForgeRegistries.MOB_EFFECTS.forEach((effect) -> {
                if (effect.getDisplayName().getString().contains("time_stop")) {
                    timeStops.add(effect);
                }
                if (effect.getDisplayName().getString().contains("time_stop_core")) {
                    timeStopCores.add(effect);
                }
            });
        });
    }

    public static boolean containsTimeStop(Collection<MobEffect> effects) {
        return !Collections.disjoint(effects, timeStops);
    }

    public static boolean containsTimeStopCore(Collection<MobEffect> effects) {
        return !Collections.disjoint(effects, timeStopCores);
    }

    public static boolean hasTimeStop(@Nullable LivingEntity entity) {
        if (entity != null) {
            Collection<MobEffect> effects = entity.getActiveEffectsMap().keySet().stream().toList();
            return (containsTimeStop(effects) || containsTimeStopCore(effects)) &&
                    !entity.isSpectator() &&
                    !(entity instanceof Player player && player.isCreative()) &&
                    !entity.isDeadOrDying();
        }
        return false;
    }
}
