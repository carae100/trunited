package io.github.dracosomething.trawakened.client.events;

import io.github.dracosomething.trawakened.entity.client.model.CustomPlayerModel.OverdrivenModel;
import io.github.dracosomething.trawakened.entity.client.model.ModModelLayer;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = trawakened.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void RegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayer.LICH, () -> LayerDefinition.create(OverdrivenModel.createMesh(CubeDeformation.NONE), 64, 64));
    }
}
