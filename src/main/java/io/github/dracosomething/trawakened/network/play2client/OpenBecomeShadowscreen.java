package io.github.dracosomething.trawakened.network.play2client;

import io.github.dracosomething.trawakened.menu.BecomeShadowScreen;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class OpenBecomeShadowscreen {
    private final UUID targetId;

    public OpenBecomeShadowscreen(UUID id) {
        targetId = id;
    }

    public OpenBecomeShadowscreen(FriendlyByteBuf buf) {
        targetId = buf.readUUID();
    }


    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(targetId);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            ClientHandler handler = new ClientHandler();
            ctx.get().enqueueWork(() -> handler.handle(targetId));
        }

        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static class ClientHandler {
        public void handle(UUID id) {
            Minecraft mc = Minecraft.getInstance();

            mc.setScreen(new BecomeShadowScreen(id));
        }
    }}
