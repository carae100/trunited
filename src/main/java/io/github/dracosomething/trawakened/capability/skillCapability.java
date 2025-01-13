package io.github.dracosomething.trawakened.capability;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.capability.SkillStorage;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.ability.skill.unique.DegenerateSkill;
import com.github.manasmods.tensura.ability.skill.unique.StarvedSkill;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.config.TensuraConfig;
import com.github.manasmods.tensura.data.TensuraTags;
import com.github.manasmods.tensura.entity.magic.breath.BreathEntity;
import com.github.manasmods.tensura.world.TensuraGameRules;
import io.github.dracosomething.trawakened.registry.skillregistry;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class skillCapability {
    public static void devourAllSkills(LivingEntity target, LivingEntity owner){
        devourAllSkills2(target, owner);
    }

    private static void devourAllSkills2(LivingEntity target, LivingEntity owner) {
        if (!target.getType().is(TensuraTags.EntityTypes.NO_SKILL_PLUNDER)) {
            List<ManasSkillInstance> targetSkills = SkillAPI.getSkillsFrom(target).getLearnedSkills().stream().filter(skillCapability::canDevour).toList();
            Iterator var4 = targetSkills.iterator();

            while(var4.hasNext()) {
                ManasSkillInstance targetInstance = (ManasSkillInstance)var4.next();
                if (!targetInstance.isTemporarySkill() && targetInstance.getMastery() >= 0 && SkillUtils.learnSkill(owner, targetInstance.getSkill(), skillregistry.AZAZEL.hashCode())) {
                    if (owner instanceof Player) {
                        Player player = (Player)owner;
                        player.displayClientMessage(Component.translatable("tensura.skill.acquire", new Object[]{targetInstance.getSkill().getName()}).setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)), false);
                    }

                    owner.getLevel().playSound((Player)null, owner.getX(), owner.getY(), owner.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            }

        }
    }

    protected static boolean canDevour(ManasSkillInstance instance) {
        if (!instance.isTemporarySkill() && instance.getMastery() >= 0) {
            if (instance.getSkill() == skillregistry.AZAZEL.get()) {
                return false;
            } else if (instance.getSkill() instanceof StarvedSkill) {
                return true;
            } else if (instance.getSkill() instanceof DegenerateSkill) {
                return true;
            } else {
                ManasSkill var3 = instance.getSkill();
                if (!(var3 instanceof Skill)) {
                    return false;
                } else {
                    Skill devouredSkill = (Skill)var3;
                    return devouredSkill.getType().equals(Skill.SkillType.INTRINSIC) || devouredSkill.getType().equals(Skill.SkillType.COMMON) || devouredSkill.getType().equals(Skill.SkillType.EXTRA) || devouredSkill.getType().equals(Skill.SkillType.RESISTANCE);
                }
            }
        } else {
            return false;
        }
    }

    public static void devourEP(LivingEntity target, LivingEntity owner, float amountToMax) {
        if (!target.getType().is(TensuraTags.EntityTypes.NO_EP_PLUNDER)) {
            SkillStorage storage = SkillAPI.getSkillsFrom(owner);
            Optional<ManasSkillInstance> predator = storage.getSkill(skillregistry.AZAZEL.get());
            if (!predator.isEmpty()) {
                ManasSkillInstance instance = (ManasSkillInstance)predator.get();
                CompoundTag tag = instance.getOrCreateTag();
                CompoundTag predationList;
                if (tag.contains("predationList")) {
                    predationList = (CompoundTag)tag.get("predationList");
                    if (predationList == null) {
                        return;
                    }

                    String targetID = EntityType.getKey(target.getType()).toString();
                    if (predationList.contains(targetID)) {
                        return;
                    }

                    predationList.putBoolean(targetID, true);
                    instance.markDirty();
                } else {
                    predationList = new CompoundTag();
                    predationList.putBoolean(EntityType.getKey(target.getType()).toString(), true);
                    tag.put("predationList", predationList);
                    instance.markDirty();
                }

                storage.syncChanges();
                double EP = Math.min(SkillUtils.getEPGain(target, owner), (Double) TensuraConfig.INSTANCE.skillsConfig.maximumEPSteal.get() / (double)amountToMax);
                if (target instanceof Player) {
                    Player playerTarget = (Player)target;
                    if (TensuraGameRules.canEpSteal(target.getLevel())) {
                        int minEP = TensuraGameRules.getMinEp(target.getLevel());
                        if (minEP > 0) {
                            EP -= (double)minEP;
                        }

                        if (EP <= 0.0) {
                            return;
                        }

                        SkillHelper.gainMaxMP(owner, EP * (double)amountToMax);
                        TensuraEPCapability.setSkippingEPDrop(target, true);
                        saveMagiculeIntoStorage(owner, EP * (double)(1.0F - amountToMax));
                        double finalEP = EP;
                        TensuraPlayerCapability.getFrom(playerTarget).ifPresent((cap) -> {
                            cap.setBaseMagicule(cap.getBaseMagicule() - finalEP / 2.0, playerTarget);
                            cap.setBaseAura(cap.getBaseAura() - finalEP / 2.0, playerTarget);
                        });
                        TensuraPlayerCapability.sync(playerTarget);
                    }
                } else {
                    SkillHelper.gainMaxMP(owner, EP * (double)amountToMax);
                    saveMagiculeIntoStorage(owner, EP * (double)(1.0F - amountToMax));
                    SkillHelper.reduceEP(target, owner, 1.0, true, true);
                    TensuraEPCapability.setSkippingEPDrop(target, true);
                }

            }
        }
    }


    protected static void saveMagiculeIntoStorage(LivingEntity owner, double amount) {
        ManasSkillInstance instance = getSkillInstance(owner);
        if (instance != null) {
            CompoundTag tag = instance.getOrCreateTag();
            tag.putDouble("MpStomach", tag.getDouble("MpStomach") + amount);
            instance.markDirty();
        }
    }

    protected static ManasSkillInstance getSkillInstance(LivingEntity owner) {
        SkillStorage storage = SkillAPI.getSkillsFrom(owner);
        Optional<ManasSkillInstance> optional = storage.getSkill(skillregistry.AZAZEL.get());
        return (ManasSkillInstance)optional.orElse((ManasSkillInstance) null);
    }
}
