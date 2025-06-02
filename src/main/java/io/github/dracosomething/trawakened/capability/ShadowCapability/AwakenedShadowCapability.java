package io.github.dracosomething.trawakened.capability.ShadowCapability;

import io.github.dracosomething.trawakened.handler.CapabilityHandler;
import io.github.dracosomething.trawakened.library.shadowRank;
import io.github.dracosomething.trawakened.network.TRAwakenedNetwork;
import io.github.dracosomething.trawakened.network.play2client.SyncShadowCapabiliyPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;

import java.util.Objects;
import java.util.UUID;

public class AwakenedShadowCapability implements IShadowCapability {
    public static final Capability<IShadowCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<IShadowCapability>() {});
    private boolean isShadow = false;
    private boolean isArisen = false;
    private int tries = 3;
    private UUID ownerUUID = UUID.randomUUID();
    private shadowRank rank = shadowRank.BASIC;
    private boolean hasShadow = false;
    private CompoundTag ShadowStorage = new CompoundTag();

    public static LazyOptional<IShadowCapability> getFrom(LivingEntity entity) {
        return entity.getCapability(CAPABILITY);
    }

    public static void sync(LivingEntity entity) {
        if (!entity.level.isClientSide()) {
            getFrom(entity).ifPresent(data -> TRAwakenedNetwork.INSTANCE.send(
                    PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity),
                    new SyncShadowCapabiliyPacket(data, entity.getId())
            ));
        }
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("isShadow", isShadow);
        tag.putBoolean("isArisen", isArisen);
        tag.putInt("tries", tries);
        tag.putString("ownerUUID", ownerUUID.toString());
        tag.put("rank", rank.toNBT());
        tag.put("storage", ShadowStorage);
        tag.putBoolean("hasShadow", hasShadow);
        return tag;
    }

    public void deserializeNBT(CompoundTag compoundTag) {
        this.isShadow = compoundTag.getBoolean("isShadow");
        this.isArisen = compoundTag.getBoolean("isArisen");
        this.tries = compoundTag.getInt("tries");
        this.ownerUUID = UUID.fromString(compoundTag.getString("ownerUUID"));
        this.rank = shadowRank.fromNBT(compoundTag.getCompound("rank"));
        this.hasShadow = compoundTag.getBoolean("hasShadow");
        this.ShadowStorage = compoundTag.getCompound("storage");
    }

    public static boolean isShadow(LivingEntity entity) {
        IShadowCapability capability = CapabilityHandler.getCapability(entity, CAPABILITY);
        return capability == null ? false : capability.isShadow();
    }

    public static boolean isOwnerShadow(LivingEntity target, LivingEntity entity) {
        return AwakenedShadowCapability.isArisen(target) &&
                Objects.equals(AwakenedShadowCapability.getOwnerUUID(target).toString(), entity.getUUID().toString()) &&
                AwakenedShadowCapability.isShadow(target);
    }

    public static void setShadow(LivingEntity entity, boolean isShadow) {
        IShadowCapability capability = CapabilityHandler.getCapability(entity, CAPABILITY);
        if (capability == null) return;
        capability.setShadow(isShadow);
    }

    public static boolean isArisen(LivingEntity entity) {
        IShadowCapability capability = CapabilityHandler.getCapability(entity, CAPABILITY);
        return capability == null ? false : capability.isArisen();
    }

    public static void setArisen(LivingEntity entity, boolean isArisen) {
        IShadowCapability capability = CapabilityHandler.getCapability(entity, CAPABILITY);
        if (capability == null) return;
        capability.setArisen(isArisen);
    }

    public static int getTries(LivingEntity entity) {
        IShadowCapability capability = CapabilityHandler.getCapability(entity, CAPABILITY);
        return capability == null ? 0 : capability.getTries();
    }

    public static void setTries(LivingEntity entity, int amount) {
        IShadowCapability capability = CapabilityHandler.getCapability(entity, CAPABILITY);
        if (capability == null) return;
        capability.setTries(amount);
    }

    public static UUID getOwnerUUID(LivingEntity entity) {
        IShadowCapability capability = CapabilityHandler.getCapability(entity, CAPABILITY);
        return capability == null ? UUID.randomUUID() : capability.getOwnerUUID();
    }

    public static void setOwnerUUID(LivingEntity entity, UUID uuid) {
        IShadowCapability capability = CapabilityHandler.getCapability(entity, CAPABILITY);
        if (capability == null) return;
        capability.setOwnerUUID(uuid);
    }

    public static shadowRank getRank(LivingEntity entity) {
        IShadowCapability capability = CapabilityHandler.getCapability(entity, CAPABILITY);
        return capability == null ? shadowRank.BASIC : capability.getRank();
    }

    public static void setRank(LivingEntity entity, shadowRank rank) {
        IShadowCapability capability = CapabilityHandler.getCapability(entity, CAPABILITY);
        if (capability == null) return;
        capability.setRank(rank);
    }

    public static boolean hasShadow(LivingEntity entity) {
        IShadowCapability capability = CapabilityHandler.getCapability(entity, CAPABILITY);
        return capability == null ? false : capability.hasShadow();
    }

    public static void setHasShadow(LivingEntity entity, boolean hasShadow) {
        IShadowCapability capability = CapabilityHandler.getCapability(entity, CAPABILITY);
        if (capability == null) return;
        capability.setHasShadow(hasShadow);
    }

    public static CompoundTag getStorage(LivingEntity entity) {
        IShadowCapability capability = CapabilityHandler.getCapability(entity, CAPABILITY);
        return capability == null ? new CompoundTag() : capability.getStorage();
    }

    public static void setStorage(LivingEntity entity, CompoundTag shadowStorage) {
        IShadowCapability capability = CapabilityHandler.getCapability(entity, CAPABILITY);
        if (capability == null) return;
        capability.setStorage(shadowStorage);
    }

    public boolean isShadow() {
        return isShadow;
    }

    public void setShadow(boolean shadow) {
        isShadow = shadow;
    }

    public boolean isArisen() {
        return isArisen;
    }

    public void setArisen(boolean arisen) {
        this.isArisen = arisen;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int amount) {
        tries = amount;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(UUID uuid) {
        ownerUUID = uuid;
    }

    public shadowRank getRank() {
        return this.rank;
    }

    public void setRank(shadowRank rank) {
        this.rank = rank;
    }

    public boolean hasShadow() {
        return hasShadow;
    }

    public void setHasShadow(boolean hasShadow) {
        this.hasShadow = hasShadow;
    }

    public CompoundTag getStorage() {
        return ShadowStorage;
    }

    public void setStorage(CompoundTag storage) {
        this.ShadowStorage = storage;
    }
}
