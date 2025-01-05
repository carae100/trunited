package io.github.dracosomething.trawakened.mixin;

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
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.config.TensuraConfig;
import com.github.manasmods.tensura.data.TensuraTags;
import com.github.manasmods.tensura.entity.magic.breath.BreathEntity;
import com.github.manasmods.tensura.entity.magic.breath.BreathPart;
import com.github.manasmods.tensura.entity.magic.breath.GluttonyMistProjectile;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import com.github.manasmods.tensura.world.TensuraGameRules;
import com.mojang.math.Vector3f;
import io.github.dracosomething.trawakened.registry.particleRegistry;
import io.github.dracosomething.trawakened.util.trawakenedDamage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Mixin(GluttonyMistProjectile.class)
public class GluttonyMistProjectileMixin {
    @Unique
    private static Entity test_addon$getOwner2(Projectile projectile){
        return projectile.getOwner();
    }

    @Inject(
            method = "onHitEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z")
    )
    public void Kill(EntityHitResult entityHitResult, CallbackInfo ci){
        LivingEntity entity = (LivingEntity) entityHitResult.getEntity();
        DamageSource damageSource = TensuraDamageSources.DEVOURED;
        Entity var6 = test_addon$getOwner2((Projectile) (Object) this);
        LivingEntity owner;
        if (var6 instanceof LivingEntity) {
            owner = (LivingEntity)var6;
            damageSource = trawakenedDamage.assimilation(owner);
        }
        if(test_addon$getOwner2((Projectile) (Object) this) != null){
            if(SkillUtils.hasSkill(test_addon$getOwner2((Projectile) (Object) this), Objects.requireNonNull(SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:starkill"))))){
                if(entity.hurt(DamageSourceHelper.addSkillAndCost(damageSource, 20.0,  SkillUtils.getSkillOrNull(test_addon$getOwner2((Projectile) (Object) this), Objects.requireNonNull(SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:starkill"))))).bypassArmor().bypassEnchantments().bypassMagic(), entity.getMaxHealth()/4)){
                    var6 = test_addon$getOwner2((Projectile) (Object) this);
                    if (var6 instanceof LivingEntity) {
                        owner = (LivingEntity)var6;

                            this.devourAllSkills(entity, owner);
                            this.devourEP(entity, owner, 0.8F);
                            if (owner instanceof Player) {
                                Player player = (Player)owner;
                                List<ItemEntity> list = owner.level.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(entity.position(), 2.0, 2.0, 2.0));
                                Iterator var8 = list.iterator();

                                while(var8.hasNext()) {
                                    ItemEntity item = (ItemEntity)var8.next();
                                    if (player.addItem(item.getItem())) {
                                        item.discard();
                                    } else {
                                        item.moveTo(owner.position());
                                    }
                                }
                            }
                        }
                }
            }
        }
    }

    @Inject(
            method = "spawnParticle",
            at = @At("HEAD"),
            remap = false,
            cancellable = true)
    public void StopParticles(CallbackInfo ci){
        if(test_addon$getOwner2((Projectile) (Object) this) != null){
            if(SkillUtils.hasSkill(test_addon$getOwner2((Projectile) (Object) this), Objects.requireNonNull(SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:starkill"))))){
                BreathEntity entity = (BreathEntity) (Object) this;
                BreathPart[] var1 = entity.parts;
                int var2 = var1.length;

                for(int var3 = 0; var3 < var2; ++var3) {
                    BreathPart part = var1[var3];
                    TensuraParticleHelper.addParticlesAroundSelf(part, particleRegistry.FLESHPARTICLE.get(), 0.9);
                    TensuraParticleHelper.addParticlesAroundSelf(part, particleRegistry.FLESHPARTICLE.get(), 0.7);
                    TensuraParticleHelper.addParticlesAroundSelf(part, particleRegistry.FLESHPARTICLE.get(), 0.5);
                    TensuraParticleHelper.addParticlesAroundSelf(part, particleRegistry.FLESHPARTICLE.get(), 0.3);
                    TensuraParticleHelper.addParticlesAroundSelf(part, particleRegistry.FLESHPARTICLE.get(), 0.1);
                }
                ci.cancel();
            }
        }
    }

    @Unique
    protected void devourAllSkills(LivingEntity target, LivingEntity owner) {
        BreathEntity entity = (BreathEntity) (Object) this;
        if (!target.getType().is(TensuraTags.EntityTypes.NO_SKILL_PLUNDER)) {
            List<ManasSkillInstance> targetSkills = SkillAPI.getSkillsFrom(target).getLearnedSkills().stream().filter(this::canDevour).toList();
            Iterator var4 = targetSkills.iterator();

            while(var4.hasNext()) {
                ManasSkillInstance targetInstance = (ManasSkillInstance)var4.next();
                if (!targetInstance.isTemporarySkill() && targetInstance.getMastery() >= 0 && SkillUtils.learnSkill(owner, targetInstance.getSkill(), entity.getSkill().getRemoveTime())) {
                    if (owner instanceof Player) {
                        Player player = (Player)owner;
                        player.displayClientMessage(Component.translatable("tensura.skill.acquire", new Object[]{targetInstance.getSkill().getName()}).setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)), false);
                    }

                    owner.getLevel().playSound((Player)null, owner.getX(), owner.getY(), owner.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            }

        }
    }

    @Unique
    protected boolean canDevour(ManasSkillInstance instance) {
        BreathEntity entity = (BreathEntity) (Object) this;
        if (!instance.isTemporarySkill() && instance.getMastery() >= 0) {
            if (instance.getSkill() == entity.getSkill().getSkill()) {
                return false;
            } else if (instance.getSkill() instanceof StarvedSkill) {
                return true;
            } else if (instance.getSkill() instanceof DegenerateSkill && entity.getLevel().getGameRules().getBoolean(TensuraGameRules.RIMURU_MODE)) {
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

    @Unique
    protected void devourEP(LivingEntity target, LivingEntity owner, float amountToMax) {
        BreathEntity entity = (BreathEntity) (Object) this;
        if (!target.getType().is(TensuraTags.EntityTypes.NO_EP_PLUNDER)) {
            SkillStorage storage = SkillAPI.getSkillsFrom(owner);
            Optional<ManasSkillInstance> predator = storage.getSkill(entity.getSkill().getSkill());
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
                        this.saveMagiculeIntoStorage(owner, EP * (double)(1.0F - amountToMax));
                        double finalEP = EP;
                        TensuraPlayerCapability.getFrom(playerTarget).ifPresent((cap) -> {
                            cap.setBaseMagicule(cap.getBaseMagicule() - finalEP / 2.0, playerTarget);
                            cap.setBaseAura(cap.getBaseAura() - finalEP / 2.0, playerTarget);
                        });
                        TensuraPlayerCapability.sync(playerTarget);
                    }
                } else {
                    SkillHelper.gainMaxMP(owner, EP * (double)amountToMax);
                    this.saveMagiculeIntoStorage(owner, EP * (double)(1.0F - amountToMax));
                    SkillHelper.reduceEP(target, owner, 1.0, true, true);
                    TensuraEPCapability.setSkippingEPDrop(target, true);
                }

            }
        }
    }

    @Unique
    protected void saveMagiculeIntoStorage(LivingEntity owner, double amount) {
        BreathEntity entity = (BreathEntity) (Object) this;
        ManasSkillInstance instance = this.getSkillInstance(owner);
        if (instance != null) {
            CompoundTag tag = instance.getOrCreateTag();
            tag.putDouble("MpStomach", tag.getDouble("MpStomach") + amount);
            instance.markDirty();
        }
    }

    @Unique
    @Nullable
    protected ManasSkillInstance getSkillInstance(LivingEntity owner) {
        BreathEntity entity = (BreathEntity) (Object) this;
        SkillStorage storage = SkillAPI.getSkillsFrom(owner);
        Optional<ManasSkillInstance> optional = storage.getSkill(entity.getSkill().getSkill());
        return (ManasSkillInstance)optional.orElse((ManasSkillInstance) null);
    }
}
