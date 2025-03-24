package io.github.dracosomething.trawakened.commands;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.command.TensuraPermissions;
import com.github.manasmods.tensura.util.PermissionHelper;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.dracosomething.trawakened.ability.skill.ultimate.ShadowMonarch;
import io.github.dracosomething.trawakened.capability.alternateFearCapability.AwakenedFearCapability;
import io.github.dracosomething.trawakened.commands.argument.rankArgument;
import io.github.dracosomething.trawakened.library.FearTypes;
import io.github.dracosomething.trawakened.registry.permisionRegistry;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(
        modid = "trawakened",
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class ShadowCommand {

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("shadow")
                .requires((commandSource) ->
                        PermissionHelper.hasPermissionOrIsConsole(
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
                                                List<String> validTargets = skill.getShadowStorage().getAllKeys().stream().filter(shadow -> {
                                                    return skill.getShadowStorage().getCompound(shadow).getCompound("rank") == rankArgument.getRank(context, "rank").toNBT();
                                                }).toList();
                                                validTargets.forEach(shadow -> {
                                                    EntityType<?> type = EntityType.byString(skill.getShadowStorage().getCompound(shadow).getString("entityType")).get();
                                                    LivingEntity entity = (LivingEntity) type.create(player.level);
                                                    entity.deserializeNBT();
                                                });
                                            }
                                        })
                                )
                        )
                        .then(Commands.literal("top"))
                        .then(Commands.literal("above"))
                        .then(Commands.literal("name"))
                )
        );
    }
}
