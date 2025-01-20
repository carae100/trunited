package io.github.dracosomething.trawakened.mixin;

import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.capability.smithing.SmithingCapability;
import com.github.manasmods.tensura.data.recipe.SmithingBenchRecipe;
import com.github.manasmods.tensura.menu.SmithingBenchMenu;
import io.github.dracosomething.trawakened.registry.skillregistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

@Mixin(SmithingBenchRecipe.class)
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
                SkillUtils.isSkillMastered(player, skillregistry.STARKILL.get()) ||
                SkillUtils.isSkillMastered(player, skillregistry.AZAZEL.get()) ||
                SkillUtils.isSkillMastered(player, skillregistry.AKASHIC_PLANE.get())
                ? true : (Boolean) SmithingCapability.getFrom(player).map((data) -> {
                    return data.hasSchematics(this.requiredSchematics);
                }).orElse(false)
        );
    }
}
