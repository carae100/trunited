package io.github.dracosomething.trawakened.event;

import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = trawakened.MODID)
public class ModEvents {
    @SubscribeEvent
    public static void cancelHealing(LivingHealEvent event){
        if(event.getEntity().hasEffect(effectRegistry.HEALPOISON.get())){
            event.setCanceled(true);
        }
    }
}
