package io.github.dracosomething.trunited.race;

import com.github.manasmods.tensura.ability.TensuraSkill;
import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.registry.race.TensuraRaces;
import com.github.manasmods.tensura.registry.skill.ResistanceSkills;
import com.github.manasmods.tensura.registry.skill.UniqueSkills;
import com.github.manasmods.tensura.util.JumpPowerHelper;
import com.mojang.datafixers.util.Pair;
import io.github.dracosomething.trunited.registry.skillregistry;

import java.util.ArrayList;
import java.util.List;

public class honkaiapostle extends Race {
    public honkaiapostle() {
        super(Difficulty.EASY);
    }

    @Override
    public double getBaseHealth() {
        return 5;
    }

    @Override
    public float getPlayerSize() {
        return 2;
    }

    @Override
    public double getBaseAttackDamage() {
        return 1;
    }

    @Override
    public double getBaseAttackSpeed() {
        return 2;
    }

    @Override
    public double getKnockbackResistance() {
        return 3;
    }

    @Override
    public double getJumpHeight() {
        return JumpPowerHelper.defaultPlayer();
    }

    @Override
    public double getMovementSpeed() {
        return 0.3;
    }

    @Override
    public Pair<Double, Double> getBaseAuraRange() {
        return Pair.of(100.0, 1000.0);
    }

    @Override
    public Pair<Double, Double> getBaseMagiculeRange() {
        return Pair.of(500.0, 950.0);
    }

    public boolean isMajin() {
        return true;
    }

    public @Override Race getDefaultEvolution(){return (Race) TensuraRaces.HUMAN.get();}

    public @Override Race getAwakeningEvolution(){return (Race) TensuraRaces.HUMAN.get();}

    public @Override Race getHarvestFestivalEvolution(){return (Race) TensuraRaces.HUMAN.get();}

    public List<TensuraSkill> getIntrinsicSkills() {
        List<TensuraSkill> list = new ArrayList();
        list.add((TensuraSkill) skillregistry.get.getValue());
        list.add((TensuraSkill) ResistanceSkills.COLD_RESISTANCE.get());
        return list;
    }

    public List<Race> getNextEvolutions() {
        List<Race> list = new ArrayList();
        list.add((Race) TensuraRaces.HUMAN.get());
        return list;
    }
}
