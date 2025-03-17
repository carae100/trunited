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
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.data.TensuraTags;
import com.github.manasmods.tensura.entity.human.FalmuthKnightEntity;
import com.github.manasmods.tensura.entity.human.IOtherworlder;
import com.github.manasmods.tensura.entity.human.OtherworlderEntity;
import com.github.manasmods.tensura.entity.human.ShogoTaguchiEntity;
import com.github.manasmods.tensura.entity.template.HumanoidNPCEntity;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity;
import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.race.dwarf.DwarfRace;
import com.github.manasmods.tensura.race.elf.ElfRace;
import com.github.manasmods.tensura.race.human.HumanRace;
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
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
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
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 500.0).add(Attributes.ARMOR, 0.5).add(Attributes.ATTACK_DAMAGE, 55.0).add(Attributes.ATTACK_KNOCKBACK, 5.0).add(Attributes.MOVEMENT_SPEED, 0.20000000298023224).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.2).add((Attribute) ForgeMod.SWIM_SPEED.get(), 1.0).add((Attribute)ForgeMod.ATTACK_RANGE.get(), 2.0).build();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new HumanoidNPCEntity.EatingItemGoal(this, (entity) -> {
            return this.shouldHeal();
        }, 3.0F));
        this.goalSelector.addGoal(3, new CrossbowAttackGoal(this, 1.2, 20.0F));
        this.goalSelector.addGoal(3, new RangedBowAttackGoal(this, 1.0, 20, 20.0F));
        this.goalSelector.addGoal(3, new HumanoidNPCEntity.SpearTypeAttackGoal(this, 1.0, 20, 20.0F));
        this.goalSelector.addGoal(3, new HumanoidNPCEntity.NPCMeleeAttackGoal(this, 2.0, true));
        this.goalSelector.addGoal(4, new WanderingFollowOwnerGoal(this, 1.5, 10.0F, 5.0F, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.2));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.targetSelector.addGoal(1, new TensuraTamableEntity.TensuraOwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new TensuraTamableEntity.TensuraOwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new TensuraTamableEntity.TensuraHurtByTargetGoal(this, new Class[]{ShogoTaguchiEntity.class})).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(5, new NonTameRandomTargetGoal(this, Player.class, false, this::shouldAttackPlayer));
        this.targetSelector.addGoal(6, new NonTameRandomTargetGoal(this, LivingEntity.class, false, (entity) -> {
            LivingEntity entity1 = (LivingEntity) entity;
            return entity1.getType().is(TensuraTags.EntityTypes.OTHERWORLDER_PREY);
        }));
        this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal(this, true));
    }

    private boolean shouldAttackPlayer(Object o) {
        LivingEntity target = (LivingEntity) o;
        Race race = TensuraPlayerCapability.getRace(target);
        if (race != null && !race.isSpiritual()) {
            if (TensuraEPCapability.getEP(target) < 5000.0) {
                return false;
            } else if (TensuraEPCapability.isMajin(target)) {
                return true;
            } else if (race instanceof ElfRace) {
                return false;
            } else if (race instanceof DwarfRace) {
                return false;
            } else {
                return !(race instanceof HumanRace);
            }
        } else {
            return false;
        }
    }

    public ResourceLocation getTextureLocation() {
        return DefaultPlayerSkin.getDefaultSkin();
    }

    public void tick() {
        super.tick();
    }

    public void setTarget(@Nullable LivingEntity pLivingEntity) {
        super.setTarget(pLivingEntity);
    }
}
