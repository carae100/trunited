package io.github.dracosomething.trawakened.client;

import com.github.manasmods.tensura.entity.client.player.OtherworlderRenderer;
import com.github.manasmods.tensura.registry.entity.TensuraEntityTypes;
import io.github.dracosomething.trawakened.registry.entityRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = "trawakened", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class mobRenderers {
    @SubscribeEvent
    public static void register(FMLClientSetupEvent e) {
        registerEntityRenderers();
    }

    public static void registerEntityRenderers() {
        EntityRenderers.register((EntityType) entityRegistry.DEFAULT_OTHER_WORLDER.get(), (pContext) -> {
            return new OtherworlderRenderer(pContext, false);
        });
    }
}
