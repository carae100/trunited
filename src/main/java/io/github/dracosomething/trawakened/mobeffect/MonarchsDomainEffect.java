package io.github.dracosomething.trawakened.mobeffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.ForgeRegistries;

public class MonarchsDomainEffect extends MobEffect {
    public MonarchsDomainEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, "99c6aeb0-c1b8-4387-9a43-d3650e41dead", 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(Attributes.ATTACK_DAMAGE, "99c6aeb0-c1b8-4387-9a43-d3650e41dead", 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(Attributes.ATTACK_SPEED, "99c6aeb0-c1b8-4387-9a43-d3650e41dead", 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(Attributes.MAX_HEALTH, "99c6aeb0-c1b8-4387-9a43-d3650e41dead", 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
