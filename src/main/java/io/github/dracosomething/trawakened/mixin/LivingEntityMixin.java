package io.github.dracosomething.trawakened.mixin;

import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow @Final private Map<MobEffect, MobEffectInstance> activeEffects;

    @Shadow public abstract boolean hasEffect(MobEffect p_21024_);

    private LivingEntityMixin(){}

    @Inject(
            method = "curePotionEffects",
            at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/Iterator;next()Ljava/lang/Object;"),
            cancellable = true,
            remap = false
    )
    private void InjectPlague(ItemStack curativeItem, CallbackInfoReturnable<Boolean> cir) {
        if (hasEffect(new MobEffectInstance((MobEffect) effectRegistry.PLAGUEEFFECT.get()).getEffect())){
            cir.setReturnValue(false);
        }
    }
}
