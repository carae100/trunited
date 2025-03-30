package io.github.dracosomething.trawakened.network.play2client;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.github.dracosomething.trawakened.menu.NamingScreen;
import io.github.dracosomething.trawakened.menu.WordScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class openWordScreen {
    private final UUID targetId;
    private final ManasSkillInstance instance;

    public openWordScreen(UUID id, ManasSkillInstance instance) {
        targetId = id;
        this.instance = instance;
    }

    public openWordScreen(FriendlyByteBuf buf) {
        targetId = buf.readUUID();
        instance = ManasSkillInstance.fromNBT(buf.readNbt());
    }


    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(targetId);
        buf.writeNbt(instance.toNBT());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            openWordScreen.ClientHandler handler = new openWordScreen.ClientHandler();
            ctx.get().enqueueWork(() -> handler.handle(targetId, instance));
        }

        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static class ClientHandler {
        public void handle(UUID id, ManasSkillInstance instance) {
            Minecraft mc = Minecraft.getInstance();

            mc.setScreen(new WordScreen(id, instance));
        }
    }
}
