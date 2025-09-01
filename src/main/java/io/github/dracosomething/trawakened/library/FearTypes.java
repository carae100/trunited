package io.github.dracosomething.trawakened.library;

import com.github.manasmods.tensura.registry.blocks.TensuraBlocks;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.entity.TensuraEntityTypes;
import com.google.common.collect.Lists;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import io.github.dracosomething.trawakened.registry.entityRegistry;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
            ), null, List.of(MobEffects.HERO_OF_THE_VILLAGE)),
    ALTERNATES("alternate", null, null, null),
    DARKNESS("dark",
            Registry.BLOCK.stream().filter((block1) -> {
                try {
                    if (block1 == null) {
                        return false;
                    }
                    String itemName = block1.getName().toString();
                    return itemName.contains("black") || itemName.contains("Black");
                } catch (Exception e) {
                    return false;
                }
            }).toList(),
            Registry.ITEM.stream().filter((item1) -> {
                try {
                    ItemStack defaultInstance = item1.getDefaultInstance();
                    if (defaultInstance == null || defaultInstance.isEmpty()) {
                        return false;
                    }
                    String itemName = item1.getName(defaultInstance).toString();
                    return itemName.contains("black") || itemName.contains("Black");
                } catch (Exception e) {
                    return false;
                }
            }).toList(),
            List.of(
                    TensuraEntityTypes.DARK_CUBE.get(),
                    TensuraEntityTypes.DARKNESS_CANNON.get()
            ),
            List.of(
                    MobEffects.DARKNESS,
                    MobEffects.BLINDNESS,
                    TensuraMobEffects.TRUE_BLINDNESS.get()
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
    OTHERWORLDER("otherworlder", null, null,
            List.of(
                TensuraEntityTypes.HINATA_SAKAGUCHI.get(),
                TensuraEntityTypes.KIRARA_MIZUTANI.get(),
                TensuraEntityTypes.KYOYA_TACHIBANA.get(),
                TensuraEntityTypes.MAI_FURUKI.get(),
                TensuraEntityTypes.MARK_LAUREN.get(),
                TensuraEntityTypes.SHIN_RYUSEI.get(),
                TensuraEntityTypes.SHINJI_TANIMURA.get(),
                TensuraEntityTypes.SHIZU.get(),
                TensuraEntityTypes.SHOGO_TAGUCHI.get(),
                entityRegistry.DEFAULT_OTHER_WORLDER.get()
            )),
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
                    Items.MILK_BUCKET,
                    Items.POTION,
                    Items.LINGERING_POTION,
                    Items.SPLASH_POTION
            ),
            null),
    TRYPOFOBIA("trypofobia", List.of(
                Blocks.SPONGE,
                Blocks.WET_SPONGE
            ),
            List.of(
                    Items.SPONGE,
                    Items.WET_SPONGE
            ),
            null),
    SPIDER("spider",
            Registry.BLOCK.stream().filter((block1) -> {
                try {
                    if (block1 == null) {
                        return false;
                    }
                    String itemName = block1.getName().toString();
                    return (itemName.contains("spider") || itemName.contains("Spider"));
                } catch (Exception e) {
                    return false;
                }
            }).toList(),
            Registry.ITEM.stream().filter((item1) -> {
                try {
                    ItemStack defaultInstance = item1.getDefaultInstance();
                    if (defaultInstance == null || defaultInstance.isEmpty()) {
                        return false;
                    }
                    String itemName = item1.getName(defaultInstance).toString();
                    return (itemName.contains("spider") || itemName.contains("Spider")) &&
                            (!itemName.contains("schematic") || !itemName.contains("Schematic"));
                } catch (Exception e) {
                    return false;
                }
            }).toList(),
            List.of(
                    TensuraEntityTypes.BLACK_SPIDER.get(),
                    TensuraEntityTypes.KNIGHT_SPIDER.get(),
                    EntityType.SPIDER,
                    EntityType.CAVE_SPIDER
            )),
    WARMTH("heat",
            List.of(
                    Blocks.FIRE,
                    Blocks.LAVA,
                    Blocks.LAVA_CAULDRON,
                    Blocks.CAMPFIRE,
                    Blocks.SOUL_CAMPFIRE,
                    Blocks.SOUL_FIRE,
                    Blocks.TORCH,
                    Blocks.SOUL_TORCH,
                    Blocks.WALL_TORCH,
                    Blocks.SOUL_WALL_TORCH,
                    Blocks.GLOWSTONE,
                    Blocks.LANTERN,
                    Blocks.SOUL_LANTERN,
                    TensuraBlocks.BLACK_FIRE.get()
            ),
            List.of(
                    Items.LAVA_BUCKET,
                    Items.FIRE_CHARGE,
                    Items.TORCH,
                    Items.SOUL_TORCH,
                    Items.GLOWSTONE,
                    Items.CAMPFIRE,
                    Items.LANTERN,
                    Items.SOUL_TORCH,
                    Items.SOUL_CAMPFIRE,
                    Items.SOUL_LANTERN
            ),
            null,
            List.of(
                    TensuraMobEffects.MAGIC_FLAME.get(),
                    TensuraMobEffects.BLACK_BURN.get()
            )
    ),
    COLD("cold",
            List.of(
                    Blocks.ICE,
                    Blocks.BLUE_ICE,
                    Blocks.FROSTED_ICE,
                    Blocks.PACKED_ICE,
                    Blocks.SNOW,
                    Blocks.SNOW_BLOCK,
                    Blocks.POWDER_SNOW,
                    Blocks.POWDER_SNOW_CAULDRON,
                    Blocks.WATER,
                    Blocks.WATER_CAULDRON
            ),
            List.of(
                    Items.WATER_BUCKET,
                    Items.ICE,
                    Items.BLUE_ICE,
                    Items.PACKED_ICE,
                    Items.SNOW,
                    Items.SNOW_BLOCK,
                    Items.SNOWBALL,
                    Items.POWDER_SNOW_BUCKET
            ),
            null,
            List.of(TensuraMobEffects.CHILL.get())),
    GERM("germ",
            null,
            List.of(Items.POISONOUS_POTATO),
            null,
            List.of(
                    MobEffects.POISON,
                    MobEffects.CONFUSION,
                    MobEffects.WITHER,
                    TensuraMobEffects.FATAL_POISON.get(),
                    effectRegistry.PLAGUEEFFECT.get(),
                    effectRegistry.BRAINDAMAGE.get()
            )),
    INSECT("insect",
            Registry.BLOCK.stream().filter((block1) -> {
                try {
                    if (block1 == null) {
                        return false;
                    }
                    Component itemName = block1.getName();
                    return itemName.contains(Component.literal("spider")) ||
                            itemName.contains(Component.literal("Spider")) ||
                            itemName.contains(Component.literal("ant")) ||
                            itemName.contains(Component.literal("Ant"));
                } catch (Exception e) {
                    return false;
                }
            }).toList(),
            Registry.ITEM.stream().filter((item1) -> {
                try {
                    ItemStack defaultInstance = item1.getDefaultInstance();
                    if (defaultInstance == null || defaultInstance.isEmpty()) {
                        return false;
                    }
                    String itemName = item1.getName(defaultInstance).toString();
                    return (itemName.contains("spider") || itemName.contains("Spider")) &&
                            (itemName.contains("ant") || itemName.contains("Ant")) &&
                            (!itemName.contains("schematic") || !itemName.contains("Schematic"));
                } catch (Exception e) {
                    return false;
                }
            }).toList(),
            List.of(
                    TensuraEntityTypes.BLACK_SPIDER.get(),
                    TensuraEntityTypes.KNIGHT_SPIDER.get(),
                    EntityType.SPIDER,
                    EntityType.CAVE_SPIDER,
                    TensuraEntityTypes.GIANT_ANT.get(),
                    EntityType.BEE
            )),
    HEIGHT("height", null, null, null);

    private final String name;
    private final List<Block> block;
    private final List<Item> item;
    private final List<EntityType<?>> entity;
    private final List<MobEffect> effect;

    private FearTypes(String name, List<Block> block, List<Item> item, List<EntityType<?>> entity, List<MobEffect> effect) {
        this.name = name;
        this.block = block;
        this.item = item;
        this.entity = entity;
        this.effect = effect;
    }

    private FearTypes(String name, List<Block> block, List<Item> item, List<EntityType<?>> entity) {
        this.name = name;
        this.block = block;
        this.item = item;
        this.entity = entity;
        this.effect = null;
    }

    private FearTypes() {
        this.name = null;
        this.block = null;
        this.item = null;
        this.entity = null;
        this.effect = null;
    }

    private static final List<FearTypes> list = Arrays.stream(FearTypes.values()).toList();
    private static final Map<String, FearTypes> FEARS_BY_NAME = Arrays.stream(values()).collect(Collectors.toMap((fearType) -> {
        return cleanName(fearType.name);
    }, (fearType) -> {
        return fearType;
    }));

    private static String cleanName(String p_126663_) {
        return p_126663_.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
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

    public List<MobEffect> getEffect() {
        return effect;
    }

    public static List<FearTypes> getList() {
        return list;
    }

    public static Map<String, FearTypes> getFearsByName() {
        return FEARS_BY_NAME;
    }

    public String toString() {
        return name;
    }

    public static FearTypes getRandom() {
        Random random = new Random();
        if (list == null) return FearTypes.OTHERWORLDER;
        FearTypes fear = (FearTypes) list.get(random.nextInt(0, list.size()));
//        FearTypes fear = FearTypes.TRUTH;
        return fear == null ? FearTypes.DARKNESS : fear;
    }

    public CompoundTag ToNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", getName());
        CompoundTag blocks = new CompoundTag();
        getBlock().forEach((block) -> {
            if (block != null) {
                blocks.put(block.toString(), block.asItem().getShareTag(block.asItem().getDefaultInstance()));
            }
        });
        tag.put("blocks", blocks);
        CompoundTag items = new CompoundTag();
        getItem().forEach((block) -> {
            if (block != null) {
                items.put(block.toString(), block.getShareTag(block.getDefaultInstance()));
            }
        });
        tag.put("items", items);
        CompoundTag entities = new CompoundTag();
        getEntity().forEach((block) -> {
            if (block != null) {
                entities.putString(block.toString(), EntityType.getKey(block).toString());
            }
        });
        tag.put("entities", entities);
        CompoundTag effects = new CompoundTag();
        getEffect().forEach((block) -> {
            if (block != null) {
                effects.putInt(block.toString(), MobEffect.getId(block));
            }
        });
        tag.put("effects", effects);
        return tag;
    }

    public static FearTypes fromNBT(CompoundTag tag) {
        return getByName(tag.getString("name"));
    }

    public static FearTypes fromString(String string) {
        return getByName(string);
    }

    public ResourceLocation toLocation() {
        return new ResourceLocation("trawakened" , name.toLowerCase().replace(" ", "_"));
    }
}
