package io.github.dracosomething.trawakened.mixin;

import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ForgeEventFactory.class)
public class ForgeEventFactoryMixin {
    @Inject(
            method = "onLivingHeal",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void StopHeal(LivingEntity entity, float amount, CallbackInfoReturnable<Float> cir){
        if(entity.hasEffect(effectRegistry.HEALPOISON.get())){
            cir.cancel();
        }
    }
}
