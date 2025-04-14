package io.github.dracosomething.trawakened.item;

import com.github.manasmods.tensura.item.templates.SimpleSwordItem;
import com.github.manasmods.tensura.item.templates.custom.TwoHandedLongSword;
import net.minecraft.world.item.Tier;

public class SimpleHammerItem extends TwoHandedLongSword {
    public SimpleHammerItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier,
                            double attackRangeModifier, double critChance,
                            double critDamageMultiplier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, attackRangeModifier,
                critChance, critDamageMultiplier, 0, 0, pProperties);
    }

    public SimpleHammerItem(Tier pTier, SimpleSwordItem.SwordModifier modifier) {
        super(pTier, modifier.getAttackDamageModifier(), modifier.getAttackSpeedModifier(), 1,
                1, 1, 0, 0, modifier.getProperties());
    }

    public SimpleHammerItem(Tier pTier, SimpleSwordItem.SwordModifier modifier, Properties properties) {
        super(pTier, modifier.getAttackDamageModifier(), modifier.getAttackSpeedModifier(), 1,
                1, 1, 0, 0, properties);
    }
}
