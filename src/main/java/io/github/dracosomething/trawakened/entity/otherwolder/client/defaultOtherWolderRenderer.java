package io.github.dracosomething.trawakened.entity.otherwolder.client;

import com.github.manasmods.tensura.entity.client.player.PlayerLikeModel;
import com.github.manasmods.tensura.entity.client.player.PlayerLikeRenderer;
import com.github.manasmods.tensura.entity.human.IOtherworlder;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.TamableAnimal;
import org.jetbrains.annotations.NotNull;

public class defaultOtherWolderRenderer<T extends TamableAnimal & IOtherworlder> extends PlayerLikeRenderer<T> {
    public defaultOtherWolderRenderer(EntityRendererProvider.Context pContext, boolean slim) {
        super(pContext, new PlayerLikeModel(pContext.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slim), 0.5F);
        this.addLayer(new HumanoidArmorLayer(this, new HumanoidModel(pContext.bakeLayer(slim ? ModelLayers.PLAYER_SLIM_INNER_ARMOR : ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel(pContext.bakeLayer(slim ? ModelLayers.PLAYER_SLIM_OUTER_ARMOR : ModelLayers.PLAYER_OUTER_ARMOR))));
    }

    protected boolean shouldShowName(T pEntity) {
        return true;
    }

    public @NotNull ResourceLocation getTextureLocation(T entity) {
        return ((IOtherworlder)entity).getTextureLocation();
    }
}
