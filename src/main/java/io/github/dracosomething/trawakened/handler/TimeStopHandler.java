package io.github.dracosomething.trawakened.handler;

import io.github.dracosomething.trawakened.trawakened;
import io.github.dracosomething.trawakened.world.levelStorage.IsTimeStoppedServer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = trawakened.MODID)
public class TimeStopHandler {
    @SubscribeEvent
    public static void setTimeStopped(MobEffectEvent.Added event) {
        Level level = event.getEntity().level;
        System.out.println(level.getLevelData());
//        IsTimeStoppedServer data = IsTimeStoppedServer.get(level);
    }
}
