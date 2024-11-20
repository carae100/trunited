package io.github.dracosomething.trunited.race;

import com.github.manasmods.tensura.ability.TensuraSkill;
import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.registry.race.TensuraRaces;
import com.github.manasmods.tensura.registry.skill.ResistanceSkills;
import com.github.manasmods.tensura.registry.skill.UniqueSkills;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;

import java.util.ArrayList;
import java.util.List;

public class testrace extends Race {
    public testrace() {
        super(Difficulty.EASY);
    }

    @Override
    public double getBaseHealth() {
        return 1;
    }

    @Override
    public float getPlayerSize() {
        return 1;
    }

    @Override
    public double getBaseAttackDamage() {
        return 1;
    }

    @Override
    public double getBaseAttackSpeed() {
        return 1;
    }

    @Override
    public double getKnockbackResistance() {
        return 1;
    }

    @Override
    public double getJumpHeight() {
        return 1;
    }

    @Override
    public double getMovementSpeed() {
        return 1;
    }

    @Override
    public Pair<Double, Double> getBaseAuraRange() {
        return Pair.of(1.0, 2.0);
    }

    @Override
    public Pair<Double, Double> getBaseMagiculeRange() {
        return Pair.of(1.0, 2.0);
    }

    public boolean isMajin() {
        return true;
    }

    public List<TensuraSkill> getIntrinsicSkills() {
        List<TensuraSkill> list = new ArrayList();
        list.add((TensuraSkill) UniqueSkills.GREAT_SAGE.get());
        list.add((TensuraSkill) ResistanceSkills.COLD_RESISTANCE.get());
        return list;
    }

    public List<Race> getNextEvolutions() {
        List<Race> list = new ArrayList();
        list.add((Race) TensuraRaces.WIGHT_KING.get());
        list.add((Race) TensuraRaces.HUMAN.get());
        return list;
    }
}
