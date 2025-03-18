package io.github.dracosomething.trawakened.ability.skill.extra.System;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class Mutilation extends SystemExtra {
    public Mutilation() {
        super("mutilation");
    }

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        instance.getOrCreateTag().putBoolean("isActive", true);
        instance.setCoolDown(15);
    }

    @Override
    public void onDamageEntity(ManasSkillInstance instance, LivingEntity entity, LivingHurtEvent event) {
        if (instance.getOrCreateTag().getBoolean("isActive")) {
            event.setAmount(event.getAmount()*3);
            instance.getOrCreateTag().putBoolean("isActive", false);
        }
    }
}
