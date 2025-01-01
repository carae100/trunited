package io.github.dracosomething.trawakened.mixin;

import io.github.dracosomething.trawakened.capability.trawakenedPlayerCapability;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    private MouseHandlerMixin(){}

    @Inject(
            method = "onPress",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onPressInject(long p_91531_, int p_91532_, int p_91533_, int p_91534_, CallbackInfo ci){
        if(trawakenedPlayerCapability.hasPlague(Minecraft.getInstance().player)){
            KeyMapping.releaseAll();
            ci.cancel();
        }
        if(trawakenedPlayerCapability.isOverwhelmed(Minecraft.getInstance().player)){
            KeyMapping.releaseAll();
            ci.cancel();
        }
    }

    @Inject(
            method = "onScroll",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onScrollInject(long p_91527_, double p_91528_, double p_91529_, CallbackInfo ci){
        if(trawakenedPlayerCapability.hasPlague(Minecraft.getInstance().player) && !Minecraft.getInstance().isPaused()){
            KeyMapping.releaseAll();
            ci.cancel();
        }
        if(trawakenedPlayerCapability.isOverwhelmed(Minecraft.getInstance().player) && !Minecraft.getInstance().isPaused()){
            KeyMapping.releaseAll();
            ci.cancel();
        }
    }

    @Inject(
            method = "onMove",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onMoveInject(long p_91527_, double p_91528_, double p_91529_, CallbackInfo ci){
        if(trawakenedPlayerCapability.hasPlague(Minecraft.getInstance().player) && !Minecraft.getInstance().isPaused()){
            KeyMapping.releaseAll();
            ci.cancel();
        }
        if(trawakenedPlayerCapability.isOverwhelmed(Minecraft.getInstance().player) && !Minecraft.getInstance().isPaused()){
            KeyMapping.releaseAll();
            ci.cancel();
        }
    }
}
