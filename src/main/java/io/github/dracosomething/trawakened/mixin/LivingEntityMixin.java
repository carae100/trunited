package io.github.dracosomething.trawakened.mixin;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import io.github.dracosomething.trawakened.ability.skill.ultimate.herrscherofplague;
import io.github.dracosomething.trawakened.ability.skill.unique.Alternate;
import io.github.dracosomething.trawakened.capability.trawakenedPlayerCapability;
import io.github.dracosomething.trawakened.mobeffect.PlagueEffect;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.registry.skillregistry;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
//    @Shadow @Final private Map<MobEffect, MobEffectInstance> activeEffects;
    @Shadow public abstract MobType getMobType();

    @Shadow
    public float yHeadRotO;
    float stuckYaw = 0;

    @Shadow
    public abstract boolean hasEffect(MobEffect p_21024_);

//    @Shadow public float flyingSpeed;

    @Shadow
    public abstract void setYBodyRot(float p_21309_);

    @Shadow
    public abstract void setYHeadRot(float p_21306_);

    @Shadow
    public abstract void setSprinting(boolean p_21284_);

    @Shadow
    protected abstract float tickHeadTurn(float p_21260_, float p_21261_);

    @Shadow
    public abstract void forceAddEffect(MobEffectInstance p_147216_, @Nullable Entity p_147217_);

    @Shadow @javax.annotation.Nullable public abstract MobEffectInstance getEffect(MobEffect p_21125_);

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(
            method = "curePotionEffects",
            at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/Iterator;next()Ljava/lang/Object;"),
            cancellable = true,
            remap = false
    )
    private void InjectPlague(ItemStack curativeItem, CallbackInfoReturnable<Boolean> cir) {
        if (hasEffect(new MobEffectInstance((MobEffect) effectRegistry.PLAGUEEFFECT.get()).getEffect())) {
            cir.setReturnValue(false);
        }
        if (hasEffect(new MobEffectInstance((MobEffect) effectRegistry.TIMESTOP.get()).getEffect())) {
            cir.setReturnValue(false);
        }
        if (hasEffect(new MobEffectInstance((MobEffect) effectRegistry.TIMESTOP_CORE.get()).getEffect())) {
            cir.setReturnValue(false);
        }
        if (hasEffect(new MobEffectInstance(effectRegistry.OVERWHELMED.get()).getEffect())) {
            cir.setReturnValue(false);
        }
        if (hasEffect(new MobEffectInstance(effectRegistry.MELT.get()).getEffect())) {
            Random rand = new Random();
            int chance = rand.nextInt(101);
            if (chance < 10) {
                cir.setReturnValue(true);
            } else {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(
            method = "hasLineOfSight",
            at = @At("HEAD"),
            cancellable = true
    )
    public void canSee(Entity p_147185_, CallbackInfoReturnable<Boolean> cir) {
        if (trawakenedPlayerCapability.hasPlague((LivingEntity) (Object) this)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"), cancellable = true)
    public void tick(CallbackInfo callbackInfo) {
        LivingEntity living = (LivingEntity) (Object) this;
        if (trawakenedPlayerCapability.hasPlague((LivingEntity) (Object) this)) {
            this.setXRot(90);
            this.xRotO = 90;
            this.setYHeadRot(stuckYaw);
            this.yHeadRotO = stuckYaw;
            this.setYBodyRot(stuckYaw);
            this.setShiftKeyDown(false);
            this.setSprinting(false);
        } else {
            this.stuckYaw = this.getYRot();
        }
        if(living.hasEffect(effectRegistry.TIMESTOP.get())){
            callbackInfo.cancel();
        }
    }

    @Inject(
            method = "heal",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stopHeal(float p_21116_, CallbackInfo ci) {
        if (trawakenedPlayerCapability.hasHealPoison((LivingEntity) (Object) this)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "getMobType",
            at = @At("HEAD"),
            cancellable = true
    )
    private void IsNotFriendly(CallbackInfoReturnable<MobType> cir) {
        ManasSkillInstance instance = SkillUtils.getSkillOrNull(((LivingEntity) (Object) this), skillregistry.ALTERNATE.get());
        if (instance != null) {
            CompoundTag tag = instance.getOrCreateTag();
            Alternate.Assimilation assimilation = Alternate.Assimilation.fromNBT(tag.getCompound("assimilation"));
            if (assimilation == Alternate.Assimilation.OVERDRIVEN || assimilation == Alternate.Assimilation.FLAWED) {
                cir.setReturnValue(MobType.ILLAGER);
            }
        }
    }
}
