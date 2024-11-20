package io.github.dracosomething.trunited;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.io.*;

// The value here should match an entry in the META-IN F/mods.toml file
@Mod(trunited.MODID)
public class trunited {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "trunited";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String TENSURA_CONFIG = "common.toml";
    private File configFile;


    public trunited() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::onCommonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        configFile = new File("config/tensura-reincarnated/" + TENSURA_CONFIG);
    }

    @SubscribeEvent
    public void onCommonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("common setiup");
        // Load and edit the .toml file
        editTomlFile();
        LOGGER.info("common setup works");
    }

    public void editTomlFile() {
        File tomlFile = new File("config/tensura-reincarnated/common.toml"); // Adjust the path as needed

        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(tomlFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading the TOML file: " + e.getMessage());
            return;
        }

        String content = contentBuilder.toString();
        String[] newItem = {"trunited:test"}; // The item you want to add
        String startingraces = "startingRaces"; // The key for the list in your TOML
        String randomraces = "possibleRandomRaces";

        // Find the line containing the list and add the new item
        String startingline = startingraces + " = [";
        String randomline = randomraces + " = [";
        int index = content.indexOf(startingline);
        int index2 = content.indexOf(randomline);
        for (int i = 0; i < newItem.length; i++) {
            String ItemtoAdd = newItem[i];
            if (index != -1 || index2 != -1) {
                // Find the end of the list
                int endIndex = content.indexOf("]", index) + 1;
                int endIndex2 = content.indexOf("]", index2) + 1;
                if (endIndex != 0 || index2 != 0) {
                    // Insert the new item before the closing bracket
                    String startingRaces = content.substring(index, endIndex);
                    String possibleRandomRaces = content.substring(index2, endIndex2);
                    if (!startingRaces.contains(ItemtoAdd)) {
                        String updatedList = startingRaces.replace("]", ", \"" + ItemtoAdd + "\"]");
                        content = content.replace(startingRaces, updatedList);
                        System.out.println("startingRaces has been updated");
                    }
                    if (!possibleRandomRaces.contains(ItemtoAdd)) {
                        String updatedList = possibleRandomRaces.replace("]", ", \"" + ItemtoAdd + "\"]");
                        content = content.replace(possibleRandomRaces, updatedList);
                        System.out.println("possibleRandomRaces has been updated");
                    }
                } else {
                    System.out.println("Closing bracket not found for list.");
                }
            } else {
                System.out.println("List identifier not found.");
            }
        }

        // Step 3: Write the modified content back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tomlFile))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error writing to the TOML file: " + e.getMessage());
        }

        System.out.println("Item added to TOML list successfully.");
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
