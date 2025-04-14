package io.github.dracosomething.trawakened.world;

import com.github.manasmods.tensura.item.templates.SimpleBowItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import io.github.dracosomething.trawakened.registry.items.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class trawakenedItemProperties {
    public static void addItemProperties(final FMLClientSetupEvent event) {
        event.enqueueWork(() ->
        {
            bowPulling(weapons.SHADOW_BOW.get());
        });
    }

    private static void bowPulling(Item bow) {
        ItemProperties.register(bow, new ResourceLocation("pull"), (itemStack, level, entity, i) -> {
            if (entity == null) {
                return 0.0F;
            } else if (entity.getUseItem() != itemStack) {
                return 0.0F;
            } else {
                float chargeTicks = ((SimpleBowItem)itemStack.getItem()).getChargeTicks();
                return (float)(itemStack.getUseDuration() - entity.getUseItemRemainingTicks()) / chargeTicks;
            }
        });
        ItemProperties.register(bow, new ResourceLocation("pulling"), (itemStack, level, entity, i) -> {
            return entity != null && entity.isUsingItem() && entity.getUseItem() == itemStack ? 1.0F : 0.0F;
        });
    }
}
