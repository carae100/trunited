package io.github.dracosomething.trawakened.mixin;

import com.github.manasmods.tensura.entity.SlimeEntity;
import com.github.manasmods.tensura.entity.SupermassiveSlimeEntity;
import io.github.dracosomething.trawakened.ability.skill.ultimate.herrscherofplague;
import io.github.dracosomething.trawakened.capability.trawakenedPlayerCapability;
import io.github.dracosomething.trawakened.mobeffect.PlagueEffect;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SupermassiveSlimeEntity.class)
public class SupermassiveSlimeEntityMixin {

    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true
    )
    private void StopGoals(CallbackInfo ci){
        if (herrscherofplague.active) {
            if(trawakenedPlayerCapability.hasPlague((LivingEntity) (Object) this)){
                if (PlagueEffect.getOwner((LivingEntity) (Object) this) == herrscherofplague.Owner) {
                    ci.cancel();
                }
            }
        }
    }
}
