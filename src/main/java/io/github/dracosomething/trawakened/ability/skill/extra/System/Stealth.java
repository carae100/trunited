package io.github.dracosomething.trawakened.ability.skill.extra.System;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class Stealth extends SystemExtra {
    public Stealth() {
        super("stealth");
    }

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(TensuraMobEffects.PRESENCE_CONCEALMENT.get(), 600, 2));
        instance.setCoolDown(180);
    }
}
