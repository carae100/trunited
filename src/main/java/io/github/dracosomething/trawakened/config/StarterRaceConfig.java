package io.github.dracosomething.trawakened.config;

import io.github.dracosomething.trawakened.trawakened;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

public class StarterRaceConfig {
    public static final StarterRaceConfig INSTANCE;
    public static final ForgeConfigSpec SPEC;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> startingRaces;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> startingSkills;

    public StarterRaceConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Modded starter races");
        startingRaces = builder.comment("List of default starter races")
                .comment("default: \"trawakened:honkai_apostle\"")
                .defineList("Starter Races", Arrays.asList("trawakened:honkai_apostle"), (check) -> true);
        builder.pop();
        builder.push("Modded starter skills");
        startingSkills = builder.comment("List of default starter skills")
                .comment("default: \"trawakened:alternate\", \"trawakened:starkill\", \"trawakened:system\", \"trawakened:akashic_plane\"")
                .defineList("Starter Skills", Arrays.asList("trawakened:alternate", "trawakened:starkill", "trawakened:system", "trawakened:akashic_plane"), (check) -> true);
    }

    static {
        Pair<StarterRaceConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder()
                .configure(StarterRaceConfig::new);
        INSTANCE = pair.getKey();
        SPEC = pair.getValue();
    }
}
