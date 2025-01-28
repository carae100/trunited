package io.github.dracosomething.trawakened.mobeffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class emptyEffect extends MobEffect {
    public emptyEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int amplifier) {
        return pDuration % 20 == 0;
    }
}
