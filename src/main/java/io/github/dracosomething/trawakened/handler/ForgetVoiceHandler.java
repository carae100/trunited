package io.github.dracosomething.trawakened.handler;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.event.RemoveSkillEvent;
import com.github.manasmods.manascore.api.skills.event.SkillEvent;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import io.github.dracosomething.trawakened.ability.skill.ultimate.powerofhonkai;
import io.github.dracosomething.trawakened.ability.skill.ultimate.willofhonkai;
import io.github.dracosomething.trawakened.ability.skill.unique.voiceofhonkai;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = trawakened.MODID)
public class ForgetVoiceHandler {
    @SubscribeEvent
    public static void turnMajin(RemoveSkillEvent event) {
        if (event.getSkillInstance().getSkill() instanceof voiceofhonkai) {
            Entity entity = event.getEntity();
            if (entity instanceof LivingEntity living) {
                ManasSkillInstance instance = event.getSkillInstance();
                if (instance.getOrCreateTag().getBoolean("isUserMajin")) {
                    TensuraEPCapability.getFrom(living).ifPresent((cap) -> {
                        cap.setMajin(instance.getOrCreateTag().getBoolean("isUserMajin"));
                    });
                    TensuraEPCapability.sync(living);
                }
            }
        }
        if (event.getSkillInstance().getSkill() instanceof willofhonkai) {
            Entity entity = event.getEntity();
            if (entity instanceof LivingEntity living) {
                ManasSkillInstance instance = event.getSkillInstance();
                if (instance.getOrCreateTag().getBoolean("isUserMajin")) {
                    TensuraEPCapability.getFrom(living).ifPresent((cap) -> {
                        cap.setMajin(instance.getOrCreateTag().getBoolean("isUserMajin"));
                    });
                    TensuraEPCapability.sync(living);
                }
            }
        }
        if (event.getSkillInstance().getSkill() instanceof powerofhonkai) {
            Entity entity = event.getEntity();
            if (entity instanceof Player player) {
                ManasSkillInstance instance = event.getSkillInstance();
                if (instance.getOrCreateTag().getBoolean("isUserBlessed")) {
                    TensuraPlayerCapability.getFrom(player).ifPresent((data) -> {
                        data.setBlessed(instance.getOrCreateTag().getBoolean("isUserBlessed"));
                    });
                    TensuraPlayerCapability.sync(player);
                }
            }
        }
    }
}
