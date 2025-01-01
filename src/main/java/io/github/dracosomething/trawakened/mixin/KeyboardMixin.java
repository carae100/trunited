package io.github.dracosomething.trawakened.mixin;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import io.github.dracosomething.trawakened.capability.trawakenedPlayerCapability;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardMixin {
    @Shadow public abstract void keyPress(long p_90894_, int p_90895_, int p_90896_, int p_90897_, int p_90898_);

    private KeyboardMixin(){}

    @Inject(
            method = "keyPress",
            at = @At("HEAD"),
            cancellable = true
    )
    public void keyPress(long p_90894_, int p_90895_, int p_90896_, int p_90897_, int p_90898_, CallbackInfo ci){
        if(trawakenedPlayerCapability.hasPlague(Minecraft.getInstance().player)){
            KeyMapping.releaseAll();
            ci.cancel();
        }
        if(trawakenedPlayerCapability.isOverwhelmed(Minecraft.getInstance().player)){
            KeyMapping.releaseAll();
            ci.cancel();
        }
    }
}
