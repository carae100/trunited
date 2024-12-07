package io.github.dracosomething.trawakened.handler;

import com.github.manasmods.tensura.ability.skill.extra.SpatialDominationSkill;
import com.github.manasmods.tensura.ability.skill.unique.ReflectorSkill;
import com.github.manasmods.tensura.client.TensuraGUIHelper;
import com.github.manasmods.tensura.handler.client.ClientRaceHandler;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import java.awt.Color;
import javax.annotation.Nullable;

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
    private static final ResourceLocation FREEZING = new ResourceLocation("trawakened", "textures/gui/overlay/freezing.png");

    public ClientOverlayHandler() {
    }

    @SubscribeEvent
    public static void registerOverlay(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(), "freezing", (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
            gui.setupOverlayRenderState(true, false);
            LocalPlayer player = gui.getMinecraft().player;
            if (player != null) {
                MobEffectInstance effectInstance = player.getEffect((MobEffect)TensuraMobEffects.FROST.get());
                if (effectInstance != null) {
                    TensuraGUIHelper.renderFadingTextureWithDuration(effectInstance.getDuration(), 200, FREEZING, (double)screenHeight, (double)screenWidth);
                }
            }
        });
        event.registerBelow(VanillaGuiOverlay.FROSTBITE.id(), "web_silenced", (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
            gui.setupOverlayRenderState(true, false);
            LocalPlayer player = gui.getMinecraft().player;
            if (player != null) {
                if (player.hasEffect((MobEffect)TensuraMobEffects.WEBBED.get()) && player.hasEffect((MobEffect)TensuraMobEffects.SILENCE.get())) {
                    TensuraGUIHelper.renderTextureOverlay(WEBBED_SILENCED, 1.0F, (double)screenHeight, (double)screenWidth);
                }
            }
        });
        event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(), "petrification", (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
            gui.setupOverlayRenderState(true, false);
            LocalPlayer player = gui.getMinecraft().player;
            if (player != null) {
                MobEffectInstance effectInstance = player.getEffect((MobEffect)TensuraMobEffects.PETRIFICATION.get());
                if (effectInstance != null) {
                    float alphaValue = (float)(0.33 * (double)effectInstance.getAmplifier() + 1.0);
                    if (alphaValue > 1.0F) {
                        alphaValue = 1.0F;
                    }

                    TensuraGUIHelper.renderTextureOverlay(PETRIFICATION, alphaValue, (double)screenHeight, (double)screenWidth);
                }
            }
        });
        event.registerAbove(VanillaGuiOverlay.EXPERIENCE_BAR.id(), "slime_jump_charge", (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
            if (ClientRaceHandler.jumpChargingTicks != 0L) {
                float progression = 0.016666668F * (float)Math.min(60L, ClientRaceHandler.jumpChargingTicks);
                int textureWidth = Math.round(182.0F * progression);
                gui.blit(poseStack, (screenWidth - 182) / 2, screenHeight - 29, 0, 89, textureWidth, 5);
            }
        });
        renderSkillOverlay(event);
    }

    private static void renderSkillOverlay(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(), "fading_glow_border", (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
            gui.setupOverlayRenderState(true, false);
            LocalPlayer player = gui.getMinecraft().player;
            if (player != null) {
                MobEffectInstance effectInstance = fadingGlowBorderEffect(player);
                if (effectInstance != null) {
                    TensuraGUIHelper.renderFadingTextureWithDuration(effectInstance.getDuration(), 200, effectInstance.getEffect().getColor(), new ResourceLocation("tensura", "textures/gui/overlay/glow_border.png"), (double)screenHeight, (double)screenWidth);
                }
            }
        });
        event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(), "effect_glow_border", (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
            gui.setupOverlayRenderState(true, false);
            int color = glowBorderEffectColor(gui.getMinecraft().player);
            if (color != -1) {
                TensuraGUIHelper.renderTextureOverlay(new ResourceLocation("tensura", "textures/gui/overlay/glow_border.png"), color, 1.0F, (double)screenHeight, (double)screenWidth);
            }
        });
        event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(), "shadow_border", (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
            gui.setupOverlayRenderState(true, false);
            LocalPlayer player = gui.getMinecraft().player;
            if (player != null) {
                if (player.hasEffect((MobEffect)TensuraMobEffects.SHADOW_STEP.get()) || player.hasEffect((MobEffect)TensuraMobEffects.SLEEP_MODE.get()) || player.hasEffect((MobEffect)TensuraMobEffects.BATS_MODE.get())) {
                    TensuraGUIHelper.renderTextureOverlay(new ResourceLocation("tensura", "textures/gui/overlay/shadow_border.png"), 0, 1.0F, (double)screenHeight, (double)screenWidth);
                }
            }
        });
        event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(), "lightning_border", (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
            gui.setupOverlayRenderState(true, false);
            LocalPlayer player = gui.getMinecraft().player;
            if (player != null) {
                MobEffectInstance effectInstance = lightningBorderEffect(player);
                if (effectInstance != null) {
                    TensuraGUIHelper.renderFadingTextureWithDuration(effectInstance.getDuration(), 200, effectInstance.getEffect().getColor(), new ResourceLocation("tensura", "textures/gui/overlay/lightning_border.png"), (double)screenHeight, (double)screenWidth);
                }
            }
        });
    }

    private static MobEffectInstance lightningBorderEffect(Player player) {
        MobEffectInstance effectInstance = player.getEffect((MobEffect)TensuraMobEffects.DRAGON_MODE.get());
        if (effectInstance != null) {
            return effectInstance;
        } else {
            effectInstance = player.getEffect((MobEffect)TensuraMobEffects.BEAST_TRANSFORMATION.get());
            if (effectInstance != null) {
                return effectInstance;
            } else {
                effectInstance = player.getEffect((MobEffect)TensuraMobEffects.MAD_OGRE.get());
                if (effectInstance != null) {
                    return effectInstance;
                } else {
                    effectInstance = player.getEffect((MobEffect)TensuraMobEffects.OGRE_BERSERKER.get());
                    if (effectInstance != null) {
                        return effectInstance;
                    } else {
                        effectInstance = player.getEffect((MobEffect)TensuraMobEffects.RAMPAGE.get());
                        return effectInstance;
                    }
                }
            }
        }
    }

    private static int glowBorderEffectColor(@Nullable Player player) {
        if (player == null) {
            return -1;
        } else if (ReflectorSkill.hasFullCounter(player)) {
            return (new Color(255, 241, 0, 255)).getRGB();
        } else if (SpatialDominationSkill.hasFaultField(player)) {
            return (new Color(14, 231, 203, 255)).getRGB();
        } else if (player.hasEffect((MobEffect)TensuraMobEffects.LUST_EMBRACEMENT.get())) {
            return ((MobEffect)TensuraMobEffects.LUST_EMBRACEMENT.get()).getColor();
        } else if (player.hasEffect((MobEffect)TensuraMobEffects.MAGICULE_POISON.get())) {
            return ((MobEffect)TensuraMobEffects.MAGICULE_POISON.get()).getColor();
        } else if (player.hasEffect((MobEffect)TensuraMobEffects.FALSIFIER.get())) {
            return ((MobEffect)TensuraMobEffects.FALSIFIER.get()).getColor();
        } else if (player.hasEffect((MobEffect)TensuraMobEffects.ALL_SEEING.get())) {
            return ((MobEffect)TensuraMobEffects.ALL_SEEING.get()).getColor();
        } else {
            return player.hasEffect((MobEffect)TensuraMobEffects.PRESENCE_CONCEALMENT.get()) ? ((MobEffect)TensuraMobEffects.PRESENCE_CONCEALMENT.get()).getColor() : -1;
        }
    }

    private static MobEffectInstance fadingGlowBorderEffect(Player player) {
        MobEffectInstance effectInstance = player.getEffect((MobEffect)TensuraMobEffects.FUTURE_VISION.get());
        if (effectInstance != null) {
            return effectInstance;
        } else {
            effectInstance = player.getEffect((MobEffect)TensuraMobEffects.MIND_CONTROL.get());
            if (effectInstance != null) {
                return effectInstance;
            } else {
                effectInstance = player.getEffect((MobEffect)TensuraMobEffects.INFECTION.get());
                if (effectInstance != null) {
                    return effectInstance;
                } else {
                    effectInstance = player.getEffect((MobEffect)TensuraMobEffects.FEAR.get());
                    return effectInstance;
                }
            }
        }
    }
}

