package io.github.dracosomething.trawakened.race;

import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.TensuraSkill;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.registry.race.TensuraRaces;
import com.github.manasmods.tensura.registry.skill.ResistanceSkills;
import com.github.manasmods.tensura.util.JumpPowerHelper;
import com.mojang.datafixers.util.Pair;
import io.github.dracosomething.trawakened.registry.raceRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class awakenedapostle extends honkaiapostle {
    public awakenedapostle() {}

    @Override
    public double getBaseHealth() {
        return 20;
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
        return 2.5;
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
        return 0.2;
    }

    @Override
    public Pair<Double, Double> getBaseAuraRange() {
        return Pair.of(1000.0, 1500.0);
    }

    @Override
    public Pair<Double, Double> getBaseMagiculeRange() {
        return Pair.of(1000.0, 2000.0);
    }

    public boolean isMajin() {
        return false;
    }

    public Race getDefaultEvolution(Player player){return (Race) TensuraRaces.RACE_REGISTRY.get().getValue(raceRegistry.HERRSCHER_SEED_AWAKENED);}

    public Race getAwakeningEvolution(Player player){return (Race) TensuraRaces.RACE_REGISTRY.get().getValue(raceRegistry.HERRSCHER_SEED_AWAKENED);}

    public Race getHarvestFestivalEvolution(Player player){return (Race) TensuraRaces.RACE_REGISTRY.get().getValue(raceRegistry.HERRSCHER_SEED_AWAKENED);}

    public List<TensuraSkill> getIntrinsicSkills(Player player) {
        List<TensuraSkill> list = new ArrayList();
        list.add((TensuraSkill) SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:powerofhonkai")));
        list.add((TensuraSkill) ResistanceSkills.PHYSICAL_ATTACK_RESISTANCE.get());
        return list;
    }

    public List<Race> getNextEvolutions(Player player) {
        List<Race> list = new ArrayList();
        list.add((Race) ((IForgeRegistry<?>) TensuraRaces.RACE_REGISTRY.get()).getValue(raceRegistry.HERRSCHER_SEED_AWAKENED));
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
        if (SkillUtils.isSkillMastered(player, Objects.requireNonNull(SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:voiceofhonkai"))))){
            chance += 50;
        }
        if (TensuraPlayerCapability.getBaseEP(player) >= 150000) {
            chance2 += 50;
        }

        return (double) chance + chance2;
    }

    public List<Component> getRequirementsForRendering(Player player) {
        List<Component> list = new ArrayList();
        list.add(Component.translatable("trawakened.requirement.evolution.awakened_apostle"));
        list.add(Component.translatable("trawakened.requirement.evolution.MoreEp"));
        return list;
    }
}
