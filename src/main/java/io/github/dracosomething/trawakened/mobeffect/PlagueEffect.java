package io.github.dracosomething.trawakened.mobeffect;

import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.capability.effects.TensuraEffectsCapability;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.effect.template.DamageAction;
import com.github.manasmods.tensura.entity.MetalSlimeEntity;
import com.github.manasmods.tensura.entity.SlimeEntity;
import com.github.manasmods.tensura.entity.SupermassiveSlimeEntity;
import com.github.manasmods.tensura.event.UpdateEPEvent;
import com.github.manasmods.tensura.handler.EntityEPHandler;
import com.github.manasmods.tensura.registry.particle.TensuraParticles;
import com.github.manasmods.tensura.world.TensuraGameRules;
import io.github.dracosomething.trawakened.ability.skill.ultimate.herrscherofplague;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.util.trawakenedDamage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import java.util.Objects;

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
                    entity.hurt(trawakenedDamage.PLAGUE, (float) pAmplifier * 4);
                }
            }
        } else {
            SkillHelper.checkThenAddEffectSource(entity, getOwner(entity), (MobEffect) effectRegistry.PLAGUEEFFECT.get(), 1000, 3);
        }
        if(entity.isDeadOrDying()){
            AABB aabb = new AABB((double) (entity.getX() - 4), (double) (entity.getY() - 4), (double) (entity.getZ() - 4), (double) (entity.getX() + 4), (double) (entity.getY() + 4), (double) (entity.getZ() + 4));
            List<Entity> entities = entity.level.getEntities((Entity) null, aabb, Entity::isAlive);
            List<Entity> ret = new ArrayList();
            new Vec3((double) entity.getX(), (double) entity.getY(), (double) entity.getZ());
            Iterator var16 = entities.iterator();

            while (var16.hasNext()) {
                Entity entity2 = (Entity) var16.next();

                double x = entity2.getX();
                double y = entity2.getY();
                double z = entity2.getZ();
                double cmp = (double) (4 * 4) - ((double) entity2.getX() - x) * ((double) entity2.getX() - x) - ((double) entity2.getY() - y) * ((double) entity2.getY() - y) - ((double) entity2.getZ() - z) * ((double) entity2.getZ() - z);
                if (cmp > 0.0) {
                    ret.add(entity2);
                }
            }

            for (Entity entity2 : ret) {
                if (entity2 instanceof LivingEntity) {
                    if (entity.getRandom().nextInt(100) <= 20) {
                        if (!(entity2 == getOwner(entity))) {
                            SkillHelper.checkThenAddEffectSource((LivingEntity) entity2, getOwner(entity), (MobEffect) effectRegistry.PLAGUEEFFECT.get(), 32767, 3);
                        }
                    }
                }
            }
            TensuraParticleHelper.addParticlesAroundSelf(entity, (ParticleOptions) ParticleTypes.SQUID_INK);

            Player source = getOwner(entity);
            if (source != null) {
                TensuraEPCapability.getFrom(entity).ifPresent((cap) -> {
                    double epGain = SkillUtils.getEPGain(Objects.requireNonNull(getOwner(entity)));
                    double EP = TensuraEPCapability.getCurrentEP(Objects.requireNonNull(getOwner(entity)));
                    cap.setEP(Objects.requireNonNull(getOwner(entity)), (cap.getEP() - epGain) + EP);
                });
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
            if(e.getEntity() != getOwner(source)) {
                LivingEntity target = e.getEntity();
                SkillHelper.checkThenAddEffectSource(target, getOwner(source), (MobEffect) effectRegistry.PLAGUEEFFECT.get(), 32767, 3);
            }
        }
    }

    @Override
    public void onBeingDamaged(LivingHurtEvent e) {
        if (e.getEntity().getRandom().nextInt(100) <= 15) {
            if(e.getEntity() != getOwner(e.getEntity())) {
                LivingEntity target = e.getEntity();
                SkillHelper.checkThenAddEffectSource(target, getOwner(e.getEntity()), (MobEffect) effectRegistry.PLAGUEEFFECT.get(), 32767, 3);
            }
        }
    }

    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration % 10 == 0;
    }
}
