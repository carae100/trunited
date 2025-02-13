package io.github.dracosomething.trawakened.client.events;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import io.github.dracosomething.trawakened.entity.client.model.CustomPlayerModel.LichModeModel;
import io.github.dracosomething.trawakened.entity.client.renderer.CustomPlayerRenderer.LichModeRenderer;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
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
        player.getPlayerInfo().textureLocations.remove(MinecraftProfileTexture.Type.SKIN);
        player.getPlayerInfo().textureLocations.put(MinecraftProfileTexture.Type.SKIN, LichModeRenderer.TEXTURE);
        getModel(event.getRenderer()).ifPresent(model -> event.getRenderer().model = model);
    }

    public static Optional<PlayerModel<AbstractClientPlayer>> getModel(final PlayerRenderer renderer) {
        final LichModeModel<AbstractClientPlayer> model = new LichModeModel<>(LichModeModel.createMesh(new CubeDeformation(1, 1, 1)).getRoot().bake(64, 64));
        //Remove Player Model Layers
        renderer.layers.clear();
        //Add Armor Layer
//        renderer.addLayer(new SmallSlimeArmorLayer<>(renderer));

        return Optional.of(model);
    }
}
