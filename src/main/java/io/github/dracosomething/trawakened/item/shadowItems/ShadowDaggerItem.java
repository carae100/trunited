package io.github.dracosomething.trawakened.item.shadowItems;

import com.github.manasmods.tensura.item.templates.custom.SimpleLongSwordItem;
import com.github.manasmods.tensura.item.templates.custom.SimpleShortSwordItem;
import io.github.dracosomething.trawakened.library.SoulBoundItem;
import net.minecraft.world.item.Tier;

public class ShadowDaggerItem extends SimpleShortSwordItem implements SoulBoundItem {
    public ShadowDaggerItem(Tier pTier, Properties properties) {
        super(pTier, properties);
    }

    public boolean canBeDropped() {
        return false;
    }
}
