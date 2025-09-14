package io.github.dracosomething.trawakened.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillUtils;

import io.github.dracosomething.trawakened.ability.skill.ultimate.ShadowMonarch;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.world.entity.player.Player;

@Mixin(value = SkillUtils.class, remap = false)
public class SkillUtilsMixin {
    
    // Método auxiliar para calcular multiplicador dinâmico
    private static float getSystemMultiplier(Player player) {
        if (!SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SYSTEM.get()).isPresent()) {
            return 1.0f; // Sem System = sem multiplicador
        }
        
        // Verificar se tem Shadow Monarch
        boolean hasShadowMonarch = SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SHADOW_MONARCH.get()).isPresent();
        
        if (!hasShadowMonarch) {
            // Sem Shadow Monarch: verificar maestria do System
            ManasSkillInstance systemInstance = SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SYSTEM.get()).get();
            boolean hasSystemMastery = systemInstance.getMastery() >= 1.0;
            return hasSystemMastery ? 3.5f : 2.5f;
        }
        
        // Com Shadow Monarch: verificar maestria e awakened
        ManasSkillInstance shadowInstance = SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SHADOW_MONARCH.get()).get();
        boolean hasShadowMonarchMastery = shadowInstance.getMastery() >= 1.0;
        
        boolean isShadowMonarchAwakened = false;
        if (shadowInstance.getSkill() instanceof ShadowMonarch skill) {
            isShadowMonarchAwakened = skill.getData().getBoolean("awakened");
        }

        // Determinar multiplicador baseado no Shadow Monarch
        if (isShadowMonarchAwakened) {
            return 7.0f; // Shadow Monarch awakened
        } else if (hasShadowMonarchMastery) {
            return 5.5f; // Shadow Monarch com maestria
        } else {
            return 4.5f; // Shadow Monarch básico
        }
    }
    
    @Inject(
        method = "getMagiculeGain",
        at = @At("RETURN"),
        cancellable = true,
        remap = false
    )
    private static void applySystemSkillMagiculeMultiplier(Player player, boolean majin, CallbackInfoReturnable<Float> cir) {
        float multiplier = getSystemMultiplier(player);
        if (multiplier > 1.0f) {
            float currentValue = cir.getReturnValue();
            float newValue = currentValue * multiplier;
            
            // Debug log para verificar se está funcionando
            System.out.println("[System Skill] Magicule Multiplier aplicado: " + currentValue + " -> " + newValue + " (x" + multiplier + ")");
            
            cir.setReturnValue(newValue);
        }
    }

    @Inject(
        method = "getAuraGain",
        at = @At("RETURN"),
        cancellable = true,
        remap = false
    )
    private static void applySystemSkillAuraMultiplier(Player player, boolean majin, CallbackInfoReturnable<Float> cir) {
        float multiplier = getSystemMultiplier(player);
        if (multiplier > 1.0f) {
            float currentValue = cir.getReturnValue();
            float newValue = currentValue * multiplier;
            
            // Debug log para verificar se está funcionando
            System.out.println("[System Skill] Aura Multiplier aplicado: " + currentValue + " -> " + newValue + " (x" + multiplier + ")");
            
            cir.setReturnValue(newValue);
        }
    }
}
