package io.github.dracosomething.trawakened.event.client;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import io.github.dracosomething.trawakened.ability.skill.unique.Alternate;
import io.github.dracosomething.trawakened.entity.client.model.CustomPlayerModel.IntruderModel;
import io.github.dracosomething.trawakened.entity.client.model.CustomPlayerModel.OverdrivenModel;
import io.github.dracosomething.trawakened.entity.client.renderer.CustomPlayerRenderer.OverdrivenRenderer;
import io.github.dracosomething.trawakened.registry.skillregistry;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = trawakened.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeEventBusClientEvents {
    @SubscribeEvent
    public static void testAnimationEvent(final RenderPlayerEvent.Pre event) {
        if (!(event.getEntity() instanceof AbstractClientPlayer)) return;
        final AbstractClientPlayer player = (AbstractClientPlayer) event.getEntity();
        ManasSkillInstance instance = SkillUtils.getSkillOrNull(player, skillregistry.ALTERNATE.get());
        if (instance != null) {
            CompoundTag tag = instance.getOrCreateTag();
            Alternate.Assimilation assimilation = Alternate.Assimilation.fromNBT(tag.getCompound("assimilation"));
            if (assimilation == Alternate.Assimilation.OVERDRIVEN) {
                player.getPlayerInfo().textureLocations.remove(MinecraftProfileTexture.Type.SKIN);
                player.getPlayerInfo().textureLocations.put(MinecraftProfileTexture.Type.SKIN, OverdrivenRenderer.TEXTURE);
                getModelOverdriven(event.getRenderer()).ifPresent(model -> event.getRenderer().model = model);
            }
            Alternate.AlternateType alternateType = Alternate.AlternateType.fromNBT(tag.getCompound("alternate_type"));
            if (alternateType == Alternate.AlternateType.INTRUDER) {
                getModelIntruder(event.getRenderer()).ifPresent(model -> event.getRenderer().model = model);
            }
        }
    }

    public static Optional<PlayerModel<AbstractClientPlayer>> getModelOverdriven(final PlayerRenderer renderer) {
        final OverdrivenModel<AbstractClientPlayer> model = new OverdrivenModel<>(OverdrivenModel.createMesh(new CubeDeformation(1, 1, 1)).getRoot().bake(64, 64));
        //Remove Player Model Layers
        renderer.layers.clear();
        //Add Armor Layer
//        renderer.addLayer(new SmallSlimeArmorLayer<>(renderer));

        return Optional.of(model);
    }

    public static Optional<PlayerModel<AbstractClientPlayer>> getModelIntruder(final PlayerRenderer renderer) {
        final IntruderModel<AbstractClientPlayer> model = new IntruderModel<>(IntruderModel.createMesh(new CubeDeformation(1, 1, 1)).getRoot().bake(64, 64));
        //Remove Player Model Layers
        renderer.layers.clear();
        //Add Armor Layer
//        renderer.addLayer(new SmallSlimeArmorLayer<>(renderer));

        return Optional.of(model);
    }
}
