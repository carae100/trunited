package io.github.dracosomething.trawakened.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ScarletModeConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SCARLET_MODE;

    static {
        BUILDER.push("Scarlet Mode");

        SCARLET_MODE = BUILDER.define("Scarlet Mode", false);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
