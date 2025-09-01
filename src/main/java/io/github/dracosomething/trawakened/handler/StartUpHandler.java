package io.github.dracosomething.trawakened.handler;

import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.skill.Skill;
import io.github.dracosomething.trawakened.helper.classHelper;
import io.github.dracosomething.trawakened.library.SoulBoundItem;
import io.github.dracosomething.trawakened.trawakened;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

import static io.github.dracosomething.trawakened.event.ModEvents.list_unique;
import static io.github.dracosomething.trawakened.event.ModEvents.list_ultimate;

@Mod.EventBusSubscriber(modid = trawakened.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class StartUpHandler {
    public static List<ItemStack> items = new ArrayList<>();
    public static List<Item> soulBoundItems = new ArrayList<>();

    @SubscribeEvent
    public static void onConstructMod(FMLConstructModEvent event) {
        event.enqueueWork(() -> {
            soulBoundItems = ForgeRegistries.ITEMS.getValues().stream().filter((item) -> {
                return item != Items.AIR && classHelper.hasInterface(item.getClass(), SoulBoundItem.class);
            }).toList();
        });
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            soulBoundItems = ForgeRegistries.ITEMS.getValues().stream().filter((item) -> {
                return item != Items.AIR && classHelper.hasInterface(item.getClass(), SoulBoundItem.class);
            }).toList();
            list_unique = SkillAPI.getSkillRegistry().getValues().stream().filter((manasSkill) -> {
                boolean var10000;
                if (manasSkill instanceof Skill skill) {
                    if (skill.getType().equals(Skill.SkillType.UNIQUE)) {
                        var10000 = true;
                        return var10000;
                    }
                }

                var10000 = false;
                return var10000;
            }).toList();
            list_ultimate = SkillAPI.getSkillRegistry().getValues().stream().filter((manasSkill) -> {
                boolean var10000;
                if (manasSkill instanceof Skill skill) {
                    if (skill.getType().equals(Skill.SkillType.ULTIMATE)) {
                        var10000 = true;
                        return var10000;
                    }
                }

                var10000 = false;
                return var10000;
            }).toList();
        });
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            soulBoundItems = ForgeRegistries.ITEMS.getValues().stream().filter((item) -> {
                return item != Items.AIR && classHelper.hasInterface(item.getClass(), SoulBoundItem.class);
            }).toList();
        });
    }

    @SubscribeEvent
    public static void onServerSetup(FMLDedicatedServerSetupEvent event) {
        event.enqueueWork(() -> {
            soulBoundItems = ForgeRegistries.ITEMS.getValues().stream().filter((item) -> {
                return item != Items.AIR && classHelper.hasInterface(item.getClass(), SoulBoundItem.class);
            }).toList();
        });
    }
}
