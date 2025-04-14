package io.github.dracosomething.trawakened.item.shadowItems;

import com.github.manasmods.tensura.item.TensuraArmourMaterials;
import io.github.dracosomething.trawakened.library.SoulBoundItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class shadowArmorItem extends GeoArmorItem implements IAnimatable, SoulBoundItem {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public shadowArmorItem(EquipmentSlot equipmentSlot) {
        super(TensuraArmourMaterials.HIHIIROKANE, equipmentSlot, new Item.Properties().stacksTo(0).fireResistant().tab(CreativeModeTab.TAB_COMBAT));
    }

    public void registerControllers(AnimationData animationData) {

    }

    public AnimationFactory getFactory() {
        return this.factory;
    }

    public boolean canBeDropped() {
        return false;
    }
}
