package io.github.dracosomething.trawakened.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

public class DataTabletItem extends Item {
    public DataTabletItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        System.out.println("ggggggggggg");

        if(p_41433_.getItemInHand(p_41434_).hasTag()){
            p_41433_.getItemInHand(p_41434_).setTag(new CompoundTag());
        }

        return super.use(p_41432_, p_41433_, p_41434_);
    }

    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return p_41453_.hasTag();
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        if(p_41421_.hasTag()){
            p_41423_.add(Component.translatable("trawakened.skill.mode.willofhonkai.copy"));
        }

        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }
}
