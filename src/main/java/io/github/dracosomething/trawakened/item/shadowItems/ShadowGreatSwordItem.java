package io.github.dracosomething.trawakened.item.shadowItems;

import com.github.manasmods.tensura.item.templates.SimpleSwordItem;
import com.github.manasmods.tensura.item.templates.custom.SimpleGreatSwordItem;
import io.github.dracosomething.trawakened.library.SoulBoundItem;
import net.minecraft.world.item.Tier;

public class ShadowGreatSwordItem extends SimpleGreatSwordItem implements SoulBoundItem {
    public ShadowGreatSwordItem(Tier pTier, Properties properties) {
        super(pTier, properties);
    }

    public boolean canBeDropped() {
        return false;
    }
}
