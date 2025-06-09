package io.github.dracosomething.trawakened.world;

import net.minecraft.world.level.GameRules;

public class trawakenedGamerules {
    public static GameRules.Key<GameRules.BooleanValue> NORMAL_FEAR;
    public static GameRules.Key<GameRules.BooleanValue> DESTRUCTION_GRIEFING;

    public static void registryGameRules() {
        NORMAL_FEAR = GameRules.register("fears", GameRules.Category.MISC, GameRules.BooleanValue.create(false));
        DESTRUCTION_GRIEFING = GameRules.register("destruction_griefing", GameRules.Category.MISC, GameRules.BooleanValue.create(true));
    }
}
