package io.github.dracosomething.trawakened.mixin.client;

import io.github.dracosomething.trawakened.capability.trawakenedPlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input {
    @Shadow
    private @Final Options options;

    public KeyboardInputMixin(){}

    @Shadow
    private static float calculateImpulse(boolean p_205578_, boolean p_205579_) {
        return 0;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void tick(boolean p_234118_, float p_234119_) {
        if (trawakenedPlayerCapability.isOverwhelmed(Minecraft.getInstance().player)) {
            if(this.options.keyShift.isDown() && this.options.keySprint.isDown()) {
                this.up = this.options.keyRight.isDown();
                this.down = this.options.keyPickItem.isDown();
                this.left = this.options.keySaveHotbarActivator.isDown();
                this.right = this.options.keySwapOffhand.isDown();
                this.forwardImpulse = calculateImpulse(this.up, this.left);
                this.leftImpulse = calculateImpulse(this.down, this.right);
            }
            this.jumping = this.options.keyUse.isDown();
            this.shiftKeyDown = this.options.keyPlayerList.isDown();
            if (p_234118_) {
                this.leftImpulse *= p_234119_ - 0.1F;
                this.forwardImpulse *= p_234119_ - 0.1F;
            }
        }
        this.up = this.options.keyUp.isDown();
        this.down = this.options.keyDown.isDown();
        this.left = this.options.keyLeft.isDown();
        this.right = this.options.keyRight.isDown();
        this.forwardImpulse = calculateImpulse(this.up, this.down);
        this.leftImpulse = calculateImpulse(this.left, this.right);
        this.jumping = this.options.keyJump.isDown();
        this.shiftKeyDown = this.options.keyShift.isDown();
        if (p_234118_) {
            this.leftImpulse *= p_234119_;
            this.forwardImpulse *= p_234119_;
        }

    }
}
