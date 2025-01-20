package io.github.dracosomething.trawakened.mixin;

import io.github.dracosomething.trawakened.ability.skill.ultimate.herrscherofplague;
import io.github.dracosomething.trawakened.capability.trawakenedPlayerCapability;
import io.github.dracosomething.trawakened.mobeffect.PlagueEffect;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input {
    @Shadow
    private @Final Options options;

    @Shadow
    private static float calculateImpulse(boolean p_205578_, boolean p_205579_) {
        return 0;
    }

    @Inject(
            method = "tick",
            at = @At("HEAD"),
            remap = false,
            cancellable = true)
    public void ShuffelMapping(boolean p_234118_, float p_234119_, CallbackInfo ci) {
        if (trawakenedPlayerCapability.isOverwhelmed(Minecraft.getInstance().player)) {
            if(this.options.keyShift.isDown() && this.options.keySprint.isDown()) {
                this.up = this.options.keyRight.isDown();
                this.down = this.options.keyPickItem.isDown();
                this.left = this.options.keySaveHotbarActivator.isDown();
                this.right = this.options.keyAdvancements.isDown();
                this.forwardImpulse = calculateImpulse(this.up, this.left);
                this.leftImpulse = calculateImpulse(this.down, this.right);
            }
            this.jumping = this.options.keyUse.isDown();
            this.shiftKeyDown = this.options.keyPlayerList.isDown();
            if (p_234118_) {
                this.leftImpulse *= p_234119_ - 0.1F;
                this.forwardImpulse *= p_234119_ - 0.1F;
            }
            ci.cancel();
        }
    }
}
