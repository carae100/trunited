package io.github.dracosomething.trawakened.mixin;

import com.github.manasmods.tensura.entity.SlimeEntity;
import io.github.dracosomething.trawakened.ability.skill.ultimate.herrscherofplague;
import io.github.dracosomething.trawakened.capability.trawakenedPlayerCapability;
import io.github.dracosomething.trawakened.mobeffect.PlagueEffect;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SlimeEntity.class)
public class SlimeEntityMixin {

    @Inject(
            method = "jumpFromGround",
            at = @At("HEAD"),
            cancellable = true
    )
    private void jumpingSquishTick(CallbackInfo ci){
        if (herrscherofplague.active) {
            if(trawakenedPlayerCapability.hasPlague((LivingEntity) (Object) this)){
                if (PlagueEffect.getOwner((LivingEntity) (Object) this) == herrscherofplague.Owner) {
                    ci.cancel();
                }
            }
        }
    }

    @Inject(
            method = "damageCollidedEntity",
            at = @At("HEAD"),
            cancellable = true,
    remap = false)
    private void noDamage(Entity entity, CallbackInfo ci){
        if (herrscherofplague.active) {
            if(trawakenedPlayerCapability.hasPlague((LivingEntity) (Object) this)){
                if (PlagueEffect.getOwner((LivingEntity) (Object) this) == herrscherofplague.Owner) {
                    ci.cancel();
                }
            }
        }
    }
}
