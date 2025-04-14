package io.github.dracosomething.trawakened.registry;

import com.github.manasmods.tensura.world.biome.*;
import io.github.dracosomething.trawakened.world.biome.AshBiome;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class biomeRegistry {
    private static final DeferredRegister<Biome> registry;
    public static final RegistryObject<Biome> ASH_BIOME;

    public static void init(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.BIOMES, "trawakened");
        ASH_BIOME = registry.register("ash_biome", AshBiome::create);
    }
}
