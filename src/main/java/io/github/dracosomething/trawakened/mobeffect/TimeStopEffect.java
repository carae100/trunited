package io.github.dracosomething.trawakened.mobeffect;

import com.github.manasmods.tensura.effect.template.TensuraMobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class TimeStopEffect extends TensuraMobEffect {
    public TimeStopEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int amplifier) {
        return pDuration % 20 == 0;
    }
}
