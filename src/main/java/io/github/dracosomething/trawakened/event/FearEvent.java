package io.github.dracosomething.trawakened.event;

import io.github.dracosomething.trawakened.library.FearTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;

/**
 * fires every fear update
 */
public class FearEvent extends Event {
    private FearTypes fearTypes;
    private LivingEntity entity;

    public FearEvent (FearTypes fearTypes, LivingEntity entity) {
        this.fearTypes = fearTypes;
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public FearTypes getFearTypes() {
        return fearTypes;
    }
}
