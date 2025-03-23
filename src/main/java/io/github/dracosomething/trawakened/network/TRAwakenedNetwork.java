package io.github.dracosomething.trawakened.network;

import io.github.dracosomething.trawakened.network.play2client.*;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TRAwakenedNetwork {
    private static final String PROTOCOL_VERSION = ModList.get().getModFileById("trawakened").versionString().replaceAll("\\.", "");
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("trawakened", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );


    public static void register() {
        int i = 0;
        INSTANCE.registerMessage(++i, SyncFearCapabilityPacket.class, SyncFearCapabilityPacket::toBytes, SyncFearCapabilityPacket::new, SyncFearCapabilityPacket::handle);
        INSTANCE.registerMessage(++i, SyncShadowCapabiliyPacket.class, SyncShadowCapabiliyPacket::toBytes, SyncShadowCapabiliyPacket::new, SyncShadowCapabiliyPacket::handle);
        INSTANCE.registerMessage(++i, ArisePlayerPacket.class, ArisePlayerPacket::toBytes, ArisePlayerPacket::new, ArisePlayerPacket::handle);
        INSTANCE.registerMessage(++i, OpenBecomeShadowscreen.class, OpenBecomeShadowscreen::toBytes, OpenBecomeShadowscreen::new, OpenBecomeShadowscreen::handle);
        INSTANCE.registerMessage(++i, OpenNamingscreen.class, OpenNamingscreen::toBytes, OpenNamingscreen::new, OpenNamingscreen::handle);
        INSTANCE.registerMessage(++i, NamePacket.class, NamePacket::toBytes, NamePacket::new, NamePacket::handle);
    }

    public static <T> void toServer(T message) {
        INSTANCE.sendToServer(message);
    }
}
