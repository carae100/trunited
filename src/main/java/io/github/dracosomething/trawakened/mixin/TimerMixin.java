package io.github.dracosomething.trawakened.mixin;

import io.github.dracosomething.trawakened.ability.skill.ultimate.herrscheroftime;
import io.github.dracosomething.trawakened.ability.skill.unique.Starkill;
import net.minecraft.Util;
import net.minecraft.client.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Timer.class)
public class TimerMixin {
    @Inject(
            method = "advanceTime",
            at = @At("HEAD")
    )
    private void ChangeTime(long p_92526_, CallbackInfoReturnable<Integer> cir){
        p_92526_ = herrscheroftime.time;
    }
}
