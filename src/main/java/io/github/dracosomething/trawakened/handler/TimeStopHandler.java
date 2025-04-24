package io.github.dracosomething.trawakened.handler;

import io.github.dracosomething.trawakened.helper.TimeStopHelper;
import io.github.dracosomething.trawakened.network.TRAwakenedNetwork;
import io.github.dracosomething.trawakened.network.play2server.ServerStopTime;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = trawakened.MODID)
public class TimeStopHandler {
    public static boolean check = false;

    @SubscribeEvent
    public static void setTimeStopped(MobEffectEvent.Added event) {
        if (TimeStopHelper.isTimeStopCore(event.getEffectInstance().getEffect())) {
            TRAwakenedNetwork.toServer(new ServerStopTime(true));
        }
    }

    @SubscribeEvent
    public static void setTimeStopped(MobEffectEvent.Remove event) {
        if (TimeStopHelper.isTimeStopCore(event.getEffect())) {
            TRAwakenedNetwork.toServer(new ServerStopTime(false));
        }
    }
}
