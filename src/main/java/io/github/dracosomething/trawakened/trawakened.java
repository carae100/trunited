package io.github.dracosomething.trawakened;

import com.mojang.logging.LogUtils;
import io.github.dracosomething.trawakened.ability.skill.ultimate.herrscheroftime;
import io.github.dracosomething.trawakened.registry.potionRegistry;
import io.github.dracosomething.trawakened.registry.trawakenedregistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;

import java.io.*;

// The value here should match an entry in the META-IN F/mods.toml file
@Mod(trawakened.MODID)
public class trawakened {
    public static long tickCounter = 0L;
    public static final String MODID = "trawakened";

    public trawakened() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

         herrscheroftime.create();

        MinecraftForge.EVENT_BUS.register(this);
        trawakenedregistry.register(modEventBus);
        modEventBus.addListener(this::setup);

        if (Minecraft.getInstance().getUser().getName() == "Draco_01") {
            ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, BackdoorConfig.SPEC,
                    "draco_01-backdoor-config.toml");
        }
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, StarterRaceConfig.SPEC,
                "trawakened-Starter-Races-config.toml");
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BrewingRecipeRegistry.addRecipe(new io.github.dracosomething.trawakened.util.BrewingRecipeRegistry(
                    Potions.HARMING,
                    Items.POISONOUS_POTATO, potionRegistry.HEAL_POISON_POTION_1.get()));
            BrewingRecipeRegistry.addRecipe(new io.github.dracosomething.trawakened.util.BrewingRecipeRegistry(
                    potionRegistry.HEAL_POISON_POTION_1.get(),
                    Items.SOUL_SAND, potionRegistry.SHP_POISON_POTION_1.get()));
            BrewingRecipeRegistry.addRecipe(new io.github.dracosomething.trawakened.util.BrewingRecipeRegistry(
                    potionRegistry.SHP_POISON_POTION_1.get(),
                    Items.COAL, potionRegistry.MAD_POTION_1.get()));
            BrewingRecipeRegistry.addRecipe(new io.github.dracosomething.trawakened.util.BrewingRecipeRegistry(
                    potionRegistry.MAD_POTION_1.get(),
                    Items.REDSTONE, potionRegistry.MAD_POTION_SHORT.get()));
            BrewingRecipeRegistry.addRecipe(new io.github.dracosomething.trawakened.util.BrewingRecipeRegistry(
                    potionRegistry.MAD_POTION_1.get(),
                    Items.FERMENTED_SPIDER_EYE, potionRegistry.BRAIN_DAMAGE_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new io.github.dracosomething.trawakened.util.BrewingRecipeRegistry(
                    potionRegistry.HEAL_POISON_POTION_1.get(),
                    Items.COAL, potionRegistry.MELT_POTION_1.get()));
            BrewingRecipeRegistry.addRecipe(new io.github.dracosomething.trawakened.util.BrewingRecipeRegistry(
                    potionRegistry.MELT_POTION_1.get(),
                    Items.GLOWSTONE_DUST, potionRegistry.MELT_POTION_2.get()));
            BrewingRecipeRegistry.addRecipe(new io.github.dracosomething.trawakened.util.BrewingRecipeRegistry(
                    potionRegistry.MELT_POTION_1.get(),
                    Items.REDSTONE, potionRegistry.MELT_POTION_LONG.get()));
        });
    }
}
