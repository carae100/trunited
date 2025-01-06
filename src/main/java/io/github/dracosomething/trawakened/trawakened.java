package io.github.dracosomething.trawakened;

import com.mojang.logging.LogUtils;
import io.github.dracosomething.trawakened.registry.trawakenedregistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;

import java.io.*;

// The value here should match an entry in the META-IN F/mods.toml file
@Mod(trawakened.MODID)
public class trawakened {
    public static long tickCounter = 0L;

    // Define mod id in a common place for everything to reference
    public static final String MODID = "trawakened";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String TENSURA_CONFIG = "common.toml";
    private File configFile;


    public trawakened() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

//        herrscheroftime.create();

        // Register the commonSetup method for modloading

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        trawakenedregistry.register(modEventBus);

        GeckoLib.initialize();

        configFile = new File("config/tensura-reincarnated/" + TENSURA_CONFIG);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, StarterRaceConfig.SPEC, "trawakened-Starter-Races-config.toml");
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class clientSetup{
        @SubscribeEvent
        public static void  onClientSetup(FMLClientSetupEvent event) {
        }
    }
}
