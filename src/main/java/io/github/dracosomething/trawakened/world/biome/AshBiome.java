package io.github.dracosomething.trawakened.world.biome;

import com.github.manasmods.manascore.api.world.gen.biome.BiomeBuilder;
import com.github.manasmods.manascore.api.world.gen.biome.BiomeGenerationSettingsHelper;
import com.github.manasmods.manascore.api.world.gen.biome.MobSpawnHelper;
import com.github.manasmods.tensura.registry.biome.TensuraPlacedFeatures;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;

import java.awt.*;

public class AshBiome {
    public static Biome create() {
        BiomeGenerationSettingsHelper generationSettingsHelper = (
                new BiomeGenerationSettingsHelper()
        ).addCarver(
                GenerationStep.Carving.AIR, Carvers.CAVE
        ).addCarver(
                GenerationStep.Carving.AIR, Carvers.CAVE_EXTRA_UNDERGROUND
        ).addCarver(
                GenerationStep.Carving.AIR, Carvers.CANYON
        ).addFeature(
                GenerationStep.Decoration.LAKES, MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND
        ).apply(
                BiomeDefaultFeatures::addDefaultCrystalFormations
        ).apply(
                BiomeDefaultFeatures::addDefaultMonsterRoom
        ).apply(
                BiomeDefaultFeatures::addDefaultUndergroundVariety
        ).apply(
                BiomeDefaultFeatures::addSurfaceFreezing
        ).apply(
                BiomeDefaultFeatures::addForestFlowers
        ).apply(
                BiomeDefaultFeatures::addForestGrass
        ).apply(
                BiomeDefaultFeatures::addPlainGrass
        ).apply(
                BiomeDefaultFeatures::addDefaultExtraVegetation
        ).apply(
                BiomeDefaultFeatures::addDefaultOres,
                BiomeDefaultFeatures::addDefaultSoftDisks
        );
        MobSpawnHelper mobSpawnHelper = (
                new MobSpawnHelper()
        ).apply(
                BiomeDefaultFeatures::farmAnimals
        );
        return BiomeBuilder
                .forest(generationSettingsHelper, mobSpawnHelper)
                .grassColor(new Color(154, 188, 66))
                .build();
    }
}
