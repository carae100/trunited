package io.github.dracosomething.trawakened.capability.ShadowCapability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AwakenedShadowCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
    private final IShadowCapability defaultCapability = new AwakenedShadowCapability();
    private final LazyOptional<IShadowCapability> cap = LazyOptional.of(() -> {
        return this.defaultCapability;
    });

    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction direction) {
        return cap == AwakenedShadowCapability.CAPABILITY ? this.cap.cast() : LazyOptional.empty();
    }

    public CompoundTag serializeNBT() {
        return this.defaultCapability.serializeNBT();
    }

    public void deserializeNBT(CompoundTag compoundTag) {
        this.defaultCapability.deserializeNBT(compoundTag);
    }
}
