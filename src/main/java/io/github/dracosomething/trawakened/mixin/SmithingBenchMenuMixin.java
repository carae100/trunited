package io.github.dracosomething.trawakened.mixin;

import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.capability.smithing.SmithingCapability;
import com.github.manasmods.tensura.data.recipe.SmithingBenchRecipe;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = SmithingBenchRecipe.class, priority = 1000000)
public class SmithingBenchMenuMixin {
    @Shadow
    private @Final List<ResourceLocation> requiredSchematics;

    @Inject(
            method = "hasUnlocked",
            at= @At("HEAD"),
            remap = false,
            cancellable = true)
    private void allRecipes(Player player, CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(
                player.isCreative() ||
                SkillUtils.isSkillMastered(player, skillRegistry.STARKILL.get()) ||
                SkillUtils.isSkillMastered(player, skillRegistry.AZAZEL.get()) ||
                SkillUtils.isSkillMastered(player, skillRegistry.AKASHIC_PLANE.get()) ||
                SkillUtils.isSkillMastered(player, skillRegistry.HERRSCHEROFTHEWORLD.get())
                ? true : (Boolean) SmithingCapability.getFrom(player).map((data) -> {
                    return data.hasSchematics(this.requiredSchematics);
                }).orElse(false)
        );
    }
}
