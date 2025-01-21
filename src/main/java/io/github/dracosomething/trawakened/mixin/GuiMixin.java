package io.github.dracosomething.trawakened.mixin;

import com.github.manasmods.tensura.handler.client.HUDHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ResourceBundle;

@OnlyIn(Dist.CLIENT)
@Mixin(HUDHandler.class)
public class GuiMixin {
    @Shadow
    private static LocalPlayer player;

    @Unique
    private static final ResourceLocation CUSTOM_HEARTS = new ResourceLocation("trawakened", "textures/gui/hearts/batrachotoxin_hearts.png");

    @Inject(method = "getHealthBarToRender", at = @At("HEAD"), cancellable = true)
    private static void setCustomHearts(boolean spiritual, CallbackInfoReturnable<ResourceLocation> cir) {
            if (player.hasEffect(effectRegistry.MELT.get())) {
                RenderSystem.setShaderTexture(0, CUSTOM_HEARTS);
            }

    }
}
