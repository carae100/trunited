package io.github.dracosomething.trawakened.mobeffect;

import com.github.manasmods.tensura.effect.template.TensuraMobEffect;
import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class DestroyedBrainEffect extends TensuraMobEffect {
    public DestroyedBrainEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int p_19468_) {
        entity.addEffect(new MobEffectInstance(effectRegistry.SHPPOISON.get(), 100, 1, true, true, true));
        DamageSourceHelper.directSpiritualHurt(entity, (Entity)null, TensuraDamageSources.INSANITY, (float) (p_19468_*15));
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int amplifier) {
        return pDuration % 20 == 0;
    }
}
