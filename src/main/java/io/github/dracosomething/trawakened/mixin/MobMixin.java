package io.github.dracosomething.trawakened.mixin;

import io.github.dracosomething.trawakened.ability.skill.ultimate.herrscherofplague;
import io.github.dracosomething.trawakened.mobeffect.PlagueEffect;
import io.github.dracosomething.trawakened.registry.effectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import javax.annotation.Nullable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {
    @Shadow @Nullable private LivingEntity target;

    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @ModifyVariable(method = "setTarget", at = @At("HEAD"), argsOnly = true)
    public LivingEntity setTarget(LivingEntity value){
        if (herrscherofplague.active) {
            if (this.hasEffect(new MobEffectInstance((MobEffect) effectRegistry.PLAGUEEFFECT.get()).getEffect())) {
                if (PlagueEffect.getOwner(this) == herrscherofplague.Owner) {
                    return null;
                }
            }
        }
//        if(this.hasEffect(new MobEffectInstance(effectRegistry.OVERWHELMED.get()).getEffect())){
//            return null;
//        }

        return value;
    }
}
