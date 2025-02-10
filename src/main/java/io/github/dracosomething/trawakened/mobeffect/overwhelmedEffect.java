package io.github.dracosomething.trawakened.mobeffect;

import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.world.trawakenedDamage;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class overwhelmedEffect extends MobEffect {
    public overwhelmedEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        p_19467_.addEffect(new MobEffectInstance(effectRegistry.SHPPOISON.get(), 1000, 1, true,true,true));
    }

    @Override
    public void removeAttributeModifiers(LivingEntity p_19469_, AttributeMap p_19470_, int p_19471_) {
        p_19469_.hurt(trawakenedDamage.INSANITY, 100);
    }

    public boolean isDurationEffectTick(int pDuration, int amplifier) {
        return pDuration % 20 == 0;
    }
}
