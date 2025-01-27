package io.github.dracosomething.trawakened.mixin;

import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class entityMixin {
    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true
    )
    public void stopTime(CallbackInfo ci){
        if(Minecraft.getInstance().player != null) {
            if (Minecraft.getInstance().player.hasEffect(effectRegistry.TIMESTOP_CORE.get())) {
                ci.cancel();
            }
        }
    }
}
