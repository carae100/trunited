package io.github.dracosomething.trawakened.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class StarterRaceConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> STARTER_RACES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> STARTER_SKILLS;

    static {
        BUILDER.push("Modded starter races");

        STARTER_RACES = BUILDER
                .comment("List of default starter races")
                .comment("default: \"trawakened:honkai_apostle\"")
                .define("Starter Races", Arrays.asList("trawakened:honkai_apostle"));

        BUILDER.pop();

        BUILDER.push("Modded starter skills");

        STARTER_SKILLS = BUILDER
                .comment("List of default starter skills")
                .comment("default: \"trawakened:alternate\", \"trawakened:starkill\", \"trawakened:system\", \"trawakened:akashic_plane\"")
                .define("Starter Skills", Arrays.asList("trawakened:alternate", "trawakened:starkill", "trawakened:system", "trawakened:akashic_plane"));

        SPEC = BUILDER.build();
    }
}
