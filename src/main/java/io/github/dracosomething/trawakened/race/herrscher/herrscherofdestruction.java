package io.github.dracosomething.trawakened.race.herrscher;

import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.TensuraSkill;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.race.Race;
import io.github.dracosomething.trawakened.api.race.HerrscherRace;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class herrscherofdestruction extends HerrscherRace {
    public herrscherofdestruction() {
        super(Generation.FIRST);
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
    public double getEvolutionPercentage(Player player) {
        int chance = 0;
        int chance1 = 0;
        int chance2 = 0;
        if (SkillUtils.isSkillMastered(player, Objects.requireNonNull(SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:powerofhonkai"))))) {
            chance += 25;
        }
        if (TensuraPlayerCapability.getBaseMagicule(player) >= 100000) {
            chance1 += 25;
        }
        if (TensuraPlayerCapability.getBaseEP(player) >= 1000000) {
            chance2 += 50;
        }
            return (double) chance + chance1 + chance2;
    }

        public List<Component> getRequirementsForRendering (Player player) {
            List<Component> list = new ArrayList();
            list.add(Component.translatable("trawakened.requirement.evolution.herrscher_of_destruction"));
            list.add(Component.translatable("trawakened.requirement.evolution.MoreMp"));
            list.add(Component.translatable("trawakened.requirement.evolution.MoreEp"));
            return list;
        }
    }

