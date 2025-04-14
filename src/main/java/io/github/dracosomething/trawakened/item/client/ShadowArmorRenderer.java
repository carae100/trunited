package io.github.dracosomething.trawakened.item.client;

import com.github.manasmods.tensura.item.armor.client.TensuraGeoArmorRenderer;
import io.github.dracosomething.trawakened.item.shadowItems.shadowArmorItem;
import net.minecraft.resources.ResourceLocation;

public class ShadowArmorRenderer extends TensuraGeoArmorRenderer<shadowArmorItem> {
    public ShadowArmorRenderer() {
        super(new ShadowArmorModel());
    }

    public ResourceLocation getTextureLocation(shadowArmorItem animatable) {

        return this.getGeoModelProvider().getTextureResource(animatable);
    }
}
