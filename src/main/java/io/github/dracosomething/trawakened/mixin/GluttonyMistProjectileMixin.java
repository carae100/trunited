package io.github.dracosomething.trawakened.mixin;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillClientUtils;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.data.TensuraTags;
import com.github.manasmods.tensura.entity.magic.breath.BreathEntity;
import com.github.manasmods.tensura.entity.magic.breath.BreathPart;
import com.github.manasmods.tensura.entity.magic.breath.GluttonyMistProjectile;
import com.github.manasmods.tensura.entity.magic.breath.PredatorMistProjectile;
import com.github.manasmods.tensura.event.SkillPlunderEvent;
import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.dracosomething.trawakened.registry.particleRegistry;
import io.github.dracosomething.trawakened.registry.skillregistry;
import io.github.dracosomething.trawakened.world.trawakenedDamage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Mixin(GluttonyMistProjectile.class)
public class GluttonyMistProjectileMixin extends PredatorMistProjectile {
    public GluttonyMistProjectileMixin(EntityType<? extends GluttonyMistProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private static LivingEntity test_addon$getOwner2(Projectile projectile){
        return (LivingEntity) projectile.getOwner();
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
            damageSource = trawakenedDamage.assimilation(owner);
        }
        if(test_addon$getOwner2((Projectile) (Object) this) != null){
            if(SkillUtils.hasSkill(test_addon$getOwner2((Projectile) (Object) this), skillregistry.STARKILL.get()) && SkillClientUtils.isSkillHeldClient(entity, skillregistry.STARKILL.get())){
                if(entity.hurt(DamageSourceHelper.addSkillAndCost(damageSource, 20.0,  SkillUtils.getSkillOrNull(test_addon$getOwner2((Projectile) (Object) this), Objects.requireNonNull(SkillAPI.getSkillRegistry().getValue(new ResourceLocation("trawakened:starkill"))))).bypassArmor().bypassEnchantments().bypassMagic(), 75)){
                    var6 = test_addon$getOwner2((Projectile) (Object) this);
                    if (var6 instanceof LivingEntity) {
                        owner = (LivingEntity)var6;
                        if(SkillUtils.isSkillMastered(owner, skillregistry.STARKILL.get())) {
                            owner.getPersistentData().putInt("assimilation_kills", owner.getPersistentData().getInt("assimilation_kills") + 1);
                        }
                            this.devourAllSkills(entity, owner);
                            this.devourEP(entity, owner, 0.02F);
                            if (owner instanceof Player) {
                                Player player = (Player)owner;
                                List<ItemEntity> list = owner.level.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(entity.position(), 2.0, 2.0, 2.0));
                                Iterator var8 = list.iterator();

                                while(var8.hasNext()) {
                                    ItemEntity item = (ItemEntity)var8.next();
                                    if (player.addItem(item.getItem())) {
                                        item.discard();
                                    } else {
                                        item.moveTo(owner.position());
                                    }
                                }
                            }
                        }
                }
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
            if(SkillUtils.hasSkill(test_addon$getOwner2((Projectile) (Object) this), skillregistry.STARKILL.get()) && SkillClientUtils.isSkillHeldClient(test_addon$getOwner2((Projectile) (Object) this), skillregistry.STARKILL.get())){
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

    protected void devourAllSkills(LivingEntity target, LivingEntity owner) {
        if (!target.getType().is(TensuraTags.EntityTypes.NO_SKILL_PLUNDER)) {
            List<ManasSkillInstance> targetSkills = SkillAPI.getSkillsFrom(target).getLearnedSkills().stream().filter(this::canDevour).toList();
            Iterator var4 = targetSkills.iterator();
            Random random1 = new Random();

            while(var4.hasNext()) {
                if (random1.nextInt(1, 100) >= 75) {
                    ManasSkillInstance targetInstance = (ManasSkillInstance) var4.next();
                    if (!targetInstance.isTemporarySkill() && targetInstance.getMastery() >= 0) {
                        SkillPlunderEvent event = new SkillPlunderEvent(target, owner, false, targetInstance.getSkill());
                        if (!MinecraftForge.EVENT_BUS.post(event) && SkillUtils.learnSkill(owner, event.getSkill(), this.getSkill().getRemoveTime())) {
                            if (owner instanceof Player) {
                                Player player = (Player) owner;
                                player.displayClientMessage(Component.translatable("tensura.skill.acquire", new Object[]{event.getSkill().getName()}).setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)), false);
                            }

                            owner.getLevel().playSound((Player) null, owner.getX(), owner.getY(), owner.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                        }
                    }
                }
            }

        }
    }
}
