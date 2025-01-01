package io.github.dracosomething.trawakened.mixin;

import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.entity.magic.breath.GluttonyMistProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(GluttonyMistProjectile.class)
public class GluttonyMistProjectileMixin {
    @Unique
    private static Entity test_addon$getOwner2(Projectile projectile){
        return projectile.getOwner();
    }

    @Inject(
            method = "spawnParticle",
            at = @At("HEAD"),
            remap = false,
            cancellable = true)
    public void StopParticles(CallbackInfo ci){
        if(test_addon$getOwner2((Projectile) (Object) this) != null){
            if(SkillUtils.hasSkill(test_addon$getOwner2((Projectile) (Object) this), Objects.requireNonNull(SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:starkill"))))){
                ci.cancel();
            }
        }
    }
}
