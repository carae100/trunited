package io.github.dracosomething.trawakened.mixin;

import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillUtils;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SkillUtils.class, remap = false)
public class SkillUtilsMixin {
    
    @Inject(
        method = "getMagiculeGain",
        at = @At("RETURN"),
        cancellable = true,
        remap = false
    )
    private static void applySystemSkillMagiculeMultiplier(Player player, boolean majin, CallbackInfoReturnable<Float> cir) {
        if (SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SYSTEM.get()).isPresent()) {
            float currentValue = cir.getReturnValue();
            cir.setReturnValue(currentValue * 2.5f);
        }
    }

    @Inject(
        method = "getAuraGain",
        at = @At("RETURN"),
        cancellable = true,
        remap = false
    )
    private static void applySystemSkillAuraMultiplier(Player player, boolean majin, CallbackInfoReturnable<Float> cir) {
        if (SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SYSTEM.get()).isPresent()) {
            float currentValue = cir.getReturnValue();
            cir.setReturnValue(currentValue * 2.5f);
        }
    }
}
