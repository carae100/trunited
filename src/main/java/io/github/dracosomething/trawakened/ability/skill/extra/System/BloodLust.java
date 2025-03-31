package io.github.dracosomething.trawakened.ability.skill.extra.System;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import io.github.dracosomething.trawakened.helper.skillHelper;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class BloodLust extends SystemExtra {
    public BloodLust() {
        super("bloodlust");
    }

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        List<Entity> list = skillHelper.DrawSphereAndGetEntitiesInIt(entity, 50, false).stream().filter((entity1 -> {
            return entity1 instanceof LivingEntity living && TensuraEPCapability.getCurrentEP(living) <= TensuraEPCapability.getCurrentEP(entity) * 0.75;
        })).toList();
        for (Entity entity1 : list) {
            if (entity1 instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(effectRegistry.BLOODLUST_DEBUFF.get(), 20000*20, 1, false, false, false));
                living.addEffect(new MobEffectInstance(TensuraMobEffects.BURDEN.get(), 20000*20, 0, false, false, false));
            }
        }
        instance.setCoolDown(60);
    }
}
