package io.github.dracosomething.trawakened.network.play2client;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import io.github.dracosomething.trawakened.ability.skill.ultimate.ShadowMonarch;
import io.github.dracosomething.trawakened.client.screen.ShadowSummonScreen;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class OpenShadowSummonScreen {
    private final UUID playerUUID;

    public OpenShadowSummonScreen(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public static void toBytes(OpenShadowSummonScreen msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerUUID);
    }

    public OpenShadowSummonScreen(FriendlyByteBuf buf) {
        this.playerUUID = buf.readUUID();
    }

    public static void handle(OpenShadowSummonScreen msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            handleClient(msg);
        });
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient(OpenShadowSummonScreen msg) {
        Player player = Minecraft.getInstance().level.getPlayerByUUID(msg.playerUUID);
        if (player != null) {
            ManasSkillInstance instance = SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SHADOW_MONARCH.get()).orElse(null);
            if (instance != null && instance.getSkill() instanceof ShadowMonarch skill) {
                Minecraft.getInstance().setScreen(new ShadowSummonScreen(skill, instance));
            }
        }
    }
}