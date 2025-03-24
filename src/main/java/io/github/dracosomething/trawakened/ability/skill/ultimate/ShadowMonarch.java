package io.github.dracosomething.trawakened.ability.skill.ultimate;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.event.UnlockSkillEvent;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.data.TensuraTags;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import io.github.dracosomething.trawakened.capability.ShadowCapability.AwakenedShadowCapability;
import io.github.dracosomething.trawakened.event.BecomeShadowEvent;
import io.github.dracosomething.trawakened.helper.skillHelper;
import io.github.dracosomething.trawakened.network.TRAwakenedNetwork;
import io.github.dracosomething.trawakened.network.play2client.OpenBecomeShadowscreen;
import io.github.dracosomething.trawakened.network.play2client.OpenNamingscreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.Random;

public class ShadowMonarch extends Skill {
    private CompoundTag ShadowStorage;

    public ShadowMonarch() {
        super(SkillType.ULTIMATE);
        ShadowStorage = new CompoundTag();
    }

    public int modes() {
        return 5;
    }

    public int nextMode(LivingEntity entity, TensuraSkillInstance instance, boolean reverse) {
        if (reverse) {
            return instance.getMode() == 1 ? 5 : instance.getMode() - 1;
        } else {
            return instance.getMode() == 5 ? 1 : instance.getMode() + 1;
        }
    }

    @Override
    public Component getModeName(int mode) {
        return switch (mode) {
            case 1 -> Component.translatable("trawakened.skill.shadow_monarch.mode.arise");
            case 2 -> Component.translatable("trawakened.skill.shadow_monarch.mode.storage");
            default -> Component.empty();
        };
    }

    public void onLearnSkill(ManasSkillInstance instance, LivingEntity living, UnlockSkillEvent event) {
        instance.getOrCreateTag().putInt("maxStorage", 5);
        instance.getOrCreateTag().put("ShadowStorage", ShadowStorage);
    }

    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        Random random = new Random();
        LivingEntity target = SkillHelper.getTargetingEntity(entity, ForgeMod.REACH_DISTANCE.get().getDefaultValue(), false, true);
        List<LivingEntity> targetList = skillHelper.GetLivingEntities(entity, 50, false);
        switch (instance.getMode()) {
            case 1:
                if (!entity.isShiftKeyDown()) {
                    if (AwakenedShadowCapability.isShadow(target) && !AwakenedShadowCapability.isArisen(target)) {
                        if (target != null && AwakenedShadowCapability.getTries(target) > 0) {
                            int chance;
                            if (TensuraEPCapability.getCurrentEP(target) * 0.75 <= TensuraEPCapability.getCurrentEP(entity)) {
                                chance = 100;
                            } else {
                                chance = (TensuraEPCapability.getCurrentEP(target) <= TensuraEPCapability.getCurrentEP(entity) ? 50 : 30);
                            }
                            if (target instanceof Player player && player instanceof ServerPlayer serverPlayer) {
                                TRAwakenedNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new OpenBecomeShadowscreen(entity.getUUID()));
                            } else {
                                AwakenedShadowCapability.setTries(target, AwakenedShadowCapability.getTries(target) - 1);
                                if (AwakenedShadowCapability.getTries(target) == 0 ) {
                                    target.discard();
                                    return;
                                }
                                if (entity instanceof Player player) {
                                    player.sendSystemMessage(Component.translatable("message.tries_left", AwakenedShadowCapability.getTries(target), target.getName()));
                                }
                                if (random.nextInt(0, 100) <= chance) {
                                    target.setHealth(target.getMaxHealth());
                                    AwakenedShadowCapability.setArisen(target, true);
                                    target.removeEffect(TensuraMobEffects.PRESENCE_CONCEALMENT.get());
                                    target.removeEffect(MobEffects.DAMAGE_RESISTANCE);
                                    target.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                                    skillHelper.tameAnything(entity, target, this);
                                    AwakenedShadowCapability.setOwnerUUID(target, entity.getUUID());
                                    BecomeShadowEvent event = new BecomeShadowEvent(target, entity, true);
                                    MinecraftForge.EVENT_BUS.post(event);
                                    if (target.getType().getTags().toList().contains(TensuraTags.EntityTypes.HERO_BOSS) || target instanceof Player) {
                                        if (entity instanceof Player player && player instanceof ServerPlayer serverPlayer) {
                                            TRAwakenedNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new OpenNamingscreen(target.getUUID()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    targetList.forEach((living -> {
                        if (TensuraEPCapability.getCurrentEP(target) * 0.75 <= TensuraEPCapability.getCurrentEP(entity)) {
                            AwakenedShadowCapability.setTries(target, AwakenedShadowCapability.getTries(target) - 1);
                            AwakenedShadowCapability.setArisen(target, true);
                            AwakenedShadowCapability.setOwnerUUID(target, entity.getUUID());
                        }
                    }));
                }
                break;
            case 2:
                if (AwakenedShadowCapability.isArisen(target) &&
                        AwakenedShadowCapability.getOwnerUUID(target) == entity.getUUID() &&
                        AwakenedShadowCapability.isShadow(target)) {
                    if (ShadowStorage.getAllKeys().size() <= instance.getOrCreateTag().getInt("maxStorage")) {
                        ShadowStorage.put(target.getUUID().toString(), shadowToNBT(target));
                        instance.getOrCreateTag().put("ShadowStorage", ShadowStorage);
                        target.discard();
                    }
                }
                break;
        }
    }

    public boolean canTick(ManasSkillInstance instance, LivingEntity entity) {
        return true;
    }

    public void onTick(ManasSkillInstance instance, LivingEntity living) {
        int masteryPercentige = (int)((float)(106 * (instance.getMastery() + 100)) / 100.0F);
        int maxShadows = (5 * masteryPercentige) + 5;
        instance.getOrCreateTag().putInt("maxShadows", maxShadows);
    }

    private CompoundTag shadowToNBT (LivingEntity entity) {
        CompoundTag tag = new CompoundTag();
        tag.put("EntityData", entity.serializeNBT());
        tag.putString("entityType", EntityType.getKey(entity.getType()).toString());
        return tag;
    }
}
