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

import java.util.UUID;

public class HonkaiBeastEffect  extends SkillMobEffect implements Transformation, DamageAction {
    protected static final String HONKAI = "263f92dc-ea72-43bc-899d-679cddc22d23";

    public HonkaiBeastEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "263f92dc-ea72-43bc-899d-679cddc22d23", 30.0, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.ARMOR, "263f92dc-ea72-43bc-899d-679cddc22d23", 10.0, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "263f92dc-ea72-43bc-899d-679cddc22d23", 0.01, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.MAX_HEALTH, "263f92dc-ea72-43bc-899d-679cddc22d23", 50.0, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, "263f92dc-ea72-43bc-899d-679cddc22d23", 100.0, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier((Attribute) ForgeMod.ATTACK_RANGE.get(), "263f92dc-ea72-43bc-899d-679cddc22d23", 2.0, AttributeModifier.Operation.ADDITION);
    }

    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
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

//    @Override
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
    public double getAttributeModifierValue(int pAmplifier, AttributeModifier pModifier) {
        return pModifier.getId().equals(UUID.fromString("263f92dc-ea72-43bc-899d-679cddc22d23")) ? pModifier.getAmount() : pModifier.getAmount() * (double)(pAmplifier + 1);
    }

    public boolean isDurationEffectTick(int pDuration, int amplifier) {
        return pDuration % 20 == 0;
    }
}

