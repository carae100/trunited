package io.github.dracosomething.trawakened.helper;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import io.github.dracosomething.trawakened.event.FearActivateEvent;
import io.github.dracosomething.trawakened.event.FearEvent;
import io.github.dracosomething.trawakened.event.FearPenaltyEvent;
import io.github.dracosomething.trawakened.library.FearTypes;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.AwakenedFearCapability;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.registry.skillRegistry;
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
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

public class FearHelper extends SightHelper{
    public static void fearPenalty(LivingEntity entity, Integer scared) {
        if (MathHelper.isBetween(scared, 5, 10)) {
            addSingularPenaltyEffecy(entity, TensuraMobEffects.FEAR.get(), 1);
        } else if (MathHelper.isBetween(scared, 10, 15)) {
            addSingularPenaltyEffecy(entity, TensuraMobEffects.FEAR.get(), 2);
        } else if (MathHelper.isBetween(scared, 15, 20)) {
            addSingularPenaltyEffecy(entity, TensuraMobEffects.FEAR.get(), 3);
        } else if (MathHelper.isBetween(scared, 20, 25)) {
            addSingularPenaltyEffecy(entity, TensuraMobEffects.FEAR.get(), 4);
        } else if (MathHelper.isBetween(scared, 25, 30)) {
            addSingularPenaltyEffecy(entity, TensuraMobEffects.FEAR.get(), 5);
            addSingularPenaltyEffecy(entity, MobEffects.DIG_SLOWDOWN, 1);
        } else if (MathHelper.isBetween(scared, 30, 35)) {
            addSingularPenaltyEffecy(entity, TensuraMobEffects.FEAR.get(), 5);
            addSingularPenaltyEffecy(entity, MobEffects.DIG_SLOWDOWN, 3);
        } else if (MathHelper.isBetween(scared, 35, 40)) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN), 5);
        } else if (MathHelper.isBetween(scared, 40, 45)) {
            addSingularPenaltyEffecy(entity, TensuraMobEffects.FEAR.get(), 7);
            addSingularPenaltyEffecy(entity, MobEffects.DIG_SLOWDOWN, 5);
        } else if (MathHelper.isBetween(scared, 45, 50)) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN), 7);
        } else if (scared.equals(50)) {
            decreaseSHP(entity, 10);
        } else if (MathHelper.isBetween(scared, 50, 55)) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN), 7);
            addSingularPenaltyEffecy(entity, MobEffects.WEAKNESS, 1);
        } else if (MathHelper.isBetween(scared, 55, 60)) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN), 7);
            addSingularPenaltyEffecy(entity, MobEffects.WEAKNESS, 4);
        } else if (MathHelper.isBetween(scared, 60, 65)) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS), 7);
        } else if (MathHelper.isBetween(scared, 65, 70)) {
            addSingularPenaltyEffecy(entity, TensuraMobEffects.FEAR.get(), 10);
            addFearPenaltyEffect(entity, List.of(MobEffects.WEAKNESS, MobEffects.DIG_SLOWDOWN), 7);
        } else if (MathHelper.isBetween(scared, 70, 75)) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN), 10);
            addSingularPenaltyEffecy(entity, MobEffects.WEAKNESS, 7);
        } else if (scared.equals(75)) {
            decreaseSHP(entity, 25);
        } else if (MathHelper.isBetween(scared, 75, 80)) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS), 10);
            addSingularPenaltyEffecy(entity, TensuraMobEffects.PARALYSIS.get(), 1);
        } else if (MathHelper.isBetween(scared, 80, 85)) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS), 10);
            addSingularPenaltyEffecy(entity, TensuraMobEffects.PARALYSIS.get(), 5);
        } else if (MathHelper.isBetween(scared, 85, 90)) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS, TensuraMobEffects.PARALYSIS.get()), 10);
        } else if (MathHelper.isBetween(scared, 90, 95)) {
            addSingularPenaltyEffecy(entity, TensuraMobEffects.FEAR.get(), 15);
            addFearPenaltyEffect(entity, List.of(MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS, TensuraMobEffects.PARALYSIS.get()), 10);
        } else if (MathHelper.isBetween(scared, 95, 100)) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.PARALYSIS.get(), MobEffects.DIG_SLOWDOWN), 15);
            addFearPenaltyEffect(entity, List.of(MobEffects.WEAKNESS, TensuraMobEffects.PARALYSIS.get()), 10);
        } else if (scared.equals(100)) {
            decreaseSHP(entity, 55);
        } else if (MathHelper.isBetween(scared, 100, 105)) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, TensuraMobEffects.PARALYSIS.get(), MobEffects.WEAKNESS), 15);
            addSingularPenaltyEffecy(entity, TensuraMobEffects.CHILL.get(), 1);
        } else if (MathHelper.isBetween(scared, 105, 110)) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, TensuraMobEffects.PARALYSIS.get(), MobEffects.WEAKNESS), 15);
            addSingularPenaltyEffecy(entity, TensuraMobEffects.CHILL.get(), 8);
        } else if (MathHelper.isBetween(scared, 110, 115)) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), TensuraMobEffects.CHILL.get(), MobEffects.DIG_SLOWDOWN, TensuraMobEffects.PARALYSIS.get(), MobEffects.WEAKNESS), 15);
        } else if (MathHelper.isBetween(scared, 115, 120)) {
            addSingularPenaltyEffecy(entity, TensuraMobEffects.FEAR.get(), 22);
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.CHILL.get(), MobEffects.DIG_SLOWDOWN, TensuraMobEffects.PARALYSIS.get(), MobEffects.WEAKNESS), 15);
        } else if (MathHelper.isBetween(scared, 120, 125)) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN), 22);
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.CHILL.get(), TensuraMobEffects.PARALYSIS.get(), MobEffects.WEAKNESS), 15);
        } else if (scared.equals(125)) {
            decreaseSHP(entity, 75);
        } else if (MathHelper.isBetween(scared, 125, 130)) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS), 22);
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.CHILL.get(), TensuraMobEffects.PARALYSIS.get()), 15);
            addFearPenaltyEffect(entity, List.of(MobEffects.WITHER), 1);
        } else if (MathHelper.isBetween(scared, 130, 135)) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS, TensuraMobEffects.PARALYSIS.get()), 22);
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.CHILL.get()), 15);
            addFearPenaltyEffect(entity, List.of(MobEffects.WITHER), 8);
        } else if (MathHelper.isBetween(scared, 135, 140)) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS, TensuraMobEffects.PARALYSIS.get()), 22);
            addFearPenaltyEffect(entity, List.of(MobEffects.WITHER, TensuraMobEffects.CHILL.get()), 15);
        } else if (MathHelper.isBetween(scared, 140, 145)) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS, TensuraMobEffects.PARALYSIS.get(), TensuraMobEffects.CHILL.get()), 22);
            addSingularPenaltyEffecy(entity, MobEffects.WITHER, 15);
        } else if (MathHelper.isBetween(scared, 145, 150)) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS, TensuraMobEffects.PARALYSIS.get(), TensuraMobEffects.CHILL.get(), MobEffects.WITHER), 22);
        } else if (scared.equals(150)) {
            decreaseSHP(entity, 105);
        } else if (MathHelper.isAboveOrEqualTo(scared, 150)) {
            addFearPenaltyEffect(entity, List.of(TensuraMobEffects.FEAR.get(), MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS, TensuraMobEffects.PARALYSIS.get(), TensuraMobEffects.CHILL.get(), MobEffects.WITHER), 22);
            addSingularPenaltyEffecy(entity, effectRegistry.BRAINDAMAGE.get(), 5);
        }
    }


    public static void addSingularPenaltyEffecy (LivingEntity entity, MobEffect effect, int amplifier) {
        addFearPenaltyEffect(entity, List.of(effect), amplifier);
    }

    public static void addFearPenaltyEffect(LivingEntity entity, List<MobEffect> mobEffects, int amplifier) {
        FearPenaltyEvent.FearPenaltyEffectEvent.Pre preEvent = new FearPenaltyEvent.FearPenaltyEffectEvent.Pre(AwakenedFearCapability.getFearType(entity), entity, mobEffects);
        if (!MinecraftForge.EVENT_BUS.post(preEvent)) {
            FearPenaltyEvent.FearPenaltyEffectEvent event = new FearPenaltyEvent.FearPenaltyEffectEvent(AwakenedFearCapability.getFearType(entity), entity, mobEffects);
            MinecraftForge.EVENT_BUS.post(event);
            for (MobEffect mobEffect : mobEffects) {
                SkillHelper.addEffectWithSource(entity, entity, mobEffect, 5000, amplifier, false, false, false, false);
            }
            FearPenaltyEvent.FearPenaltyEffectEvent.Post postEvent = new FearPenaltyEvent.FearPenaltyEffectEvent.Post(AwakenedFearCapability.getFearType(entity), entity, mobEffects);
            MinecraftForge.EVENT_BUS.post(postEvent);
        }
    }

    public static void decreaseSHP(LivingEntity entity, int amount) {
        FearPenaltyEvent.FearPenaltySHPEvent.Pre preEvent = new FearPenaltyEvent.FearPenaltySHPEvent.Pre(AwakenedFearCapability.getFearType(entity), entity, amount, entity.getAttributeBaseValue(TensuraAttributeRegistry.MAX_SPIRITUAL_HEALTH.get()));
        if (!MinecraftForge.EVENT_BUS.post(preEvent)) {
            FearPenaltyEvent.FearPenaltySHPEvent event = new FearPenaltyEvent.FearPenaltySHPEvent(AwakenedFearCapability.getFearType(entity), entity, amount);
            MinecraftForge.EVENT_BUS.post(event);
            if (entity.getAttributes().getInstance(TensuraAttributeRegistry.MAX_SPIRITUAL_HEALTH.get()) != null) {
                entity.getAttributes().getInstance(TensuraAttributeRegistry.MAX_SPIRITUAL_HEALTH.get()).setBaseValue(entity.getAttributeBaseValue(TensuraAttributeRegistry.MAX_SPIRITUAL_HEALTH.get()) - amount);
            }
            FearPenaltyEvent.FearPenaltySHPEvent.Post postEvent = new FearPenaltyEvent.FearPenaltySHPEvent.Post(AwakenedFearCapability.getFearType(entity), entity, amount, entity.getAttributeBaseValue(TensuraAttributeRegistry.MAX_SPIRITUAL_HEALTH.get()));
            MinecraftForge.EVENT_BUS.post(postEvent);
            AwakenedFearCapability.increaseScared(entity);
            AwakenedFearCapability.setScaredCooldown(entity, 9000);
        }
    }

    public static void resetData(LivingEntity entity) {
        if (entity.isDeadOrDying()) {
            if (entity.getPersistentData().hasUUID("alternate_UUID")) {
                ManasSkillInstance instance = SkillUtils.getSkillOrNull(entity.level.getPlayerByUUID(entity.getPersistentData().getUUID("alternate_UUID")), skillRegistry.ALTERNATE.get());
                if (instance != null) {
                    CompoundTag tag = instance.getOrCreateTag();
                    tag.putInt("original_scared", 0);
                    tag.putBoolean("is_locked", false);
                }
            }
        }
    }

    public static void fearUpdates(LivingEntity entity) {
        // checks if fear isn't on cooldown
        if (!AwakenedFearCapability.onCooldown(entity)) {
            // gets all necessary data from the fear
            FearTypes fear = AwakenedFearCapability.getFearType(entity);
            List<Block> blocks = fear.getBlock();
            List<Item> items = fear.getItem();
            List<EntityType<?>> entityTypes = fear.getEntity();
            List<MobEffect> mobEffects = fear.getEffect();
            // gets the 2 sight positions
            Vec3 vec3 = getSightPos1(entity);
            Vec3 vec31 = getSightPos2(entity);
            // gets the line of sight
            AABB sight = getLineOfSight(entity, vec3, vec31);
            // gets the blocks in sight
            Iterable<BlockPos> blocksInSight = getBlocksInSight(vec3, vec31);
            // gets the entities in sight
            List<Entity> entities = getEntitiesInSight(entity, sight);
            // checks if the fear is not one of the special fears
            if (checkAll(fear, entities, entity)) {
                // does the checks
                effectCheck(mobEffects, entity);
                entityCheck(entityTypes, entity, entities);
                blockCheck(blocks, entity, blocksInSight);
                itemCheck(items, entity, entities);
            }
            FearEvent event = new FearEvent(AwakenedFearCapability.getFearType(entity), entity);
            MinecraftForge.EVENT_BUS.post(event);
        } else {
            // decreases cooldown every second
            AwakenedFearCapability.decreaseCooldown(entity);
            FearEvent event = new FearEvent(AwakenedFearCapability.getFearType(entity), entity);
            MinecraftForge.EVENT_BUS.post(event);
        }
    }

    public static void effectCheck(List<MobEffect> mobEffects, LivingEntity entity) {
        if (mobEffects != null) {
            for (MobEffect effect : mobEffects) {
                if (entity.hasEffect(effect) && !AwakenedFearCapability.onCooldown(entity) && !AwakenedFearCapability.onCooldown(entity)) {
                    ActivateScared(entity);
                }
            }
        }
    }

    public static void entityCheck(List<EntityType<?>> entityTypes, LivingEntity entity, List<Entity> entities) {
        if (entityTypes != null) {
            for (EntityType<?> entityType : entityTypes) {
                for (Entity entity1 : entities) {
                    if (entity1.getType().equals(entityType) && !AwakenedFearCapability.onCooldown(entity)) {
                        ActivateScared(entity);
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
                        ActivateScared(entity);
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
                        ActivateScared(entity);
                    }
                }
                for (Entity entity1 : entities) {
                    if (entity1 instanceof ItemEntity itemEntity) {
                        if (itemEntity.getItem().getItem().equals(item) && !AwakenedFearCapability.onCooldown(entity)) {
                            ActivateScared(entity);
                        }
                    }
                }
            }
        }
    }

    public static boolean checkAll(FearTypes fear, List<Entity> entities, LivingEntity entity) {
        switch (fear) {
            case ALTERNATES:
                for (Entity entity1 : entities) {
                    if (entity1 instanceof LivingEntity living && !AwakenedFearCapability.onCooldown(entity)) {
                        if (SkillUtils.hasSkill(entity1, skillRegistry.ALTERNATE.get())) {
                            ActivateScared(entity);
                        }
                    }
                }
                return false;
            case OCEAN:
                Vec3 oceanStart = new Vec3(entity.getX() - 10, entity.getY(), entity.getZ() - 10);
                Vec3 oceanEnd = new Vec3(entity.getX() + 10, entity.getY() + 20, entity.getZ() + 10);
                Iterable<BlockPos> oceanBlocksAround = BlockPos.betweenClosed(new BlockPos(oceanStart), new BlockPos(oceanEnd));
                for (BlockPos blockPos : oceanBlocksAround) {
                    if (entity.level.getBlockState(blockPos).getBlock().equals(Blocks.WATER)) {
                        ActivateScared(entity);
                    }
                    return false;
                }
                return false;
            case TRUTH:
                return false;
            case HEIGHT:
                if (entity.getY() >= 200) {
                    ActivateScared(entity);
                }
                return false;
            default:
                return true;
        }
    }

    private static void ActivateScared(LivingEntity entity) {
        int oldScared = AwakenedFearCapability.getScared(entity);
        FearActivateEvent event = new FearActivateEvent(AwakenedFearCapability.getFearType(entity), entity, oldScared, AwakenedFearCapability.getScared(entity));
        if (!MinecraftForge.EVENT_BUS.post(event)) {
            AwakenedFearCapability.increaseScared(entity);
            AwakenedFearCapability.setScaredCooldown(entity, AwakenedFearCapability.getScared(entity) <= 3 ? 6000 : 9000);
        }
    }
}
