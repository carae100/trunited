package io.github.dracosomething.trawakened;

import com.mojang.logging.LogUtils;
import io.github.dracosomething.trawakened.registry.trawakenedregistry;
import io.github.dracosomething.trawakened.util.ModItemProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.io.*;

// The value here should match an entry in the META-IN F/mods.toml file
@Mod(trawakened.MODID)
public class trawakened {

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
        modEventBus.addListener(this::clientSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        trawakenedregistry.register(modEventBus);

        configFile = new File("config/tensura-reincarnated/" + TENSURA_CONFIG);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, StarterRaceConfig.SPEC, "trawakened-Starter-Races-config.toml");
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event){
        ModItemProperties.addCustomItemProperties();
    }
}
