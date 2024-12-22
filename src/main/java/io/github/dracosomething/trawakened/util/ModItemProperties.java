package io.github.dracosomething.trawakened.util;

import io.github.dracosomething.trawakened.registry.itemRegistry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ModItemProperties {
    public static void addCustomItemProperties() {
        makeStaff(itemRegistry.staffitem.get());
    }

    private static void makeStaff(Item item) {
        ItemProperties.register(item, new ResourceLocation("attack_range"), (p_174640_, p_174641_, p_174642_, p_174643_) -> p_174640_.hasTag() ? (float)p_174640_.getTag().getInt("attack_range") : 0.0F);
    }
}
