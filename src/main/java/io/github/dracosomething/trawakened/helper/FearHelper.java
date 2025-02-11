package io.github.dracosomething.trawakened.helper;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import io.github.dracosomething.trawakened.api.FearTypes;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.AwakenedFearCapability;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.registry.skillregistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FearHelper extends SightHelper{
    public static void fearPenalty(LivingEntity entity) {
        if (AwakenedFearCapability.getScared(entity) >= 5) {
            addSingularPenaltyEffecy(entity, TensuraMobEffects.FEAR.get(), 1);
        } else if (AwakenedFearCapability.getScared(entity) >= 10 ) {
            addSingularPenaltyEffecy(entity, TensuraMobEffects.FEAR.get(), 2);
        } else if (AwakenedFearCapability.getScared(entity) >= 15 ) {
            addSingularPenaltyEffecy(entity, TensuraMobEffects.FEAR.get(), 3);
        } else if (AwakenedFearCapability.getScared(entity) >= 20 ) {
            addSingularPenaltyEffecy(entity, TensuraMobEffects.FEAR.get(), 4);
        } else if (AwakenedFearCapability.getScared(entity) >= 25 ) {
            addSingularPenaltyEffecy(entity, TensuraMobEffects.FEAR.get(), 5);
            addSingularPenaltyEffecy(entity, MobEffects.DIG_SLOWDOWN, 1);
        } else if (AwakenedFearCapability.getScared(entity) >= 30 ) {
            addSingularPenaltyEffecy(entity, TensuraMobEffects.FEAR.get(), 5);
            addSingularPenaltyEffecy(entity, MobEffects.DIG_SLOWDOWN, 3);
        } else if (AwakenedFearCapability.getScared(entity) >= 35 ) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN), 5);
        } else if (AwakenedFearCapability.getScared(entity) >= 40 ) {
            addSingularPenaltyEffecy(entity, TensuraMobEffects.FEAR.get(), 7);
            addSingularPenaltyEffecy(entity, MobEffects.DIG_SLOWDOWN, 5);
        } else if (AwakenedFearCapability.getScared(entity) >= 45 ) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN), 7);
        } else if (AwakenedFearCapability.getScared(entity) == 50) {
            decreaseSHP(entity, 10);
        } else if (AwakenedFearCapability.getScared(entity) >= 50 ) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN), 7);
            addSingularPenaltyEffecy(entity, MobEffects.WEAKNESS, 1);
        } else if (AwakenedFearCapability.getScared(entity) >= 55 ) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN), 7);
            addSingularPenaltyEffecy(entity, MobEffects.WEAKNESS, 4);
        } else if (AwakenedFearCapability.getScared(entity) >= 60 ) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS), 7);
        } else if (AwakenedFearCapability.getScared(entity) >= 65 ) {
            addSingularPenaltyEffecy(entity, TensuraMobEffects.FEAR.get(), 10);
            addFearPenaltyEffect(entity, List.of(MobEffects.WEAKNESS, MobEffects.DIG_SLOWDOWN), 7);
        } else if (AwakenedFearCapability.getScared(entity) >= 70 ) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN), 10);
            addSingularPenaltyEffecy(entity, MobEffects.WEAKNESS, 7);
        } else if (AwakenedFearCapability.getScared(entity) == 75 ) {
            decreaseSHP(entity, 25);
        } else if (AwakenedFearCapability.getScared(entity) >= 75 ) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS), 10);
            addSingularPenaltyEffecy(entity, TensuraMobEffects.PARALYSIS.get(), 1);
        } else if (AwakenedFearCapability.getScared(entity) >= 80 ) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS), 10);
            addSingularPenaltyEffecy(entity, TensuraMobEffects.PARALYSIS.get(), 5);
        } else if (AwakenedFearCapability.getScared(entity) >= 85) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS, TensuraMobEffects.PARALYSIS.get()), 10);
        } else if (AwakenedFearCapability.getScared(entity) >= 90 ) {
            addSingularPenaltyEffecy(entity, TensuraMobEffects.FEAR.get(), 15);
            addFearPenaltyEffect(entity, List.of(MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS, TensuraMobEffects.PARALYSIS.get()), 10);
        } else if (AwakenedFearCapability.getScared(entity) >= 95 ) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.PARALYSIS.get(), MobEffects.DIG_SLOWDOWN), 15);
            addFearPenaltyEffect(entity, List.of(MobEffects.WEAKNESS, TensuraMobEffects.PARALYSIS.get()), 10);
        } else if (AwakenedFearCapability.getScared(entity) == 100 ) {
            decreaseSHP(entity, 55);
        } else if (AwakenedFearCapability.getScared(entity) >= 100 ) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, TensuraMobEffects.PARALYSIS.get(), MobEffects.WEAKNESS), 15);
            addSingularPenaltyEffecy(entity, TensuraMobEffects.CHILL.get(), 1);
        } else if (AwakenedFearCapability.getScared(entity) >= 105 ) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, TensuraMobEffects.PARALYSIS.get(), MobEffects.WEAKNESS), 15);
            addSingularPenaltyEffecy(entity, TensuraMobEffects.CHILL.get(), 8);
        }  else if (AwakenedFearCapability.getScared(entity) >= 110 ) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), TensuraMobEffects.CHILL.get(), MobEffects.DIG_SLOWDOWN, TensuraMobEffects.PARALYSIS.get(), MobEffects.WEAKNESS), 15);
        } else if (AwakenedFearCapability.getScared(entity) >= 115 ) {
            addSingularPenaltyEffecy(entity, TensuraMobEffects.FEAR.get(), 22);
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.CHILL.get(), MobEffects.DIG_SLOWDOWN, TensuraMobEffects.PARALYSIS.get(), MobEffects.WEAKNESS), 15);
        } else if (AwakenedFearCapability.getScared(entity) >= 120 ) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN), 22);
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.CHILL.get(), TensuraMobEffects.PARALYSIS.get(), MobEffects.WEAKNESS), 15);
        } else if (AwakenedFearCapability.getScared(entity) >= 125 ) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS), 22);
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.CHILL.get(), TensuraMobEffects.PARALYSIS.get()), 15);
            addFearPenaltyEffect(entity, List.of(MobEffects.WITHER), 1);
        } else if (AwakenedFearCapability.getScared(entity) >= 130 ) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS, TensuraMobEffects.PARALYSIS.get()), 22);
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.CHILL.get()), 15);
            addFearPenaltyEffect(entity, List.of(MobEffects.WITHER), 8);
        } else if (AwakenedFearCapability.getScared(entity) >= 135 ) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS, TensuraMobEffects.PARALYSIS.get()), 22);
            addFearPenaltyEffect(entity, List.of(MobEffects.WITHER, TensuraMobEffects.CHILL.get()), 15);
        } else if (AwakenedFearCapability.getScared(entity) >= 140 ) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS, TensuraMobEffects.PARALYSIS.get(), TensuraMobEffects.CHILL.get()), 22);
            addSingularPenaltyEffecy(entity, MobEffects.WITHER, 15);
        } else if (AwakenedFearCapability.getScared(entity) >= 145 ) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS, TensuraMobEffects.PARALYSIS.get(), TensuraMobEffects.CHILL.get(), MobEffects.WITHER), 22);
        } else if (AwakenedFearCapability.getScared(entity) == 150) {
            decreaseSHP(entity, 105);
        } else if (AwakenedFearCapability.getScared(entity) >= 150) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS, TensuraMobEffects.PARALYSIS.get(), TensuraMobEffects.CHILL.get(), MobEffects.WITHER), 22);
            addSingularPenaltyEffecy(entity, effectRegistry.BRAINDAMAGE.get(), 5);
        }
    }

    public static void addSingularPenaltyEffecy (LivingEntity entity, MobEffect effect, int amplifier) {
        addFearPenaltyEffect(entity, List.of(effect), amplifier);
    }

    public static void addFearPenaltyEffect(LivingEntity entity, List<MobEffect> mobEffects, int amplifier) {
        for (MobEffect mobEffect : mobEffects) {
            SkillHelper.addEffectWithSource(entity, entity, mobEffect, 10, amplifier, false, false, false, false);
        }
    }

    public static void decreaseSHP(LivingEntity entity, int amount) {
        if(entity.getAttributes().getInstance(TensuraAttributeRegistry.MAX_SPIRITUAL_HEALTH.get()) != null) {
            entity.getAttributes().getInstance(TensuraAttributeRegistry.MAX_SPIRITUAL_HEALTH.get()).setBaseValue(entity.getAttributeBaseValue(TensuraAttributeRegistry.MAX_SPIRITUAL_HEALTH.get()) - amount);
        }
        AwakenedFearCapability.increaseScared(entity);
        AwakenedFearCapability.setScaredCooldown(entity, 9000);
    }

    public static void resetData(LivingEntity entity) {
        if (entity.isDeadOrDying()) {
            if (entity.getPersistentData().hasUUID("alternate_UUID")) {
                ManasSkillInstance instance = SkillUtils.getSkillOrNull(entity.level.getPlayerByUUID(entity.getPersistentData().getUUID("alternate_UUID")), skillregistry.ALTERNATE.get());
                if (instance != null) {
                    CompoundTag tag = instance.getOrCreateTag();
                    tag.putInt("original_scared", 0);
                    tag.putBoolean("is_locked", false);
                }
            }
        }
    }

    public static void fearUpdates(LivingEntity entity) {
        if (!AwakenedFearCapability.onCooldown(entity)) {
            FearTypes fear = AwakenedFearCapability.getFearType(entity);
            List<Block> blocks = fear.getBlock();
            List<Item> items = fear.getItem();
            List<EntityType<?>> entityTypes = fear.getEntity();
            List<MobEffect> mobEffects = fear.getEffect();
            Vec3 vec3 = getSightPos1(entity);
            Vec3 vec31 = getSightPos2(entity);
            AABB sight = getLineOfSight(entity, vec3, vec31);
            Iterable<BlockPos> blocksInSight = getBlocksInSight(vec3, vec31);
            List<Entity> entities = getEntitiesInSight(entity, sight);
            if (checkAll(fear, entities, entity)) {
                effectCheck(mobEffects, entity);
                entityCheck(entityTypes, entity, entities);
                blockCheck(blocks, entity, blocksInSight);
                itemCheck(items, entity, entities);
            }
        } else {
            AwakenedFearCapability.decreaseCooldown(entity);
        }
    }

    public static void effectCheck(List<MobEffect> mobEffects, LivingEntity entity) {
        if (mobEffects != null) {
            for (MobEffect effect : mobEffects) {
                if (entity.hasEffect(effect) && !AwakenedFearCapability.onCooldown(entity) && !AwakenedFearCapability.onCooldown(entity)) {
                    AwakenedFearCapability.increaseScared(entity);
                    AwakenedFearCapability.setScaredCooldown(entity, AwakenedFearCapability.getScared(entity) <= 3 ? 6000 : 9000);
                }
            }
        }
    }

    public static void entityCheck(List<EntityType<?>> entityTypes, LivingEntity entity, List<Entity> entities) {
        if (entityTypes != null) {
            for (EntityType<?> entityType : entityTypes) {
                for (Entity entity1 : entities) {
                    if (entity1.getType().equals(entityType) && !AwakenedFearCapability.onCooldown(entity)) {
                        AwakenedFearCapability.increaseScared(entity);
                        AwakenedFearCapability.setScaredCooldown(entity, AwakenedFearCapability.getScared(entity) <= 3 ? 6000 : 9000);
                    }
                }
            }
        }
    }

    public static void blockCheck(List<Block> blocks, LivingEntity entity, Iterable<BlockPos> blocksInSight) {
        if (blocks != null) {
            for (Block block : blocks) {
                for (BlockPos blockPos : blocksInSight) {
                    if (entity.level.getBlockState(blockPos).getBlock().equals(block) && !AwakenedFearCapability.onCooldown(entity)) {
                        AwakenedFearCapability.increaseScared(entity);
                        AwakenedFearCapability.setScaredCooldown(entity, AwakenedFearCapability.getScared(entity) <= 3 ? 6000 : 9000);
                        break;
                    }
                }
            }
        }
    }

    public static void itemCheck(List<Item> items, LivingEntity entity, List<Entity> entities) {
        if (items != null) {
            for (Item item : items) {
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    if (entity.getItemBySlot(slot).getItem().equals(item) && !AwakenedFearCapability.onCooldown(entity)) {
                        AwakenedFearCapability.increaseScared(entity);
                        AwakenedFearCapability.setScaredCooldown(entity, AwakenedFearCapability.getScared(entity) <= 3 ? 6000 : 9000);
                    }
                }
                for (Entity entity1 : entities) {
                    if (entity1 instanceof ItemEntity itemEntity) {
                        if (itemEntity.getItem().getItem().equals(item) && !AwakenedFearCapability.onCooldown(entity)) {
                            AwakenedFearCapability.increaseScared(entity);
                            AwakenedFearCapability.setScaredCooldown(entity, AwakenedFearCapability.getScared(entity) <= 3 ? 6000 : 9000);
                        }
                    }
                }
            }
        }
    }

    public static boolean checkAll(FearTypes fear, List<Entity> entities, LivingEntity entity) {
        if(fear.equals(FearTypes.ALTERNATES)) {
            for (Entity entity1 : entities) {
                if (entity1 instanceof LivingEntity living && !AwakenedFearCapability.onCooldown(entity)) {
                    if (SkillUtils.hasSkill(entity1, skillregistry.ALTERNATE.get())) {
                        AwakenedFearCapability.increaseScared(entity);
                        AwakenedFearCapability.setScaredCooldown(entity, AwakenedFearCapability.getScared(entity) <= 3 ? 6000 : 9000);
                    }
                }
            }
            return false;
        } else if (fear.equals(FearTypes.OCEAN)) {
            Vec3 oceanStart = new Vec3(entity.getX() - 10, entity.getY() - 10, entity.getZ() - 10);
            Vec3 oceanEnd = new Vec3(entity.getX() + 10, entity.getY() + 10, entity.getZ() + 10);
            Iterable<BlockPos> oceanBlocksAround = BlockPos.betweenClosed(new BlockPos(oceanStart), new BlockPos(oceanEnd));
            for (BlockPos blockPos : oceanBlocksAround) {
                if (entity.level.getBlockState(blockPos).getBlock().equals(Blocks.WATER)) {
                    AwakenedFearCapability.increaseScared(entity);
                    AwakenedFearCapability.setScaredCooldown(entity, AwakenedFearCapability.getScared(entity) <= 3 ? 6000 : 9000);
                }
                return false;
            }
            return false;
        } else if (fear.equals(FearTypes.TRUTH)) {
            return false;
        } else if (fear.equals(FearTypes.HEIGHT)) {
            if (entity.getY() >= 200) {
                AwakenedFearCapability.increaseScared(entity);
                AwakenedFearCapability.setScaredCooldown(entity, AwakenedFearCapability.getScared(entity) <= 3 ? 6000 : 9000);
            }
            return false;
        } else {
            return true;
        }
    }
}
