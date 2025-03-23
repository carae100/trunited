package io.github.dracosomething.trawakened.helper;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.config.TensuraConfig;
import com.github.manasmods.tensura.entity.human.CloneEntity;
import com.github.manasmods.tensura.event.NamingEvent;
import com.github.manasmods.tensura.network.play2server.RequestNamingGUIPacket;
import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.registry.entity.TensuraEntityTypes;
import com.github.manasmods.tensura.util.TensuraAdvancementsHelper;
import io.github.dracosomething.trawakened.registry.particleRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class skillHelper {
    public static List<Entity> DrawCircle(LivingEntity entity, int radius, boolean areNotAllie){
        AABB aabb = new AABB((double) (entity.getX() - radius), (double) (entity.getY() - radius), (double) (entity.getZ() - radius), (double) (entity.getX() + radius), (double) (entity.getY() + radius), (double) (entity.getZ() + radius));
        List<Entity> entities = entity.level.getEntities((Entity) null, aabb, Entity::isAlive);
        List<Entity> ret = new ArrayList();
        new Vec3((double) entity.getX(), (double) entity.getY(), (double) entity.getZ());
        Iterator var16 = entities.iterator();

        while (var16.hasNext()) {
            Entity entity2 = (Entity) var16.next();

            double x = entity2.getX();
            double y = entity2.getY();
            double z = entity2.getZ();
            double cmp = (double) (radius * radius) - ((double) entity2.getX() - x) * ((double) entity2.getX() - x) - ((double) entity2.getY() - y) * ((double) entity2.getY() - y) - ((double) entity2.getZ() - z) * ((double) entity2.getZ() - z);
            if (cmp > 0.0) {
                if(areNotAllie) {
                    if (!entity2.isAlliedTo(entity)) {
                        ret.add(entity2);
                    }
                } else {
                    ret.add(entity2);
                }
            }
        }

        return ret;
    }

    public static List<LivingEntity> GetLivingEntities(LivingEntity entity, int radius, boolean areNotAllie) {
        List<LivingEntity> list = new ArrayList<>();
        DrawCircle(entity, radius, areNotAllie).stream().filter((entity1 -> entity1 instanceof LivingEntity)).toList().forEach((living) -> list.add((LivingEntity) living));
        return list;
    }

    public static void ParticleCircle(LivingEntity entity, double radius, ParticleOptions type) {
        for(float x = (float) (entity.getX() - (float)radius); x < entity.getX() + (float)radius; ++x) {
            for(float y = (float) (entity.getY() - (float)radius); y < entity.getY() + (float)radius; ++y) {
                for(float z = (float) (entity.getZ() - (float)radius); z < entity.getZ() + (float)radius; ++z) {
                    float cmp = (float) ((float)(radius * radius) - (entity.getX() - x) * (entity.getX() - x) - (entity.getY() - y) * (entity.getY() - y) - (entity.getZ() - z) * (entity.getZ() - z));
                    if (cmp > 0.0F && cmp < 6.1F && entity.level instanceof ServerLevel world) {
                        world.sendParticles(type, (double) x, (double) y, (double) z, 5, 1.0, 1.0, 1.0, 1.0);
                    }
                }
            }
        }
    }

    public static void name(LivingEntity sub, @Nullable ServerPlayer owner, RequestNamingGUIPacket.NamingType type, String name) {
        double var20000;
        if (sub instanceof Player player) {
            var20000 = TensuraPlayerCapability.getBaseEP(player);
        } else {
            var20000 = TensuraEPCapability.getEP(sub);
        }

        double subEP = var20000;
        TensuraEPCapability.getFrom(sub).ifPresent((namingCap) -> {
            double var10000 = 0;

            double originalCost = var10000;
            NamingEvent event = new NamingEvent(sub, owner, originalCost, type, name);
            if (!MinecraftForge.EVENT_BUS.post(event)) {
                originalCost = event.getOriginalCost();
                double cost = 0;
                if (owner != null && TensuraPlayerCapability.getMagicule(owner) < cost) {
                    owner.displayClientMessage(Component.translatable("tensura.skill.lack_magicule").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                } else {
                    namingCap.setName(event.getName());
                    sub.setCustomName(Component.literal(event.getName()));
                    if (owner != null) {
                        namingCap.setPermanentOwner(owner.getUUID());
                        TensuraAdvancementsHelper.grant(owner, TensuraAdvancementsHelper.Advancements.NAME_A_MOB);
                    }
                    if (sub instanceof Player) {
                        Player player = (Player)sub;
                        player.refreshDisplayName();
                        if (owner != null) {
                            player.displayClientMessage(Component.translatable("tensura.naming.name_success", new Object[]{event.getName(), owner.getName()}).setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)), false);
                        } else {
                            player.displayClientMessage(Component.translatable("tensura.naming.name_success.no_namer", new Object[]{event.getName()}).setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)), false);
                        }

                        TensuraPlayerCapability.setTrackedRace(player, (Race)null);
                        TensuraPlayerCapability.sync(player);
                        TensuraEPCapability.sync(player);
                    } else if (owner != null) {
                        label45: {
                            if (sub instanceof TamableAnimal) {
                                TamableAnimal tamable = (TamableAnimal)sub;
                                if (!ForgeEventFactory.onAnimalTame(tamable, owner)) {
                                    tamable.tame(owner);
                                    break label45;
                                }
                            }

                            if (sub instanceof AbstractHorse) {
                                AbstractHorse horse = (AbstractHorse)sub;
                                if (!ForgeEventFactory.onAnimalTame(horse, owner)) {
                                    horse.tameWithName(owner);
                                }
                            }
                        }
                    }

                    sub.heal(sub.getMaxHealth());
                    SkillHelper.removePredicateEffect(sub, (effect) -> {
                        return effect.getCategory().equals(MobEffectCategory.HARMFUL);
                    });
                }
            }
        });
    }

    public static CloneEntity summonClone(LivingEntity entity, LivingEntity owner, Level level, double EP, Vec3 position, ManasSkill source) {
        EntityType<CloneEntity> type = entity.isShiftKeyDown() ? (EntityType) TensuraEntityTypes.CLONE_SLIM.get() : (EntityType)TensuraEntityTypes.CLONE_DEFAULT.get();
        CloneEntity clone = new CloneEntity(type, level);
        if (owner instanceof Player player) {
            clone.tame(player);
        }

        clone.setSkill(source);
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

    public static void tameAnything(LivingEntity entity, LivingEntity entity2, ManasSkill source) {
        if (entity2 != entity) {
            Level level = entity.getLevel();
            double var10000;
            if (entity instanceof Player) {
                Player player = (Player) entity;
                var10000 = TensuraPlayerCapability.getBaseMagicule(player);
            } else {
                var10000 = TensuraEPCapability.getEP(entity);
            }

            double MP = var10000;
            double EP = MP * 0.1;
            if (!SkillHelper.outOfMagicule(entity, EP)) {
                if (entity2 instanceof Player player && player != entity) {
                    BlockHitResult result = SkillHelper.getPlayerPOVHitResult(player.level, player, ClipContext.Fluid.NONE, 0.0);
                    CloneEntity clone = skillHelper.summonClone(player, entity, level, EP, result.getLocation(), source);
                    CloneEntity.copyEffects(player, clone);
                    EquipmentSlot[] var10 = EquipmentSlot.values();
                    int var11 = var10.length;

                    for (int var12 = 0; var12 < var11; ++var12) {
                        EquipmentSlot slot = var10[var12];
                        clone.setItemSlot(slot, player.getItemBySlot(slot).copy());
                    }
                    double damage = player.getHealth() - (player.getMaxHealth() * 0.75);
                    if (damage <= player.getHealth()) {
                        player.kill();
                    } else {
                        player.setHealth((float) damage);
                    }
                } else {
                    if (entity2 instanceof TamableAnimal animal) {
                        if (entity instanceof Player player) {
                            animal.tame(player);
                        }
                    } else {
                        assert entity2 instanceof LivingEntity;
                        double ep = TensuraEPCapability.getEP((LivingEntity) entity2);
                        assert entity instanceof ServerPlayer;
                        skillHelper.name((LivingEntity) entity2, (ServerPlayer) entity, RequestNamingGUIPacket.NamingType.LOW, "");
                        entity2.setCustomName(Component.empty());
                        entity2.setCustomNameVisible(false);
                    }
                }
            }
        }
    }

    public static Entity cloneMob(LivingEntity entity, EntityType<?> type) {
        Level level = entity.level;
        Entity entity1 = type.create(level);
        TensuraParticleHelper.addServerParticlesAroundSelf(entity1, particleRegistry.FLESHPARTICLE.get(), 1.0);
        TensuraParticleHelper.addServerParticlesAroundSelf(entity1, particleRegistry.FLESHPARTICLE.get(), 2.0);
        TensuraParticleHelper.addServerParticlesAroundSelf(entity, particleRegistry.FLESHPARTICLE.get(), 1.0);
        TensuraParticleHelper.addServerParticlesAroundSelf(entity, particleRegistry.FLESHPARTICLE.get(), 2.0);
        Vec3 lookVec = entity.getEyePosition(1.0F);
        entity1.setPos(lookVec);
        level.addFreshEntity(entity1);
        skillHelper.name((LivingEntity) entity1, (ServerPlayer) entity, RequestNamingGUIPacket.NamingType.HIGH, "");
        return entity1;
    }
}
