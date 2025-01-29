package io.github.dracosomething.trawakened.helper;

import io.github.dracosomething.trawakened.registry.enchantRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Map;

public class EngravingHelper {
    public static ItemStack RemoveEnchantments(ItemStack itemStack, Enchantment toClear) {

        Map<Enchantment, Integer> enchantments = itemStack.getAllEnchantments();
        enchantments.remove(toClear);
        EnchantmentHelper.setEnchantments(enchantments, itemStack);

        return itemStack;
    }
}
