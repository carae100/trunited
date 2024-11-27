package io.github.dracosomething.trawakened.mobeffect;

import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.effect.template.DamageAction;
import com.github.manasmods.tensura.effect.template.SkillMobEffect;
import com.github.manasmods.tensura.effect.template.Transformation;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.particle.TensuraParticles;
import com.github.manasmods.tensura.util.damage.TensuraDamageSource;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public class HonkaiBeastEffect  extends SkillMobEffect implements Transformation, DamageAction {
    protected static final String HONKAI = "c585ceb0-3f6a-11ee-be56-0242ac120002";

    public HonkaiBeastEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "c585ceb0-3f6a-11ee-be56-0242ac120002", 30.0, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.ARMOR, "c585ceb0-3f6a-11ee-be56-0242ac120002", 10.0, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "c585ceb0-3f6a-11ee-be56-0242ac120002", 0.01, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.MAX_HEALTH, "c585ceb0-3f6a-11ee-be56-0242ac120002", 50.0, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, "c585ceb0-3f6a-11ee-be56-0242ac120002", 100.0, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier((Attribute) ForgeMod.ATTACK_RANGE.get(), "c585ceb0-3f6a-11ee-be56-0242ac120002", 2.0, AttributeModifier.Operation.ADDITION);
    }

    public void applyEffectTick(LivingEntity entity, int pAmplifier) {
        if (pAmplifier >= 1 || !this.failedToActivate(entity, this)) {
            float damage = (float)pAmplifier * 10.0F;
            DamageSource source = (new TensuraDamageSource("out_of_energy")).setNotTensuraMagic().bypassArmor();
            if (damage > 0.0F) {
                entity.hurt(source, damage);
            }

            TensuraParticleHelper.addParticlesAroundSelf(entity, (ParticleOptions) TensuraParticles.PURPLE_LIGHTNING_SPARK.get());
        }

    }

    @Override
    public void onPlayerAttack(AttackEntityEvent e) {
        if(e.getTarget() instanceof LivingEntity){
            LivingEntity entity = (LivingEntity) e.getTarget();
            Explosion.BlockInteraction interaction = Explosion.BlockInteraction.BREAK;
            entity.level.explode(entity, entity.getX(), entity.getY(), entity.getZ(), 5F, interaction);
        }
    }

    public void removeAttributeModifiers(LivingEntity entity, AttributeMap pAttributeMap, int pAmplifier) {
        super.removeAttributeModifiers(entity, pAttributeMap, pAmplifier);
        TensuraEPCapability.updateEP(entity);
    }

    public boolean isDurationEffectTick(int pDuration, int amplifier) {
        return pDuration % 20 == 0;
    }
}

