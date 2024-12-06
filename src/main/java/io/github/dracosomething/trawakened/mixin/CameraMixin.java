package io.github.dracosomething.trawakened.mixin;

import io.github.dracosomething.trawakened.capability.trawakenedPlayerCapability;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {
    private CameraMixin(){}

    @Inject(
            method = "setup",
            at = @At("HEAD"),
            cancellable = true
    )
public void setup(BlockGetter p_90576_, Entity p_90577_, boolean p_90578_, boolean p_90579_, float p_90580_, CallbackInfo ci){
        if(trawakenedPlayerCapability.hasPlague(Minecraft.getInstance().player)) {
            ci.cancel();
        }
    }
}
