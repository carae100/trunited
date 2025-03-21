package io.github.dracosomething.trawakened.network.play2client;

import io.github.dracosomething.trawakened.capability.ShadowCapability.AwakenedShadowCapability;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.AwakenedFearCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

class ClientAccess {
    static final Minecraft minecraft = Minecraft.getInstance();

    private ClientAccess() {
    }

    static void updatePlayerCapability(int entityId, CompoundTag tag) {
        if (minecraft.level != null) {
            Entity entity = minecraft.level.getEntity(entityId);
            if (entity instanceof LivingEntity) {
                LivingEntity player = (LivingEntity) entity;
                AwakenedFearCapability.getFrom(player).ifPresent((data) -> data.deserializeNBT(tag));
                player.refreshDimensions();
            }
        }
    }

    static void updateShadowCapability(int entityId, CompoundTag tag) {
        if (minecraft.level != null) {
            Entity entity = minecraft.level.getEntity(entityId);
            if (entity instanceof LivingEntity living) {
                AwakenedShadowCapability.getFrom(living).ifPresent((data) -> data.deserializeNBT(tag));
                living.refreshDimensions();
            }
        }
    }
}