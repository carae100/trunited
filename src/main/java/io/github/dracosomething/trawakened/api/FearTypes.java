package io.github.dracosomething.trawakened.api;

import com.github.manasmods.tensura.entity.human.OtherworlderEntity;
import com.github.manasmods.tensura.entity.human.PlayerLikeEntity;
import com.github.manasmods.tensura.registry.entity.TensuraEntityTypes;
import com.google.common.collect.Lists;
import io.github.dracosomething.trawakened.registry.entityRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public enum FearTypes {
    RELIGION("religion",
            List.of(
                Blocks.BREWING_STAND,
                Blocks.LECTERN),
            List.of(
                    Items.BREWING_STAND,
                    Items.LECTERN
            ), null),
    ALTERNATES("alternate", null, null, null),
    DARKNESS("dark",
            Registry.BLOCK.stream().filter((block1) -> {
                return block1.getName().contains(Component.literal("black")) || block1.getName().contains(Component.literal("Black"));
            }).toList(),
            Registry.ITEM.stream().filter((item1) -> {
                return item1.getName(item1.getDefaultInstance()).contains(Component.literal("black")) || item1.getName(item1.getDefaultInstance()).contains(Component.literal("Black"));
            }).toList(),
            List.of(
                    TensuraEntityTypes.DARK_CUBE.get(),
                    TensuraEntityTypes.DARKNESS_CANNON.get()
            )),
    TRUTH("truth", null, null, null),
    OCEAN("ocean", null, null, null),
    CREEPER("creeper",
            List.of(
                    Blocks.CREEPER_HEAD,
                    Blocks.CREEPER_WALL_HEAD
            ),
            List.of(
                    Items.CREEPER_HEAD,
                    Items.CREEPER_BANNER_PATTERN,
                    Items.CREEPER_SPAWN_EGG
            ),
            List.of(EntityType.CREEPER)),
    OTHERWORLDER("otherworlder", null, null, List.of(
            TensuraEntityTypes.CLONE_DEFAULT.get(),
            TensuraEntityTypes.CLONE_SLIM.get(),
            TensuraEntityTypes.HINATA_SAKAGUCHI.get(),
            TensuraEntityTypes.KIRARA_MIZUTANI.get(),
            TensuraEntityTypes.KYOYA_TACHIBANA.get(),
            TensuraEntityTypes.MAI_FURUKI.get(),
            TensuraEntityTypes.MARK_LAUREN.get(),
            TensuraEntityTypes.SHIN_RYUSEI.get(),
            TensuraEntityTypes.SHINJI_TANIMURA.get(),
            TensuraEntityTypes.SHIZU.get(),
            TensuraEntityTypes.SHOGO_TAGUCHI.get(),
            entityRegistry.DEFAULT_OTHER_WORLDER.get())),
    LIQUID("liquid",
            List.of(
                Blocks.WATER,
                Blocks.WATER_CAULDRON,
                Blocks.LAVA,
                Blocks.LAVA_CAULDRON
            ),
            List.of(
                    Items.WATER_BUCKET,
                    Items.LAVA_BUCKET,
                    Items.MILK_BUCKET
            ),
            null);

    private final String name;
    private final List<Block> block;
    private final List<Item> item;
    private final List<EntityType<?>> entity;

    private FearTypes(String name, List<Block> block, List<Item> item, List<EntityType<?>> entity) {
        this.name = name;
        this.block = block;
        this.item = item;
        this.entity = entity;
    }

    private FearTypes() {
        this.name = null;
        this.block = null;
        this.item = null;
        this.entity = null;
    }

    private static final List<FearTypes> list = FearTypes.getFears().stream().toList();
    private static final Map<String, FearTypes> FEARS_BY_NAME = (Map)Arrays.stream(values()).collect(Collectors.toMap((fearType) -> {
        return cleanName(fearType.name);
    }, (fearType) -> {
        return fearType;
    }));

    private static String cleanName(String p_126663_) {
        return p_126663_.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
    }

    public static Collection<String> getNames() {
        List<String> $$2 = Lists.newArrayList();
        FearTypes[] var3 = values();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            FearTypes $$3 = var3[var5];
            $$2.add($$3.getName());
        }

        return $$2;
    }

    public static Collection<FearTypes> getFears() {
        List<FearTypes> $$2 = Lists.newArrayList();
        FearTypes[] var3 = values();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            FearTypes $$3 = var3[var5];
            $$2.add($$3);
        }

        return $$2;
    }

    @Nullable
    public static FearTypes getByName(@Nullable String p_126658_) {
        return p_126658_ == null ? null : (FearTypes) FEARS_BY_NAME.get(cleanName(p_126658_));
    }

    public String getName() {
        return name;
    }

    public List<Block> getBlock() {
        return block;
    }

    public List<EntityType<?>> getEntity() {
        return entity;
    }

    public List<Item> getItem() {
        return item;
    }

    @Override
    public String toString() {
        return name;
    }

    public static FearTypes getRandom(){
        Random random = new Random();
        FearTypes fear = (FearTypes) list.get(random.nextInt(0, list.size()));
        return fear;
    }
}
