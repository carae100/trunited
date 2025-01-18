package io.github.dracosomething.trawakened.mixin;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.config.TensuraConfig;
import com.github.manasmods.tensura.menu.RaceSelectionMenu;
import com.github.manasmods.tensura.world.TensuraGameRules;
import com.github.manasmods.tensura.world.savedata.UniqueSkillSaveData;
import io.github.dracosomething.trawakened.registry.skillregistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.github.manasmods.tensura.menu.RaceSelectionMenu.getReincarnationSkills;
import static com.github.manasmods.tensura.menu.RaceSelectionMenu.getSecondReincarnationSkills;

@Mixin(RaceSelectionMenu.class)
public abstract class RaceSelectionMenuMixin {
    @Inject(
            method = "getReincarnationSkills",
            at = @At(value = "HEAD"),
            remap = false,
            cancellable = true)
    private static void NewUniques(ServerLevel level, boolean ignoreGameRule, Player roller, CallbackInfoReturnable<List<ManasSkill>> cir) {
        List<ManasSkill> skills = new ArrayList<ManasSkill>(((List)TensuraConfig.INSTANCE.skillsConfig.reincarnationSkills.get()).stream().filter((skill) -> {
            if (ignoreGameRule) {
                return true;
            } else if (!level.getGameRules().getBoolean(TensuraGameRules.TRULY_UNIQUE)) {
                return true;
            } else {
                return !UniqueSkillSaveData.get(level.getServer().overworld()).hasSkill(new ResourceLocation(skill.toString()));
            }
        }).map((skill) -> {
            return (ManasSkill)SkillAPI.getSkillRegistry().getValue(new ResourceLocation(skill.toString()));
        }).filter((skill) -> {
            if (skill == null) {
                return false;
            } else {
                return roller == null || !SkillUtils.hasSkill(roller, (ManasSkill) skill);
            }
        }).toList());
        skills.add(SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:starkill")));
        skills.add(skillregistry.AKASHIC_PLANE.get());
        System.out.println(skills);
        cir.setReturnValue(skills);
//        List<ManasSkill> list = new ArrayList<>(List.of());
//        list.add(SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:starkill")));
//        cir.setReturnValue(list);
    }
}

