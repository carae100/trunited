package io.github.dracosomething.trawakened.network.play2client;

import io.github.dracosomething.trawakened.capability.ShadowCapability.IShadowCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncShadowCapabiliyPacket {
    private final CompoundTag tag;
    private final int entityId;

    public SyncShadowCapabiliyPacket(FriendlyByteBuf buf) {
        this.tag = buf.readAnySizeNbt();
        this.entityId = buf.readInt();
    }

    public SyncShadowCapabiliyPacket(IShadowCapability data, int entityId) {
        this.tag = data.serializeNBT();
        this.entityId = entityId;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(this.tag);
        buf.writeInt(this.entityId);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        (ctx.get()).enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> {
                return () -> {
                    ClientAccess.updatePlayerCapability(this.entityId, this.tag);
                };
            });
        });
    }
}
