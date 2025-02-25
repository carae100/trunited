package io.github.dracosomething.trawakened.race.herrscher;

import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.TensuraSkill;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.race.Race;
import io.github.dracosomething.trawakened.library.race.HerrscherRace;
import io.github.dracosomething.trawakened.capability.trawakenedPlayerCapability;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class HerrscherOfTheWorld extends HerrscherRace {
    public HerrscherOfTheWorld() {
        super(Generation.FIRST);
    }

    public List<Race> getNextEvolutions(Player player) {
        List<Race> list = new ArrayList();
        return list;
    }

    public List<TensuraSkill> getIntrinsicSkills(Player player) {
        List<TensuraSkill> list = new ArrayList();
        list.add(skillRegistry.HERRSCHEROFTHEWORLD.get());
        return list;
    }

    @Override
    public double getEvolutionPercentage(Player player) {
        int chance = 0;
        int chance1 = 0;
        int chance2 = 0;
        if (SkillUtils.isSkillMastered(player, skillRegistry.POWEROFHONKAI.get())) {
            chance += 25;
        }
        if (player.hasEffect(effectRegistry.SHPPOISON.get()) && TensuraEPCapability.getSpiritualHealth(player) <= trawakenedPlayerCapability.getMaxSpiritualHealth(player)/2) {
        }
        chance1 += 50;
        if (TensuraPlayerCapability.getBaseEP(player) >= 1000000) {
            chance2 += 25;
        }
        return (double) chance+chance1+chance2;
    }

    public List<Component> getRequirementsForRendering (Player player) {
        List<Component> list = new ArrayList();
        list.add(Component.translatable("trawakened.requirement.evolution.herrscher_of_the_world.effect"));
        list.add(Component.translatable("trawakened.requirement.evolution.herrscher_of_destruction"));
        list.add(Component.translatable("trawakened.requirement.evolution.MoreEp"));
        return list;
    }
}
