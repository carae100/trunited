package io.github.dracosomething.trawakened.ability.skill.ultimate;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.event.UnlockSkillEvent;
import com.github.manasmods.manascore.skill.SkillRegistry;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.AwakenedFearCapability;
import io.github.dracosomething.trawakened.helper.skillHelper;
import io.github.dracosomething.trawakened.library.AlternateType;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

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
        return 6;
    }

    @Override
    public int nextMode(LivingEntity entity, TensuraSkillInstance instance, boolean reverse) {
        if (reverse) {
            return instance.getMode() == 1 ? 6 : instance.getMode() - 1;
        } else {
            return instance.getMode() == 6 ? 1 : instance.getMode() + 1;
        }
    }

    @Override
    public Component getModeName(int mode) {
        switch (mode) {
            case 1 -> {
                return Component.translatable("trawakened.skill.mode.false_gabriel.create_alternates");
            }
            case 2 -> {
                return Component.translatable("trawakened.skill.mode.false_gabriel.suicide");
            }
            case 3 -> {
                return Component.translatable("trawakened.skill.mode.false_gabriel.metaphysical_plane");
            }
            case 4 -> {
                return Component.translatable("trawakened.skill.mode.false_gabriel.scare");
            }
            case 5 -> {
                return Component.translatable("trawakened.skill.mode.false_gabriel.new_fear");
            }
            case 6 -> {
                return Component.translatable("trawakened.skill.mode.false_gabriel.convert");
            }
        }
        return null;
    }

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        List<Entity> entityList = skillHelper.DrawCircle(entity, 80, true);
        LivingEntity target = SkillHelper.getTargetingEntity(entity, 7, false);
        switch (instance.getMode()) {
            case 1:
                for (Entity creature : entityList) {
                    if (creature instanceof LivingEntity living) {
                        if (living instanceof Player player) {
                            LivingEntity Clone = skillHelper.summonClone(player, entity, entity.level, TensuraPlayerCapability.getBaseMagicule(player), new Vec3(player.getX(), player.getY(), player.getZ()), this);
                            SkillUtils.learnSkill(Clone, skillRegistry.ALTERNATE.get());
                            ManasSkillInstance alternate = SkillUtils.getSkillOrNull(Clone, skillRegistry.ALTERNATE.get());
                            CompoundTag tag = alternate.getOrCreateTag();
                            AwakenedFearCapability.SetIsAlternate(entity, true);
                            entity.removeEffect(TensuraMobEffects.PRESENCE_CONCEALMENT.get());
                            AlternateType.Assimilation assimilation = AlternateType.Assimilation.getRandomAssimilation();
                            tag.put("assimilation", assimilation.toNBT());
                            tag.put("alternate_type", assimilation.getType().toNBT());
                        } else {
                            Entity CloneAsEntity = skillHelper.cloneMob(entity, living.getType());
                            if (CloneAsEntity instanceof LivingEntity Clone) {
                                SkillUtils.learnSkill(Clone, skillRegistry.ALTERNATE.get());
                                ManasSkillInstance alternate = SkillUtils.getSkillOrNull(Clone, skillRegistry.ALTERNATE.get());
                                CompoundTag tag = alternate.getOrCreateTag();
                                AwakenedFearCapability.SetIsAlternate(entity, true);
                                entity.removeEffect(TensuraMobEffects.PRESENCE_CONCEALMENT.get());
                                AlternateType.Assimilation assimilation = AlternateType.Assimilation.getRandomAssimilation();
                                tag.put("assimilation", assimilation.toNBT());
                                tag.put("alternate_type", assimilation.getType().toNBT());
                            }
                        }
                    }
                }
                break;
        }
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
