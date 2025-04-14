package io.github.dracosomething.trawakened.item.shadowItems;

import com.github.manasmods.tensura.item.templates.SimpleSwordItem;
import io.github.dracosomething.trawakened.library.SoulBoundItem;
import net.minecraft.world.item.Tier;

public class ShadowSwordItem extends SimpleSwordItem implements SoulBoundItem {
    public ShadowSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties properties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, properties);
    }

    public ShadowSwordItem(Tier pTier, SwordModifier swordModifier) {
        super(pTier, swordModifier);
    }

    public boolean canBeDropped() {
        return false;
    }
}
