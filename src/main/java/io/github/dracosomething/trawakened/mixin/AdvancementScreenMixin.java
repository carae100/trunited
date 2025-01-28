package io.github.dracosomething.trawakened.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.dracosomething.trawakened.capability.trawakenedPlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Minecraft.class, priority = 1000000)
public class AdvancementScreenMixin {
    @Shadow
    private void handleKeybinds(){}

    private AdvancementScreenMixin(){}

    @Inject(
            method = "handleKeybinds",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;consumeClick()Z", ordinal = 5),
            remap = false,
            cancellable = true
    )
    public void noWindow(CallbackInfo ci) {
        if (trawakenedPlayerCapability.isOverwhelmed(Minecraft.getInstance().player)) {
            ci.cancel();
        }
    }
}
