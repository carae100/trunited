package io.github.dracosomething.trawakened.event;

import io.github.dracosomething.trawakened.library.FearTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * Fires when an entity gets scared
 */
@Cancelable
public class FearActivateEvent extends FearEvent {
    private int oldScared;
    private int newScared;

    public FearActivateEvent(FearTypes fearTypes, LivingEntity entity, int oldScared, int newScared) {
        super(fearTypes, entity);
        this.oldScared = oldScared;
        this.newScared = newScared;
    }

    public int getNewScared() {
        return newScared;
    }

    public int getOldScared() {
        return oldScared;
    }

    public void setNewScared(int newScared) {
        this.newScared = newScared;
    }
}
