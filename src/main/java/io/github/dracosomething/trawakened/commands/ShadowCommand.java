package io.github.dracosomething.trawakened.commands;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import io.github.dracosomething.trawakened.registry.items.*;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.util.PermissionHelper;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.dracosomething.trawakened.ability.skill.ultimate.ShadowMonarch;
import io.github.dracosomething.trawakened.capability.ShadowCapability.AwakenedShadowCapability;
import io.github.dracosomething.trawakened.commands.argument.rankArgument;
import io.github.dracosomething.trawakened.helper.skillHelper;
import io.github.dracosomething.trawakened.library.shadowRank;
import io.github.dracosomething.trawakened.registry.permisionRegistry;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Mod.EventBusSubscriber(
        modid = "trawakened",
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class ShadowCommand {

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("shadow")
                .requires((commandSource) ->
                        PermissionHelper.hasPermissonAndIsPlayer(
                                commandSource,
                                permisionRegistry.PLAYER_HAS_SHADOWMONARCH
                        )
                )
                .then(Commands.literal("summon")
                        .then(Commands.literal("rank")
                                .then(Commands.argument("rank", rankArgument.rank())
                                        .executes((context) -> {
                                            ServerPlayer player =(ServerPlayer) context.getSource().getEntity();
                                            ManasSkillInstance instance = SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SHADOW_MONARCH.get()).get();
                                            if (instance.getSkill() instanceof ShadowMonarch skill) {
                                                List<String> validTargets = skill.getShadowStorage().getAllKeys().stream().filter((shadow) -> {
                                                    return shadowRank.fromNBT(skill.getShadowStorage().getCompound(shadow).getCompound("rank")) == rankArgument.getRank(context, "rank");
                                                }).toList();
                                                if (validTargets.isEmpty()) {
                                                    player.sendSystemMessage(Component.translatable("trawakened.command.shadow.summon.empty"));
                                                    return 0;
                                                }
                                                validTargets.forEach((shadow) -> {
                                                    EntityType<?> type = EntityType.byString(skill.getShadowStorage().getCompound(shadow).getString("entityType")).get();
                                                    LivingEntity entity = (LivingEntity) type.create(player.level);
                                                    entity.deserializeNBT(skill.getShadowStorage().getCompound(shadow).getCompound("EntityData"));
                                                    entity.setPos(player.position());
                                                    entity.addEffect(new MobEffectInstance(MobEffects.GLOWING));
                                                    player.level.addFreshEntity(entity);
                                                    skill.getShadowStorage().remove(shadow);
                                                });
                                                return 1;
                                            }
                                            return 0;
                                        })
                                )
                        )
                        .then(Commands.literal("top")
                                .then(Commands.argument("number", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            ServerPlayer player =(ServerPlayer) context.getSource().getEntity();
                                            ManasSkillInstance instance = SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SHADOW_MONARCH.get()).get();
                                            if (instance.getSkill() instanceof ShadowMonarch skill) {
                                                List<String> validTargets = skill.getShadowStorage().getAllKeys().stream().filter(shadow -> {
                                                    return skill.getShadowStorage().getAllKeys().stream().toList().indexOf(shadow) < IntegerArgumentType.getInteger(context, "number");
                                                }).toList();
                                                if (validTargets.isEmpty()) {
                                                    player.sendSystemMessage(Component.translatable("trawakened.command.shadow.summon.empty"));
                                                    return 0;
                                                }
                                                validTargets.forEach((shadow) -> {
                                                    EntityType<?> type = EntityType.byString(skill.getShadowStorage().getCompound(shadow).getString("entityType")).get();
                                                    LivingEntity entity = (LivingEntity) type.create(player.level);
                                                    entity.deserializeNBT(skill.getShadowStorage().getCompound(shadow).getCompound("EntityData"));
                                                    entity.setPos(player.position());
                                                    entity.addEffect(new MobEffectInstance(MobEffects.GLOWING));
                                                    player.level.addFreshEntity(entity);
                                                    skill.getShadowStorage().remove(shadow);
                                                });
                                                return 1;
                                            }
                                            return 0;
                                        })
                                )
                        )
                        .then(Commands.literal("above")
                                .then(Commands.argument("number", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            ServerPlayer player =(ServerPlayer) context.getSource().getEntity();
                                            ManasSkillInstance instance = SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SHADOW_MONARCH.get()).get();
                                            if (instance.getSkill() instanceof ShadowMonarch skill) {
                                                List<String> validTargets = skill.getShadowStorage().getAllKeys().stream().filter(shadow -> {
                                                    return skill.getShadowStorage().getCompound(shadow).getDouble("EP") >= IntegerArgumentType.getInteger(context, "number");
                                                }).toList();
                                                if (validTargets.isEmpty()) {
                                                    player.sendSystemMessage(Component.translatable("trawakened.command.shadow.summon.empty"));
                                                    return 0;
                                                }
                                                validTargets.forEach((shadow) -> {
                                                    EntityType<?> type = EntityType.byString(skill.getShadowStorage().getCompound(shadow).getString("entityType")).get();
                                                    LivingEntity entity = (LivingEntity) type.create(player.level);
                                                    entity.deserializeNBT(skill.getShadowStorage().getCompound(shadow).getCompound("EntityData"));
                                                    entity.setPos(player.position());
                                                    entity.addEffect(new MobEffectInstance(MobEffects.GLOWING));
                                                    player.level.addFreshEntity(entity);
                                                    skill.getShadowStorage().remove(shadow);
                                                });
                                                return 1;
                                            }
                                            return 0;
                                        })
                                )
                        )
                        .then(Commands.literal("name")
                                .then(Commands.argument("name", StringArgumentType.string())
                                        .executes(context -> {
                                            ServerPlayer player =(ServerPlayer) context.getSource().getEntity();
                                            if (StringArgumentType.getString(context, "name").isEmpty()) {
                                                player.sendSystemMessage(Component.translatable("trawakened.command.shadow.summon.empty"));
                                                return 0;
                                            }
                                            ManasSkillInstance instance = SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SHADOW_MONARCH.get()).get();
                                            if (instance.getSkill() instanceof ShadowMonarch skill) {
                                                List<String> validTargets = skill.getShadowStorage().getAllKeys().stream().filter(shadow -> {
                                                    return skill.getShadowStorage().getCompound(shadow).getString("name").equals(StringArgumentType.getString(context, "name"));
                                                }).toList();
                                                if (validTargets.isEmpty()) {
                                                    player.sendSystemMessage(Component.translatable("trawakened.command.shadow.summon.empty"));
                                                    return 0;
                                                }
                                                validTargets.forEach((shadow) -> {
                                                    EntityType<?> type = EntityType.byString(skill.getShadowStorage().getCompound(shadow).getString("entityType")).get();
                                                    LivingEntity entity = (LivingEntity) type.create(player.level);
                                                    entity.deserializeNBT(skill.getShadowStorage().getCompound(shadow).getCompound("EntityData"));
                                                    entity.setPos(player.position());
                                                    entity.addEffect(new MobEffectInstance(MobEffects.GLOWING));
                                                    player.level.addFreshEntity(entity);
                                                    skill.getShadowStorage().remove(shadow);
                                                });
                                                return 1;
                                            }
                                            return 0;
                                        })
                                )
                        )
                )
                .then(Commands.literal("dismiss")
                        .then(Commands.literal("weakest")
                                .then(Commands.argument("number", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            ServerPlayer player =(ServerPlayer) context.getSource().getEntity();
                                            ManasSkillInstance instance = SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SHADOW_MONARCH.get()).get();
                                            if (instance.getSkill() instanceof ShadowMonarch skill) {
                                                WorldBorder border = player.level.getWorldBorder();
                                                AABB world = new AABB(border.getMaxX(), player.level.getMaxBuildHeight(), border.getMaxZ(), border.getMinX(), player.level.getMinBuildHeight(), border.getMinX());
                                                AtomicInteger i = new AtomicInteger();
                                                List<Entity> list = player.level.getEntities(player, world, (entity -> {
                                                    if (entity instanceof LivingEntity living) {
                                                        if (AwakenedShadowCapability.isShadow(living) &&
                                                                AwakenedShadowCapability.isArisen(living) &&
                                                                AwakenedShadowCapability.getOwnerUUID(living).equals(player.getUUID())) {
                                                            System.out.println(i.get());
                                                            System.out.println(IntegerArgumentType.getInteger(context, "number"));
                                                            System.out.println(i.get() <= IntegerArgumentType.getInteger(context, "number"));
                                                            i.getAndIncrement();
                                                            return i.get() <= IntegerArgumentType.getInteger(context, "number");
                                                        }
                                                    }
                                                    return false;
                                                }));
                                                list.forEach(System.out::println);
                                                list.sort(Comparator.comparingDouble(shadow -> TensuraEPCapability.getCurrentEP((LivingEntity) shadow)));
                                                list.forEach(System.out::println);
                                                skillHelper.sendMessageToNearbyPlayersWithSource(30, player, Component.translatable("message.dismiss"));
                                                list.forEach((entity -> {
                                                    if (entity instanceof LivingEntity living) {
                                                        living.discard();
                                                    }
                                                }));
                                                return 1;
                                            }
                                            return 0;
                                        })
                                )
                        )
                        .then(Commands.literal("strongest")
                                .then(Commands.argument("number", IntegerArgumentType.integer())
                                        .executes((context) -> {
                                            ServerPlayer player =(ServerPlayer) context.getSource().getEntity();
                                            ManasSkillInstance instance = SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SHADOW_MONARCH.get()).get();
                                            if (instance.getSkill() instanceof ShadowMonarch skill) {
                                                WorldBorder border = player.level.getWorldBorder();
                                                AABB world = new AABB(border.getMaxX(), player.level.getMaxBuildHeight(), border.getMaxZ(), border.getMinX(), player.level.getMinBuildHeight(), border.getMinX());
                                                AtomicInteger i = new AtomicInteger();
                                                List<Entity> list = player.level.getEntities(player, world, (entity -> {
                                                    if (entity instanceof LivingEntity living) {
                                                        if (AwakenedShadowCapability.isShadow(living) &&
                                                                AwakenedShadowCapability.isArisen(living) &&
                                                                AwakenedShadowCapability.getOwnerUUID(living).equals(player.getUUID())) {
                                                            System.out.println(i.get());
                                                            System.out.println(IntegerArgumentType.getInteger(context, "number"));
                                                            System.out.println(i.get() <= IntegerArgumentType.getInteger(context, "number"));
                                                            i.getAndIncrement();
                                                            return i.get() <= IntegerArgumentType.getInteger(context, "number");
                                                        }
                                                    }
                                                    return false;
                                                }));
                                                list.forEach(System.out::println);
                                                list.sort((shadow, living) -> {
                                                    System.out.println(Double.compare(TensuraEPCapability.getCurrentEP((LivingEntity) living), TensuraEPCapability.getCurrentEP((LivingEntity) shadow)));
                                                    return Double.compare(TensuraEPCapability.getCurrentEP((LivingEntity) living), TensuraEPCapability.getCurrentEP((LivingEntity) shadow));
                                                });
                                                list.forEach(System.out::println);
                                                skillHelper.sendMessageToNearbyPlayersWithSource(30, player, Component.translatable("message.dismiss"));
                                                list.forEach((entity -> {
                                                    if (entity instanceof LivingEntity living) {
                                                        living.discard();
                                                    }
                                                }));
                                                return 1;
                                            }
                                            return 0;
                                        })
                                )
                        )
                        .then(Commands.argument("rank", rankArgument.rank())
                                .then(Commands.argument("number", IntegerArgumentType.integer())
                                        .executes((context) -> {
                                            ServerPlayer player =(ServerPlayer) context.getSource().getEntity();
                                            ManasSkillInstance instance = SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SHADOW_MONARCH.get()).get();
                                            if (instance.getSkill() instanceof ShadowMonarch skill) {
                                                WorldBorder border = player.level.getWorldBorder();
                                                AABB world = new AABB(border.getMaxX(), player.level.getMaxBuildHeight(), border.getMaxZ(), border.getMinX(), player.level.getMinBuildHeight(), border.getMinX());
                                                AtomicInteger i = new AtomicInteger();
                                                List<Entity> list = player.level.getEntities(player, world, (entity -> {
                                                    if (entity instanceof LivingEntity living) {
                                                        if (AwakenedShadowCapability.isShadow(living) &&
                                                                AwakenedShadowCapability.isArisen(living) &&
                                                                AwakenedShadowCapability.getOwnerUUID(living).equals(player.getUUID())) {
                                                            System.out.println(i.get());
                                                            System.out.println(IntegerArgumentType.getInteger(context, "number"));
                                                            System.out.println(i.get() <= IntegerArgumentType.getInteger(context, "number"));
                                                            i.getAndIncrement();
                                                            return i.get() <= IntegerArgumentType.getInteger(context, "number") &&
                                                                    AwakenedShadowCapability.getRank(living).equals(rankArgument.getRank(context, "rank"));
                                                        }
                                                    }
                                                    return false;
                                                }));
                                                skillHelper.sendMessageToNearbyPlayersWithSource(30, player, Component.translatable("message.dismiss"));
                                                list.forEach((entity -> {
                                                    if (entity instanceof LivingEntity living) {
                                                        living.discard();
                                                    }
                                                }));
                                                return 1;
                                            }
                                            return 0;
                                        })
                                )
                        )
                        .then(Commands.argument("name", StringArgumentType.string())
                                .executes((context) -> {
                                    ServerPlayer player =(ServerPlayer) context.getSource().getEntity();
                                    ManasSkillInstance instance = SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SHADOW_MONARCH.get()).get();
                                    if (instance.getSkill() instanceof ShadowMonarch skill) {
                                        WorldBorder border = player.level.getWorldBorder();
                                        AABB world = new AABB(border.getMaxX(), player.level.getMaxBuildHeight(), border.getMaxZ(), border.getMinX(), player.level.getMinBuildHeight(), border.getMinX());
                                        List<Entity> list = player.level.getEntities(player, world, (entity -> {
                                            return entity instanceof LivingEntity living &&
                                                    AwakenedShadowCapability.isShadow(living) &&
                                                    AwakenedShadowCapability.isArisen(living) &&
                                                    AwakenedShadowCapability.getOwnerUUID(living).equals(player.getUUID()) &&
                                                    living.getDisplayName().getString().equals(StringArgumentType.getString(context, "name"));
                                        }));
                                        skillHelper.sendMessageToNearbyPlayersWithSource(30, player, Component.translatable("message.dismiss"));
                                        list.forEach((entity -> {
                                            if (entity instanceof LivingEntity living) {
                                                living.discard();
                                            }
                                        }));
                                        return 1;
                                    }
                                    return 0;
                                })
                        )
                )
                .then(Commands.literal("craft")
                        .then(Commands.literal("dagger")
                                .requires((commandSource) ->
                                        PermissionHelper.hasPermissonAndIsPlayer(
                                                commandSource,
                                                permisionRegistry.SHADOWMONARCH_IS_AWAKENED
                                        )
                                )
                                .executes((context) -> {
                                    ServerPlayer player =(ServerPlayer) context.getSource().getEntity();
                                    ManasSkillInstance instance = SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SHADOW_MONARCH.get()).get();
                                    if (instance.getSkill() instanceof ShadowMonarch skill) {
                                        if (skill.getData().getBoolean("awakened")) {
                                            if (player.getInventory().contains(new ItemStack(Items.NETHER_STAR, 2)) &&
                                            player.getInventory().contains(new ItemStack(Items.DRAGON_EGG))) {
                                                if (player.getInventory().add(new ItemStack(weapons.SHADOW_DAGGER.get()))) {
                                                    player.sendSystemMessage(Component.translatable("trawakened.command.shadow.craft.success"));
                                                    return 1;
                                                } else if (skill.addItemToSpatialStorage(instance, player, new ItemStack(weapons.SHADOW_DAGGER.get()))) {
                                                    player.sendSystemMessage(Component.translatable("trawakened.command.shadow.craft.success"));
                                                    return 1;
                                                } else {
                                                    player.sendSystemMessage(Component.translatable("trawakened.command.shadow.craft.success"));
                                                    Vec3 pos = player.position();
                                                    ItemStack stack = new ItemStack(weapons.SHADOW_DAGGER.get());
                                                    ItemEntity itemDrop = new ItemEntity(player.level, pos.x, pos.y, pos.z, stack);
                                                    player.level.addFreshEntity(itemDrop);
                                                    return 1;
                                                }
                                            }
                                        }
                                    }
                                    player.sendSystemMessage(Component.translatable("trawakened.command.shadow.craft.fail"));
                                    return 0;
                                })
                        )
                )
                .then(Commands.literal("summon")
                        .then(Commands.literal("kamish")
                                .executes((context) -> {
                                    ServerPlayer player =(ServerPlayer) context.getSource().getEntity();
                                    player.sendSystemMessage(SkillHelper.comingSoon());
                                    return 0;
                                })
                        )
                )
                .then(Commands.literal("bind")
                        .requires((commandSource) ->
                                PermissionHelper.hasPermissonAndIsPlayer(
                                        commandSource,
                                        permisionRegistry.SHADOWMONARCH_IS_AWAKENED
                                )
                        )
                        .then(Commands.argument("player_name", EntityArgument.player())
                                .executes((context) -> {
                                    Player target = EntityArgument.getPlayer(context, "player_name");
                                    ServerPlayer player = (ServerPlayer) context.getSource().getEntity();
                                    if (target.getPersistentData().getInt("bind_cooldown") > 0) {
                                        ManasSkillInstance instance = SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SHADOW_MONARCH.get()).get();
                                        if (instance.getSkill() instanceof ShadowMonarch skill) {
                                            if (skill.getData().getBoolean("awakened")) {
                                                target.addEffect(new MobEffectInstance(TensuraMobEffects.PARALYSIS.get(), 255, 1200));
                                                target.addEffect(new MobEffectInstance(TensuraMobEffects.SPATIAL_BLOCKADE.get(), 255, 1200));
                                                target.getPersistentData().putInt("bind_cooldown", 600);
                                                player.sendSystemMessage(Component.translatable("trawakened.command.shadow.bind.success", target.getName().getString()));
                                                return 1;
                                            }
                                        }
                                    }
                                    player.sendSystemMessage(Component.translatable("trawakened.command.shadow.bind.fail", target.getName().getString()));
                                    return 0;
                                })
                        )
                )
                .then(Commands.literal("exchange")
                        .then(Commands.argument("shadow", EntityArgument.entity())
                                .then(Commands.argument("dimension", DimensionArgument.dimension())
                                    .executes((context) -> {
                                        ServerPlayer player = context.getSource().getPlayer();
                                        ManasSkillInstance instance = SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SHADOW_MONARCH.get()).get();
                                        Entity target = EntityArgument.getEntity(context ,"shadow");
                                        if (target instanceof LivingEntity living && instance.onCoolDown()) {
                                            if (instance.getSkill() instanceof ShadowMonarch skill) {
                                                if (AwakenedShadowCapability.isShadow(living) &&
                                                        AwakenedShadowCapability.isArisen(living) &&
                                                        AwakenedShadowCapability.getOwnerUUID(living).equals(player.getUUID())) {
                                                    ServerLevel level = DimensionArgument.getDimension(context, "dimension");
                                                    if (living.getLevel().equals(level)) {
                                                        player.teleportTo(level,
                                                                living.position().x,
                                                                living.position().y,
                                                                living.position().z,
                                                                living.getYRot(),
                                                                living.getXRot());
                                                        instance.setCoolDown(1200);
                                                    }
                                                    player.sendSystemMessage(Component.translatable("trawakened.command.shadow.exchange.dimension"));
                                                } else {
                                                    player.sendSystemMessage(Component.translatable("trawakened.command.shadow.exchange.no_shadow"));
                                                    return 0;
                                                }
                                            }
                                        }
                                        return 0;
                                    })
                                )
                        )
                )
                .then(Commands.literal("tp")
                        .then(Commands.argument("player_name", EntityArgument.player())
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayer();
                                    ManasSkillInstance instance = SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SHADOW_MONARCH.get()).get();
                                    Player target = EntityArgument.getPlayer(context, "player_name");
                                    if (instance.getSkill() instanceof ShadowMonarch skill) {
                                        if (AwakenedShadowCapability.hasShadow(target)) {
                                            if (AwakenedShadowCapability
                                                    .getStorage(target)
                                                    .getCompound("EntityData")
                                                    .getCompound("ForgeCaps")
                                                    .getCompound("trawakened:shadow")
                                                    .getString("ownerUUID").length() == 36) {
                                                if (UUID.fromString(
                                                                AwakenedShadowCapability
                                                                        .getStorage(target)
                                                                        .getCompound("EntityData")
                                                                        .getCompound("ForgeCaps")
                                                                        .getCompound("trawakened:shadow")
                                                                        .getString("ownerUUID")
                                                        )
                                                        .equals(player.getUUID())
                                                ) {
                                                    player.teleportTo(target.position().x, target.position().y, target.position().z);
                                                    AwakenedShadowCapability.setHasShadow(target, false);
                                                    CompoundTag tag = AwakenedShadowCapability.getStorage(target);
                                                    skill.getShadowStorage().put(tag.getCompound("EnitityData").getUUID("UUID").toString(), tag);
                                                    AwakenedShadowCapability.setStorage(target, new CompoundTag());
                                                }
                                            }
                                        } else {
                                            target.sendSystemMessage(Component.translatable("trawakened.command.shadow.tp.no_shadow"));
                                            return 0;
                                        }
                                    }
                                    return 0;
                                })
                        )
                )
        );
    }
}
