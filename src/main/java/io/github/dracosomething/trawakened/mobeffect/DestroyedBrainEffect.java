package io.github.dracosomething.trawakened.mobeffect;

import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.effect.template.TensuraMobEffect;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.dracosomething.trawakened.capability.trawakenedPlayerCapability;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;

import java.util.Objects;

public class DestroyedBrainEffect extends TensuraMobEffect {
    public DestroyedBrainEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "3959affe-d3f1-4bf7-a298-3f6609baa94d", -5, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.ARMOR, "3959affe-d3f1-4bf7-a298-3f6609baa94d", -2, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "3959affe-d3f1-4bf7-a298-3f6609baa94d", -0.02, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, "3959affe-d3f1-4bf7-a298-3f6609baa94d", -5, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier((Attribute) ForgeMod.ATTACK_RANGE.get(), "3959affe-d3f1-4bf7-a298-3f6609baa94d", -2, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier((Attribute) ForgeMod.REACH_DISTANCE.get(), "3959affe-d3f1-4bf7-a298-3f6609baa94d", -2, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap p_19479_, int p_19480_) {
        super.addAttributeModifiers(entity, p_19479_, p_19480_);
        Objects.requireNonNull(entity.getAttributes().getInstance(TensuraAttributeRegistry.MAX_SPIRITUAL_HEALTH.get())).setBaseValue(entity.getAttributeBaseValue(TensuraAttributeRegistry.MAX_SPIRITUAL_HEALTH.get())- 50);
        if(TensuraEPCapability.getSpiritualHealth(entity) > trawakenedPlayerCapability.getMaxSpiritualHealth(entity)){
            TensuraEPCapability.setSpiritualHealth(entity, trawakenedPlayerCapability.getMaxSpiritualHealth(entity));
        }
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
