package io.github.dracosomething.trawakened.item;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.skill.Skill;
import io.github.dracosomething.trawakened.ability.skill.extra.System.Stealth;
import io.github.dracosomething.trawakened.ability.skill.extra.System.SystemExtra;
import io.github.dracosomething.trawakened.registry.skillRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class runeStone extends Item {
    private Supplier<? extends SystemExtra> systemExtra;
    private int levelReq;

    public runeStone(Supplier<? extends SystemExtra> systemExtra, int lvlReq) {
        super(new Properties().tab(CreativeModeTab.TAB_MISC));
        this.systemExtra = systemExtra;
        this.levelReq = lvlReq;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SYSTEM.get()).isPresent()) {
            ManasSkillInstance instance = SkillAPI.getSkillsFrom(player).getSkill(skillRegistry.SYSTEM.get()).get();
            CompoundTag tag = instance.getOrCreateTag();
            if (levelReq <= tag.getInt("level")) {
                Skill skill = systemExtra.get();
                SkillUtils.learnSkill(player, skill);
                player.sendSystemMessage(Component.translatable("tensura.skill.acquire_learning", skill.getName()));
                return InteractionResultHolder.consume(itemstack);
            }
        }
        return InteractionResultHolder.fail(itemstack);
    }
}
