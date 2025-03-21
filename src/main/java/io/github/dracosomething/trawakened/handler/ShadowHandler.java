package io.github.dracosomething.trawakened.handler;

import com.github.manasmods.manascore.api.skills.SkillAPI;
import io.github.dracosomething.trawakened.capability.ShadowCapability.AwakenedShadowCapability;
import io.github.dracosomething.trawakened.event.BecomeShadowEvent;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = trawakened.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ShadowHandler {
    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        Entity source = event.getSource().getEntity();
        if (source instanceof LivingEntity entity) {
            LivingEntity target = event.getEntity();
            if (!AwakenedShadowCapability.isShadow(target) && !AwakenedShadowCapability.isArisen(target)) {
                if (target.position().distanceTo(entity.position()) <= 50) {
                    if (SkillAPI.getSkillsFrom(entity).getSkill(skillRegistry.SHADOW_MONARCH.get()).isPresent()) {
                        BecomeShadowEvent becomeShadowEvent = new BecomeShadowEvent(event.getEntity(), event.getSource());
                        if (!MinecraftForge.EVENT_BUS.post(becomeShadowEvent)) {
                            AwakenedShadowCapability.setShadow(entity, true);
                        }
                    }
                }
            }
        }
    }
}
