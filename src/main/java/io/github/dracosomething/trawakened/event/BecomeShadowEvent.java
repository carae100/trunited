package io.github.dracosomething.trawakened.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class BecomeShadowEvent extends LivingEvent {
    private LivingEntity owner;
    private boolean arisen;

    public BecomeShadowEvent(LivingEntity entity, LivingEntity owner, boolean arisen) {
        super(entity);
        this.owner = owner;
        this.arisen = arisen;
    }

    public LivingEntity getOwner() {
        return owner;
    }

    public boolean isArisen() {
        return arisen;
    }
}
