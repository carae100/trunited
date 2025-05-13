package io.github.dracosomething.trawakened.network.play2server;

import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import io.github.dracosomething.trawakened.capability.ShadowCapability.AwakenedShadowCapability;
import io.github.dracosomething.trawakened.event.BecomeShadowEvent;
import io.github.dracosomething.trawakened.handler.TimeStopHandler;
import io.github.dracosomething.trawakened.helper.TimeStopHelper;
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

public class ServerStopTime {
    private boolean timeStopped;

    public ServerStopTime(FriendlyByteBuf buf) {
        timeStopped = buf.readBoolean();
    }

    public ServerStopTime(boolean timeStopped) {
        this.timeStopped = timeStopped;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(timeStopped);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            TimeStopHelper.TimeStopped = timeStopped;
        });
        ctx.get().setPacketHandled(true);
    }
}
