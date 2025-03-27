package io.github.dracosomething.trawakened.commands;

import com.github.manasmods.tensura.command.TensuraPermissions;
import com.github.manasmods.tensura.util.PermissionHelper;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.AwakenedFearCapability;
import io.github.dracosomething.trawakened.commands.argument.fearArgument;
import io.github.dracosomething.trawakened.library.FearTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = "trawakened",
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class AwakenedCommand {
    public AwakenedCommand(){

    }

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("awakened")
                .then(Commands.literal("get")
                        .then(Commands.literal("fear").requires((commandSource) -> PermissionHelper.hasPermissionOrIsConsole(commandSource, TensuraPermissions.PLAYER_EDIT_RACE_OTHERS))
                                .executes((context) -> {
                                    ServerPlayer player = (ServerPlayer) context.getSource().getEntity();
                                    if (player != null) {
                                        String name = AwakenedFearCapability.getFearType(player).getName();
                                        context.getSource().sendSuccess(Component.translatable("trawakened.command.fear.get_fear", player.getName(), name).setStyle(Style.EMPTY), false);
                                        return 1;
                                    } else {
                                        return 0;
                                    }
                                })
                        )
                        .then(Commands.literal("scared")
                                .executes((context) -> {
                                    ServerPlayer player =(ServerPlayer) context.getSource().getEntity();
                                    if (player != null) {
                                        int scared = AwakenedFearCapability.getScared(player);
                                        context.getSource().sendSuccess(Component.translatable("trawakened.command.fear.get_scared", player.getName(), scared).setStyle(Style.EMPTY), false);
                                        return 1;
                                    } else {
                                        return 0;
                                    }
                                })
                        )
                        .then(Commands.literal("cooldown")
                                .executes((context) -> {
                                    ServerPlayer player =(ServerPlayer) context.getSource().getEntity();
                                    if (player != null) {
                                        int cooldown = AwakenedFearCapability.getScaredCooldown(player);
                                        context.getSource().sendSuccess(Component.translatable("trawakened.command.fear.get_cooldown", player.getName(), cooldown).setStyle(Style.EMPTY), false);
                                        return 1;
                                    } else {
                                        return 0;
                                    }
                                })
                        )
                        .then(Commands.argument("player", EntityArgument.player()).requires((commandSource) -> PermissionHelper.hasPermissionOrIsConsole(commandSource, TensuraPermissions.PLAYER_EDIT_RACE_OTHERS))
                                .then(Commands.literal("fear")
                                        .executes((context) -> {
                                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                            String name = AwakenedFearCapability.getFearType(player).getName();
                                            context.getSource().sendSuccess(Component.translatable("trawakened.command.fear.get_fear", player.getName(), name).setStyle(Style.EMPTY), false);
                                            return 1;
                                        })
                                )
                                .then(Commands.literal("scared")
                                        .executes((context) -> {
                                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                            String name = AwakenedFearCapability.getFearType(player).getName();
                                            context.getSource().sendSuccess(Component.translatable("trawakened.command.fear.get_scared", player.getName(), name).setStyle(Style.EMPTY), false);
                                            return 1;
                                        })
                                )
                                .then(Commands.literal("cooldown")
                                        .executes((context) -> {
                                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                            String name = AwakenedFearCapability.getFearType(player).getName();
                                            context.getSource().sendSuccess(Component.translatable("trawakened.command.fear.get_cooldown", player.getName(), name).setStyle(Style.EMPTY), false);
                                            return 1;
                                        })
                                )
                        )
                )
                .then(Commands.literal("edit").requires((commandSource) -> PermissionHelper.hasPermissionOrIsConsole(commandSource, TensuraPermissions.PLAYER_EDIT_STAT))
                        .then(Commands.argument("player", EntityArgument.entity()).requires((commandSource) -> PermissionHelper.hasPermissionOrIsConsole(commandSource, TensuraPermissions.PLAYER_EDIT_RACE_OTHERS))
                                .then(Commands.literal("fear")
                                        .then(Commands.argument("fear", fearArgument.fear())
                                        .executes((context) -> {
                                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                            FearTypes newFear = fearArgument.getFear(context, "fear");
                                            if (newFear == null) {
                                                return 0;
                                            }
                                            AwakenedFearCapability.setFearType(player, newFear);
                                            context.getSource().sendSuccess(Component.translatable("trawakened.command.fear.set_fear", player.getName(), newFear.getName()).setStyle(Style.EMPTY), false);
                                            return 1;
                                        }))
                                )
                                .then(Commands.literal("scared")
                                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                            .executes((context) -> {
                                                ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                int newScared = IntegerArgumentType.getInteger(context, "amount");
                                                AwakenedFearCapability.setScared(player, newScared);
                                                context.getSource().sendSuccess(Component.translatable("trawakened.command.fear.set_scared", player.getName(), newScared).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GREEN)), false);
                                                return 1;
                                            })
                                        )
                                )
                                .then(Commands.literal("cooldown")
                                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                                .executes((context) -> {
                                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                    int cooldown = IntegerArgumentType.getInteger(context, "amount");
                                                    AwakenedFearCapability.setScaredCooldown(player, cooldown);
                                                    context.getSource().sendSuccess(Component.translatable("trawakened.command.fear.set_cooldown", player.getName(), cooldown).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GREEN)), false);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
        );
    }
}
