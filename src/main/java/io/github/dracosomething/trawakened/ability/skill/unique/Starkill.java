package io.github.dracosomething.trawakened.ability.skill.unique;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import net.minecraft.world.entity.LivingEntity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Starkill extends Skill {
    public Starkill() {
        super(SkillType.UNIQUE);
    }

    @Override
    public int modes() {
        return 3;
    }

    @Override
    public double getObtainingEpCost() {
        return 10000;
    }

    @Override
    public double learningCost() {
        return 1000;
    }

    @Override
    public int nextMode(LivingEntity entity, TensuraSkillInstance instance, boolean reverse) {
        if (reverse) {
            return instance.getMode() == 1 ? 3 : instance.getMode() - 1;
        } else {
            return instance.getMode() == 3 ? 1 : instance.getMode() + 1;
        }
    }

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {

    }
}
