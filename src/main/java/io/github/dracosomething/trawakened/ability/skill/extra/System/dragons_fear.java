package io.github.dracosomething.trawakened.ability.skill.extra.System;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import io.github.dracosomething.trawakened.helper.skillHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class dragons_fear extends SystemExtra {
    public dragons_fear() {
        super("dragons_fear");
    }

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        List<Entity> list = skillHelper.DrawCircle(entity, 150, true).stream().filter(living -> !living.equals(entity)).toList();
        for (Entity entity1 : list) {
            if (entity1 instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(TensuraMobEffects.INSANITY.get(), 2000, 10));
                living.addEffect(new MobEffectInstance(TensuraMobEffects.FEAR.get(), 2000, 10));
            }
        }
        instance.setCoolDown(420);
    }
}
