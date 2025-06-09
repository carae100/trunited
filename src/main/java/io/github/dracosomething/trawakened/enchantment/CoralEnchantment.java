package io.github.dracosomething.trawakened.enchantment;

import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.enchantment.IInherentEngrave;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class CoralEnchantment extends EngravingEnchantment implements IInherentEngrave{
    public CoralEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, EquipmentSlot.values());
    }

    @Override
    public ChatFormatting getNameFormatting() {
        return ChatFormatting.RED;
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public boolean isCurse() {
        return true;
    }
}
