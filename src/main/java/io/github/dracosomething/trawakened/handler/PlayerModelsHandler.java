package io.github.dracosomething.trawakened.handler;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.dracosomething.trawakened.ability.skill.unique.Alternate;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.AwakenedFearCapability;
import io.github.dracosomething.trawakened.entity.client.model.CustomPlayerModel.FlawedModel;
import io.github.dracosomething.trawakened.entity.client.model.CustomPlayerModel.IntruderModel;
import io.github.dracosomething.trawakened.entity.client.model.CustomPlayerModel.OverdrivenModel;
import io.github.dracosomething.trawakened.entity.client.renderer.CustomPlayerRenderer.FlawedPlayerRenderer;
import io.github.dracosomething.trawakened.entity.client.renderer.CustomPlayerRenderer.FlawedRenderer;
import io.github.dracosomething.trawakened.entity.client.renderer.CustomPlayerRenderer.OverdrivenRenderer;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = trawakened.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PlayerModelsHandler {
    private static ManasSkillInstance instance;

    @SubscribeEvent
    public static void getSkill(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        instance = SkillUtils.getSkillOrNull(entity, skillRegistry.ALTERNATE.get());
    }

    @SubscribeEvent
    public static void modelChangeEvent(RenderPlayerEvent.Pre event) {
        // checks if the event target is an abstract client player
        if (!(event.getEntity() instanceof AbstractClientPlayer)) return;
        final AbstractClientPlayer player = (AbstractClientPlayer) event.getEntity();
        // checks if the player has the skill
        if (instance != null) {
            // gets the skills tag
            CompoundTag tag = instance.getOrCreateTag();
            // sets the nbt data isslim to the original is slim
            AwakenedFearCapability.SetIsSlim(player, event.getRenderer().model.slim);
            // grabs the players alternate type
            Alternate.AlternateType alternateType = Alternate.AlternateType.fromNBT(tag.getCompound("alternate_type"));
            if (alternateType == Alternate.AlternateType.INTRUDER) {
                getModelIntruder(event.getRenderer()).ifPresent(model -> event.getRenderer().model = model);
            }
            // grabs the players assimilation
            Alternate.Assimilation assimilation = Alternate.Assimilation.fromNBT(tag.getCompound("assimilation"));
            if (assimilation == Alternate.Assimilation.COMPLETE) {
                player.getPlayerInfo().textureLocations.remove(MinecraftProfileTexture.Type.SKIN);
                player.getPlayerInfo().textureLocations.put(MinecraftProfileTexture.Type.SKIN, player.getPlayerInfo().getSkinLocation());
                getModelPlayer(event.getRenderer(), player).ifPresent(model -> event.getRenderer().model = model);
                return;
            } else {
                if (assimilation == Alternate.Assimilation.FLAWED) {
                    player.getPlayerInfo().textureLocations.remove(MinecraftProfileTexture.Type.SKIN);
                    player.getPlayerInfo().textureLocations.put(MinecraftProfileTexture.Type.SKIN, FlawedRenderer.TEXTURE);
                    getModelFlawed(event.getRenderer()).ifPresent(model -> event.getRenderer().model = model);
                } else if (assimilation == Alternate.Assimilation.OVERDRIVEN) {
                    player.getPlayerInfo().textureLocations.remove(MinecraftProfileTexture.Type.SKIN);
                    player.getPlayerInfo().textureLocations.put(MinecraftProfileTexture.Type.SKIN, OverdrivenRenderer.TEXTURE);
                    getModelOverdriven(event.getRenderer()).ifPresent(model -> event.getRenderer().model = model);
                }
            }
        }
    }

    public static Optional<PlayerModel<AbstractClientPlayer>> getModelOverdriven(final PlayerRenderer renderer) {
        final OverdrivenModel<AbstractClientPlayer> model = new OverdrivenModel<>(OverdrivenModel.createMesh(new CubeDeformation(1, 1, 1)).getRoot().bake(64, 64));
        renderer.layers.clear();

        return Optional.of(model);
    }

    public static Optional<PlayerModel<AbstractClientPlayer>> getModelIntruder(final PlayerRenderer renderer) {
        final IntruderModel<AbstractClientPlayer> model = new IntruderModel<>(IntruderModel.createMesh(new CubeDeformation(1, 1, 1)).getRoot().bake(64, 64));
        renderer.layers.clear();

        return Optional.of(model);
    }

    public static Optional<PlayerModel<AbstractClientPlayer>> getModelFlawed(final PlayerRenderer renderer) {
        final FlawedModel<AbstractClientPlayer> model = new FlawedModel<>(FlawedModel.createMesh(new CubeDeformation(1, 1, 1)).getRoot().bake(64, 32));
        renderer.layers.clear();

        return Optional.of(model);
    }

    public static Optional<PlayerModel<AbstractClientPlayer>> getModelPlayer(final PlayerRenderer renderer, LivingEntity entity) {
        final PlayerModel<AbstractClientPlayer> model = new PlayerModel<>(PlayerModel.createMesh(new CubeDeformation(0, 0, 0), AwakenedFearCapability.GetIsSlim(entity)).getRoot().bake(64, 64), AwakenedFearCapability.GetIsSlim(entity));
        renderer.layers.clear();

        return Optional.of(model);
    }
}
