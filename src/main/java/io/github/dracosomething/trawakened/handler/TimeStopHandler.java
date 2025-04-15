package io.github.dracosomething.trawakened.handler;

import io.github.dracosomething.trawakened.helper.TimeStopHelper;
import io.github.dracosomething.trawakened.mixin.ServerLevelMixin;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.File;
import java.io.IOException;

@Mod.EventBusSubscriber(modid = trawakened.MODID)
public class TimeStopHandler {
    @SubscribeEvent
    public static void setTimeStopped(MobEffectEvent.Added event) throws IOException {
        if (TimeStopHelper.containsTimeStopCore(event.getEffectInstance().getEffect())) {

        }
    }
}
