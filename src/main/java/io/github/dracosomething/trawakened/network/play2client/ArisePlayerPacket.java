package io.github.dracosomething.trawakened.network.play2client;

import io.github.dracosomething.trawakened.capability.ShadowCapability.AwakenedShadowCapability;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ArisePlayerPacket {
    private final UUID playerId;
    private final boolean arise;

    public ArisePlayerPacket(FriendlyByteBuf buf) {
        playerId = buf.readUUID();
        arise = buf.readBoolean();
    }

    public ArisePlayerPacket(UUID playerId, boolean arise) {
        this.playerId = playerId;
        this.arise = arise;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(playerId);
        buf.writeBoolean(arise);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player user = ctx.get().getSender();
            if (user != null && user.getLevel() instanceof ServerLevel serverLevel) {
                Entity entity = serverLevel.getEntity(this.playerId);

                if (entity instanceof LivingEntity living) {
                    AwakenedShadowCapability.setArisen(user, arise);
                    if (arise) {
                        living.sendSystemMessage(Component.literal("The target refuses to get revived"));
                        AwakenedShadowCapability.setOwnerUUID(user, living.getUUID());
                    } else {
                        living.sendSystemMessage(Component.literal("successfully arised the target"));
                    }
                    AwakenedShadowCapability.setTries(user, AwakenedShadowCapability.getTries(user)-1);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
