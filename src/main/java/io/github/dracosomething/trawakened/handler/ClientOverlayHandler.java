package io.github.dracosomething.trawakened.handler;

import com.github.manasmods.tensura.ability.skill.extra.SpatialDominationSkill;
import com.github.manasmods.tensura.ability.skill.unique.ReflectorSkill;
import com.github.manasmods.tensura.client.TensuraGUIHelper;
import com.github.manasmods.tensura.handler.client.ClientRaceHandler;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;

import java.awt.Color;
import javax.annotation.Nullable;

import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
        modid = trawakened.MODID,
        bus = Bus.MOD,
        value = {Dist.CLIENT}
)
public class ClientOverlayHandler {
    private static final ResourceLocation BLACK_SCREEN = new ResourceLocation("trawakened", "textures/gui/overlay/blackscreen.png");

    public ClientOverlayHandler() {
    }

    @SubscribeEvent
    public static void registerOverlay(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(), "black_screen", (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
            gui.setupOverlayRenderState(true, false);
            LocalPlayer player = gui.getMinecraft().player;
            if (player != null) {
                if(player.isCreative()) {
                    MobEffectInstance effectInstance = player.getEffect((MobEffect) effectRegistry.PLAGUEEFFECT.get());
                    if (effectInstance != null) {
                        TensuraGUIHelper.renderFadingTextureWithDuration(effectInstance.getDuration(), 10, BLACK_SCREEN, (double) screenHeight, (double) screenWidth);
                    }
                }
            }
        });
    }
}

