package io.github.dracosomething.trawakened.item.shadowItems;

import com.github.manasmods.tensura.item.templates.SimpleSwordItem;
import io.github.dracosomething.trawakened.item.SimpleHammerItem;
import io.github.dracosomething.trawakened.library.SoulBoundItem;
import net.minecraft.world.item.Tier;

public class ShadowHammerItem extends SimpleHammerItem implements SoulBoundItem {
    public ShadowHammerItem(Tier pTier, SimpleSwordItem.SwordModifier modifier) {
        super(pTier, modifier);
    }

    public ShadowHammerItem(Tier pTier, SimpleSwordItem.SwordModifier modifier, Properties properties) {
        super(pTier, modifier, properties);
    }

    public ShadowHammerItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, double attackRangeModifier, double critChance, double critDamageMultiplier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, attackRangeModifier, critChance, critDamageMultiplier, pProperties);
    }

    public boolean canBeDropped() {
        return false;
    }
}
