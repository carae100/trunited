package io.github.dracosomething.trawakened.ability.skill.ultimate;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.event.UnlockSkillEvent;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.mojang.math.Vector3f;
import io.github.dracosomething.trawakened.capability.ShadowCapability.AwakenedShadowCapability;
import io.github.dracosomething.trawakened.event.BecomeShadowEvent;
import io.github.dracosomething.trawakened.helper.skillHelper;
import io.github.dracosomething.trawakened.library.MonarchsDomain;
import io.github.dracosomething.trawakened.library.shadowRank;
import io.github.dracosomething.trawakened.mobeffect.MonarchsDomainEffect;
import io.github.dracosomething.trawakened.network.TRAwakenedNetwork;
import io.github.dracosomething.trawakened.network.play2client.OpenBecomeShadowscreen;
import io.github.dracosomething.trawakened.network.play2client.OpenNamingscreen;
import io.github.dracosomething.trawakened.network.play2client.openWordScreen;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ShadowMonarch extends Skill {
    private CompoundTag ShadowStorage;
    private CompoundTag data;

    public ShadowMonarch() {
        super(SkillType.ULTIMATE);
        ShadowStorage = new CompoundTag();
        data  = new CompoundTag();
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

    public Component getModeName(int mode) {
        return switch (mode) {
            case 1 -> Component.translatable("trawakened.skill.shadow_monarch.mode.arise");
            case 2 -> Component.translatable("trawakened.skill.shadow_monarch.mode.storage");
            case 3 -> Component.translatable("trawakened.skill.shadow_monarch.mode.marking");
            case 4 -> data.getBoolean("awakened") ?
                    Component.translatable("trawakened.skill.shadow_monarch.awakened.mode.monarchs_domain", data.getString("mode_name")) :
                    Component.translatable("trawakened.skill.shadow_monarch.mode.monarchs_domain");
            default -> Component.empty();
        };
    }

    public void onLearnSkill(ManasSkillInstance instance, LivingEntity living, UnlockSkillEvent event) {
        instance.getOrCreateTag().putInt("maxStorage", 5);
        instance.getOrCreateTag().put("ShadowStorage", ShadowStorage);
        instance.getOrCreateTag().put("data", data);
        if (living instanceof ServerPlayer player) {
            TRAwakenedNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new openWordScreen(player.getUUID(), instance));
        }
        System.out.println(instance.getOrCreateTag());
    }

    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        Random random = new Random();
        LivingEntity target = SkillHelper.getTargetingEntity(entity, ForgeMod.REACH_DISTANCE.get().getDefaultValue(), false, true);
        List<LivingEntity> targetList = skillHelper.GetLivingEntitiesInRange(entity, 50, false);
        switch (instance.getMode()) {
            case 1:
                if (!entity.isShiftKeyDown()) {
                    if (AwakenedShadowCapability.isShadow(target) && !AwakenedShadowCapability.isArisen(target)) {
                        if (target != null && AwakenedShadowCapability.getTries(target) > 0) {
                            skillHelper.sendMessageToNearbyPlayersWithSource(30, entity, Component.translatable("message.arise", getCommandWord()));
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
                                    if (target instanceof Mob mob && mob.isNoAi()) {
                                        mob.setNoAi(false);
                                    }
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
                                } else if (entity instanceof Player player) {
                                    player.sendSystemMessage(Component.translatable("message.tries_left", AwakenedShadowCapability.getTries(target), target.getName()));
                                }
                            }
                        }
                    }
                } else {
                    skillHelper.sendMessageToNearbyPlayersWithSource(30, entity, Component.translatable("message.arise", getCommandWord()));
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
                        if (ShadowStorage.getAllKeys().stream().findFirst().isPresent()) {
                            AwakenedShadowCapability.setHasShadow(target, true);
                            String first = ShadowStorage.getAllKeys().stream().findFirst().get();
                            ShadowStorage.getCompound(first).putString("UUID", first);
                            AwakenedShadowCapability.setStorage(target, ShadowStorage.getCompound(first));
                            ShadowStorage.getCompound(first).remove("UUID");
                            target.addEffect(new MobEffectInstance(MobEffects.GLOWING));
                            ShadowStorage.remove(first);
                        }
                    }
                }
                break;
            case 4:
                if (!data.getBoolean("awakened")) {
                    if (this.data.getCompound("domain").isEmpty()) {
                        MonarchsDomain domain = new MonarchsDomain(entity, 18000, 50, 50);
                        domain.setInstance(instance);
                        this.data.put("domain", domain.toNBT());
                        instance.getOrCreateTag().put("data", data);
                        if (!SkillHelper.outOfMagicule(entity, domain.calculateMPCost())) {
                            domain.place();
                            instance.setCoolDown(150);
                        }
                    }
                } else {
                    switch (data.getInt("mode_domain")) {
                        case 1:
                            if (this.data.getCompound("domain").isEmpty()) {
                                MonarchsDomain domain = new MonarchsDomain(entity, 18000, 200);
                                domain.setInstance(instance);
                                this.data.put("domain", domain.toNBT());
                                instance.getOrCreateTag().put("data", data);
                                if (!SkillHelper.outOfMagicule(entity, domain.calculateMPCost())) {
                                    domain.place();
                                    instance.setCoolDown(150);
                                }
                            }
                            break;
                        case 2:
                            if (this.data.getCompound("domain").isEmpty()) {
                                MonarchsDomain domain = new MonarchsDomain(entity, 600, entity.level.getWorldBorder().getSize());
                                domain.setInstance(instance);
                                this.data.put("domain", domain.toNBT());
                                instance.getOrCreateTag().put("data", data);
                                if (!SkillHelper.outOfMagicule(entity, domain.calculateMPCost())) {
                                    domain.place();
                                    instance.setCoolDown(150);
                                }
                            }
                            break;
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

    public void setCommandWord(String commandWord) {
        this.data.putString("commandWord", commandWord);
    }

    public String getCommandWord() {
        return this.data.getString("commandWord");
    }

    public CompoundTag getData() {
        return data;
    }

    public void setData(CompoundTag data) {
        this.data = data;
    }
}
