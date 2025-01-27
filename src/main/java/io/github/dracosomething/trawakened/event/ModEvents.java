package io.github.dracosomething.trawakened.event;

import io.github.dracosomething.trawakened.capability.trawakenedPlayerCapability;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = trawakened.MODID)
public class ModEvents {
    @SubscribeEvent
    public static void cancelHealing(LivingHealEvent event){
        if(event.getEntity().hasEffect(effectRegistry.HEALPOISON.get())){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void cancelDamage(LivingDamageEvent event){
        if(event.getEntity().hasEffect(effectRegistry.TIMESTOP_CORE.get())){
            event.setCanceled(true);
        }
    }

//    @SubscribeEvent
//    public static void stopTickServer(TickEvent.ClientTickEvent event){
//        if(Minecraft.getInstance().player != null) {
//            if (Minecraft.getInstance().player.hasEffect(effectRegistry.TIMESTOP_CORE.get())) {
//                event.setCanceled(true);
//            }
//        }
//    }

    @SubscribeEvent
    public static void notRemoveEffect(MobEffectEvent.Remove event) {
        LivingEntity entity = event.getEntity();

        if (entity.hasEffect(new MobEffectInstance(effectRegistry.OVERWHELMED.get()).getEffect()) ||
                entity.hasEffect(new MobEffectInstance((MobEffect) effectRegistry.PLAGUEEFFECT.get()).getEffect()) ||
                entity.hasEffect(new MobEffectInstance((MobEffect) effectRegistry.BRAINDAMAGE.get()).getEffect()) ||
                entity.hasEffect(new MobEffectInstance((MobEffect) effectRegistry.TIMESTOP_CORE.get()).getEffect()) ||
                entity.hasEffect(new MobEffectInstance((MobEffect) effectRegistry.TIMESTOP.get()).getEffect())){
            event.setCanceled(true);
        }
        if(entity.hasEffect(new MobEffectInstance(effectRegistry.MELT.get()).getEffect())){
            Random rand = new Random();
            int chance = rand.nextInt(101);
            if(chance < 10){
                event.setCanceled(true);
            } else {
                event.setCanceled(false);
            }
        }
    }
}
