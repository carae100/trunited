package io.github.dracosomething.trawakened;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class StarterRaceConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> STARTER_RACES;

    static {
        BUILDER.push("Modded starter races");

        STARTER_RACES = BUILDER
                .comment("List of default starter races")
                .comment("defoult: \"trawakened:honkai_apostle\"")
                .define("Starter Races", Arrays.asList("trawakened:honkai_apostle"));

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
