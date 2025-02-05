package io.github.dracosomething.trawakened.network;

import io.github.dracosomething.trawakened.network.play2client.SyncFearCapabilityPacket;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TRAwakenedNetwork {
    private static final String PROTOCOL_VERSION = ModList.get().getModFileById(trawakened.MODID).versionString().replaceAll("\\.", "");
    public static final SimpleChannel INSTANCE;

    public static void register() {
        int i = 0;
        ++i;
        INSTANCE.registerMessage(i, SyncFearCapabilityPacket.class, SyncFearCapabilityPacket::toBytes, SyncFearCapabilityPacket::new, SyncFearCapabilityPacket::handle);
    }

    static {
        ResourceLocation var10000 = new ResourceLocation(trawakened.MODID, "main");
        Supplier var10001 = () -> {
            return PROTOCOL_VERSION;
        };
        String var10002 = PROTOCOL_VERSION;
        Objects.requireNonNull(var10002);
        Predicate var0 = var10002::equals;
        String var10003 = PROTOCOL_VERSION;
        Objects.requireNonNull(var10003);
        INSTANCE = NetworkRegistry.newSimpleChannel(var10000, var10001, var0, var10003::equals);
    }
}
