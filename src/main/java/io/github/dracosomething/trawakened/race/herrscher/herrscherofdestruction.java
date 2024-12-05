package io.github.dracosomething.trawakened.race.herrscher;

import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.registry.skill.ExtraSkills;
import io.github.dracosomething.trawakened.race.HerrscherSeedAwakened;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

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
    public double getEvolutionPercentage(Player player) {
        int chance = 0;
        if (SkillUtils.isSkillMastered(player, Objects.requireNonNull(SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:powerofhonkai"))))) {
            chance += 25;
        } else if (TensuraPlayerCapability.getBaseMagicule(player) >= 10000) {
            chance += 25;
        } else if (TensuraPlayerCapability.getBaseEP(player) >= 500000) {
            chance += 50;
        }
            return (double) chance;
    }

        public List<Component> getRequirementsForRendering () {
            List<Component> list = new ArrayList();
            list.add(Component.translatable("trawakened.requirement.evolution.herrscher_of_destruction"));
            list.add(Component.translatable("trawakened.requirement.evolution.MoreMp"));
            list.add(Component.translatable("trawakened.requirement.evolution.MoreEp"));
            return list;
        }
    }

