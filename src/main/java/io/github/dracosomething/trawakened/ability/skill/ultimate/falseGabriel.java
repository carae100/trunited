package io.github.dracosomething.trawakened.ability.skill.ultimate;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.event.UnlockSkillEvent;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.AwakenedFearCapability;
import io.github.dracosomething.trawakened.helper.FearHelper;
import io.github.dracosomething.trawakened.helper.skillHelper;
import io.github.dracosomething.trawakened.library.AlternateType;
import io.github.dracosomething.trawakened.library.FearTypes;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import io.github.dracosomething.trawakened.world.trawakenedGamerules;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
        return 5;
    }

    @Override
    public int nextMode(LivingEntity entity, TensuraSkillInstance instance, boolean reverse) {
        if (reverse) {
            return instance.getMode() == 1 ? 5 : instance.getMode() - 1;
        } else {
            return instance.getMode() == 5 ? 1 : instance.getMode() + 1;
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
        }
        return null;
    }

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        List<Entity> entityList = skillHelper.DrawSphereAndGetEntitiesInIt(entity, 80, true).stream().filter((entity1 -> {
            return entity1 instanceof LivingEntity && !SkillUtils.hasSkill(entity1, skillRegistry.ALTERNATE.get()) && entity1 != null;
        })).toList();
        LivingEntity target = SkillHelper.getTargetingEntity(entity, 7, false);
        CompoundTag tag = instance.getOrCreateTag();
        switch (instance.getMode()) {
            case 1:
                if (!SkillHelper.outOfMagicule(entity, instance)) {
                    for (Entity creature : entityList) {
                        if (!creature.equals(entity)) {
                            if (creature instanceof LivingEntity living) {
                                if (living instanceof Player player) {
                                    LivingEntity Clone = skillHelper.summonClone(player, entity, entity.level, TensuraPlayerCapability.getBaseMagicule(player), new Vec3(player.getX(), player.getY(), player.getZ()), this);
                                    SkillUtils.learnSkill(Clone, skillRegistry.ALTERNATE.get());
                                    ManasSkillInstance alternate = SkillUtils.getSkillOrNull(Clone, skillRegistry.ALTERNATE.get());
                                    CompoundTag alternateTag = alternate.getOrCreateTag();
                                    AwakenedFearCapability.SetIsAlternate(player, true);
                                    entity.removeEffect(TensuraMobEffects.PRESENCE_CONCEALMENT.get());
                                    AlternateType.Assimilation assimilation = AlternateType.Assimilation.getRandomAssimilation();
                                    alternateTag.put("assimilation", assimilation.toNBT());
                                    alternateTag.put("alternate_type", assimilation.getType().toNBT());
                                } else {
                                    Entity CloneAsEntity = skillHelper.cloneMob(entity, living.getType());
                                    if (CloneAsEntity instanceof LivingEntity Clone) {
                                        SkillUtils.learnSkill(Clone, skillRegistry.ALTERNATE.get());
                                        ManasSkillInstance alternate = SkillUtils.getSkillOrNull(Clone, skillRegistry.ALTERNATE.get());
                                        CompoundTag tagAlternate = alternate.getOrCreateTag();
                                        AwakenedFearCapability.SetIsAlternate(Clone, true);
                                        entity.removeEffect(TensuraMobEffects.PRESENCE_CONCEALMENT.get());
                                        AlternateType.Assimilation assimilation = AlternateType.Assimilation.getRandomAssimilation();
                                        tagAlternate.put("assimilation", assimilation.toNBT());
                                        tagAlternate.put("alternate_type", assimilation.getType().toNBT());
                                    }
                                }
                            }
                        }
                    }
                    instance.setCoolDown(30);
                }
                break;
            case 2:
                if (!entityList.isEmpty()) {
                    if (!SkillHelper.outOfMagicule(entity, instance)) {
                        entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.WITHER_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);
                        entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PHANTOM_DEATH, SoundSource.PLAYERS, 1.0F, 1.0F);
                        entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.CREEPER_HURT, SoundSource.PLAYERS, 1.0F, 1.0F);
                        entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.AMBIENT_CAVE, SoundSource.PLAYERS, 1.0F, 1.0F);
                        entityList.forEach((Entity) -> {
                            if (!Entity.equals(entity)) {
                                if (Entity instanceof LivingEntity livingEntity) {
                                    if (entity.level.getGameRules().getBoolean(trawakenedGamerules.NORMAL_FEAR)) {
                                        AwakenedFearCapability.setScared(livingEntity, AwakenedFearCapability.getScared(livingEntity) + 25);
                                    } else {
                                        FearHelper.fearPenalty(livingEntity, AwakenedFearCapability.getScared(livingEntity) + 25);
                                    }
                                    SkillHelper.addEffectWithSource(livingEntity, livingEntity, effectRegistry.BRAINDAMAGE.get(), 1000, 10, false, false, false, false);
                                }
                            }
                        });
                        if (entity instanceof Player) {
                            Player player = (Player) entity;
                            player.displayClientMessage(Component.translatable("trawakened.fear.inspire_fear", new Object[]{entityList.size()}).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                        }
                        instance.setCoolDown(100);
                        instance.addMasteryPoint(entity);
                    }
                } else {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        player.displayClientMessage(Component.translatable("tensura.targeting.not_targeted").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                    }
                }
                break;
            case 3:
                if (!SkillHelper.outOfMagicule(entity, instance)) {
                    instance.getOrCreateTag().putBoolean("enabled", !instance.getOrCreateTag().getBoolean("enabled"));
                }
                break;
            case 4:
                if (!SkillHelper.outOfMagicule(entity, instance)) {
                    if (!entity.isShiftKeyDown()) {
                        AwakenedFearCapability.setScared(target, AwakenedFearCapability.getScared(target) + 35);
                    } else {
                        if (!entityList.isEmpty()) {
                            for (Entity entity1 : entityList) {
                                if (entity1 instanceof LivingEntity living) {
                                    AwakenedFearCapability.setScared(target, AwakenedFearCapability.getScared(target) + 15);
                                }
                            }
                        } else {
                            if (entity instanceof Player) {
                                Player player = (Player) entity;
                                player.displayClientMessage(Component.translatable("tensura.targeting.not_targeted").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                            }
                        }
                    }
                    instance.setCoolDown(50);
                }
                break;
            case 5:
                if (!entity.isShiftKeyDown()) {
                    if (!SkillHelper.outOfMagicule(entity, instance)) {
                        FearTypes fearTypes = FearTypes.fromString(instance.getOrCreateTag().getString("new_fear"));
                        AwakenedFearCapability.setFearType(target, fearTypes);
                        instance.setCoolDown(100);
                    }
                }
                break;
        }
    }

    @Override
    public void onScroll(ManasSkillInstance instance, LivingEntity living, double delta) {
        if (living.isShiftKeyDown()) {
            if (instance.getMode() == 5) {
                String newRange = switch (instance.getOrCreateTag().getString("new_fear")) {
                    case "religion" -> "alternate";
                    case "alternate" -> "dark";
                    case "dark" -> "ocean";
                    case "ocean" -> "creeper";
                    case "creeper" -> "otherworlder";
                    case "otherworlder" -> "liquid";
                    case "liquid" -> "trypofobia";
                    case "trypofobia" -> "spider";
                    case "spider" -> "heat";
                    case "heat" -> "cold";
                    case "cold" -> "germ";
                    case "germ" -> "insect";
                    case "insect" -> "height";
                    case "height" -> "religion";
                    default -> "";
                };
                if (instance.getOrCreateTag().getString("new_fear") != newRange) {
                    instance.getOrCreateTag().putString("new_fear", newRange);
                    if (living instanceof Player) {
                        Player player = (Player)living;
                        player.displayClientMessage(Component.translatable("trawakened.skill.thought.mode", new Object[]{newRange}).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA)), true);
                    }

                    instance.markDirty();
                }
            }
        }
    }

    @Override
    public boolean canTick(ManasSkillInstance instance, LivingEntity entity) {
        return true;
    }

    @Override
    public void onTick(ManasSkillInstance instance, LivingEntity living) {
        List<Entity> entityList = skillHelper.DrawSphereAndGetEntitiesInIt(living, instance.isMastered(living)?20:15, true);
        for (Entity entity : entityList) {
            if (entity instanceof LivingEntity livingEntity) {
                SkillHelper.checkThenAddEffectSource(livingEntity, living, effectRegistry.FEAR_AMPLIFICATION.get(), 60, 1, false, false, false, false);
            }
        }
        CompoundTag tag = instance.getOrCreateTag();
        if (tag.getBoolean("enabled")) {
            living.addEffect(new MobEffectInstance(TensuraMobEffects.PRESENCE_CONCEALMENT.get(), 120, 255, false, false, false));
        }
        living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 4, false, false, false));
        living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 4, false, false, false));
        living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 4, false, false, false));
        living.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 2, false, false, false));
    }

    @Override
    public void onLearnSkill(ManasSkillInstance instance, LivingEntity living, UnlockSkillEvent event) {
        if (SkillUtils.hasSkill(living, skillRegistry.ALTERNATE.get())) {
            SkillAPI.getSkillsFrom(living).forgetSkill(skillRegistry.ALTERNATE.get());
        }
        CompoundTag tag = instance.getOrCreateTag();
        tag.put("alternate_type", AlternateType.DETECTABLE.toNBT());
    }
}
