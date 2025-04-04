package io.github.dracosomething.trawakened.item;

import com.github.manasmods.tensura.item.templates.custom.TwoHandedLongSword;
import net.minecraft.world.item.Tier;

public class SimpleHammerItem extends TwoHandedLongSword {
    public SimpleHammerItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier,
                            double attackRangeModifier, double critChance,
                            double critDamageMultiplier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, attackRangeModifier,
                critChance, critDamageMultiplier, 0, 0, pProperties);
    }
}
