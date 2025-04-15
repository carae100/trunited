package io.github.dracosomething.trawakened.registry;

import com.github.manasmods.manascore.api.data.gen.annotation.GenerateBlockLoot;
import com.github.manasmods.manascore.api.data.gen.annotation.GenerateBlockModels;
import com.github.manasmods.tensura.block.SimpleBlock;
import com.github.manasmods.tensura.item.templates.SimpleBlockItem;
import com.github.manasmods.tensura.registry.blocks.TensuraBlocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class blockRegistry {
    @GenerateBlockLoot.WithLootTables
    private static final DeferredRegister<Block> registry;
    @GenerateBlockLoot.SelfDrop
    public static final RegistryObject<Block> ASH;

    public static void init(IEventBus modEventBus) {
        blockRegistry.Items.registry.register(modEventBus);
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.BLOCKS, "trawakened");
        ASH = registry.register("ash", () -> {
            return new SimpleBlock(Material.GRASS, (properties) -> {
                return properties.strength(2.0F, 3.0F).sound(SoundType.SAND);
            });
        });
    }

    public static class Items {
        private static final DeferredRegister<Item> registry;
        public static final RegistryObject<Item> ASH;

        public static <T extends Block> RegistryObject<Item> simpleBlockItem(RegistryObject<T> block) {
            return registry.register(block.getId().getPath(), () -> {
                return new SimpleBlockItem((Block) block.get());
            });
        }

        static {
            registry = DeferredRegister.create(ForgeRegistries.ITEMS, "trawakened");
            ASH = simpleBlockItem(blockRegistry.ASH);
        }
    }
}
