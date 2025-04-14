package io.github.dracosomething.trawakened.world.biome;

import com.mojang.datafixers.util.Pair;
import io.github.dracosomething.trawakened.registry.biomeRegistry;
import net.minecraft.SharedConstants;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.TerrainProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CubicSpline;
import net.minecraft.util.ToFloatFunction;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseRouterData;

import java.util.List;
import java.util.function.Consumer;

public final class ShadowBiomeBuilder {
    private static final float VALLEY_SIZE = 0.05F;
    private static final float LOW_START = 0.26666668F;
    public static final float HIGH_START = 0.4F;
    private static final float HIGH_END = 0.93333334F;
    private static final float PEAK_SIZE = 0.1F;
    public static final float PEAK_START = 0.56666666F;
    private static final float PEAK_END = 0.7666667F;
    public static final float NEAR_INLAND_START = -0.11F;
    public static final float MID_INLAND_START = 0.03F;
    public static final float FAR_INLAND_START = 0.3F;
    public static final float EROSION_INDEX_1_START = -0.78F;
    public static final float EROSION_INDEX_2_START = -0.375F;
    private static final float EROSION_DEEP_DARK_DRYNESS_THRESHOLD = -0.225F;
    private static final float DEPTH_DEEP_DARK_DRYNESS_THRESHOLD = 0.9F;
    private final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0F, 1.0F);
    private final Climate.Parameter[] temperatures = new Climate.Parameter[]{Climate.Parameter.span(-1.0F, -0.45F), Climate.Parameter.span(-0.45F, -0.15F), Climate.Parameter.span(-0.15F, 0.2F), Climate.Parameter.span(0.2F, 0.55F), Climate.Parameter.span(0.55F, 1.0F)};
    private final Climate.Parameter[] humidities = new Climate.Parameter[]{Climate.Parameter.span(-1.0F, -0.35F), Climate.Parameter.span(-0.35F, -0.1F), Climate.Parameter.span(-0.1F, 0.1F), Climate.Parameter.span(0.1F, 0.3F), Climate.Parameter.span(0.3F, 1.0F)};
    private final Climate.Parameter[] erosions = new Climate.Parameter[]{Climate.Parameter.span(-1.0F, -0.78F), Climate.Parameter.span(-0.78F, -0.375F), Climate.Parameter.span(-0.375F, -0.2225F), Climate.Parameter.span(-0.2225F, 0.05F), Climate.Parameter.span(0.05F, 0.45F), Climate.Parameter.span(0.45F, 0.55F), Climate.Parameter.span(0.55F, 1.0F)};
    private final Climate.Parameter FROZEN_RANGE;
    private final Climate.Parameter UNFROZEN_RANGE;
    private final Climate.Parameter mushroomFieldsContinentalness;
    private final Climate.Parameter deepOceanContinentalness;
    private final Climate.Parameter oceanContinentalness;
    private final Climate.Parameter coastContinentalness;
    private final Climate.Parameter inlandContinentalness;
    private final Climate.Parameter nearInlandContinentalness;
    private final Climate.Parameter midInlandContinentalness;
    private final Climate.Parameter farInlandContinentalness;
    private final ResourceKey<Biome>[][] PLATEAU_BIOMES;

    public ShadowBiomeBuilder() {
        this.FROZEN_RANGE = this.temperatures[0];
        this.UNFROZEN_RANGE = Climate.Parameter.span(this.temperatures[1], this.temperatures[4]);
        this.mushroomFieldsContinentalness = Climate.Parameter.span(-1.2F, -1.05F);
        this.deepOceanContinentalness = Climate.Parameter.span(-1.05F, -0.455F);
        this.oceanContinentalness = Climate.Parameter.span(-0.455F, -0.19F);
        this.coastContinentalness = Climate.Parameter.span(-0.19F, -0.11F);
        this.inlandContinentalness = Climate.Parameter.span(-0.11F, 0.55F);
        this.nearInlandContinentalness = Climate.Parameter.span(-0.11F, 0.03F);
        this.midInlandContinentalness = Climate.Parameter.span(0.03F, 0.3F);
        this.farInlandContinentalness = Climate.Parameter.span(0.3F, 1.0F);
        this.PLATEAU_BIOMES = new ResourceKey[][]{{biomeRegistry.ASH_BIOME.getKey()}};
    }

    public List<Climate.ParameterPoint> spawnTarget() {
        Climate.Parameter $$0 = Climate.Parameter.point(0.0F);
        float $$1 = 0.16F;
        return List.of(new Climate.ParameterPoint(this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.inlandContinentalness, this.FULL_RANGE), this.FULL_RANGE, $$0, Climate.Parameter.span(-1.0F, -0.16F), 0L), new Climate.ParameterPoint(this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.inlandContinentalness, this.FULL_RANGE), this.FULL_RANGE, $$0, Climate.Parameter.span(0.16F, 1.0F), 0L));
    }

    protected void addBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> p_187176_) {
        if (!SharedConstants.debugGenerateSquareTerrainWithoutNoise) {
            this.addInlandBiomes(p_187176_);
            this.addUndergroundBiomes(p_187176_);
        } else {
            DensityFunctions.Spline.Coordinate $$1 = new DensityFunctions.Spline.Coordinate(BuiltinRegistries.DENSITY_FUNCTION.getHolderOrThrow(NoiseRouterData.CONTINENTS));
            DensityFunctions.Spline.Coordinate $$2 = new DensityFunctions.Spline.Coordinate(BuiltinRegistries.DENSITY_FUNCTION.getHolderOrThrow(NoiseRouterData.EROSION));
            DensityFunctions.Spline.Coordinate $$3 = new DensityFunctions.Spline.Coordinate(BuiltinRegistries.DENSITY_FUNCTION.getHolderOrThrow(NoiseRouterData.RIDGES_FOLDED));
            p_187176_.accept(Pair.of(Climate.parameters(this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.point(0.0F), this.FULL_RANGE, 0.01F), Biomes.PLAINS));
            CubicSpline<?, ?> $$4 = TerrainProvider.buildErosionOffsetSpline($$2, $$3, -0.15F, 0.0F, 0.0F, 0.1F, 0.0F, -0.03F, false, false, ToFloatFunction.IDENTITY);
            float[] var8;
            int var9;
            int var10;
            float $$10;

            CubicSpline<?, ?> $$8 = TerrainProvider.overworldOffset($$1, $$2, $$3, false);
            if ($$8 instanceof CubicSpline.Multipoint) {
                CubicSpline.Multipoint<?, ?> $$9 = (CubicSpline.Multipoint)$$8;
                var8 = $$9.locations();
                var9 = var8.length;

                for(var10 = 0; var10 < var9; ++var10) {
                    $$10 = var8[var10];
                    p_187176_.accept(Pair.of(Climate.parameters(this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.point($$10), this.FULL_RANGE, Climate.Parameter.point(0.0F), this.FULL_RANGE, 0.0F), Biomes.SNOWY_TAIGA));
                }
            }

        }
    }

    private void addInlandBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> p_187216_) {
        this.addMidSlice(p_187216_, Climate.Parameter.span(-1.0F, -0.93333334F));
        this.addHighSlice(p_187216_, Climate.Parameter.span(-0.93333334F, -0.7666667F));
        this.addPeaks(p_187216_, Climate.Parameter.span(-0.7666667F, -0.56666666F));
        this.addHighSlice(p_187216_, Climate.Parameter.span(-0.56666666F, -0.4F));
        this.addMidSlice(p_187216_, Climate.Parameter.span(-0.4F, -0.26666668F));
        this.addMidSlice(p_187216_, Climate.Parameter.span(0.26666668F, 0.4F));
        this.addHighSlice(p_187216_, Climate.Parameter.span(0.4F, 0.56666666F));
        this.addPeaks(p_187216_, Climate.Parameter.span(0.56666666F, 0.7666667F));
        this.addHighSlice(p_187216_, Climate.Parameter.span(0.7666667F, 0.93333334F));
        this.addMidSlice(p_187216_, Climate.Parameter.span(0.93333334F, 1.0F));
    }

    private void addPeaks(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> p_187178_, Climate.Parameter p_187179_) {
        for(int $$2 = 0; $$2 < this.temperatures.length; ++$$2) {
            Climate.Parameter $$3 = this.temperatures[$$2];

            for(int $$4 = 0; $$4 < this.humidities.length; ++$$4) {
                Climate.Parameter $$5 = this.humidities[$$4];
                ResourceKey<Biome> $$9 = this.pickPlateauBiome($$2, $$4, p_187179_);
                this.addSurfaceBiome(p_187178_, $$3, $$5, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[2], p_187179_, 0.0F, $$9);
                this.addSurfaceBiome(p_187178_, $$3, $$5, this.farInlandContinentalness, this.erosions[3], p_187179_, 0.0F, $$9);
            }
        }

    }

    private void addHighSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> p_187198_, Climate.Parameter p_187199_) {
        for(int $$2 = 0; $$2 < this.temperatures.length; ++$$2) {
            Climate.Parameter $$3 = this.temperatures[$$2];

            for(int $$4 = 0; $$4 < this.humidities.length; ++$$4) {
                Climate.Parameter $$5 = this.humidities[$$4];
                ResourceKey<Biome> $$9 = this.pickPlateauBiome($$2, $$4, p_187199_);
                this.addSurfaceBiome(p_187198_, $$3, $$5, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[2], p_187199_, 0.0F, $$9);
                this.addSurfaceBiome(p_187198_, $$3, $$5, this.farInlandContinentalness, this.erosions[3], p_187199_, 0.0F, $$9);
            }
        }

    }

    private void addMidSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> p_187218_, Climate.Parameter p_187219_) {
        this.addSurfaceBiome(p_187218_, this.FULL_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[2]), p_187219_, 0.0F, Biomes.STONY_SHORE);
        this.addSurfaceBiome(p_187218_, Climate.Parameter.span(this.temperatures[1], this.temperatures[2]), this.FULL_RANGE, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], p_187219_, 0.0F, Biomes.SWAMP);
        this.addSurfaceBiome(p_187218_, Climate.Parameter.span(this.temperatures[3], this.temperatures[4]), this.FULL_RANGE, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], p_187219_, 0.0F, Biomes.MANGROVE_SWAMP);

        for(int $$2 = 0; $$2 < this.temperatures.length; ++$$2) {
            Climate.Parameter $$3 = this.temperatures[$$2];

            for(int $$4 = 0; $$4 < this.humidities.length; ++$$4) {
                Climate.Parameter $$5 = this.humidities[$$4];
                ResourceKey<Biome> $$10 = this.pickPlateauBiome($$2, $$4, p_187219_);
                this.addSurfaceBiome(p_187218_, $$3, $$5, this.farInlandContinentalness, this.erosions[2], p_187219_, 0.0F, $$10);
            }
        }

    }

    private void addUndergroundBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> p_187227_) {
        this.addUndergroundBiome(p_187227_, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(0.8F, 1.0F), this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.DRIPSTONE_CAVES);
        this.addUndergroundBiome(p_187227_, this.FULL_RANGE, Climate.Parameter.span(0.7F, 1.0F), this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.LUSH_CAVES);
        this.addBottomBiome(p_187227_, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.erosions[0], this.erosions[1]), this.FULL_RANGE, 0.0F, Biomes.DEEP_DARK);
    }

    private ResourceKey<Biome> pickPlateauBiome(int p_187234_, int p_187235_, Climate.Parameter p_187236_) {
        if (p_187236_.max() < 0L) {
            return this.PLATEAU_BIOMES[p_187234_][p_187235_];
        } else {
            return this.PLATEAU_BIOMES[p_187234_][p_187235_];
        }
    }

    private void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> p_187181_, Climate.Parameter p_187182_, Climate.Parameter p_187183_, Climate.Parameter p_187184_, Climate.Parameter p_187185_, Climate.Parameter p_187186_, float p_187187_, ResourceKey<Biome> p_187188_) {
        p_187181_.accept(Pair.of(Climate.parameters(p_187182_, p_187183_, p_187184_, p_187185_, Climate.Parameter.point(0.0F), p_187186_, p_187187_), p_187188_));
        p_187181_.accept(Pair.of(Climate.parameters(p_187182_, p_187183_, p_187184_, p_187185_, Climate.Parameter.point(1.0F), p_187186_, p_187187_), p_187188_));
    }

    private void addUndergroundBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> p_187201_, Climate.Parameter p_187202_, Climate.Parameter p_187203_, Climate.Parameter p_187204_, Climate.Parameter p_187205_, Climate.Parameter p_187206_, float p_187207_, ResourceKey<Biome> p_187208_) {
        p_187201_.accept(Pair.of(Climate.parameters(p_187202_, p_187203_, p_187204_, p_187205_, Climate.Parameter.span(0.2F, 0.9F), p_187206_, p_187207_), p_187208_));
    }

    private void addBottomBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> p_220669_, Climate.Parameter p_220670_, Climate.Parameter p_220671_, Climate.Parameter p_220672_, Climate.Parameter p_220673_, Climate.Parameter p_220674_, float p_220675_, ResourceKey<Biome> p_220676_) {
        p_220669_.accept(Pair.of(Climate.parameters(p_220670_, p_220671_, p_220672_, p_220673_, Climate.Parameter.point(1.1F), p_220674_, p_220675_), p_220676_));
    }

    public static boolean isDeepDarkRegion(double p_220666_, double p_220667_) {
        return p_220666_ < -0.22499999403953552 && p_220667_ > 0.8999999761581421;
    }

    public static String getDebugStringForPeaksAndValleys(double p_187156_) {
        if (p_187156_ < (double)NoiseRouterData.peaksAndValleys(0.05F)) {
            return "Valley";
        } else if (p_187156_ < (double)NoiseRouterData.peaksAndValleys(0.26666668F)) {
            return "Low";
        } else if (p_187156_ < (double)NoiseRouterData.peaksAndValleys(0.4F)) {
            return "Mid";
        } else {
            return p_187156_ < (double)NoiseRouterData.peaksAndValleys(0.56666666F) ? "High" : "Peak";
        }
    }

    public String getDebugStringForContinentalness(double p_187190_) {
        double $$1 = (double)Climate.quantizeCoord((float)p_187190_);
        if ($$1 < (double)this.mushroomFieldsContinentalness.max()) {
            return "Mushroom fields";
        } else if ($$1 < (double)this.deepOceanContinentalness.max()) {
            return "Deep ocean";
        } else if ($$1 < (double)this.oceanContinentalness.max()) {
            return "Ocean";
        } else if ($$1 < (double)this.coastContinentalness.max()) {
            return "Coast";
        } else if ($$1 < (double)this.nearInlandContinentalness.max()) {
            return "Near inland";
        } else {
            return $$1 < (double)this.midInlandContinentalness.max() ? "Mid inland" : "Far inland";
        }
    }

    public String getDebugStringForErosion(double p_187210_) {
        return getDebugStringForNoiseValue(p_187210_, this.erosions);
    }

    public String getDebugStringForTemperature(double p_187221_) {
        return getDebugStringForNoiseValue(p_187221_, this.temperatures);
    }

    public String getDebugStringForHumidity(double p_187232_) {
        return getDebugStringForNoiseValue(p_187232_, this.humidities);
    }

    private static String getDebugStringForNoiseValue(double p_187158_, Climate.Parameter[] p_187159_) {
        double $$2 = (double)Climate.quantizeCoord((float)p_187158_);

        for(int $$3 = 0; $$3 < p_187159_.length; ++$$3) {
            if ($$2 < (double)p_187159_[$$3].max()) {
                return "" + $$3;
            }
        }

        return "?";
    }

    @VisibleForDebug
    public Climate.Parameter[] getTemperatureThresholds() {
        return this.temperatures;
    }

    @VisibleForDebug
    public Climate.Parameter[] getHumidityThresholds() {
        return this.humidities;
    }

    @VisibleForDebug
    public Climate.Parameter[] getErosionThresholds() {
        return this.erosions;
    }

    @VisibleForDebug
    public Climate.Parameter[] getContinentalnessThresholds() {
        return new Climate.Parameter[]{this.mushroomFieldsContinentalness, this.deepOceanContinentalness, this.oceanContinentalness, this.coastContinentalness, this.nearInlandContinentalness, this.midInlandContinentalness, this.farInlandContinentalness};
    }

    @VisibleForDebug
    public Climate.Parameter[] getPeaksAndValleysThresholds() {
        return new Climate.Parameter[]{Climate.Parameter.span(-2.0F, NoiseRouterData.peaksAndValleys(0.05F)), Climate.Parameter.span(NoiseRouterData.peaksAndValleys(0.05F), NoiseRouterData.peaksAndValleys(0.26666668F)), Climate.Parameter.span(NoiseRouterData.peaksAndValleys(0.26666668F), NoiseRouterData.peaksAndValleys(0.4F)), Climate.Parameter.span(NoiseRouterData.peaksAndValleys(0.4F), NoiseRouterData.peaksAndValleys(0.56666666F)), Climate.Parameter.span(NoiseRouterData.peaksAndValleys(0.56666666F), 2.0F)};
    }

    @VisibleForDebug
    public Climate.Parameter[] getWeirdnessThresholds() {
        return new Climate.Parameter[]{Climate.Parameter.span(-2.0F, 0.0F), Climate.Parameter.span(0.0F, 2.0F)};
    }
}
