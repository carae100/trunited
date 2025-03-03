package io.github.dracosomething.trawakened.ability.skill.ultimate;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.event.UnlockSkillEvent;
import com.github.manasmods.manascore.skill.SkillRegistry;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import io.github.dracosomething.trawakened.helper.skillHelper;
import io.github.dracosomething.trawakened.library.AlternateType;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class falseGabriel extends Skill {
    public falseGabriel() {
        super(SkillType.ULTIMATE);
    }

    @Override
    public boolean meetEPRequirement(Player entity, double newEP) {
        ManasSkillInstance instance = SkillUtils.getSkillOrNull(entity, skillRegistry.ALTERNATE.get());
        if (instance != null) {
            CompoundTag tag = instance.getOrCreateTag();
            boolean isAwakened = tag.getBoolean("awakening");
            if (isAwakened) {
                return instance.isMastered(entity)  && (TensuraPlayerCapability.isTrueDemonLord(entity) || (TensuraPlayerCapability.isHeroEgg(entity) || TensuraPlayerCapability.isTrueHero(entity)));
            }
        }
        return false;
    }

    @Override
    public int modes() {
        return 5;
    }

    @Override
    public boolean canTick(ManasSkillInstance instance, LivingEntity entity) {
        return true;
    }

    @Override
    public void onTick(ManasSkillInstance instance, LivingEntity living) {
        List<Entity> entityList = skillHelper.DrawCircle(living, instance.isMastered(living)?20:15, true);
        for (Entity entity : entityList) {
            if (entity instanceof LivingEntity livingEntity) {
                SkillHelper.checkThenAddEffectSource(livingEntity, living, effectRegistry.FEAR_AMPLIFICATION.get(), 60, 1, false, false, false, false);
            }
        }
    }

    @Override
    public void onLearnSkill(ManasSkillInstance instance, LivingEntity living, UnlockSkillEvent event) {
        CompoundTag tag = instance.getOrCreateTag();
        tag.put("alternate_type", AlternateType.DETECTABLE.toNBT());
    }
}
