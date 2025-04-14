package io.github.dracosomething.trawakened.item.shadowItems;

import com.github.manasmods.tensura.item.templates.custom.SimpleLongSwordItem;
import io.github.dracosomething.trawakened.library.SoulBoundItem;
import net.minecraft.world.item.Tier;

public class LightningSwordItem extends SimpleLongSwordItem implements SoulBoundItem {
    public LightningSwordItem(Tier pTier, Properties properties) {
        super(pTier, properties);
    }

    public boolean canBeDropped() {
        return false;
    }
}
