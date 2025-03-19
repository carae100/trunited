package io.github.dracosomething.trawakened.mixin;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static com.github.manasmods.tensura.ability.SkillUtils.hasSkill;

@Mixin(SkillUtils.class)
public class SkillUtilsMixin {
    @ModifyVariable(
            method = "getMagiculeGain",
            at = @At(
                    "STORE"
            ),
            ordinal = 1,
            remap = false
    )
    private static float newMagiculeGain(float value, @Local(ordinal = 0, argsOnly = true) Player player){
        if (hasSkill(player, (ManasSkill) skillRegistry.SYSTEM.get())) {
            value += 0.0125F;
        }
        return value;
    }

    @ModifyVariable(
            method = "getAuraGain",
            at = @At(
                    "STORE"
            ),
            ordinal = 1,
            remap = false
    )
    private static float newAuraGain(float value, @Local(ordinal = 0, argsOnly = true) Player player){
        if (hasSkill(player, (ManasSkill) skillRegistry.SYSTEM.get())) {
            value += 0.0125F;
        }
        return value;
    }
}
