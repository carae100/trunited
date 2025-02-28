package io.github.dracosomething.trawakened.mobeffect;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.effect.template.TensuraMobEffect;
import com.github.manasmods.tensura.effect.template.Transformation;
import io.github.dracosomething.trawakened.ability.skill.unique.Alternate;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

import java.util.Random;

public class IntruderModeEffect extends TensuraMobEffect implements Transformation {
    private Alternate.AlternateType save;

    public IntruderModeEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int value) {
        super.addAttributeModifiers(entity, attributeMap, value);
        ManasSkillInstance instance = SkillUtils.getSkillOrNull(entity, skillRegistry.ALTERNATE.get());
        System.out.println(instance);
        if (instance != null) {
            CompoundTag tag = instance.getOrCreateTag();
            Alternate.AlternateType type = Alternate.AlternateType.fromNBT(tag.getCompound("alternate_type"));
            System.out.println(type);
            if (type != null) {
                tag.put("alternate_type", Alternate.AlternateType.INTRUDER.toNBT());
                save = type;
            }
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int value) {
        super.removeAttributeModifiers(entity, attributeMap, value);
        ManasSkillInstance instance = SkillUtils.getSkillOrNull(entity, skillRegistry.ALTERNATE.get());
        if (instance != null) {
            CompoundTag tag = instance.getOrCreateTag();
            Alternate.AlternateType type = Alternate.AlternateType.fromNBT(tag.getCompound("alternate_type"));
            if (type != null) {
                tag.put("alternate_type", save.toNBT());
            }
        }
    }
}
