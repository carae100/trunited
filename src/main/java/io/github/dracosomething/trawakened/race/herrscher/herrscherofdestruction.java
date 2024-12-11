package io.github.dracosomething.trawakened.race.herrscher;

import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.TensuraSkill;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.registry.race.TensuraRaces;
import com.github.manasmods.tensura.registry.skill.ExtraSkills;
import com.github.manasmods.tensura.util.JumpPowerHelper;
import com.mojang.datafixers.util.Pair;
import io.github.dracosomething.trawakened.race.HerrscherSeedAwakened;
import io.github.dracosomething.trawakened.registry.raceregistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class herrscherofdestruction extends HerrscherSeedAwakened {
    @Override
    public double getBaseHealth() {
        return 500;
    }

    @Override
    public double getBaseAttackDamage() {
        return 15;
    }

    @Override
    public double getBaseAttackSpeed() {
        return 4;
    }

    @Override
    public double getKnockbackResistance() {
        return 10;
    }

    @Override
    public double getMovementSpeed() {
        return 0.20;
    }

    public double getJumpHeight() {
        return JumpPowerHelper.defaultPlayer()+0.15;
    }

    @Override
    public Pair<Double, Double> getBaseAuraRange() {
        return Pair.of(5000.0, 10000.0);
    }

    @Override
    public Pair<Double, Double> getBaseMagiculeRange() {
        return Pair.of(50000.0, 100000.0);
    }

    public List<Race> getNextEvolutions(Player player) {
        List<Race> list = new ArrayList();
        return list;
    }

    public List<TensuraSkill> getIntrinsicSkills(Player player) {
        List<TensuraSkill> list = new ArrayList();
        list.add((TensuraSkill) SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:herrscherofdestructionskill")));
        return list;
    }

    @Override
    public void raceTick(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, 10, false, false, false));
    }

    @Override
    public double getEvolutionPercentage(Player player) {
        int chance = 0;
        if (SkillUtils.isSkillMastered(player, Objects.requireNonNull(SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:powerofhonkai"))))) {
            chance += 25;
        } else if (TensuraPlayerCapability.getBaseMagicule(player) >= 100000) {
            chance += 25;
        } else if (TensuraPlayerCapability.getBaseEP(player) >= 1000000) {
            chance += 50;
        }
            return (double) chance;
    }

        public List<Component> getRequirementsForRendering (Player player) {
            List<Component> list = new ArrayList();
            list.add(Component.translatable("trawakened.requirement.evolution.herrscher_of_destruction"));
            list.add(Component.translatable("trawakened.requirement.evolution.MoreMp"));
            list.add(Component.translatable("trawakened.requirement.evolution.MoreEp"));
            return list;
        }
    }

