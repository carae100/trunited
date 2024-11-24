package io.github.dracosomething.trunited.ability.skill.unique;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import net.minecraft.world.entity.LivingEntity;

public class voiceofhonkai extends Skill {
    public voiceofhonkai() {
        super(SkillType.UNIQUE);
    }

    public double getObtainingEpCost(){return 40000.0;}

    public double learningCost(){return 5000.0;}

    public boolean canBeToggled(ManasSkillInstance instance, LivingEntity entity) {
            return TensuraEPCapability.isMajin(entity) ? true : TensuraPlayerCapability.isTrueHero(entity);
    }
}
