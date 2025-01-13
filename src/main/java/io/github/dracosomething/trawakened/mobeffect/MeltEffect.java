package io.github.dracosomething.trawakened.mobeffect;

import com.github.manasmods.tensura.capability.effects.TensuraEffectsCapability;
import com.github.manasmods.tensura.effect.template.TensuraMobEffect;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Objects;

public class MeltEffect extends TensuraMobEffect {
    protected static final String MELT = "cf7fc1f5-4d77-4097-9d70-1021e3716721";

    public MeltEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int pAmplifier) {
        entity.addEffect(new MobEffectInstance(effectRegistry.HEALPOISON.get(), 100, 1, true, true, true));
        entity.setHealth(entity.getMaxHealth());
        Objects.requireNonNull(entity.getAttributes().getInstance(Attributes.MAX_HEALTH)).setBaseValue(entity.getAttributeBaseValue(Attributes.MAX_HEALTH)-((double) pAmplifier /2));
    }

    public boolean isDurationEffectTick(int pDuration, int amplifier) {
        return pDuration % 50 == 0;
    }
}
