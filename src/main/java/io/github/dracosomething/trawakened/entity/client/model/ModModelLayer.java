package io.github.dracosomething.trawakened.entity.client.model;

import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayer {
    public static final ModelLayerLocation LICH = register("lich");

    private static ModelLayerLocation register(String p_171294_) {
        return register(p_171294_, "main");
    }

    private static ModelLayerLocation register(String p_171296_, String p_171297_) {
        ModelLayerLocation modellayerlocation = createLocation(p_171296_, p_171297_);
        return modellayerlocation;
    }

    private static ModelLayerLocation createLocation(String p_171301_, String p_171302_) {
        return new ModelLayerLocation(new ResourceLocation(trawakened.MODID, p_171301_), p_171302_);
    }
}
