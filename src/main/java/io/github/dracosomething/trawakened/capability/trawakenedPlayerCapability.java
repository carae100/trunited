package io.github.dracosomething.trawakened.capability;

import com.github.manasmods.tensura.capability.race.ITensuraPlayerCapability;
import com.github.manasmods.tensura.handler.CapabilityHandler;
import net.minecraft.world.entity.player.Player;

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
}
