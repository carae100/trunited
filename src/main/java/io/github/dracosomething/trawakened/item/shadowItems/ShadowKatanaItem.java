package io.github.dracosomething.trawakened.item.shadowItems;

import com.github.manasmods.tensura.item.templates.SimpleSwordItem;
import com.github.manasmods.tensura.item.templates.custom.SimpleKatanaItem;
import io.github.dracosomething.trawakened.library.SoulBoundItem;
import net.minecraft.world.item.Tier;

public class ShadowKatanaItem extends SimpleKatanaItem implements SoulBoundItem {
    public ShadowKatanaItem(Tier pTier, Properties properties) {
        super(pTier, properties);
    }

    public boolean canBeDropped() {
        return false;
    }
}
