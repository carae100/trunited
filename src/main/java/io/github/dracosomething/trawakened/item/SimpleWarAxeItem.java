package io.github.dracosomething.trawakened.item;

import com.github.manasmods.tensura.item.templates.SimpleSwordItem;
import com.github.manasmods.tensura.item.templates.custom.TwoHandedLongSword;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.Tier;

public class SimpleWarAxeItem extends TwoHandedLongSword {
    public SimpleWarAxeItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier,
                            double attackRangeModifier, double critChance, double critDamageMultiplier,
                            double sweepChance, double oneHandedSweepChance, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier,
                attackRangeModifier, critChance, critDamageMultiplier,
                sweepChance, oneHandedSweepChance, pProperties);
    }

    public SimpleWarAxeItem(Tier pTier, SimpleSwordItem.SwordModifier modifier) {
        super(pTier, modifier.getAttackDamageModifier(), modifier.getAttackSpeedModifier(), 1,
                1, 1, 0, 0, modifier.getProperties());
    }

    public SimpleWarAxeItem(Tier pTier, SimpleSwordItem.SwordModifier modifier, Properties properties) {
        super(pTier, modifier.getAttackDamageModifier(), modifier.getAttackSpeedModifier(), 1,
                1, 1, 0, 0, properties);
    }
}
