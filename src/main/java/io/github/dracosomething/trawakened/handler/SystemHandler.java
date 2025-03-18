package io.github.dracosomething.trawakened.handler;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.capability.SkillStorage;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.event.SkillPlunderEvent;
import com.github.manasmods.tensura.event.UpdateEPEvent;
import com.github.manasmods.tensura.menu.SpatialStorageMenu;
import io.github.dracosomething.trawakened.ability.skill.unique.SystemSkill;
import io.github.dracosomething.trawakened.event.SystemLevelUpEvent;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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

    @SubscribeEvent
    public static void LevelUpMechanic(UpdateEPEvent event) {
        LivingEntity user = event.getEntity();
        if (SkillAPI.getSkillsFrom(user).getSkill(skillRegistry.SYSTEM.get()).isPresent()) {
            ManasSkillInstance instance = SkillAPI.getSkillsFrom(user).getSkill(skillRegistry.SYSTEM.get()).get();
            if (instance.getMastery() > 0.0) {
                CompoundTag tag = instance.getOrCreateTag();
                int nextLevel = tag.getInt("nextLevel");
                if (event.getNewEP() >= nextLevel) {
                    for (int i = 0; i <= (int) Math.floor(event.getNewEP()/nextLevel); i++) {
                        System.out.println(i);
                        int oldLEvel = tag.getInt("level");
                        tag.putInt("level", tag.getInt("level") + 1);
                        tag.putInt("nextLevel", tag.getInt("nextLevel") + 1150 + 150);
                        nextLevel = tag.getInt("nextLevel");
                        if (instance.getSkill() instanceof SystemSkill skill) {
                            skill.getTag().putInt("level", tag.getInt("level"));
                        }
                        SystemLevelUpEvent systemLevelUpEvent = new SystemLevelUpEvent(instance, user, oldLEvel, tag.getInt("level"));
                        MinecraftForge.EVENT_BUS.post(systemLevelUpEvent);
                        if (instance.getSkill() instanceof SystemSkill skill) {
                            skill.onLevelUp(instance, user, systemLevelUpEvent);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onJoinWorld(PlayerEvent.PlayerLoggedInEvent event) {
        Player user = event.getEntity();
        if (SkillAPI.getSkillsFrom(user).getSkill(skillRegistry.SYSTEM.get()).isPresent()) {
            ManasSkillInstance instance = SkillAPI.getSkillsFrom(user).getSkill(skillRegistry.SYSTEM.get()).get();
            if (instance.getSkill() instanceof SystemSkill skill && skill != null) {
                skill.getTag().putInt("level", instance.getOrCreateTag().getInt("level"));
                skill.getTag().putBoolean("isGui", instance.getOrCreateTag().getBoolean("isGui"));
                instance.getOrCreateTag().put("data", skill.getTag());
            }
        }
    }

    @SubscribeEvent
    public static void onCloseInventory(PlayerContainerEvent.Close event) {
        if (event.getContainer() instanceof SpatialStorageMenu) {
            Player user = event.getEntity();
            if (SkillAPI.getSkillsFrom(user).getSkill(skillRegistry.SYSTEM.get()).isPresent()) {
                ManasSkillInstance instance = SkillAPI.getSkillsFrom(user).getSkill(skillRegistry.SYSTEM.get()).get();
                if (instance.getSkill() instanceof SystemSkill skill && skill != null) {
                    instance.getOrCreateTag().putBoolean("isGui", false);
                    skill.getTag().putBoolean("isGui", instance.getOrCreateTag().getBoolean("isGui"));
                }
            }
        }
    }
}
