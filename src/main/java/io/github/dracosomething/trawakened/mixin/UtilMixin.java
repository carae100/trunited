package io.github.dracosomething.trawakened.mixin;

import io.github.dracosomething.trawakened.ability.skill.ultimate.herrscheroftime;
import net.minecraft.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = {Util.class}, priority = 2147483647)
public class UtilMixin {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public static long getMillis(){
        return herrscheroftime.time;
    }
}
