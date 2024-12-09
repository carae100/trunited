package io.github.dracosomething.trawakened.mobeffect;

import com.github.manasmods.tensura.capability.effects.TensuraEffectsCapability;
import io.github.dracosomething.trawakened.ability.skill.ultimate.herrscherofplague;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.util.trawakenedDamage;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class PlagueEffect extends MobEffect {
    public PlagueEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    public void applyEffectTick(LivingEntity entity, int pAmplifier) {
        if (herrscherofplague.active) {
            entity.addEffect(new MobEffectInstance((MobEffect) effectRegistry.PLAGUEEFFECT.get(), 1000, 2, false, false, false));
            Player source = TensuraEffectsCapability.getEffectSource(entity, this);
            if(source != null) {
                System.out.println(source);
                if (getOwner(entity) == source) {
                    if (source != null) {
                        entity.hurt(trawakenedDamage.PLAGUE, (float) pAmplifier * 2);
                    }
                }
            }
        } else {
            entity.addEffect(new MobEffectInstance((MobEffect) effectRegistry.PLAGUEEFFECT.get(), 100, 2, false, false, false));
        }
    }

    public static Player getOwner(LivingEntity entity){
        Player source = TensuraEffectsCapability.getEffectSource(entity, effectRegistry.PLAGUEEFFECT.get());
        if (source != null) {
            System.out.println(source);
            return source;
        } else {
            return null;
        }
    }

    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration % 20 == 0;
    }
}
