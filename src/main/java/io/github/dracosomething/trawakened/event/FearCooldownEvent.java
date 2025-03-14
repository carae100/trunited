package io.github.dracosomething.trawakened.event;

import io.github.dracosomething.trawakened.library.FearTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * Fires when an entities fear cooldown decreases
 */
@Cancelable
public class FearCooldownEvent extends FearEvent {
    private int cooldown;

    public FearCooldownEvent(FearTypes fearTypes, LivingEntity entity, int cooldown) {
        super(fearTypes, entity);
        this.cooldown = cooldown;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }
}
