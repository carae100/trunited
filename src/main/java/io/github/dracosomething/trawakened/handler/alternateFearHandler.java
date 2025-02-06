package io.github.dracosomething.trawakened.handler;

import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.capability.effects.ITensuraEffectsCapability;
import com.github.manasmods.tensura.capability.effects.TensuraEffectsCapability;
import com.github.manasmods.tensura.capability.ep.ITensuraEPCapability;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.capability.magicule.MagiculeChunkCapability;
import com.github.manasmods.tensura.capability.magicule.MagiculeChunkCapabilityImpl;
import com.github.manasmods.tensura.capability.race.ITensuraPlayerCapability;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.capability.skill.ITensuraSkillCapability;
import com.github.manasmods.tensura.capability.skill.TensuraSkillCapability;
import com.github.manasmods.tensura.capability.smithing.ISmithingCapability;
import com.github.manasmods.tensura.capability.smithing.SmithingCapability;
import com.github.manasmods.tensura.entity.human.CloneEntity;
import com.github.manasmods.tensura.race.Race;
import com.github.manasmods.tensura.registry.race.TensuraRaces;
import io.github.dracosomething.trawakened.api.FearTypes;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.AwakenedFearCapability;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.IFearCapability;
import io.github.dracosomething.trawakened.registry.raceregistry;
import io.github.dracosomething.trawakened.registry.skillregistry;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber(modid = trawakened.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class alternateFearHandler {
    public alternateFearHandler() {
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IFearCapability.class);
    }

    @SubscribeEvent
    static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        AwakenedFearCapability.sync(event.getEntity());
        System.out.println( AwakenedFearCapability.getFearType(event.getEntity()));
    }

    @SubscribeEvent
    static void onPlayerTrack(PlayerEvent.StartTracking e) {
        Entity var2 = e.getTarget();
        if (var2 instanceof LivingEntity living) {
            AwakenedFearCapability.sync(living);
            AwakenedFearCapability.sync(e.getEntity());
        }
    }

    @SubscribeEvent
    static void onPlayerClone(PlayerEvent.Clone e) {
        e.getOriginal().reviveCaps();
        AwakenedFearCapability.getFrom(e.getOriginal()).ifPresent((oldData) -> {
            AwakenedFearCapability.getFrom(e.getEntity()).ifPresent((data) -> {
                data.deserializeNBT((CompoundTag)oldData.serializeNBT());
            });
        });
        e.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent e) {
        AwakenedFearCapability.sync(e.getEntity());
    }

    @SubscribeEvent
    static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent e) {
        AwakenedFearCapability.sync(e.getEntity());
    }

    @SubscribeEvent
    static void onTickLiving(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (!AwakenedFearCapability.onCooldown(entity)) {
            FearTypes fear = AwakenedFearCapability.getFearType(entity);
            List<Block> blocks = fear.getBlock();
            List<Item> items = fear.getItem();
            List<EntityType<?>> entityTypes = fear.getEntity();
            List<MobEffect> mobEffects = fear.getEffect();
            Vec3 vec3 = entity.getEyePosition(1).add(0, -3, 0);
            Vec3 vec31 = entity.getEyePosition();
            if (entity.getYRot() >= 0 && entity.getYRot() <= 90) {
                vec31.add(0, 10, 0);
                System.out.println(vec3);
            } else if (entity.getYRot() >= 91 && entity.getYRot() <= 180) {
                System.out.println(vec3);
            } else if (entity.getYRot() >= 181 && entity.getYRot() <= 270) {
                System.out.println(vec3);
            } else if (entity.getYRot() >= 271 && entity.getYRot() <= 360) {
                System.out.println(vec3);
            }
            AABB sight = new AABB(vec3, vec31);
            Iterable<BlockPos> blocksInSight = BlockPos.betweenClosed(new BlockPos(vec3), new BlockPos(vec31));
            List<Entity> entities = entity.level.getEntities(entity, sight, Entity::isAlive);
            if(fear.equals(FearTypes.ALTERNATES)) {
                for (Entity entity1 : entities) {
                    if (entity1 instanceof LivingEntity living) {
                        if (SkillUtils.hasSkill(entity1, skillregistry.STARKILL.get())) {
                            AwakenedFearCapability.increaseScared(entity);
                            AwakenedFearCapability.setScaredCooldown(entity, AwakenedFearCapability.getScared(entity) == 3 ? 6000 : 18000);
                        }
                    }
                }
            } else if (fear.equals(FearTypes.OCEAN)) {
                blocksInSight.forEach((blockPos) -> {
                    if (entity.level.getBlockState(blockPos).getBlock().equals(Blocks.WATER)) {
                        AwakenedFearCapability.increaseScared(entity);
                        AwakenedFearCapability.setScaredCooldown(entity, AwakenedFearCapability.getScared(entity) == 3 ? 6000 : 18000);
                    }
                });
            } else if (fear.equals(FearTypes.TRUTH)) {
                return;
            } else {
                if (mobEffects != null) {
                    for (MobEffect effect : mobEffects) {
                        if (entity.hasEffect(effect)) {
                            AwakenedFearCapability.increaseScared(entity);
                            AwakenedFearCapability.setScaredCooldown(entity, AwakenedFearCapability.getScared(entity) == 3 ? 6000 : 18000);
                        }
                    }
                }
                if (items != null) {
                    for (Item item : items) {
                        for (EquipmentSlot slot : EquipmentSlot.values()) {
                            if (entity.getItemBySlot(slot).getItem().equals(item)) {
                                AwakenedFearCapability.increaseScared(entity);
                                AwakenedFearCapability.setScaredCooldown(entity, AwakenedFearCapability.getScared(entity) == 3 ? 6000 : 18000);
                            }
                        }
                        for (Entity entity1 : entities) {
                            if (entity1 instanceof ItemEntity itemEntity) {
                                if (itemEntity.getItem().getItem().equals(item)) {
                                    AwakenedFearCapability.increaseScared(entity);
                                    AwakenedFearCapability.setScaredCooldown(entity, AwakenedFearCapability.getScared(entity) == 3 ? 6000 : 18000);
                                }
                            }
                        }
                    }
                }
                if (blocks != null) {
                    for (Block block : blocks) {
                        blocksInSight.forEach((blockPos) -> {
                            if (entity.level.getBlockState(blockPos).getBlock().equals(block)) {
                                AwakenedFearCapability.increaseScared(entity);
                                AwakenedFearCapability.setScaredCooldown(entity, AwakenedFearCapability.getScared(entity) == 3 ? 6000 : 18000);
                            }
                        });
                    }
                }
                if (entityTypes != null) {
                    for (EntityType<?> entityType : entityTypes) {
                        for (Entity entity1 : entities) {
                            if (entity1.getType().equals(entityType)) {
                                AwakenedFearCapability.increaseScared(entity);
                                AwakenedFearCapability.setScaredCooldown(entity, AwakenedFearCapability.getScared(entity) == 3 ? 6000 : 18000);
                            }
                        }
                    }
                }
            }
        } else {
            AwakenedFearCapability.decreaseCooldown(entity);
        }
    }

    @Nullable
    public static <T> T getCapability(Entity entity, Capability<T> capability) {
        return entity.getCapability(capability).isPresent() ? entity.getCapability(capability).orElseThrow(() -> {
            return new IllegalArgumentException("Lazy optional must not be empty");
        }) : null;
    }
}
