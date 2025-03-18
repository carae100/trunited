package io.github.dracosomething.trawakened.ability.skill.extra.System;

import com.github.manasmods.tensura.ability.skill.Skill;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class SystemExtra extends Skill {
    private String name;

    @Override
    public MutableComponent getName() {
        return Component.translatable("trawakened.skill.system.extra." + name);
    }

    public SystemExtra(String name) {
        super(SkillType.EXTRA);
        this.name = name;
    }

    @Override
    public int getMaxMastery() {
        return 0;
    }
}
