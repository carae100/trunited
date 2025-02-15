package io.github.dracosomething.trawakened.mobeffect;

import com.github.manasmods.tensura.effect.template.DamageAction;
import com.github.manasmods.tensura.effect.template.TensuraMobEffect;
import io.github.dracosomething.trawakened.helper.skillHelper;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TimeStopCoreEffect extends TensuraMobEffect implements DamageAction {
    public TimeStopCoreEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity p_19467_, int p_19468_) {
        List<Entity> list = skillHelper.DrawCircle(p_19467_, 160, false);
        for (Entity entity1 : list){
            if(entity1 instanceof LivingEntity living && living != p_19467_) {
                living.addEffect(new MobEffectInstance(effectRegistry.TIMESTOP.get(), 1, 1, false, false, false));
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int amplifier) {
        return pDuration % 20 == 0;
    }
}
