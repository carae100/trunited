package io.github.dracosomething.trawakened.mobeffect;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.effect.template.TensuraMobEffect;
import com.github.manasmods.tensura.effect.template.Transformation;
import io.github.dracosomething.trawakened.ability.skill.unique.Alternate;
import io.github.dracosomething.trawakened.registry.skillregistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class AlternateModeEffect extends TensuraMobEffect implements Transformation {
    public AlternateModeEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int value) {
        super.addAttributeModifiers(entity, attributeMap, value);
        ManasSkillInstance instance = SkillUtils.getSkillOrNull(entity, skillregistry.ALTERNATE.get());
        System.out.println(instance);
        if (instance != null) {
            CompoundTag tag = instance.getOrCreateTag();
            Alternate.Assimilation assimilation = Alternate.Assimilation.fromNBT(tag.getCompound("assimilation"));
            System.out.println(assimilation);
            if (assimilation != null) {
                Alternate.Assimilation newAssimailation = Alternate.Assimilation.getRandomAssimilation();
                while (newAssimailation == Alternate.Assimilation.COMPLETE) {
                    newAssimailation = Alternate.Assimilation.getRandomAssimilation();
                }
                System.out.println(newAssimailation.getName());
                tag.put("assimilation", newAssimailation.toNBT());
            }
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int value) {
        super.removeAttributeModifiers(entity, attributeMap, value);
        ManasSkillInstance instance = SkillUtils.getSkillOrNull(entity, skillregistry.ALTERNATE.get());
        if (instance != null) {
            CompoundTag tag = instance.getOrCreateTag();
            Alternate.Assimilation assimilation = Alternate.Assimilation.fromNBT(tag.getCompound("assimilation"));
            if (assimilation != null) {
                tag.put("assimilation", Alternate.Assimilation.COMPLETE.toNBT());
            }
        }
    }
}
