package io.github.dracosomething.trawakened.race;

import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.TensuraSkill;
import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.registry.race.TensuraRaces;
import com.github.manasmods.tensura.registry.skill.ResistanceSkills;
import com.github.manasmods.tensura.util.JumpPowerHelper;
import com.mojang.datafixers.util.Pair;
import io.github.dracosomething.trawakened.registry.raceregistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

public class honkaiapostle extends Race {
    public honkaiapostle() {
        super(Difficulty.EXTREME);
    }

    @Override
    public double getBaseHealth() {
        return 5;
    }

    @Override
    public float getPlayerSize() {
        return 2.0F;
    }

    @Override
    public double getBaseAttackDamage() {
        return 1;
    }

    @Override
    public double getBaseAttackSpeed() {
        return 4;
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
        return 0.1;
    }

    @Override
    public Pair<Double, Double> getBaseAuraRange() {
        return Pair.of(500.0, 950.0);
    }

    @Override
    public Pair<Double, Double> getBaseMagiculeRange() {
        return Pair.of(500.0, 1000.0);
    }

    public boolean isMajin() {
        return true;
    }

    public @Override Race getDefaultEvolution(){return (Race) TensuraRaces.RACE_REGISTRY.get().getValue(raceregistry.AWAKENED_APOSTLE);}

    public @Override Race getAwakeningEvolution(){return (Race) TensuraRaces.RACE_REGISTRY.get().getValue(raceregistry.AWAKENED_APOSTLE);}

    public @Override Race getHarvestFestivalEvolution(){return (Race) TensuraRaces.RACE_REGISTRY.get().getValue(raceregistry.ENSLAVED_APOSTLE);}

    public List<TensuraSkill> getIntrinsicSkills() {
        List<TensuraSkill> list = new ArrayList();
        list.add((TensuraSkill) SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:voiceofhonkai")));
        list.add((TensuraSkill) ResistanceSkills.PHYSICAL_ATTACK_RESISTANCE.get());
        list.add((TensuraSkill) ResistanceSkills.PIERCE_RESISTANCE.get());
        return list;
    }

    public List<Race> getNextEvolutions() {
        List<Race> list = new ArrayList();
//        list.add((Race) TensuraRaces.HUMAN.get());
        list.add((Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceregistry.AWAKENED_APOSTLE));
        list.add((Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceregistry.ENSLAVED_APOSTLE));
        return list;
    }
}
