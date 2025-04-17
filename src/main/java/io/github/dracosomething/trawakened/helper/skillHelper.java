package io.github.dracosomething.trawakened.helper;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
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
import java.util.function.Predicate;

public class skillHelper {
    public static List<Entity> DrawSphereAndGetEntitiesInIt(LivingEntity entity, Level level, Vec3 pos, int radius, boolean areNotAllie){
        AABB aabb = new AABB((double) (pos.x - radius), (double) (pos.y - radius), (double) (pos.z - radius), (double) (pos.x + radius), (double) (pos.y + radius), (double) (pos.z + radius));
        List<Entity> entities = level.getEntities((Entity) null, aabb, Entity::isAlive);
        List<Entity> ret = new ArrayList();
        Iterator var16 = entities.iterator();

        while (var16.hasNext()) {
            Entity entity2 = (Entity) var16.next();

            double x = entity2.getX();
            double y = entity2.getY();
            double z = entity2.getZ();
            double cmp = (double) (radius * radius) - ((double) pos.x - x) * ((double) pos.x - x) - ((double) pos.y - y) * ((double) pos.y - y) - ((double) pos.z - z) * ((double) pos.z - z);
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

    public static List<Entity> DrawSphereAndGetEntitiesInIt(LivingEntity entity, int radius, boolean areNotAllie){
        return DrawSphereAndGetEntitiesInIt(entity, entity.level, entity.position(), radius, areNotAllie);
    }

    public static List<Entity> DrawSphereAndGetEntitiesInIt(LivingEntity entity, Level level, int radius, boolean areNotAllie){
        return DrawSphereAndGetEntitiesInIt(entity, level, entity.position(), radius, areNotAllie);
    }

    public static List<Entity> DrawSphereAndGetEntitiesInIt(LivingEntity entity, Vec3 pos, int radius, boolean areNotAllie){
        return DrawSphereAndGetEntitiesInIt(entity, entity.level, pos, radius, areNotAllie);
    }

    public static List<Entity> DrawSphereAndGetEntitiesInIt(LivingEntity entity, int radius){
        return DrawSphereAndGetEntitiesInIt(entity, entity.level, entity.position(), radius, false);
    }

    public static List<LivingEntity> GetLivingEntitiesInRange(LivingEntity entity, int radius, boolean areNotAllie) {
        List<LivingEntity> list = new ArrayList<>();
        DrawSphereAndGetEntitiesInIt(entity, radius, areNotAllie).stream().filter((entity1 -> entity1 instanceof LivingEntity)).toList().forEach((living) -> list.add((LivingEntity) living));
        return list;
    }

    public static List<LivingEntity> GetLivingEntitiesInRange(LivingEntity entity, Level level, Vec3 pos, int radius, boolean areNotAllie) {
        List<LivingEntity> list = new ArrayList<>();
        DrawSphereAndGetEntitiesInIt(entity, level, pos, radius, areNotAllie).stream().filter((entity1 -> entity1 instanceof LivingEntity)).toList().forEach((living) -> list.add((LivingEntity) living));
        return list;
    }

    public static List<LivingEntity> GetLivingEntitiesInRange(LivingEntity entity, int radius) {
        List<LivingEntity> list = new ArrayList<>();
        DrawSphereAndGetEntitiesInIt(entity, entity.level, entity.position(), radius, false).stream().filter((entity1 -> entity1 instanceof LivingEntity)).toList().forEach((living) -> list.add((LivingEntity) living));
        return list;
    }

    public static List<LivingEntity> GetLivingEntitiesInRange(LivingEntity entity, Vec3 pos, int radius) {
        List<LivingEntity> list = new ArrayList<>();
        DrawSphereAndGetEntitiesInIt(entity, entity.level, pos, radius, false).stream().filter((entity1 -> entity1 instanceof LivingEntity)).toList().forEach((living) -> list.add((LivingEntity) living));
        return list;
    }

    public static List<Player> getPlayersInRange(LivingEntity entity, Vec3 pos, int radius, Predicate<? super Player> predicate) {
        List<Player> list = new ArrayList<>();
        List<Player> finalList = list;
        DrawSphereAndGetEntitiesInIt(entity, radius, false)
                .stream()
                .filter(entity1 -> entity1 instanceof Player)
                .toList()
                .forEach((entity1) -> {
                    finalList.add((Player) entity1);
                });
        if (entity instanceof Player player) finalList.add(player);
        list = finalList.stream().filter(predicate).toList();
        return list;
    }

    public static void sendMessageToNearbyPlayers(int radius, LivingEntity entity, Component message) {
        DrawSphereAndGetEntitiesInIt(entity, radius, false)
                .stream()
                .filter(entity1 -> entity1 instanceof Player)
                .toList()
                .forEach((entity1) -> {
                    if (entity instanceof Player player) {
                        player.sendSystemMessage(message);
                    }
                });
    }

    public static void sendMessageToNearbyPlayersWithSource(int radius, LivingEntity entity, Component message) {
        Component newMessage = Component.literal("<" + entity.getDisplayName().getString() + "> " + message.getString());
        sendMessageToNearbyPlayers(radius, entity, newMessage);
    }

    public static void ParticleSphere(LivingEntity entity, double radius, ParticleOptions type) {
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

    public static void ParticleCircle(LivingEntity entity, double radius, ParticleOptions type) {
        ParticleCircle(entity, radius, 1, type);
    }

    public static void ParticleCircle(LivingEntity entity, double radius, int amount, ParticleOptions type) {
        for(float x = (float) (entity.getX() - (float)radius); x < entity.getX() + (float)radius; ++x) {
            for(float z = (float) (entity.getZ() - (float)radius); z < entity.getZ() + (float)radius; ++z) {
                float cmp = (float) ((float)(radius * radius) - (entity.getX() - x) * (entity.getX() - x) - (entity.getZ() - z) * (entity.getZ() - z));
                if (cmp > 0.0F && entity.level instanceof ServerLevel world) {
                    for (int i = 0; i < amount; i++) {
                        world.sendParticles(type, (double) x, (double) entity.getY()-1.5, (double) z, 5, 1.0, 1.0, 1.0, 1.0);
                    }
                }
            }
        }
    }

    public static void ParticleCircle(Vec3 pos, Level level, double radius, int amount, ParticleOptions type) {
        for(float x = (float) (pos.x - (float)radius); x < pos.x + (float)radius; ++x) {
            for (float z = (float) (pos.z - (float) radius); z < pos.z + (float) radius; ++z) {
                float cmp = (float) ((float)(radius * radius) - (pos.x - x) * (pos.x - x) - (pos.z - z) * (pos.z - z));
                if (cmp > 0.0F && cmp < 6.1F && level instanceof ServerLevel world) {
                    for (int i = 0; i < amount; ++i) {
                        world.sendParticles(type, (double) x, (double) pos.y - 1.5, (double) z, 5, 1.0, 1.0, 1.0, 1.0);
                    }
                }
            }
        }
    }

    public static void ParticleRing(LivingEntity entity, double radius, ParticleOptions type) {
        ParticleRing(entity, radius, 1, 1, type);
    }

    public static void ParticleRing(LivingEntity entity, double radius, double height, ParticleOptions type) {
        ParticleRing(entity, radius, height, 1, type);
    }

    public static void ParticleRing(LivingEntity entity, double radius, double height, int amount, ParticleOptions type) {
        ParticleRing(entity.position(), entity.level, radius, height, amount, type);
    }

    public static void ParticleRing(Vec3 pos, Level level, double radius, double height, int amount, ParticleOptions type) {
        for(float x = (float) (pos.x - (float)radius); x < pos.x + (float)radius; ++x) {
            for (float y = (float) pos.y; y < pos.y + height; ++y) {
                for (float z = (float) (pos.z - (float) radius); z < pos.z + (float) radius; ++z) {
                    float cmp = (float) ((float)(radius * radius) - (pos.x - x) * (pos.x - x) - (pos.y - y) * (pos.y - y) - (pos.z - z) * (pos.z - z));
                    if (cmp > 0.0F && cmp < 6.1F && level instanceof ServerLevel world) {
                        for (int i = 0; i < amount; ++i) {
                            world.sendParticles(type, (double) x, (double) pos.y - 1.5, (double) z, 5, 1.0, 1.0, 1.0, 1.0);
                        }
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
