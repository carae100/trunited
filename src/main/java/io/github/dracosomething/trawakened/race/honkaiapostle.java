package io.github.dracosomething.trawakened.race;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.TensuraSkill;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.registry.race.TensuraRaces;
import com.github.manasmods.tensura.registry.skill.ResistanceSkills;
import com.github.manasmods.tensura.util.JumpPowerHelper;
import com.mojang.datafixers.util.Pair;
import io.github.dracosomething.trawakened.registry.raceregistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

public class honkaiapostle extends Race {
    public honkaiapostle() {
        super(Difficulty.EXTREME);
    }

    @Override
    public double getBaseHealth() {
        return 8;
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
        return 6.5;
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

    public Race getDefaultEvolution(Player player) {
        return (Race) TensuraRaces.RACE_REGISTRY.get().getValue(raceregistry.AWAKENED_APOSTLE);
    }

    public Race getAwakeningEvolution(Player player) {
        return (Race) TensuraRaces.RACE_REGISTRY.get().getValue(raceregistry.AWAKENED_APOSTLE);
    }

    public Race getHarvestFestivalEvolution(Player player) {
        return (Race) TensuraRaces.RACE_REGISTRY.get().getValue(raceregistry.ENSLAVED_APOSTLE);
    }

    public List<TensuraSkill> getIntrinsicSkills(Player player) {
        List<TensuraSkill> list = new ArrayList();
        list.add((TensuraSkill) SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:voiceofhonkai")));
        return list;
    }

    public List<Race> getNextEvolutions(Player player) {
        List<Race> list = new ArrayList();
//        list.add((Race) TensuraRaces.HUMAN.get());
        list.add((Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceregistry.AWAKENED_APOSTLE));
        list.add((Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceregistry.ENSLAVED_APOSTLE));
        return list;
    }

    @Override
    public void raceTick(Player player) {
        if (TensuraPlayerCapability.getRace(player).equals((Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceregistry.AWAKENED_APOSTLE))) {
            ArrayList<ManasSkillInstance> list = new ArrayList<>(SkillAPI.getSkillsFrom(player).getLearnedSkills());
            for (ManasSkillInstance instance : list) {
                ManasSkill var3 = instance.getSkill();
                Skill skill = (Skill) var3;
                if (skill.getType().equals(Skill.SkillType.ULTIMATE)) {
                    if (!skill.equals((TensuraSkill) SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:powerofhonkai"))) && !skill.equals((TensuraSkill) SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:willofhonkai")))) {
                        SkillAPI.getSkillsFrom(player).forgetSkill(skill);
                    }
                }
            }
        }
    }
}
