package io.github.dracosomething.trawakened.capability.alternateFearCapability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AwakenedFearCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
    private final IFearCapability defaultCapability = new AwakenedFearCapability();
    private final LazyOptional<IFearCapability> cap = LazyOptional.of(() -> {
        return this.defaultCapability;
    });

    public AwakenedFearCapabilityProvider() {
    }

    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == AwakenedFearCapability.CAPABILITY ? this.cap.cast() : LazyOptional.empty();
    }

    public CompoundTag serializeNBT() {
        return (CompoundTag)this.defaultCapability.serializeNBT();
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.defaultCapability.deserializeNBT(nbt);
    }
}
