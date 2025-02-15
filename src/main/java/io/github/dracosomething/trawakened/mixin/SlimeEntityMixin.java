package io.github.dracosomething.trawakened.mixin;

import com.github.manasmods.tensura.entity.SlimeEntity;
import io.github.dracosomething.trawakened.capability.trawakenedPlayerCapability;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlimeEntity.class)
public class SlimeEntityMixin {

    @Inject(
            method = "jumpFromGround",
            at = @At("HEAD"),
            cancellable = true
    )
    private void jumpingSquishTick(CallbackInfo ci) {
        if (trawakenedPlayerCapability.hasPlague((LivingEntity) (Object) this)) {
            ci.cancel();
        }
    }


    @Inject(
            method = "damageCollidedEntity",
            at = @At("HEAD"),
            cancellable = true,
            remap = false)
    private void noDamage(Entity entity, CallbackInfo ci) {
        if (trawakenedPlayerCapability.hasPlague((LivingEntity) (Object) this)) {
            ci.cancel();
        }
    }
}
