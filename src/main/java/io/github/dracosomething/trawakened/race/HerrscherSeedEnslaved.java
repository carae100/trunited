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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

public class HerrscherSeedEnslaved extends enslavedapostle {
    public HerrscherSeedEnslaved() {}

    @Override
    public double getBaseHealth() {
        return 100;
    }

    @Override
    public double getBaseAttackDamage() {
        return 10;
    }

    @Override
    public double getBaseAttackSpeed() {
        return 1;
    }

    @Override
    public double getKnockbackResistance() {
        return 10;
    }

    @Override
    public double getJumpHeight() {
        return JumpPowerHelper.defaultPlayer()+0.15;
    }

    @Override
    public double getMovementSpeed() {
        return 0.4;
    }

    @Override
    public Pair<Double, Double> getBaseAuraRange() {
        return Pair.of(5000.0, 10000.0);
    }

    @Override
    public Pair<Double, Double> getBaseMagiculeRange() {
        return Pair.of(12000.0, 30000.0);
    }

//    public @Override Race getDefaultEvolution(){return (Race) TensuraRaces.HUMAN.get();}
//
//    public @Override Race getAwakeningEvolution(){return (Race) TensuraRaces.HUMAN.get();}
//
//    public @Override Race getHarvestFestivalEvolution(){return (Race) TensuraRaces.HUMAN.get();}

    public List<TensuraSkill> getIntrinsicSkills(Player player) {
        List<TensuraSkill> list = new ArrayList();
        return list;
    }

    public List<Race> getNextEvolutions(Player player) {
        List<Race> list = new ArrayList();
        return list;
    }

    @Override
    public List<Race> getPreviousEvolutions(Player player) {
        List<Race> list = new ArrayList();
        list.add((Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceregistry.ENSLAVED_APOSTLE));
        return list;
    }

    @Override
    public double getEvolutionPercentage(Player player) {
        int chance = 0;
        if (TensuraPlayerCapability.isTrueDemonLord(player)){
            chance += 50;
        }
        else if (TensuraPlayerCapability.getBaseEP(player) >= 150000){
            chance += (int) Math.floor(TensuraPlayerCapability.getBaseEP(player) / 300000 * 100);
        }

        return chance;
    }

    public List<Component> getRequirementsForRendering(Player player) {
        List<Component> list = new ArrayList();
        list.add(Component.translatable("trawakened.requirement.evolution.herrscher_seed_enslaved"));
        return list;
    }
}
