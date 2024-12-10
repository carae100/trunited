package io.github.dracosomething.trawakened.mobeffect;

import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.capability.effects.TensuraEffectsCapability;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.effect.template.DamageAction;
import com.github.manasmods.tensura.entity.MetalSlimeEntity;
import com.github.manasmods.tensura.entity.SlimeEntity;
import com.github.manasmods.tensura.entity.SupermassiveSlimeEntity;
import com.github.manasmods.tensura.event.UpdateEPEvent;
import com.github.manasmods.tensura.handler.EntityEPHandler;
import com.github.manasmods.tensura.registry.particle.TensuraParticles;
import io.github.dracosomething.trawakened.ability.skill.ultimate.herrscherofplague;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.util.trawakenedDamage;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlagueEffect extends MobEffect implements DamageAction {
    public PlagueEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    public void applyEffectTick(LivingEntity entity, int pAmplifier) {
        if (herrscherofplague.active) {
            Player source = TensuraEffectsCapability.getEffectSource(entity, this);
            if(getOwner(entity) == source){
                for(int i = 0; i < 2; i++){
                    if(i==1){
                        SkillHelper.checkThenAddEffectSource(entity, source, (MobEffect) effectRegistry.PLAGUEEFFECT.get(), 1000, 3);
                    }
                }
            }
            if (source != null) {
                if (getOwner(entity) == source) {
                    entity.hurt(trawakenedDamage.plague(source), (float) pAmplifier * 4);
                }
            }
        }
    }

    public static Player getOwner(LivingEntity entity) {
        Player source = TensuraEffectsCapability.getEffectSource(entity, effectRegistry.PLAGUEEFFECT.get());
        if (source != null) {
            return source;
        } else {
            return null;
        }
    }

    @Override
    public void onDamagingEntity(LivingEntity source, LivingHurtEvent e) {
        if (source.getRandom().nextInt(100) <= 20) {
            LivingEntity target = e.getEntity();
            SkillHelper.checkThenAddEffectSource(target, getOwner(source), (MobEffect) effectRegistry.PLAGUEEFFECT.get(), 32767, 3);
        }
    }

    @Override
    public void onKillEntity(LivingEntity source, LivingDeathEvent e) {
        AABB aabb = new AABB((double) (source.getX() - 4), (double) (source.getY() - 4), (double) (source.getZ() - 4), (double) (source.getX() + 4), (double) (source.getY() + 4), (double) (source.getZ() + 4));
        List<Entity> entities = source.level.getEntities((Entity) null, aabb, Entity::isAlive);
        List<Entity> ret = new ArrayList();
        new Vec3((double) source.getX(), (double) source.getY(), (double) source.getZ());
        Iterator var16 = entities.iterator();

        while (var16.hasNext()) {
            Entity entity = (Entity) var16.next();

            double x = entity.getX();
            double y = entity.getY();
            double z = entity.getZ();
            double cmp = (double) (4 * 4) - ((double) source.getX() - x) * ((double) source.getX() - x) - ((double) source.getY() - y) * ((double) source.getY() - y) - ((double) source.getZ() - z) * ((double) source.getZ() - z);
            if (cmp > 0.0) {
                ret.add(entity);
            }
        }

        Iterator var13 = ret.iterator();

        while(var13.hasNext()) {
            Entity entity = (Entity)var13.next();
            if (source.getRandom().nextInt(100) <= 20) {
                SkillHelper.checkThenAddEffectSource((LivingEntity) entity, getOwner(source), (MobEffect) effectRegistry.PLAGUEEFFECT.get(), 32767, 3);
            }
        }
        TensuraParticleHelper.addParticlesAroundSelf(source, (ParticleOptions) ParticleTypes.SQUID_INK, 0.3);
    }

    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration % 10 == 0;
    }
}
