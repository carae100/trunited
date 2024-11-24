package io.github.dracosomething.trawakened.race;

import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.TensuraSkill;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.registry.race.TensuraRaces;
import com.github.manasmods.tensura.registry.skill.ResistanceSkills;
import com.github.manasmods.tensura.util.JumpPowerHelper;
import com.mojang.datafixers.util.Pair;
import io.github.dracosomething.trawakened.capability.trawakenedPlayerCapability;
import io.github.dracosomething.trawakened.registry.raceregistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

public class enslavedapostle extends honkaiapostle {
    public enslavedapostle() {}

    @Override
    public double getBaseHealth() {
        return 40;
    }

    @Override
    public float getPlayerSize() {
        return 2.0F;
    }

    @Override
    public double getBaseAttackDamage() {
        return 5;
    }

    @Override
    public double getBaseAttackSpeed() {
        return 2;
    }

    @Override
    public double getKnockbackResistance() {
        return 5;
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
        return Pair.of(1000.0, 5000.0);
    }

    @Override
    public Pair<Double, Double> getBaseMagiculeRange() {
        return Pair.of(950.0, 1950.0);
    }

    public boolean isMajin() {
        return true;
    }

    public @Override Race getDefaultEvolution(){return (Race) TensuraRaces.HUMAN.get();}

    public @Override Race getAwakeningEvolution(){return (Race) TensuraRaces.HUMAN.get();}

    public @Override Race getHarvestFestivalEvolution(){return (Race) TensuraRaces.HUMAN.get();}

    public List<TensuraSkill> getIntrinsicSkills() {
        List<TensuraSkill> list = new ArrayList();
        list.add((TensuraSkill) SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:voiceofhonkai")));
        list.add((TensuraSkill) ResistanceSkills.PHYSICAL_ATTACK_NULLIFICATION.get());
        return list;
    }

//    public List<Race> getNextEvolutions() {
//        List<Race> list = new ArrayList();
//        list.add((Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceregistry.AWAKENED_APOSTLE));
//        return list;
//    }

    @Override
    public List<Race> getPreviousEvolutions() {
        List<Race> list = new ArrayList();
        list.add((Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceregistry.HONKAI_APOSTLE));
        return list;
    }

    @Override
    public double getEvolutionPercentage(Player player) {
        int chance = 0;
        if (trawakenedPlayerCapability.isDemonLordSeed(player)){
            chance += 100;
        }

        return chance;
    }
}
