package io.github.dracosomething.trawakened.item.shadowItems;

import com.github.manasmods.tensura.item.templates.SimpleBowItem;
import io.github.dracosomething.trawakened.library.SoulBoundItem;

public class ShadowBowItem extends SimpleBowItem implements SoulBoundItem {
    public ShadowBowItem(Properties pProperties, int pRange, float pChargeTicks, double pBaseDamage, float pInaccuracy) {
        super(pProperties, pRange, pChargeTicks, pBaseDamage, pInaccuracy);
    }

    public boolean canBeDropped() {
        return false;
    }
}
