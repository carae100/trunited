package io.github.dracosomething.trawakened.mobeffect;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.effect.template.TensuraMobEffect;
import com.github.manasmods.tensura.effect.template.Transformation;
import io.github.dracosomething.trawakened.library.AlternateType;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

import java.util.Random;

public class AlternateModeEffect extends TensuraMobEffect implements Transformation {
    public AlternateModeEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    private AlternateType.Assimilation assimilationSave;

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int value) {
        super.addAttributeModifiers(entity, attributeMap, value);
        ManasSkillInstance instance = SkillUtils.getSkillOrNull(entity, skillRegistry.ALTERNATE.get());
        System.out.println(instance);
        if (instance != null) {
            CompoundTag tag = instance.getOrCreateTag();
            AlternateType.Assimilation assimilation = AlternateType.Assimilation.fromNBT(tag.getCompound("assimilation"));
            System.out.println(assimilation);
            if (assimilation != null) {
                Random random = new Random();
                AlternateType.Assimilation newAssimailation = random.nextInt(1, 3) == 1? AlternateType.Assimilation.FLAWED : AlternateType.Assimilation.OVERDRIVEN;
                System.out.println(newAssimailation.getName());
                tag.put("assimilation", newAssimailation.toNBT());
                assimilationSave = assimilation;
            }
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int value) {
        super.removeAttributeModifiers(entity, attributeMap, value);
        ManasSkillInstance instance = SkillUtils.getSkillOrNull(entity, skillRegistry.ALTERNATE.get());
        if (instance != null) {
            CompoundTag tag = instance.getOrCreateTag();
            AlternateType.Assimilation assimilation = AlternateType.Assimilation.fromNBT(tag.getCompound("assimilation"));
            if (assimilation != null) {
                tag.put("assimilation", AlternateType.Assimilation.COMPLETE.toNBT());
            }
        }
    }

    public AlternateType.Assimilation getAssimilationSave() {
        return assimilationSave;
    }

    public void setAssimilationSave(AlternateType.Assimilation assimilationSave) {
        this.assimilationSave = assimilationSave;
    }
}
