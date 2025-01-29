package io.github.dracosomething.trawakened.mixin;

import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TensuraEPCapability.class)
public class TensuraEPCapabilityMixin {
    @Inject(
            method = "healSpiritualHealth",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void StopRegen(LivingEntity entity, double value, CallbackInfo ci){
        if(entity.hasEffect(effectRegistry.SHPPOISON.get()) || entity.hasEffect(effectRegistry.TIMESTOP.get()) || entity.hasEffect(effectRegistry.TIMESTOP_CORE.get())){
            ci.cancel();
        }
    }
}
