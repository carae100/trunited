package io.github.dracosomething.trawakened.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class dimensionRegistry {
    public static final ResourceKey<Level> SHADOW = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("trawakened", "shadow_dimension"));;
    public static final ResourceKey<DimensionType> SHADOW_TYPE = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, SHADOW.location());
    public static final ResourceKey<NoiseGeneratorSettings> SHADOW_NOICE = ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, SHADOW.location());

    public static void init() {
        System.out.println("registering dimensions");
        System.out.println(SHADOW);
        System.out.println(SHADOW_TYPE);
        System.out.println(SHADOW_NOICE);
    }
}
