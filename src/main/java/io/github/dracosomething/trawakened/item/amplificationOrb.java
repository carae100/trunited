package io.github.dracosomething.trawakened.item;

import io.github.dracosomething.trawakened.library.SoulBoundItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class amplificationOrb extends Item implements SoulBoundItem {
    public amplificationOrb(Properties p_41383_) {
        super(p_41383_);
    }

    public boolean canBeDropped() {
        return false;
    }
}
