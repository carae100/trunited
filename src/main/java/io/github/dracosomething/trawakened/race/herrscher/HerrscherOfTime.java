package io.github.dracosomething.trawakened.race.herrscher;

import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.TensuraSkill;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.race.Race;
import io.github.dracosomething.trawakened.api.race.HerrscherRace;
import io.github.dracosomething.trawakened.registry.skillregistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HerrscherOfTime extends HerrscherRace {
    public HerrscherOfTime() {
        super(Generation.FIRST);
    }

    public List<Race> getNextEvolutions(Player player) {
        List<Race> list = new ArrayList();
        return list;
    }

    public @NotNull List<TensuraSkill> getIntrinsicSkills(@NotNull Player player) {
        List<TensuraSkill> list = new ArrayList<>();
        list.add(skillregistry.HERRSCHEROFTIME.get());
        return list;
    }

    @Override
    public double getEvolutionPercentage(@NotNull Player player) {
        int chance = 0;
        int chance1 = 0;
        int chance2 = 0;
        if (SkillUtils.isSkillMastered(player, Objects.requireNonNull(SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:willofhonkai"))))) {
            chance += 25;
        }
        if (player.getMainHandItem() == Items.CLOCK.getDefaultInstance() && player.getHealth() == player.getMaxHealth()/2) {
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
