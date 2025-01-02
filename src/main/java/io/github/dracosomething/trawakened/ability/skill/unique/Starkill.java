package io.github.dracosomething.trawakened.ability.skill.unique;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.capability.skill.TensuraSkillCapability;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.entity.human.CloneEntity;
import com.github.manasmods.tensura.entity.magic.breath.BreathEntity;
import com.github.manasmods.tensura.registry.entity.TensuraEntityTypes;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.registry.particleRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class Starkill extends Skill {
    public ResourceLocation getSkillIcon() {
        return new ResourceLocation("trawakened", "textures/skill/analog_horror_skills/unique/starkill.png");
    }

    public Starkill() {
        super(SkillType.UNIQUE);
    }

    @Override
    public int modes() {
        return 3;
    }

    @Override
    public double getObtainingEpCost() {
        return 10000;
    }

    @Override
    public double learningCost() {
        return 1000;
    }

    @Override
    public int nextMode(LivingEntity entity, TensuraSkillInstance instance, boolean reverse) {
        if (reverse) {
            return instance.getMode() == 1 ? 3 : instance.getMode() - 1;
        } else {
            return instance.getMode() == 3 ? 1 : instance.getMode() + 1;
        }
    }

    @Override
    public Component getModeName(int mode) {
        MutableComponent var10000;
        switch (mode) {
            case 1 -> var10000 = Component.translatable("trawakened.skill.mode.starkill.create_apostle");
            case 2 -> var10000 = Component.translatable("trawakened.skill.mode.starkill.assimilation");
            case 3 -> var10000 = Component.translatable("trawakened.skill.mode.starkill.infinity");
            default -> var10000 = Component.empty();
        }

        return var10000;
    }

    private boolean on = true;
    private int mode = 1;

    @Override
    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        switch (instance.getMode()){
            case 1:
                Level level = entity.getLevel();
                double var10000;
                LivingEntity target = SkillHelper.getTargetingEntity(entity, 5.0, false);
                if (entity instanceof Player) {
                    Player player = (Player)entity;
                    var10000 = TensuraPlayerCapability.getBaseMagicule(player);
                } else {
                    var10000 = TensuraEPCapability.getEP(entity);
                }

                double MP = var10000;
                double EP = MP * 0.1;
                if (!SkillHelper.outOfMagicule(entity, EP)) {
                    if(target instanceof Player) {
                        BlockHitResult result = SkillHelper.getPlayerPOVHitResult(target.level, target, ClipContext.Fluid.NONE, 0.0);
                        CloneEntity clone = this.summonClone(target, entity, level, EP, result.getLocation());
                        CloneEntity.copyEffects(target, clone);
                        EquipmentSlot[] var10 = EquipmentSlot.values();
                        int var11 = var10.length;

                        for (int var12 = 0; var12 < var11; ++var12) {
                            EquipmentSlot slot = var10[var12];
                            clone.setItemSlot(slot, target.getItemBySlot(slot).copy());
                        }
                        target.kill();
                        this.addMasteryPoint(instance, entity);
                        instance.setCoolDown(instance.isMastered(entity) ? 1 : 3);
                    } else {
                        if(target instanceof TamableAnimal animal){
                            if(entity instanceof Player player) {
                                animal.tame(player);
                                this.addMasteryPoint(instance, entity);
                                instance.setCoolDown(instance.isMastered(entity) ? 1 : 3);
                            }
                        } else {
                            BlockHitResult result = SkillHelper.getPlayerPOVHitResult(target.level, target, ClipContext.Fluid.NONE, 0.0);
                            CloneEntity clone = this.summonClone(target, entity, level, EP, result.getLocation());
                            CloneEntity.copyEffects(target, clone);
                            EquipmentSlot[] var10 = EquipmentSlot.values();
                            int var11 = var10.length;

                            for (int var12 = 0; var12 < var11; ++var12) {
                                EquipmentSlot slot = var10[var12];
                                clone.setItemSlot(slot, target.getItemBySlot(slot).copy());
                            }
                            target.setHealth(0);
                            this.addMasteryPoint(instance, entity);
                            instance.setCoolDown(35);
                        }
                    }
                }
            break;
            case 2:
            default:
                break;
            case 3:
                if(!SkillHelper.outOfMagicule(entity, instance)){
                    if(entity.isShiftKeyDown()){
                        if(entity instanceof Player player) {
                            switch (mode) {
                                case 1:
                                    player.displayClientMessage(Component.translatable("trawakened.skill.mode.starkill.infinity.overwhelmed").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
                                    mode = 2;
                                    break;
                                case 2:
                                    player.displayClientMessage(Component.translatable("trawakened.skill.mode.starkill.infinity.spatial_attack").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
                                    mode = 3;
                                    break;
                                case 3:
                                    player.displayClientMessage(Component.translatable("trawakened.skill.mode.starkill.infinity.analyze").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
                                    mode = 1;
                                    break;
                            }
                            if(instance.onCoolDown()) {
                                instance.setCoolDown(instance.getCoolDown());
                            }
                        }
                    } else {
                        switch (mode){
                            case 1:
                                this.analize(instance, entity, on);
                                on = !on;
                                break;
                            case 2:
                                if (!SkillHelper.outOfMagicule(entity, instance)) {
                                    AABB aabb = new AABB((double) (entity.getX() - 8), (double) (entity.getY() - 8), (double) (entity.getZ() - 8), (double) (entity.getX() + 8), (double) (entity.getY() + 8), (double) (entity.getZ() + 8));
                                    List<Entity> entities = entity.level.getEntities((Entity) null, aabb, Entity::isAlive);
                                    List<Entity> ret = new ArrayList();
                                    new Vec3((double) entity.getX(), (double) entity.getY(), (double) entity.getZ());
                                    Iterator var16 = entities.iterator();

                                    while (var16.hasNext()) {
                                        Entity entity2 = (Entity) var16.next();

                                        int radius = 10;

                                        double x = entity2.getX();
                                        double y = entity2.getY();
                                        double z = entity2.getZ();
                                        double cmp = (double) (radius * radius) - ((double) entity2.getX() - x) * ((double) entity2.getX() - x) - ((double) entity2.getY() - y) * ((double) entity2.getY() - y) - ((double) entity2.getZ() - z) * ((double) entity2.getZ() - z);
                                        if (cmp > 0.0) {
                                            ret.add(entity2);
                                        }
                                    }

                                    for (Entity entity2 : ret) {
                                        if (entity2 instanceof LivingEntity) {
                                            if (entity2 != entity) {
                                                SkillHelper.checkThenAddEffectSource((LivingEntity) entity2, entity, (MobEffect) effectRegistry.OVERWHELMED.get(), entity.level.random.nextInt(600, 6000), 3);
                                            }
                                        }
                                    }
                                    TensuraParticleHelper.addServerParticlesAroundSelf(entity, ParticleTypes.FLASH, 0.1);
                                    entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.WITHER_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);
                                    instance.setCoolDown(40);
                                }
                                break;
                            case 3:
                                LivingEntity target2 = SkillHelper.getTargetingEntity(entity, 5.0, false);
                                target2.hurt(TensuraDamageSources.SEVERANCE_UPDATE, 6);
                                TensuraParticleHelper.addServerParticlesAroundSelf(target2, ParticleTypes.SWEEP_ATTACK, 0.5);
                                instance.setCoolDown(10);
                                break;
                        }
                    }
                }
                break;
        }
    }

    private int heldseconds = 0;

    public boolean onHeld(ManasSkillInstance instance, LivingEntity entity, int heldTicks) {
        if (instance.onCoolDown()) {
            return false;
        } else {
            if (instance.getMode() == 2) {
                if (heldTicks % 60 == 0 && heldTicks > 0) {
                    this.addMasteryPoint(instance, entity);
                } else if (heldTicks % 20 == 0){
                    heldseconds++;
                }

                BreathEntity.spawnPredationMist((EntityType) TensuraEntityTypes.GLUTTONY_MIST.get(), entity, instance, this.magiculeCost(entity, instance), (float) 3, 0, true);
//                entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean canIgnoreCoolDown(ManasSkillInstance instance, LivingEntity entity) {
        switch (instance.getMode()) {
            case 1, 2 -> {
                return false;
            }
            case 3 -> {
                if(entity.isShiftKeyDown()){
                    return true;
                }else {
                    switch (mode) {
                        case 2, 3 -> {
                            return false;
                        }
                        case 1 -> {
                            return true;
                        }
                        default -> {
                            return false;
                        }
                    }
                }
            }
            default -> {
                return true;
            }
        }
    }

    @Override
    public void onRelease(ManasSkillInstance instance, LivingEntity entity, int heldTicks) {
        if (instance.getMode() == 2){
            instance.setCoolDown(2 * heldseconds);
            heldseconds = 0;
        }
    }

    private void analize(ManasSkillInstance instance, LivingEntity entity, boolean on) {
        if (entity instanceof Player) {
            if (on) {
                Player player = (Player) entity;
                TensuraSkillCapability.getFrom(player).ifPresent((cap) -> {
                    int level;
                    level = 5;
                    if (cap.getAnalysisLevel() != level) {
                        cap.setAnalysisLevel(level);
                        cap.setAnalysisDistance(instance.isMastered(entity) ? 35 : 15);
                        entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                    } else {
                        cap.setAnalysisLevel(0);
                        entity.getLevel().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                    }

                    TensuraSkillCapability.sync(player);
                });
            }
        }
    }

    private CloneEntity summonClone(LivingEntity entity, LivingEntity owner, Level level, double EP, Vec3 position) {
        EntityType<CloneEntity> type = entity.isShiftKeyDown() ? (EntityType) TensuraEntityTypes.CLONE_SLIM.get() : (EntityType)TensuraEntityTypes.CLONE_DEFAULT.get();
        CloneEntity clone = new CloneEntity(type, level);
        if (owner instanceof Player player) {
            clone.tame(player);
        }

        clone.setSkill(this);
        clone.copyStatsAndSkills(entity, true);
        TensuraEPCapability.setLivingEP(clone, EP);
        clone.setHealth(clone.getMaxHealth());
        clone.setPos(position);
        CloneEntity.copyRotation(entity, clone);
        level.addFreshEntity(clone);
        TensuraParticleHelper.addServerParticlesAroundSelf(owner, particleRegistry.FLESHPARTICLE.get(), 1.0);
        TensuraParticleHelper.addServerParticlesAroundSelf(owner, particleRegistry.FLESHPARTICLE.get(), 2.0);
        TensuraParticleHelper.addServerParticlesAroundSelf(entity, particleRegistry.FLESHPARTICLE.get(), 1.0);
        TensuraParticleHelper.addServerParticlesAroundSelf(entity, particleRegistry.FLESHPARTICLE.get(), 2.0);
        return clone;
    }
}
