package io.github.dracosomething.trawakened.mixin.client;

import com.google.common.collect.ImmutableMap;
import io.github.dracosomething.trawakened.entity.client.renderer.CustomPlayerRenderer.OverdrivenRenderer;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LoadingFailedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(EntityRenderers.class)
public class EntityRenderersMixin {
    @Inject(method = "createPlayerRenderers(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;)Ljava/util/Map;", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void createLichRenderer(EntityRendererProvider.Context context, CallbackInfoReturnable<Map<String, EntityRenderer<? extends Player>>> cir, ImmutableMap.Builder<String, EntityRenderer<? extends Player>> builder) {
        if (noLoadingExceptions()) {
            builder.put(trawakened.MODID + ":lich", new OverdrivenRenderer(context));
        }
    }

    @Unique
    private static boolean noLoadingExceptions() {
        System.out.println();
        LoadingFailedException error = ClientModLoaderAccessor.getError();
        return error == null || error.getErrors().isEmpty();
    }
}
