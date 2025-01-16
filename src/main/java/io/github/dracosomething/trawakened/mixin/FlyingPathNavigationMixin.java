package io.github.dracosomething.trawakened.mixin;

import io.github.dracosomething.trawakened.ability.skill.ultimate.herrscherofplague;
import io.github.dracosomething.trawakened.mobeffect.PlagueEffect;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FlyingPathNavigation.class)
public abstract class FlyingPathNavigationMixin extends PathNavigation {
    private FlyingPathNavigationMixin(Mob p_26515_, Level p_26516_) {
        super(p_26515_, p_26516_);
    }

    @Inject(method = "tick ", at = @At("HEAD"), cancellable = true)
    public void tickInject(CallbackInfo ci) {
            if (this.mob.hasEffect(new MobEffectInstance((MobEffect) effectRegistry.PLAGUEEFFECT.get()).getEffect())) {
                if (herrscherofplague.active) {
                    if (PlagueEffect.getOwner(mob) == herrscherofplague.Owner) {
                    ci.cancel();
                }
            }
        }
//        if(this.mob.hasEffect(new MobEffectInstance(effectRegistry.OVERWHELMED.get()).getEffect())){
//            ci.cancel();
//        }
    }
}
