package io.github.dracosomething.trawakened.race;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.TensuraSkill;
import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.registry.race.TensuraRaces;
import com.github.manasmods.tensura.registry.skill.ResistanceSkills;
import com.github.manasmods.tensura.util.JumpPowerHelper;
import com.mojang.datafixers.util.Pair;
import io.github.dracosomething.trawakened.capability.trawakenedPlayerCapability;
import io.github.dracosomething.trawakened.registry.raceRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class enslavedapostle extends honkaiapostle {
    public enslavedapostle() {}

    @Override
    public double getBaseHealth() {
        return 100;
    }

    @Override
    public float getPlayerSize() {
        return 2.0F;
    }

    @Override
    public double getBaseAttackDamage() {
        return 2;
    }

    @Override
    public double getBaseAttackSpeed() {
        return 5;
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
        return 0.22;
    }

    @Override
    public Pair<Double, Double> getBaseAuraRange() {
        return Pair.of(1000.0, 5000.0);
    }

    @Override
    public Pair<Double, Double> getBaseMagiculeRange() {
        return Pair.of(1500.0, 6000.0);
    }

    public boolean isMajin() {
        return true;
    }

    public Race getDefaultEvolution(Player player){return (Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceRegistry.HERRSCHER_SEED_ENSLAVED);}

    public Race getAwakeningEvolution(Player player){return (Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceRegistry.HERRSCHER_SEED_ENSLAVED);}

    public Race getHarvestFestivalEvolution(Player player){return (Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceRegistry.HERRSCHER_SEED_ENSLAVED);}

    public List<TensuraSkill> getIntrinsicSkills(Player player) {
        List<TensuraSkill> list = new ArrayList();
        list.add((TensuraSkill) SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:willofhonkai")));
        list.add((TensuraSkill) ResistanceSkills.PHYSICAL_ATTACK_RESISTANCE.get());
        return list;
    }

    public List<Race> getNextEvolutions(Player player) {
        List<Race> list = new ArrayList();
        list.add((Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceRegistry.HERRSCHER_SEED_ENSLAVED));
        return list;
    }

    public List<Race> getPreviousEvolutions(Player player) {
        List<Race> list = new ArrayList();
        list.add((Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceRegistry.HONKAI_APOSTLE));
        return list;
    }

    @Override
    public double getEvolutionPercentage(Player player) {
        int chance = 0;
        int chance2 = 0;
        ManasSkillInstance instance = SkillUtils.getSkillOrNull(player, Objects.requireNonNull(SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:voiceofhonkai"))));
        if (trawakenedPlayerCapability.isDemonLordSeed(player)){
            chance += 50;
        }
        if (SkillUtils.hasSkill(player, Objects.requireNonNull(SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:voiceofhonkai")))) && instance.getMastery() >= instance.getMaxMastery()/2){
            chance2 += 50;
        }

        return (double) chance+chance2;
    }

    public List<Component> getRequirementsForRendering(Player player) {
        List<Component> list = new ArrayList();
        list.add(Component.translatable("trawakened.requirement.evolution.enslaved_apostle"));
        list.add(Component.translatable("trawakened.requirement.evolution.half_mastery"));
        return list;
    }
}
