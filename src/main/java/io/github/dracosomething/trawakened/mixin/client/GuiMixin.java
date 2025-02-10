package io.github.dracosomething.trawakened.mixin.client;

import com.github.manasmods.tensura.handler.client.HUDHandler;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@OnlyIn(Dist.CLIENT)
@Mixin(HUDHandler.class)
public class GuiMixin {
    @Shadow
    private static LocalPlayer player;

    @Unique
    private static final ResourceLocation DARK_BLUE_HEARTS = new ResourceLocation("trawakened", "textures/gui/hearts/dark_blue_bar.png");

    @Unique
    private static final ResourceLocation HP_BAR = new ResourceLocation("trawakened", "textures/gui/hearts/hp_bar.png");

    @Unique
    private static final ResourceLocation MELT_HP_BAR = new ResourceLocation("trawakened", "textures/gui/hearts/melt_hp_bar.png");

    @Unique
    private static final ResourceLocation PLAGUE_HP_BAR = new ResourceLocation("trawakened", "textures/gui/hearts/plague_hp_bar.png");

    @Inject(method = "getHealthBarToRender", at = @At("HEAD"), cancellable = true, remap = false)
    private static void setCustomHearts(boolean spiritual, CallbackInfoReturnable<ResourceLocation> cir) {
        if (spiritual) {
            if (player.hasEffect((MobEffect)effectRegistry.BRAINDAMAGE.get())) {
                cir.setReturnValue(DARK_BLUE_HEARTS);
            } else if (player.hasEffect(effectRegistry.SHPPOISON.get())) {
                cir.setReturnValue(HP_BAR);
            }
        } else if (player.hasEffect((MobEffect) effectRegistry.MELT.get())) {
            cir.setReturnValue(MELT_HP_BAR);
        } else if (player.hasEffect(effectRegistry.PLAGUEEFFECT.get())) {
            cir.setReturnValue(PLAGUE_HP_BAR);
        } else if (player.hasEffect(effectRegistry.HEALPOISON.get())) {
            cir.setReturnValue(DARK_BLUE_HEARTS);
        }
    }
}
