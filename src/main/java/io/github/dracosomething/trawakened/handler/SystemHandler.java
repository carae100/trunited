package io.github.dracosomething.trawakened.handler;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.capability.SkillStorage;
import com.github.manasmods.tensura.event.SkillPlunderEvent;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "trawakened")
public class SystemHandler {
    private static boolean isThunder;
    private static boolean isNight;

    @SubscribeEvent
    public static void CantPlunder(SkillPlunderEvent event) {
        if (event.getSkill().equals(skillRegistry.SYSTEM.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void Regen(TickEvent.PlayerTickEvent event) {
        SkillStorage storage = SkillAPI.getSkillsFrom(event.player);
        if (storage.getSkill(skillRegistry.SYSTEM.get()).isPresent()) {
            ManasSkillInstance skill = storage.getSkill(skillRegistry.SYSTEM.get()).get();
            skill.getOrCreateTag().putInt("seconds", skill.getOrCreateTag().getInt("seconds")+1);
            if (skill.getOrCreateTag().getInt("seconds") == 3) {
                event.player.setHealth(event.player.getHealth()+1);
            }
        }
    }

    @SubscribeEvent
    public static void Recovery(PlayerSleepInBedEvent event) {
        isThunder = event.getEntity().level.isThundering();
        isNight = event.getEntity().level.isNight();
    }

    @SubscribeEvent
    public static void WakeUpHeal(PlayerWakeUpEvent event) {
        if (!isThunder || isNight) {
            float p40 = (float) (event.getEntity().getHealth()*0.40);
            float p35 = (float) (event.getEntity().getHealth()*0.35);
            if (p40 > p35) {
                event.getEntity().setHealth(p40);
            } else {
                event.getEntity().setHealth(p35);
            }
        }
    }
}
