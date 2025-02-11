package io.github.dracosomething.trawakened.api.fear;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.List;

public interface FearOperations {
    List<Block> getBlock();
    List<EntityType<?>> getEntity();
    List<Item> getItem();
    List<MobEffect> getEffect();
    String getName();
}
