package io.github.dracosomething.trawakened.mixin;

import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true
    )
    public void stopTime(BooleanSupplier p_104727_, CallbackInfo ci){
        if(Minecraft.getInstance().player != null) {
            if (Minecraft.getInstance().player.hasEffect(effectRegistry.TIMESTOP_CORE.get())) {
                ci.cancel();
            }
        }
    }

    @Inject(
            method = "lambda$tickEntities$4",
            at = @At("HEAD"),
            cancellable = true
    )
    public void stopTimeEntities(Entity p_194183_, CallbackInfo ci){
        if(Minecraft.getInstance().player != null) {
            if (Minecraft.getInstance().player.hasEffect(effectRegistry.TIMESTOP_CORE.get())) {
                if(p_194183_ != Minecraft.getInstance().player) {
                    ci.cancel();
                }
            }
        }
    }
}
