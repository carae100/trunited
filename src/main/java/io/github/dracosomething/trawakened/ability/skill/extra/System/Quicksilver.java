package io.github.dracosomething.trawakened.ability.skill.extra.System;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class Quicksilver extends SystemExtra {
    public Quicksilver() {
        super("quicksilver");
    }

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        AttributeModifier modifier = new AttributeModifier("f444bd0f-d420-4c07-ba1a-f23118508a6f", 2, AttributeModifier.Operation.MULTIPLY_TOTAL);
        entity.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addPermanentModifier(modifier);
        instance.setCoolDown(120);
    }
}
