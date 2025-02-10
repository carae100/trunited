package io.github.dracosomething.trawakened.handler;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillHelper;
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
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.race.TensuraRaces;
import io.github.dracosomething.trawakened.api.FearTypes;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.AwakenedFearCapability;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.IFearCapability;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.registry.raceregistry;
import io.github.dracosomething.trawakened.registry.skillregistry;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
import java.util.Objects;

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
            Vec3 vec3 = entity.getEyePosition().subtract(0, 10, 0);
            Vec3 vec31 = entity.getEyePosition();
            if (entity.getYHeadRot() >= -45.0F && entity.getYHeadRot() <= 45.0F) {
                vec31 = vec31.add(5, 10, 10);
                vec3 = vec3.subtract(5, 0,0);
            } else if (entity.getYHeadRot() >= 45.1F && entity.getYHeadRot() <= 135.0F) {
                vec31 = vec31.add(0, 10, 5);
                vec31 = vec31.subtract(10, 0, 0);
                vec3 = vec3.subtract(0,0,5);
            } else if (entity.getYHeadRot() >= -135F && entity.getYHeadRot() <= -45.1F) {
                vec31 = vec31.add(10, 10, 5);
                vec3 = vec3.subtract(0,0,5);
            } else if(entity.getYHeadRot() >= 135.1F && entity.getYHeadRot() <= 180F) {
                vec31 = vec31.add(5, 10, 0);
                vec31 = vec31.subtract(0,0,10);
                vec3 = vec3.subtract(5,0,0);
            } else if (entity.getYHeadRot() <= -135.1F && entity.getYHeadRot() >= -180F) {
                vec31 = vec31.add(5, 10, 0);
                vec31 = vec31.subtract(0,0,10);
                vec3 = vec3.subtract(5,0,0);
            }
            AABB sight = new AABB(vec3, vec31);
            Iterable<BlockPos> blocksInSight = BlockPos.betweenClosed(new BlockPos(vec3), new BlockPos(vec31));
            List<Entity> entities = entity.level.getEntities(entity, sight);
            if(fear.equals(FearTypes.ALTERNATES)) {
                for (Entity entity1 : entities) {
                    if (entity1 instanceof LivingEntity living && !AwakenedFearCapability.onCooldown(entity)) {
                        if (SkillUtils.hasSkill(entity1, skillregistry.ALTERNATE.get())) {
                            AwakenedFearCapability.increaseScared(entity);
                            AwakenedFearCapability.setScaredCooldown(entity, AwakenedFearCapability.getScared(entity) <= 3 ? 6000 : 9000);
                        }
                    }
                }
            } else if (fear.equals(FearTypes.OCEAN)) {
                Vec3 oceanStart = new Vec3(entity.getX() - 10, entity.getY() - 10, entity.getZ() - 10);
                Vec3 oceanEnd = new Vec3(entity.getX() + 10, entity.getY() + 10, entity.getZ() + 10);
                Iterable<BlockPos> oceanBlocksAround = BlockPos.betweenClosed(new BlockPos(oceanStart), new BlockPos(oceanEnd));
                oceanBlocksAround.forEach((blockPos) -> {
                    if (entity.level.getBlockState(blockPos).getBlock().equals(Blocks.WATER)) {
                        AwakenedFearCapability.increaseScared(entity);
                        AwakenedFearCapability.setScaredCooldown(entity, AwakenedFearCapability.getScared(entity) <= 3 ? 6000 : 9000);
                    }
                });
            } else if (fear.equals(FearTypes.TRUTH)) {
                return;
            } else if (fear.equals(FearTypes.HEIGHT)) {
                if (entity.getY() >= 200) {
                    AwakenedFearCapability.increaseScared(entity);
                    AwakenedFearCapability.setScaredCooldown(entity, AwakenedFearCapability.getScared(entity) <= 3 ? 6000 : 9000);
                }
            } else {
                if (mobEffects != null) {
                    for (MobEffect effect : mobEffects) {
                        if (entity.hasEffect(effect) && !AwakenedFearCapability.onCooldown(entity) && !AwakenedFearCapability.onCooldown(entity)) {
                            AwakenedFearCapability.increaseScared(entity);
                            AwakenedFearCapability.setScaredCooldown(entity, AwakenedFearCapability.getScared(entity) <= 3 ? 6000 : 9000);
                        }
                    }
                }
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
        } else {
            AwakenedFearCapability.decreaseCooldown(entity);
        }
//        FearTypes.fearPenalty(entity);
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

    @Nullable
    public static <T> T getCapability(Entity entity, Capability<T> capability) {
        return entity.getCapability(capability).isPresent() ? entity.getCapability(capability).orElseThrow(() -> {
            return new IllegalArgumentException("Lazy optional must not be empty");
        }) : null;
    }
}
