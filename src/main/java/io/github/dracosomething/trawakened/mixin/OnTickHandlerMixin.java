package io.github.dracosomething.trawakened.mixin;

import com.github.manasmods.tensura.handler.OnTickHandler;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OnTickHandler.class)
public class OnTickHandlerMixin {
    @Shadow private static void spiritualRegeneration(LivingEntity entity) {}

    @Inject(
            method = "spiritualRegeneration",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void StopRegen(LivingEntity entity, CallbackInfo ci){
        if(entity.hasEffect(effectRegistry.SHPPOISON.get()) || entity.hasEffect(effectRegistry.TIMESTOP.get()) || entity.hasEffect(effectRegistry.TIMESTOP_CORE.get())){
            ci.cancel();
        }
    }
}
