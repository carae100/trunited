package io.github.dracosomething.trawakened.network.play2client;

import io.github.dracosomething.trawakened.capability.alternateFearCapability.IFearCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncFearCapabilityPacket {
    private final CompoundTag tag;
    private final int entityId;

    public SyncFearCapabilityPacket(FriendlyByteBuf buf) {
        this.tag = buf.readAnySizeNbt();
        this.entityId = buf.readInt();
    }

    public SyncFearCapabilityPacket(IFearCapability data, int entityId) {
        this.tag = (CompoundTag)data.serializeNBT();
        this.entityId = entityId;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(this.tag);
        buf.writeInt(this.entityId);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context)ctx.get()).enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> {
                return () -> {
                    ClientAccess.updatePlayerCapability(this.entityId, this.tag);
                };
            });
        });
        ((NetworkEvent.Context)ctx.get()).setPacketHandled(true);
    }
}
