package io.github.dracosomething.trawakened.handler;

import io.github.dracosomething.trawakened.helper.EngravingHelper;
import io.github.dracosomething.trawakened.helper.classHelper;
import io.github.dracosomething.trawakened.library.SoulBoundItem;
import io.github.dracosomething.trawakened.trawakened;
import io.github.dracosomething.trawakened.registry.items.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.List;

@Mod.EventBusSubscriber(modid = trawakened.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SoulBoundItemsHandler {
    private static List<ItemStack> items = new ArrayList<>();
    private static List<Item> soulBoundItems;

    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            soulBoundItems = ForgeRegistries.ITEMS.getValues().stream().filter((item) -> {
                return classHelper.hasInterface(item.getClass(), SoulBoundItem.class) || (item.equals(Items.AIR));
            }).toList();
        });
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            player.getInventory().items.forEach((item) -> {
                if (soulBoundItems.contains(item.getItem())) {
                    SoulBoundItem soulBoundItem = (SoulBoundItem) item.getItem();
                    if (item.getDamageValue() >= soulBoundItem.minimumDurability() &&
                    player.experienceLevel >= soulBoundItem.getXPRequirement()) {
                        item.setDamageValue(item.getDamageValue() - soulBoundItem.getDurabilityCost());
                        items.add(item);
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void onDrop(LivingDropsEvent event) {
        event.getDrops().removeAll(items);
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        items.forEach((item) -> {
            SoulBoundItem soulBoundItem = (SoulBoundItem) item.getItem();
            if (!soulBoundItem.keepsEnchantments()) {
                EngravingHelper.RemoveAllEnchantments(item);
            }
            event.getEntity().getInventory().add(item);
        });
    }

    @SubscribeEvent
    public static void onPlayerDropitem(ItemTossEvent event) {
        if (soulBoundItems.contains(event.getEntity().getItem().getItem())) {
            SoulBoundItem item = (SoulBoundItem) event.getEntity().getItem().getItem();
            if (!item.canBeDropped()) {
                ItemStack stack = event.getEntity().getItem();
                event.getPlayer().getInventory().add(stack);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void keepItems(TickEvent.PlayerTickEvent event) {
            event.player.getInventory().items.forEach((item) -> {
                if (soulBoundItems.contains(item.getItem())) {
                    SoulBoundItem soulBoundItem = (SoulBoundItem) item.getItem();
                    if (!item.getOrCreateTag().hasUUID("owner")) {
                        item.getOrCreateTag().putUUID("owner", event.player.getUUID());
                    }
                    if (event.player.getUUID() != item.getOrCreateTag().getUUID("owner")) {
                        if (event.player.getLevel().getServer() != null) {
                            event.player.getLevel().getServer().getAllLevels().forEach((level) -> {
                                Entity entity = level.getEntity(item.getOrCreateTag().getUUID("owner"));
                                if (entity instanceof Player player) {
                                    player.getInventory().add(item);
                                    event.player.getInventory().removeItem(item);
                                }
                            });
                        }
                    }
                }
            });
    }
}
