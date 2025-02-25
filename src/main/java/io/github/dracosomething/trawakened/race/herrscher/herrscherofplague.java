package io.github.dracosomething.trawakened.race.herrscher;

import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.TensuraSkill;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.race.Race;
import io.github.dracosomething.trawakened.library.race.HerrscherRace;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class herrscherofplague extends HerrscherRace {

    public herrscherofplague() {
        super(Generation.FIRST, skillRegistry.HERRSCHEROFPLAGUE.get());
    }

    public List<Race> getNextEvolutions(Player player) {
        List<Race> list = new ArrayList();
        return list;
    }

    @Override
    public double getEvolutionPercentage(Player player) {
        int chance = 0;
        int chance1 = 0;
        int chance2 = 0;
        if (SkillUtils.isSkillMastered(player, Objects.requireNonNull(SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:willofhonkai"))))) {
            chance += 25;
        }
        if (player.hasEffect(MobEffects.POISON) && player.hasEffect(MobEffects.BLINDNESS)) {
            chance1 += 50;
        }
        if (TensuraPlayerCapability.getBaseEP(player) >= 1000000) {
            chance2 += 25;
        }
            return (double) chance+chance1+chance2;
    }

        public List<Component> getRequirementsForRendering (Player player) {
            List<Component> list = new ArrayList();
            list.add(Component.translatable("trawakened.requirement.evolution.herrscher_of_pestilence.effect"));
            list.add(Component.translatable("trawakened.requirement.evolution.herrscher_of_pestilence.skill"));
            list.add(Component.translatable("trawakened.requirement.evolution.MoreEp"));
            return list;
        }
    }

