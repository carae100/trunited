package io.github.dracosomething.trawakened.capability;

import com.github.manasmods.tensura.capability.race.ITensuraPlayerCapability;
import com.github.manasmods.tensura.handler.CapabilityHandler;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.List;

import static com.github.manasmods.tensura.capability.race.TensuraPlayerCapability.CAPABILITY;

public class trawakenedPlayerCapability {

    public static boolean isDemonLordSeed(Player player) {
        ITensuraPlayerCapability capability = (ITensuraPlayerCapability) CapabilityHandler.getCapability(player, CAPABILITY);
        return capability == null ? false : capability.isDemonLordSeed();
    }
    public static boolean isHeroEgg(Player player) {
        ITensuraPlayerCapability capability = (ITensuraPlayerCapability) CapabilityHandler.getCapability(player, CAPABILITY);
        return capability == null ? false : capability.isHeroEgg();
    }

    public static float getMaxSpiritualHealth(LivingEntity entity) {
        return (float)entity.getAttributeValue((Attribute)TensuraAttributeRegistry.MAX_SPIRITUAL_HEALTH.get());
    }
}
