package io.github.dracosomething.trawakened.entity.otherwolder;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.TensuraSkill;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.ability.skill.unique.BerserkerSkill;
import com.github.manasmods.tensura.api.entity.ai.CrossbowAttackGoal;
import com.github.manasmods.tensura.api.entity.ai.WanderingFollowOwnerGoal;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.data.TensuraTags;
import com.github.manasmods.tensura.entity.human.FalmuthKnightEntity;
import com.github.manasmods.tensura.entity.human.IOtherworlder;
import com.github.manasmods.tensura.entity.human.OtherworlderEntity;
import com.github.manasmods.tensura.entity.human.ShogoTaguchiEntity;
import com.github.manasmods.tensura.entity.template.HumanoidNPCEntity;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity;
import com.github.manasmods.tensura.registry.particle.TensuraParticles;
import com.github.manasmods.tensura.registry.skill.UniqueSkills;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class defaultOtherWolder  extends OtherworlderEntity implements IOtherworlder {
    public defaultOtherWolder(EntityType<? extends OtherworlderEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes().add(Attributes.ARMOR, 0.0).add(Attributes.ATTACK_DAMAGE, 20.0).add(Attributes.ATTACK_KNOCKBACK, 5.0).add(Attributes.MAX_HEALTH, 200.0).add(Attributes.MOVEMENT_SPEED, 0.20000000298023224).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.0).add((Attribute) ForgeMod.SWIM_SPEED.get(), 1.0).add((Attribute)ForgeMod.ATTACK_RANGE.get(), 2.0).build();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new HumanoidNPCEntity.EatingItemGoal(this, (entity) -> {
            return this.shouldHeal();
        }, 3.0F));
        this.goalSelector.addGoal(3, new CrossbowAttackGoal(this, 1.2, 20.0F));
        this.goalSelector.addGoal(3, new RangedBowAttackGoal(this, 1.0, 20, 20.0F));
        this.goalSelector.addGoal(3, new HumanoidNPCEntity.NPCMeleeAttackGoal(this, 2.0, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.2));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.targetSelector.addGoal(1, new TensuraTamableEntity.TensuraOwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new TensuraTamableEntity.TensuraOwnerHurtTargetGoal(this));
//        this.targetSelector.addGoal(5, new NonTameRandomTargetGoal(this, Player.class, false, this.shouldAttackPlayer());
        this.targetSelector.addGoal(6, new NonTameRandomTargetGoal(this, LivingEntity.class, false, (entity) -> {
            Entity entity1 = (Entity) entity;
            return entity1.getType().is(TensuraTags.EntityTypes.OTHERWORLDER_PREY);
        }));
        this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal(this, true));
    }

    public ResourceLocation getTextureLocation() {
        return DefaultPlayerSkin.getDefaultSkin();
    }

    public void tick() {
        super.tick();
        if (this.isAlive() && this.getHealth() < this.getMaxHealth() && this.tickCount % 40 == 0 && this.getSurvivor() != null) {
            this.heal(10.0F);
            TensuraParticleHelper.addServerParticlesAroundSelf(this, ParticleTypes.HEART);
        }

    }

    public void setTarget(@Nullable LivingEntity pLivingEntity) {
        LivingEntity oldTarget = this.getTarget();
        super.setTarget(pLivingEntity);
        LivingEntity newTarget = this.getTarget();
        ManasSkillInstance instance = this.getBerserker();
        if (instance != null) {
            if (oldTarget == null && newTarget != null) {
                this.activateBerserker();
            } else if (oldTarget != null && newTarget == null) {
                this.activateBerserker();
            }

        }
    }

    private void activateBerserker() {
        double EP = TensuraEPCapability.getEP(this) * 12.5;
        AttributeInstance armor = (AttributeInstance) Objects.requireNonNull(this.getAttribute(Attributes.ARMOR));
        if (armor.getModifier(BerserkerSkill.BERSERKER) != null) {
            armor.removePermanentModifier(BerserkerSkill.BERSERKER);
            this.getLevel().playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 1.0F, 1.0F);
        } else {
            AttributeModifier armorModifier = new AttributeModifier(BerserkerSkill.BERSERKER, "BerserkerArmor", BerserkerSkill.getArmor(EP) / 4.0, AttributeModifier.Operation.ADDITION);
            armor.addPermanentModifier(armorModifier);
            this.getLevel().playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.0F);
            this.getLevel().playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 0.5F, 1.0F);
            TensuraParticleHelper.addServerParticlesAroundSelf(this, (ParticleOptions) TensuraParticles.SOLAR_FLASH.get(), 1.0);
            TensuraParticleHelper.addServerParticlesAroundSelf(this, (ParticleOptions)TensuraParticles.YELLOW_LIGHTNING_SPARK.get(), 3.0);
            TensuraParticleHelper.addServerParticlesAroundSelf(this, (ParticleOptions)TensuraParticles.YELLOW_LIGHTNING_SPARK.get(), 2.0);
            TensuraParticleHelper.spawnServerParticles(this.level, (ParticleOptions)TensuraParticles.PURPLE_LIGHTNING_SPARK.get(), this.getX(), this.getY() + (double)(this.getBbHeight() / 2.0F), this.getZ(), 100, 0.08, 0.08, 0.08, 0.2, true);
        }

        AttributeInstance damage = (AttributeInstance)Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE));
        if (damage.getModifier(BerserkerSkill.BERSERKER) != null) {
            damage.removePermanentModifier(BerserkerSkill.BERSERKER);
        } else {
            damage.addPermanentModifier(new AttributeModifier(BerserkerSkill.BERSERKER, "BerserkerAttack", BerserkerSkill.getAttack(EP) * 1.5, AttributeModifier.Operation.ADDITION));
        }

        AttributeInstance speed = (AttributeInstance)Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED));
        if (speed.getModifier(BerserkerSkill.BERSERKER) != null) {
            speed.removePermanentModifier(BerserkerSkill.BERSERKER);
        } else {
            speed.addPermanentModifier(new AttributeModifier(BerserkerSkill.BERSERKER, "BerserkerSpeed", BerserkerSkill.getSpeed(EP) / 200.0, AttributeModifier.Operation.ADDITION));
        }

    }

    @Nullable
    private ManasSkillInstance getBerserker() {
        Optional<ManasSkillInstance> skill = SkillAPI.getSkillsFrom(this).getSkill((ManasSkill)UniqueSkills.BERSERKER.get());
        if (skill.isEmpty()) {
            return null;
        } else {
            return !((ManasSkillInstance)skill.get()).canInteractSkill(this) ? null : (ManasSkillInstance)skill.get();
        }
    }

    @Nullable
    private ManasSkillInstance getSurvivor() {
        Optional<ManasSkillInstance> skill = SkillAPI.getSkillsFrom(this).getSkill((ManasSkill)UniqueSkills.SURVIVOR.get());
        if (skill.isEmpty()) {
            return null;
        } else {
            return !((ManasSkillInstance)skill.get()).canInteractSkill(this) ? null : (ManasSkillInstance)skill.get();
        }
    }
}
