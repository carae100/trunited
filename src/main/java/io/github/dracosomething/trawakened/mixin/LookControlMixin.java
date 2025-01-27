package io.github.dracosomething.trawakened.mixin;

import io.github.dracosomething.trawakened.ability.skill.ultimate.herrscherofplague;
import io.github.dracosomething.trawakened.mobeffect.PlagueEffect;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LookControl.class)
public class LookControlMixin {
    private LookControlMixin(){}

    @Shadow @Final protected Mob mob;

    @Inject(
            method = "setLookAt(DDDFF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void setLookAt(double p_24951_, double p_24952_, double p_24953_, float p_24954_, float p_24955_, CallbackInfo ci) {
            if (this.mob.hasEffect(new MobEffectInstance((MobEffect) effectRegistry.PLAGUEEFFECT.get()).getEffect())) {
                if (herrscherofplague.active) {
                    if (PlagueEffect.getOwner(mob) == herrscherofplague.Owner) {
                    ci.cancel();
                }
            }
        }
    }
}
