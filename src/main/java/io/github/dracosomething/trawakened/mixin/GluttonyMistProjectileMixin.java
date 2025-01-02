package io.github.dracosomething.trawakened.mixin;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.entity.magic.breath.BreathEntity;
import com.github.manasmods.tensura.entity.magic.breath.BreathPart;
import com.github.manasmods.tensura.entity.magic.breath.GluttonyMistProjectile;
import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import com.mojang.math.Vector3f;
import io.github.dracosomething.trawakened.registry.particleRegistry;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.units.qual.A;
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
            method = "onHitEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z")
    )
    public void Kill(EntityHitResult entityHitResult, CallbackInfo ci){
        LivingEntity entity = (LivingEntity) entityHitResult.getEntity();
        DamageSource damageSource = TensuraDamageSources.DEVOURED;
        Entity var6 = test_addon$getOwner2((Projectile) (Object) this);
        LivingEntity owner;
        if (var6 instanceof LivingEntity) {
            owner = (LivingEntity)var6;
            damageSource = TensuraDamageSources.devour(owner);
        }

        if(test_addon$getOwner2((Projectile) (Object) this) != null){
            if(SkillUtils.hasSkill(test_addon$getOwner2((Projectile) (Object) this), Objects.requireNonNull(SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:starkill"))))){
                entity.hurt(DamageSourceHelper.addSkillAndCost(damageSource, 20.0,  SkillUtils.getSkillOrNull(test_addon$getOwner2((Projectile) (Object) this), Objects.requireNonNull(SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:starkill"))))).bypassArmor().bypassEnchantments().bypassMagic().bypassInvul(), entity.getMaxHealth());
            }
        }
    }

    @Inject(
            method = "spawnParticle",
            at = @At("HEAD"),
            remap = false,
            cancellable = true)
    public void StopParticles(CallbackInfo ci){
        if(test_addon$getOwner2((Projectile) (Object) this) != null){
            if(SkillUtils.hasSkill(test_addon$getOwner2((Projectile) (Object) this), Objects.requireNonNull(SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:starkill"))))){
                BreathEntity entity = (BreathEntity) (Object) this;
                BreathPart[] var1 = entity.parts;
                int var2 = var1.length;

                for(int var3 = 0; var3 < var2; ++var3) {
                    BreathPart part = var1[var3];
                    TensuraParticleHelper.addParticlesAroundSelf(part, particleRegistry.FLESHPARTICLE.get(), 0.9);
                    TensuraParticleHelper.addParticlesAroundSelf(part, particleRegistry.FLESHPARTICLE.get(), 0.7);
                    TensuraParticleHelper.addParticlesAroundSelf(part, particleRegistry.FLESHPARTICLE.get(), 0.5);
                    TensuraParticleHelper.addParticlesAroundSelf(part, particleRegistry.FLESHPARTICLE.get(), 0.3);
                    TensuraParticleHelper.addParticlesAroundSelf(part, particleRegistry.FLESHPARTICLE.get(), 0.1);
                }
                ci.cancel();
            }
        }
    }
}
