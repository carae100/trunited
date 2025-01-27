package io.github.dracosomething.trawakened.mixin;

import com.github.manasmods.tensura.api.entity.controller.JumpingEntityMoveControl;
import com.github.manasmods.tensura.entity.MetalSlimeEntity;
import com.github.manasmods.tensura.entity.SlimeEntity;
import io.github.dracosomething.trawakened.ability.skill.ultimate.herrscherofplague;
import io.github.dracosomething.trawakened.capability.trawakenedPlayerCapability;
import io.github.dracosomething.trawakened.mobeffect.PlagueEffect;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JumpingEntityMoveControl.class)
public class JumpingEntityMoveControlMixin {

@Shadow
public @Final PathfinderMob mob;

    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true
    )
    private void StopGoals(CallbackInfo ci){
            if (mob.hasEffect(new MobEffectInstance((MobEffect) effectRegistry.PLAGUEEFFECT.get()).getEffect())) {
                if (herrscherofplague.active) {
                    if (PlagueEffect.getOwner(mob) == herrscherofplague.Owner) {
                    ci.cancel();
                }
            }
        }
        if(mob.hasEffect(new MobEffectInstance(effectRegistry.TIMESTOP.get()).getEffect())){
            ci.cancel();
        }
    }
}
