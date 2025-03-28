package io.github.dracosomething.trawakened.ability.skill.ultimate;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.event.UnlockSkillEvent;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.data.TensuraTags;
import com.github.manasmods.tensura.entity.human.HinataSakaguchiEntity;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import io.github.dracosomething.trawakened.capability.ShadowCapability.AwakenedShadowCapability;
import io.github.dracosomething.trawakened.event.BecomeShadowEvent;
import io.github.dracosomething.trawakened.helper.skillHelper;
import io.github.dracosomething.trawakened.library.shadowRank;
import io.github.dracosomething.trawakened.network.TRAwakenedNetwork;
import io.github.dracosomething.trawakened.network.play2client.OpenBecomeShadowscreen;
import io.github.dracosomething.trawakened.network.play2client.OpenNamingscreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ForgeEntityTypeTagsProvider;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.Objects;
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
            case 3 -> Component.translatable("trawakened.skill.shadow_monarch.mode.marking");
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
                skillHelper.sendMessageToNearbyPlayersWithSource(30, entity, Component.translatable("message.arise"));
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
                                    shadowRank rank = shadowRank.calculateRank(target);
                                    SkillHelper.setFollow(target);
                                    AwakenedShadowCapability.setRank(target, rank);
                                    AwakenedShadowCapability.setOwnerUUID(target, entity.getUUID());
                                    BecomeShadowEvent event = new BecomeShadowEvent(target, entity, true);
                                    MinecraftForge.EVENT_BUS.post(event);
                                    if (target.getType().getTags().toList().contains(Tags.EntityTypes.BOSSES) ||
                                            target instanceof Player) {
                                        if (entity instanceof Player player && player instanceof ServerPlayer serverPlayer) {
                                            TRAwakenedNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new OpenNamingscreen(target.getUUID()));
                                        }
                                    }
                                    AwakenedShadowCapability.sync(target);
                                    instance.addMasteryPoint(entity);
                                }
                            }
                        }
                    }
                } else {
                    targetList.forEach((living -> {
                        if (AwakenedShadowCapability.isShadow(living) && !AwakenedShadowCapability.isArisen(living)) {
                            if (living != null && AwakenedShadowCapability.getTries(living) > 0) {
                                if (TensuraEPCapability.getCurrentEP(living) * 0.75 <= TensuraEPCapability.getCurrentEP(entity)) {
                                    target.setHealth(target.getMaxHealth());
                                    AwakenedShadowCapability.setArisen(target, true);
                                    target.removeEffect(TensuraMobEffects.PRESENCE_CONCEALMENT.get());
                                    target.removeEffect(MobEffects.DAMAGE_RESISTANCE);
                                    target.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                                    skillHelper.tameAnything(entity, target, this);
                                    shadowRank rank = shadowRank.calculateRank(target);
                                    SkillHelper.setFollow(target);
                                    AwakenedShadowCapability.setRank(target, rank);
                                    AwakenedShadowCapability.setOwnerUUID(target, entity.getUUID());
                                    BecomeShadowEvent event = new BecomeShadowEvent(target, entity, true);
                                    MinecraftForge.EVENT_BUS.post(event);
                                    if (target.getType().getTags().toList().contains(Tags.EntityTypes.BOSSES) ||
                                            target instanceof Player) {
                                        if (entity instanceof Player player && player instanceof ServerPlayer serverPlayer) {
                                            TRAwakenedNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new OpenNamingscreen(target.getUUID()));
                                        }
                                    }
                                    AwakenedShadowCapability.sync(target);
                                    instance.addMasteryPoint(entity);
                                }
                            }
                        }
                    }));
                }
                break;
            case 2:
                if (AwakenedShadowCapability.isArisen(target) &&
                        Objects.equals(AwakenedShadowCapability.getOwnerUUID(target).toString(), entity.getUUID().toString()) &&
                        AwakenedShadowCapability.isShadow(target)) {
                    if (ShadowStorage.getAllKeys().size() < instance.getOrCreateTag().getInt("maxStorage")) {
                        ShadowStorage.put(target.getUUID().toString(), shadowToNBT(target));
                        instance.getOrCreateTag().put("ShadowStorage", ShadowStorage);
                        setShadowStorage(instance.getOrCreateTag().getCompound("ShadowStorage"));
                        target.discard();
                    } else if (entity instanceof Player player) {
                        player.sendSystemMessage(Component.translatable("trawakened.monarch_shadow.full_storage"));
                    }
                }
                break;
            case 3:
                if (target instanceof Player) {
                    if (!AwakenedShadowCapability.isShadow(target) &&
                            AwakenedShadowCapability.hasShadow(target)) {
                        AwakenedShadowCapability.setHasShadow(target, true);
                        if (ShadowStorage.getAllKeys().stream().findFirst().isPresent()) {
                            String first = ShadowStorage.getAllKeys().stream().findFirst().get();
                            ShadowStorage.putString("UUID", first);
                            AwakenedShadowCapability.setStorage(target, ShadowStorage.getCompound(first));
                            System.out.println(AwakenedShadowCapability.getStorage(target));
                            target.addEffect(new MobEffectInstance(MobEffects.GLOWING));
                            ShadowStorage.remove(first);
                        }
                    }
                }
                break;
        }
    }

    public boolean canTick(ManasSkillInstance instance, LivingEntity entity) {
        return true;
    }

    public void onTick(ManasSkillInstance instance, LivingEntity living) {
        int masteryPercentige = (int)((float)(instance.getMastery() * 100 / instance.getMaxMastery()));
        int maxShadows = (5 * masteryPercentige) + 5;
        instance.getOrCreateTag().putInt("maxStorage", maxShadows);
    }

    private CompoundTag shadowToNBT (LivingEntity entity) {
        CompoundTag tag = new CompoundTag();
        tag.put("EntityData", entity.serializeNBT());
        tag.putString("entityType", EntityType.getKey(entity.getType()).toString());
        tag.put("rank", AwakenedShadowCapability.getRank(entity).toNBT());
        if (!entity.getDisplayName().toString().isEmpty() || !entity.getDisplayName().toString().equals("")) {
            tag.putString("name", entity.getDisplayName().toString());
        }
        tag.putDouble("EP", TensuraEPCapability.getCurrentEP(entity));
        return tag;
    }

    public CompoundTag getShadowStorage() {
        return ShadowStorage;
    }

    public void setShadowStorage(CompoundTag shadowStorage) {
        ShadowStorage = shadowStorage;
    }
}
