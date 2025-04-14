package io.github.dracosomething.trawakened.item.shadowItems;

import com.github.manasmods.tensura.item.templates.SimpleSwordItem;
import io.github.dracosomething.trawakened.item.SimpleWarAxeItem;
import io.github.dracosomething.trawakened.library.SoulBoundItem;
import net.minecraft.world.item.Tier;

public class ShadowWarAxeItem extends SimpleWarAxeItem implements SoulBoundItem {
    public ShadowWarAxeItem(Tier pTier, SimpleSwordItem.SwordModifier modifier, Properties properties) {
        super(pTier, modifier, properties);
    }

    public ShadowWarAxeItem(Tier pTier, SimpleSwordItem.SwordModifier modifier) {
        super(pTier, modifier);
    }

    public ShadowWarAxeItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, double attackRangeModifier, double critChance, double critDamageMultiplier, double sweepChance, double oneHandedSweepChance, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, attackRangeModifier, critChance, critDamageMultiplier, sweepChance, oneHandedSweepChance, pProperties);
    }

    public boolean canBeDropped() {
        return false;
    }
}
