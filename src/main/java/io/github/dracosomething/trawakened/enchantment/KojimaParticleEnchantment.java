package io.github.dracosomething.trawakened.enchantment;

import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.enchantment.IInherentEngrave;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class KojimaParticleEnchantment extends EngravingEnchantment implements IInherentEngrave {
    public KojimaParticleEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.BREAKABLE, EquipmentSlot.values());
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
