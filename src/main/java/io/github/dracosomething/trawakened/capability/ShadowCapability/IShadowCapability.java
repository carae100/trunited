package io.github.dracosomething.trawakened.capability.ShadowCapability;

import io.github.dracosomething.trawakened.library.shadowRank;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public interface IShadowCapability extends INBTSerializable<CompoundTag> {
    boolean isShadow();

    void setShadow(boolean shadow);

    boolean isArisen();

    void setArisen(boolean arisen);

    int getTries();

    void setTries(int amount);

    UUID getOwnerUUID();

    void setOwnerUUID(UUID uuid);

    shadowRank getRank();

    void setRank(shadowRank rank);
}
