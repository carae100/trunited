package io.github.dracosomething.trawakened.race;

import com.github.manasmods.tensura.ability.TensuraSkill;
import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.registry.race.TensuraRaces;
import com.github.manasmods.tensura.util.JumpPowerHelper;
import com.mojang.datafixers.util.Pair;
import io.github.dracosomething.trawakened.capability.trawakenedPlayerCapability;
import io.github.dracosomething.trawakened.registry.raceRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

public class HerrscherSeedAwakened extends awakenedapostle {
    public HerrscherSeedAwakened() {}

    @Override
    public double getBaseHealth() {
        return 50;
    }

    @Override
    public double getBaseAttackDamage() {
        return 7;
    }

    @Override
    public double getBaseAttackSpeed() {
        return 3;
    }

    @Override
    public double getKnockbackResistance() {
        return 6;
    }

    @Override
    public double getMovementSpeed() {
        return 0.22;
    }

    public double getJumpHeight() {
        return JumpPowerHelper.defaultPlayer()+0.15;
    }

    @Override
    public Pair<Double, Double> getBaseAuraRange() {
        return Pair.of(1500.0, 2500.0);
    }

    @Override
    public Pair<Double, Double> getBaseMagiculeRange() {
        return Pair.of(10000.0, 25000.0);
    }

    public @Override Race getDefaultEvolution(Player player){return (Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceRegistry.HERRSCHER_OF_DESTRUCTION);}

    public @Override Race getAwakeningEvolution(Player player){return (Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceRegistry.HERRSCHER_OF_DESTRUCTION);}

    public @Override Race getHarvestFestivalEvolution(Player player){return (Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceRegistry.HERRSCHER_OF_DESTRUCTION);}

    public List<TensuraSkill> getIntrinsicSkills(Player player) {
        List<TensuraSkill> list = new ArrayList();
        return list;
    }

    public List<Race> getNextEvolutions(Player player) {
        List<Race> list = new ArrayList();
        list.add((Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceRegistry.HERRSCHER_OF_DESTRUCTION));
        list.add((Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceRegistry.HERRSCHER_OF_THE_WORLD));
        return list;
    }

    @Override
    public List<Race> getPreviousEvolutions(Player player) {
        List<Race> list = new ArrayList();
        list.add((Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceRegistry.AWAKENED_APOSTLE));
        return list;
    }

    @Override
    public double getEvolutionPercentage(Player player) {
        int chance = 0;
        if (trawakenedPlayerCapability.isHeroEgg(player)){
            chance += 100;
        }

        return (double) chance;
    }

    public List<Component> getRequirementsForRendering(Player player) {
        List<Component> list = new ArrayList();
        list.add(Component.translatable("trawakened.requirement.evolution.herrscher_seed_awakened"));
        return list;
    }
}
