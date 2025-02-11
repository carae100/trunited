package io.github.dracosomething.trawakened.capability.alternateFearCapability;

import io.github.dracosomething.trawakened.api.FearTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public interface IFearCapability extends INBTSerializable<CompoundTag> {
    /**
     * returns the fear of an entity
     * @param entity
     * @return the fear of entity
     */
    FearTypes getFear(LivingEntity entity);

    /**
     * sets the fear of an entity
     * @param entity
     * @param types
     */
    void setFear(LivingEntity entity, FearTypes types);

    /**
     * return the times an entity has been scared
     * @param entity
     * @return scared amount of entity
     */
    int getScaredAmount(LivingEntity entity);

    /**
     * sets the amount an entity has been scared
     * @param amount
     * @param entity
     */
    void setScaredAmount(int amount, LivingEntity entity);

    /**
     * returns the fear cooldown of an entity
     * @param entity
     * @return cooldown of an entities fear
     */
    int getCooldown(LivingEntity entity);

    /**
     * sets the cooldown of an entities fear
     * @param entity
     * @param amount
     */
    void setCooldown(LivingEntity entity, int amount);

    boolean getIsAlternate(LivingEntity entity);

    void setIsAlternate(LivingEntity entity, boolean isAlternate);
}
