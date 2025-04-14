package io.github.dracosomething.trawakened.registry;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.util.PermissionHelper;
import io.github.dracosomething.trawakened.ability.skill.ultimate.ShadowMonarch;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;
import net.minecraftforge.server.permission.nodes.PermissionDynamicContextKey;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;

@Mod.EventBusSubscriber(
        modid = "trawakened",
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class permisionRegistry {
    public static final PermissionNode<Boolean> PLAYER_HAS_SHADOWMONARCH = createNode("shadow_monarch.use", false, 0);
    public static final PermissionNode<Boolean> SHADOWMONARCH_IS_AWAKENED = createNodeAwakened("shadow_monarch.awakened", false, 0);

    public static PermissionNode<Boolean> createNode(String node, boolean allowConsole, int permissionLevel) {
        return new PermissionNode<>("trawakened", node, PermissionTypes.BOOLEAN, (player, playerUUID, context) -> {
            return player == null ? allowConsole : player.hasPermissions(permissionLevel) && SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SHADOW_MONARCH.get()).isPresent();
        }, new PermissionDynamicContextKey[0]);
    }

    public static PermissionNode<Boolean> createNodeAwakened(String node, boolean allowConsole, int permissionLevel) {
        return new PermissionNode<>("trawakened", node, PermissionTypes.BOOLEAN, (player, playerUUID, context) -> {
            return player == null ? allowConsole : player.hasPermissions(permissionLevel) &&
                    SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SHADOW_MONARCH.get()).isPresent() &&
                    (SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SHADOW_MONARCH.get()).get().getSkill() instanceof
                            ShadowMonarch skill &&
                            skill.getData().getBoolean("awakened"));
        }, new PermissionDynamicContextKey[0]);
    }

    @SubscribeEvent
    public static void registerPermissions(PermissionGatherEvent.Nodes e) {
        e.addNodes(new PermissionNode[]{PLAYER_HAS_SHADOWMONARCH, SHADOWMONARCH_IS_AWAKENED});
    }
}
