package io.github.dracosomething.trawakened.event;

import com.github.manasmods.tensura.event.SpiritualHurtEvent;
import io.github.dracosomething.trawakened.capability.trawakenedPlayerCapability;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;
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
    public static void DoubleSpiritualDamage(SpiritualHurtEvent event){
        if(event.getEntity().hasEffect(effectRegistry.CREATIVE_MENU.get())){
            event.setAmount(
                    (float) (event.getAmount() *
                    Math.ceil(
                            (double) Objects.requireNonNull(
                                    event.getEntity().getEffect(
                                            effectRegistry.CREATIVE_MENU.get()
                                    )
                            ).getAmplifier() /2)
                    )
            );
        }
        if(event.getEntity().hasEffect(effectRegistry.SPIRITUAL_BLOCK.get())){
            event.setAmount(
                    (float) Math.floor(event.getAmount() -
                            Math.ceil(
                                    (double) Objects.requireNonNull(
                                            event.getEntity().getEffect(
                                                    effectRegistry.SPIRITUAL_BLOCK.get()
                                            )
                                    ).getAmplifier() / 15)
                    )
            );
        }
    }

    @SubscribeEvent
    public static void DoubleDamage(LivingDamageEvent event){
        if(event.getEntity().hasEffect(effectRegistry.CREATIVE_MENU.get())){
            event.setAmount(
                    (float) (event.getAmount() *
                            Math.ceil(
                                    (double) Objects.requireNonNull(
                                            event.getEntity().getEffect(
                                                    effectRegistry.CREATIVE_MENU.get()
                                            )
                                    ).getAmplifier() /2)
                    )
            );
        }
    }

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
