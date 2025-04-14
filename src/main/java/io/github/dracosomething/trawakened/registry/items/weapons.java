package io.github.dracosomething.trawakened.registry.items;

import com.github.manasmods.tensura.item.TensuraToolTiers;
import com.github.manasmods.tensura.item.templates.SimpleBowItem;
import com.github.manasmods.tensura.item.templates.SimpleSwordItem;
import com.github.manasmods.tensura.item.templates.custom.SimpleGreatSwordItem;
import com.github.manasmods.tensura.item.templates.custom.SimpleKatanaItem;
import com.github.manasmods.tensura.item.templates.custom.SimpleLongSwordItem;
import com.github.manasmods.tensura.item.templates.custom.SimpleShortSwordItem;
import io.github.dracosomething.trawakened.item.shadowItems.*;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class weapons {
    private static final DeferredRegister<Item> registry;
    public static final RegistryObject<Item> SHADOW_HAMMER;
    public static final RegistryObject<Item> SHADOW_SWORD;
    public static final RegistryObject<Item> SHADOW_KATANA;
    public static final RegistryObject<Item> SHADOW_GREATSWORD;
    public static final RegistryObject<Item> SHADOW_WAR_AXE;
    public static final RegistryObject<Item> SHADOW_BOW;
    public static final RegistryObject<Item> SHADOW_DAGGER;
    public static final RegistryObject<Item> LIGHTNING_SWORD;
    private static Item.Properties unbreakableItemProperties = new Item.Properties().stacksTo(0).fireResistant().tab(CreativeModeTab.TAB_COMBAT);

    public static void init(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.ITEMS, "trawakened");
        SHADOW_HAMMER = registry.register("shadow_hammer", () -> {
            return new ShadowHammerItem(TensuraToolTiers.HIHIIROKANE, SimpleSwordItem.SwordModifier.FIRE_RESISTED, unbreakableItemProperties);
        });
        SHADOW_SWORD = registry.register("shadow_sword", () -> {
            return new ShadowSwordItem(TensuraToolTiers.HIHIIROKANE,
                    SimpleSwordItem.SwordModifier.FIRE_RESISTED.getAttackDamageModifier(),
                    SimpleSwordItem.SwordModifier.FIRE_RESISTED.getAttackSpeedModifier(),
                    unbreakableItemProperties);
        });
        SHADOW_KATANA = registry.register("shadow_katana", () -> {
            return new ShadowKatanaItem(TensuraToolTiers.HIHIIROKANE, unbreakableItemProperties);
        });
        SHADOW_GREATSWORD = registry.register("shadow_great_sword", () -> {
            return new ShadowGreatSwordItem(TensuraToolTiers.HIHIIROKANE, unbreakableItemProperties);
        });
        SHADOW_WAR_AXE = registry.register("shadow_war_axe", () -> {
            return new ShadowWarAxeItem(TensuraToolTiers.HIHIIROKANE, SimpleSwordItem.SwordModifier.FIRE_RESISTED, unbreakableItemProperties);
        });
        SHADOW_BOW = registry.register("shadow_bow", () -> {
            return new ShadowBowItem(unbreakableItemProperties, 60, 60.0F, 2.75, 1.4F);
        });
        LIGHTNING_SWORD = registry.register("lightning_sword", () -> {
            return new LightningSwordItem(TensuraToolTiers.HIHIIROKANE, unbreakableItemProperties);
        });
        SHADOW_DAGGER = registry.register("shadow_dagger", () -> {
            return new ShadowDaggerItem(TensuraToolTiers.HIHIIROKANE, unbreakableItemProperties);
        });
    }
}
