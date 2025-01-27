package io.github.dracosomething.trawakened.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BackdoorConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_BACKDOOR;

    static {
        BUILDER.push("client back door");

        ENABLE_BACKDOOR = BUILDER.define("enable back door", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
