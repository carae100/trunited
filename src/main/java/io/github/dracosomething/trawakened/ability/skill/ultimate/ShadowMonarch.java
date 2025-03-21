package io.github.dracosomething.trawakened.ability.skill.ultimate;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import io.github.dracosomething.trawakened.capability.ShadowCapability.AwakenedShadowCapability;
import io.github.dracosomething.trawakened.network.TRAwakenedNetwork;
import io.github.dracosomething.trawakened.network.play2client.OpenBecomeShadowscreen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.PacketDistributor;

import java.util.Random;

public class ShadowMonarch extends Skill {
    public ShadowMonarch() {
        super(SkillType.ULTIMATE);
    }

    @Override
    public int modes() {
        return 5;
    }

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        Random random = new Random();
        LivingEntity target = SkillHelper.getTargetingEntity(entity, ForgeMod.REACH_DISTANCE.get().getDefaultValue(), false, true);
        switch (instance.getMode()) {
            case 1:
                if (AwakenedShadowCapability.isShadow(target)) {
                    if (target != null && AwakenedShadowCapability.getTries(target) > 0) {
                        int chance;
                        if (TensuraEPCapability.getCurrentEP(target) * 0.75 <= TensuraEPCapability.getCurrentEP(entity)) {
                            chance = 100;
                        } else {
                            chance = (TensuraEPCapability.getCurrentEP(target) <= TensuraEPCapability.getCurrentEP(entity) ? 50 : 30);
                        }
                        AwakenedShadowCapability.setTries(target, AwakenedShadowCapability.getTries(target) - 1);
                        if (entity instanceof Player player && player instanceof ServerPlayer serverPlayer) {
                            TRAwakenedNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new OpenBecomeShadowscreen(entity.getUUID()));
                        } else {
                            if (random.nextInt(0, 100) <= chance) {
                                AwakenedShadowCapability.setArisen(target, true);
                            }
                        }
                    }
                }
                break;
        }
    }
}
