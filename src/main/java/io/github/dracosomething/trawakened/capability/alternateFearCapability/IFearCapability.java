package io.github.dracosomething.trawakened.capability.alternateFearCapability;

import io.github.dracosomething.trawakened.api.FearTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public interface IFearCapability extends INBTSerializable<CompoundTag> {
    FearTypes getFear(LivingEntity entity);

    void setFear(LivingEntity entity, FearTypes types);

    int getScaredAmount(LivingEntity entity);

    void setScaredAmount(int amount, LivingEntity entity);

    int getCooldown(LivingEntity entity);

    void setCooldown(LivingEntity entity, int amount);
}
