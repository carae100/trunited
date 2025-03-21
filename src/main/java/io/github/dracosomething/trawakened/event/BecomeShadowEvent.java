package io.github.dracosomething.trawakened.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class BecomeShadowEvent extends LivingDeathEvent {
    public BecomeShadowEvent(LivingEntity entity, DamageSource source) {
        super(entity, source);
    }
}
