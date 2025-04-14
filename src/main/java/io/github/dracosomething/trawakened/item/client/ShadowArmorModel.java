package io.github.dracosomething.trawakened.item.client;

import io.github.dracosomething.trawakened.item.shadowItems.shadowArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ShadowArmorModel extends AnimatedGeoModel<shadowArmorItem> {
    public ResourceLocation getModelResource(shadowArmorItem object) {
        return new ResourceLocation("tensura", "geo/armor/hihiirokane_armor.geo.json");
    }

    public ResourceLocation getTextureResource(shadowArmorItem object) {
        return new ResourceLocation("trawakened", "textures/models/armor/shadow_layer_0.png");
    }

    public ResourceLocation getAnimationResource(shadowArmorItem item) {
        return new ResourceLocation("tensura", "animations/armor.animation.json");
    }
}
