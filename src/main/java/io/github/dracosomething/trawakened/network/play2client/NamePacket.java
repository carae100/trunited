package io.github.dracosomething.trawakened.network.play2client;

import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.event.NamingEvent;
import com.github.manasmods.tensura.network.play2server.RequestNamingGUIPacket;
import io.github.dracosomething.trawakened.capability.ShadowCapability.AwakenedShadowCapability;
import io.github.dracosomething.trawakened.event.BecomeShadowEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class NamePacket {
    private final UUID playerId;
    private final String name;

    public NamePacket(FriendlyByteBuf buf) {
        playerId = buf.readUUID();
        name = buf.readUtf();
    }

    public NamePacket(UUID playerId, String name) {
        this.playerId = playerId;
        this.name = name;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(playerId);
        buf.writeUtf(name);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player user = ctx.get().getSender();
            if (user != null && user.getLevel() instanceof ServerLevel serverLevel) {
                Entity entity = serverLevel.getEntity(this.playerId);

                if (entity instanceof LivingEntity living) {
                    NamingEvent event = new NamingEvent(living, user, 0, RequestNamingGUIPacket.NamingType.MEDIUM, name);
                    event.setCalculatedCost(0);
                    MinecraftForge.EVENT_BUS.post(event);
                    living.setCustomName(Component.literal(name));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
